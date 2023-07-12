/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.engine.flink.kubernetes.service.impl;

import cn.sliew.scaleph.common.util.UUIDUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkKubernetesJob;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkKubernetesJobMapper;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.definition.job.FlinkDeploymentJobConverter;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.definition.job.FlinkSessionJobConverter;
import cn.sliew.scaleph.engine.flink.kubernetes.service.FlinkKubernetesOperatorService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.WsFlinkKubernetesJobService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.convert.WsFlinkKubernetesJobConvert;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.param.WsFlinkKubernetesJobAddParam;
import cn.sliew.scaleph.engine.flink.kubernetes.service.param.WsFlinkKubernetesJobListParam;
import cn.sliew.scaleph.engine.flink.kubernetes.service.param.WsFlinkKubernetesJobUpdateParam;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConfigService;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WsFlinkKubernetesJobServiceImpl implements WsFlinkKubernetesJobService {

    @Autowired
    private WsFlinkKubernetesJobMapper wsFlinkKubernetesJobMapper;
    @Autowired
    private FlinkKubernetesOperatorService flinkKubernetesOperatorService;

    @Autowired
    private FlinkDeploymentJobConverter flinkDeploymentJobConverter;

    @Override
    public Page<WsFlinkKubernetesJobDTO> list(WsFlinkKubernetesJobListParam param) {
        Page<WsFlinkKubernetesJob> page = new Page<>(param.getCurrent(), param.getPageSize());
        Page<WsFlinkKubernetesJob> wsFlinkKubernetesJobPage = wsFlinkKubernetesJobMapper.list(page, param.getProjectId(), param.getExecutionMode(), param.getType(), param.getDeploymentKind(), param.getState(), param.getName());
        Page<WsFlinkKubernetesJobDTO> result = new Page<>(wsFlinkKubernetesJobPage.getCurrent(), wsFlinkKubernetesJobPage.getSize(), wsFlinkKubernetesJobPage.getTotal());
        List<WsFlinkKubernetesJobDTO> wsFlinkKubernetesJobDTOS = WsFlinkKubernetesJobConvert.INSTANCE.toDto(wsFlinkKubernetesJobPage.getRecords());
        result.setRecords(wsFlinkKubernetesJobDTOS);
        return result;
    }

    @Override
    public WsFlinkKubernetesJobDTO selectOne(Long id) {
        WsFlinkKubernetesJob record = wsFlinkKubernetesJobMapper.selectOne(id);
        checkState(record != null, () -> "flink kubernetes job not exist for id = " + id);
        return WsFlinkKubernetesJobConvert.INSTANCE.toDto(record);
    }

    @Override
    public Object asYaml(Long id) throws Exception {
        WsFlinkKubernetesJobDTO wsFlinkKubernetesJobDTO = selectOne(id);
        switch (wsFlinkKubernetesJobDTO.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                return flinkDeploymentJobConverter.convertTo(wsFlinkKubernetesJobDTO);
            case FLINK_SESSION_JOB:
                return FlinkSessionJobConverter.INSTANCE.convertTo(wsFlinkKubernetesJobDTO);
            default:
                throw new RuntimeException("unsupport flink deployment mode for " + wsFlinkKubernetesJobDTO.getDeploymentKind());
        }
    }

    @Override
    public int insert(WsFlinkKubernetesJobAddParam param) {
        WsFlinkKubernetesJob record = new WsFlinkKubernetesJob();
        BeanUtils.copyProperties(param, record);
        record.setJobId(UUIDUtil.randomUUId());
        return wsFlinkKubernetesJobMapper.insert(record);
    }

    @Override
    public int update(WsFlinkKubernetesJobUpdateParam param) {
        WsFlinkKubernetesJob record = new WsFlinkKubernetesJob();
        BeanUtils.copyProperties(param, record);
        return wsFlinkKubernetesJobMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        return wsFlinkKubernetesJobMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return wsFlinkKubernetesJobMapper.deleteBatchIds(ids);
    }

    @Override
    public void deploy(Long id) throws Exception {
        WsFlinkKubernetesJobDTO jobDTO = selectOne(id);
        Object job = asYaml(id);
        switch (jobDTO.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                flinkKubernetesOperatorService.deployJob(jobDTO.getFlinkDeployment().getClusterCredentialId(), job);
                return;
            case FLINK_SESSION_JOB:
                flinkKubernetesOperatorService.deployJob(jobDTO.getFlinkSessionCluster().getClusterCredentialId(), job);
                return;
            default:
        }
    }

    @Override
    public void shutdown(Long id) throws Exception {
        WsFlinkKubernetesJobDTO jobDTO = selectOne(id);
        Object job = asYaml(id);
        switch (jobDTO.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                flinkKubernetesOperatorService.shutdownJob(jobDTO.getFlinkDeployment().getClusterCredentialId(), job);
                return;
            case FLINK_SESSION_JOB:
                flinkKubernetesOperatorService.shutdownJob(jobDTO.getFlinkSessionCluster().getClusterCredentialId(), job);
                return;
            default:
        }
    }
}
