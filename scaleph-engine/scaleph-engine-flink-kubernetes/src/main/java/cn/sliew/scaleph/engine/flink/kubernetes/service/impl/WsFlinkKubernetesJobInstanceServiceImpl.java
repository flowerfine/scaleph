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

import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.common.dict.flink.FlinkJobState;
import cn.sliew.scaleph.common.dict.flink.kubernetes.ResourceLifecycleState;
import cn.sliew.scaleph.common.util.UUIDUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkKubernetesJobInstance;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkKubernetesJobInstanceMapper;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.status.FlinkDeploymentStatus;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.status.JobStatus;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.definition.job.instance.FlinkJobInstanceConverterFactory;
import cn.sliew.scaleph.engine.flink.kubernetes.service.FlinkKubernetesOperatorService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.WsFlinkKubernetesJobInstanceService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.WsFlinkKubernetesJobService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.convert.WsFlinkKubernetesJobInstanceConvert;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.param.WsFlinkKubernetesJobInstanceDeployParam;
import cn.sliew.scaleph.engine.flink.kubernetes.service.param.WsFlinkKubernetesJobInstanceListParam;
import cn.sliew.scaleph.engine.flink.kubernetes.service.param.WsFlinkKubernetesJobInstanceShutdownParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.GenericKubernetesResourceBuilder;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Predicates;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WsFlinkKubernetesJobInstanceServiceImpl implements WsFlinkKubernetesJobInstanceService {

    @Autowired
    private WsFlinkKubernetesJobInstanceMapper wsFlinkKubernetesJobInstanceMapper;
    @Autowired
    private WsFlinkKubernetesJobService wsFlinkKubernetesJobService;
    @Autowired
    private FlinkKubernetesOperatorService flinkKubernetesOperatorService;
    @Autowired
    private FlinkJobInstanceConverterFactory flinkJobInstanceConverterFactory;

    @Override
    public Page<WsFlinkKubernetesJobInstanceDTO> list(WsFlinkKubernetesJobInstanceListParam param) {
        Page<WsFlinkKubernetesJobInstance> page = new Page<>(param.getCurrent(), param.getPageSize());
        LambdaQueryWrapper<WsFlinkKubernetesJobInstance> queryWrapper = Wrappers.lambdaQuery(WsFlinkKubernetesJobInstance.class)
                .eq(WsFlinkKubernetesJobInstance::getWsFlinkKubernetesJobId, param.getWsFlinkKubernetesJobId())
                .orderByDesc(WsFlinkKubernetesJobInstance::getId);
        Page<WsFlinkKubernetesJobInstance> wsFlinkKubernetesJobInstancePage = wsFlinkKubernetesJobInstanceMapper.selectPage(page, queryWrapper);
        Page<WsFlinkKubernetesJobInstanceDTO> result = new Page<>(wsFlinkKubernetesJobInstancePage.getCurrent(), wsFlinkKubernetesJobInstancePage.getSize(), wsFlinkKubernetesJobInstancePage.getTotal());
        List<WsFlinkKubernetesJobInstanceDTO> wsFlinkKubernetesJobInstanceDTOS = WsFlinkKubernetesJobInstanceConvert.INSTANCE.toDto(wsFlinkKubernetesJobInstancePage.getRecords());
        result.setRecords(wsFlinkKubernetesJobInstanceDTOS);
        return result;
    }

    @Override
    public WsFlinkKubernetesJobInstanceDTO selectOne(Long id) {
        WsFlinkKubernetesJobInstance record = wsFlinkKubernetesJobInstanceMapper.selectOne(id);
        checkState(record != null, () -> "flink kubernetes job instance not exist for id = " + id);
        return WsFlinkKubernetesJobInstanceConvert.INSTANCE.toDto(record);
    }

    @Override
    public Optional<WsFlinkKubernetesJobInstanceDTO> selectCurrent(Long wsFlinkKubernetesJobId) {
        Optional<WsFlinkKubernetesJobInstance> optional = wsFlinkKubernetesJobInstanceMapper.selectCurrent(wsFlinkKubernetesJobId);
        return optional.map(record -> WsFlinkKubernetesJobInstanceConvert.INSTANCE.toDto(record));
    }

    @Override
    public String mockYaml(Long wsFlinkKubernetesJobId) {
        WsFlinkKubernetesJobDTO jobDTO = wsFlinkKubernetesJobService.selectOne(wsFlinkKubernetesJobId);
        WsFlinkKubernetesJobInstanceDTO jobInstanceDTO = new WsFlinkKubernetesJobInstanceDTO();
        jobInstanceDTO.setInstanceId(jobDTO.getJobId());
        jobInstanceDTO.setWsFlinkKubernetesJobId(wsFlinkKubernetesJobId);
        jobInstanceDTO.setWsFlinkKubernetesJob(jobDTO);
        return flinkJobInstanceConverterFactory.convert(jobInstanceDTO);
    }

    @Override
    public String asYaml(Long id) throws Exception {
        WsFlinkKubernetesJobInstanceDTO jobInstanceDTO = selectOne(id);
        return flinkJobInstanceConverterFactory.convert(jobInstanceDTO);
    }

    @Override
    public void deploy(WsFlinkKubernetesJobInstanceDeployParam param) throws Exception {
        WsFlinkKubernetesJobInstance record = new WsFlinkKubernetesJobInstance();
        BeanUtils.copyProperties(param, record);
        record.setInstanceId(UUIDUtil.randomUUId());
        if (param.getJobManager() != null) {
            record.setJobManager(JacksonUtil.toJsonString(param.getJobManager()));
        }
        if (param.getTaskManager() != null) {
            record.setTaskManager(JacksonUtil.toJsonString(param.getTaskManager()));
        }
        if (param.getUserFlinkConfiguration() != null) {
            record.setUserFlinkConfiguration(JacksonUtil.toJsonString(param.getUserFlinkConfiguration()));
        }
        wsFlinkKubernetesJobInstanceMapper.insert(record);
        WsFlinkKubernetesJobInstanceDTO jobInstanceDTO = selectOne(record.getId());
        WsFlinkKubernetesJobDTO jobDTO = jobInstanceDTO.getWsFlinkKubernetesJob();
        String yaml = asYaml(record.getId());
        switch (jobDTO.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                flinkKubernetesOperatorService.deployJob(jobDTO.getFlinkDeployment().getClusterCredentialId(), yaml);
                return;
            case FLINK_SESSION_JOB:
                flinkKubernetesOperatorService.deployJob(jobDTO.getFlinkSessionCluster().getClusterCredentialId(), yaml);
                return;
            default:
        }
    }

    @Override
    public void shutdown(WsFlinkKubernetesJobInstanceShutdownParam param) throws Exception {
        WsFlinkKubernetesJobInstanceDTO jobInstanceDTO = selectOne(param.getId());
        WsFlinkKubernetesJobDTO jobDTO = jobInstanceDTO.getWsFlinkKubernetesJob();
        String yaml = asYaml(param.getId());
        switch (jobDTO.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                flinkKubernetesOperatorService.shutdownJob(jobDTO.getFlinkDeployment().getClusterCredentialId(), yaml);
                return;
            case FLINK_SESSION_JOB:
                flinkKubernetesOperatorService.shutdownJob(jobDTO.getFlinkSessionCluster().getClusterCredentialId(), yaml);
                return;
            default:
        }
    }

    @Override
    public Optional<GenericKubernetesResource> getStatus(Long id) {
        try {
            WsFlinkKubernetesJobInstanceDTO jobInstanceDTO = selectOne(id);
            return flinkKubernetesOperatorService.getJob(jobInstanceDTO);
        } catch (Exception e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    @Override
    public Optional<GenericKubernetesResource> getStatusWithoutManagedFields(Long id) {
        Optional<GenericKubernetesResource> optional = getStatus(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        GenericKubernetesResource status = optional.get();
        GenericKubernetesResourceBuilder builder = new GenericKubernetesResourceBuilder(status);
        ObjectMetaBuilder objectMetaBuilder = new ObjectMetaBuilder(status.getMetadata());
        objectMetaBuilder.removeMatchingFromManagedFields(Predicates.isTrue());
        builder.withMetadata(objectMetaBuilder.build());
        return Optional.of(builder.build());
    }

    @Override
    public Optional<GenericKubernetesResource> getJobWithoutStatus(Long id) {
        Optional<GenericKubernetesResource> optional = getStatusWithoutManagedFields(id);
        if (optional.isEmpty()) {
            return Optional.empty();
        }
        GenericKubernetesResource status = optional.get();
        GenericKubernetesResourceBuilder builder = new GenericKubernetesResourceBuilder(status);
        builder.removeFromAdditionalProperties("status");
        return Optional.of(builder.build());
    }

    @Override
    public int updateStatus(Long id, FlinkDeploymentStatus status) {
        if (status == null) {
            return -1;
        }
        WsFlinkKubernetesJobInstance record = new WsFlinkKubernetesJobInstance();
        record.setId(id);
        record.setState(EnumUtils.getEnum(ResourceLifecycleState.class, status.getLifecycleState().name()));
        if (status.getJobStatus() != null) {
            JobStatus jobStatus = status.getJobStatus();
            if (jobStatus.getState() != null) {
                record.setJobState(FlinkJobState.of(jobStatus.getState()));
            }
            if (jobStatus.getStartTime() != null) {
                record.setStartTime(new Date(Long.parseLong(jobStatus.getStartTime())));
            }
        }
        record.setError(status.getError());
        if (CollectionUtils.isEmpty(status.getClusterInfo())) {
            record.setClusterInfo(null);
        } else {
            record.setClusterInfo(JacksonUtil.toJsonString(status.getClusterInfo()));
        }
        if (status.getTaskManager() == null) {
            record.setTaskManagerInfo(null);
        } else {
            record.setTaskManagerInfo(JacksonUtil.toJsonString(status.getTaskManager()));
        }
        return wsFlinkKubernetesJobInstanceMapper.updateById(record);
    }

    @Override
    public int clearStatus(Long id) {
        WsFlinkKubernetesJobInstance record = new WsFlinkKubernetesJobInstance();
        record.setId(id);
        record.setState(null);
        record.setError(null);
        record.setClusterInfo(null);
        record.setTaskManagerInfo(null);
        return wsFlinkKubernetesJobInstanceMapper.updateById(record);
    }
}
