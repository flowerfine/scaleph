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

package cn.sliew.scaleph.application.flink.service.impl;

import cn.sliew.scaleph.common.util.UUIDUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkKubernetesDeployment;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkKubernetesDeploymentMapper;
import cn.sliew.scaleph.application.flink.resource.definition.deployment.FlinkDeployment;
import cn.sliew.scaleph.application.flink.resource.definition.deployment.FlinkDeploymentConverter;
import cn.sliew.scaleph.application.flink.service.WsFlinkKubernetesDeploymentService;
import cn.sliew.scaleph.application.flink.service.WsFlinkKubernetesTemplateService;
import cn.sliew.scaleph.application.flink.service.convert.WsFlinkKubernetesDeploymentConvert;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesDeploymentDTO;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesTemplateDTO;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesDeploymentListParam;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesDeploymentSelectListParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static cn.sliew.milky.common.check.Ensures.checkState;

@Service
public class WsFlinkKubernetesDeploymentServiceImpl implements WsFlinkKubernetesDeploymentService {

    @Autowired
    private WsFlinkKubernetesDeploymentMapper wsFlinkKubernetesDeploymentMapper;
    @Autowired
    private WsFlinkKubernetesTemplateService wsFlinkKubernetesTemplateService;

    @Override
    public Page<WsFlinkKubernetesDeploymentDTO> list(WsFlinkKubernetesDeploymentListParam param) {
        Page<WsFlinkKubernetesDeployment> page = wsFlinkKubernetesDeploymentMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(WsFlinkKubernetesDeployment.class)
                        .eq(WsFlinkKubernetesDeployment::getProjectId, param.getProjectId())
                        .eq(param.getClusterCredentialId() != null, WsFlinkKubernetesDeployment::getClusterCredentialId, param.getClusterCredentialId())
                        .like(StringUtils.hasText(param.getName()), WsFlinkKubernetesDeployment::getName, param.getName()));
        Page<WsFlinkKubernetesDeploymentDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<WsFlinkKubernetesDeploymentDTO> dtoList = WsFlinkKubernetesDeploymentConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public List<WsFlinkKubernetesDeploymentDTO> listAll(WsFlinkKubernetesDeploymentSelectListParam param) {
        LambdaQueryWrapper<WsFlinkKubernetesDeployment> queryWrapper = Wrappers.lambdaQuery(WsFlinkKubernetesDeployment.class)
                .eq(WsFlinkKubernetesDeployment::getProjectId, param.getProjectId())
                .like(StringUtils.hasText(param.getName()), WsFlinkKubernetesDeployment::getName, param.getName())
                .orderByDesc(WsFlinkKubernetesDeployment::getId);
        List<WsFlinkKubernetesDeployment> wsFlinkKubernetesDeployments = wsFlinkKubernetesDeploymentMapper.selectList(queryWrapper);
        return WsFlinkKubernetesDeploymentConvert.INSTANCE.toDto(wsFlinkKubernetesDeployments);
    }

    @Override
    public WsFlinkKubernetesDeploymentDTO selectOne(Long id) {
        WsFlinkKubernetesDeployment record = wsFlinkKubernetesDeploymentMapper.selectById(id);
        checkState(record != null, () -> "flink kubernetes deployment not exist for id = " + id);
        return WsFlinkKubernetesDeploymentConvert.INSTANCE.toDto(record);
    }

    @Override
    public WsFlinkKubernetesDeploymentDTO fromTemplate(Long templateId) {
        WsFlinkKubernetesTemplateDTO wsFlinkKubernetesTemplateDTO = wsFlinkKubernetesTemplateService.selectOne(templateId);
        WsFlinkKubernetesDeploymentDTO wsFlinkKubernetesDeploymentDTO = new WsFlinkKubernetesDeploymentDTO();
        wsFlinkKubernetesDeploymentDTO.setKubernetesOptions(wsFlinkKubernetesTemplateDTO.getKubernetesOptions());
        wsFlinkKubernetesDeploymentDTO.setJobManager(wsFlinkKubernetesTemplateDTO.getJobManager());
        wsFlinkKubernetesDeploymentDTO.setTaskManager(wsFlinkKubernetesTemplateDTO.getTaskManager());
        wsFlinkKubernetesDeploymentDTO.setPodTemplate(wsFlinkKubernetesTemplateDTO.getPodTemplate());
        wsFlinkKubernetesDeploymentDTO.setFlinkConfiguration(wsFlinkKubernetesTemplateDTO.getFlinkConfiguration());
        wsFlinkKubernetesDeploymentDTO.setLogConfiguration(wsFlinkKubernetesTemplateDTO.getLogConfiguration());
        wsFlinkKubernetesDeploymentDTO.setIngress(wsFlinkKubernetesTemplateDTO.getIngress());
        wsFlinkKubernetesDeploymentDTO.setAdditionalDependencies(wsFlinkKubernetesTemplateDTO.getAdditionalDependencies());
        wsFlinkKubernetesDeploymentDTO.setRemark("generated from template-" + wsFlinkKubernetesTemplateDTO.getName());
        return wsFlinkKubernetesDeploymentDTO;
    }

    @Override
    public FlinkDeployment asYaml(Long id) {
        WsFlinkKubernetesDeploymentDTO dto = selectOne(id);
        return asYAML(dto);
    }

    @Override
    public FlinkDeployment asYAML(WsFlinkKubernetesDeploymentDTO param) {
        return FlinkDeploymentConverter.INSTANCE.convertTo(param);
    }

    @Override
    public int insert(WsFlinkKubernetesDeploymentDTO dto) {
        WsFlinkKubernetesDeployment record = WsFlinkKubernetesDeploymentConvert.INSTANCE.toDo(dto);
        record.setDeploymentId(UUIDUtil.randomUUId());
        return wsFlinkKubernetesDeploymentMapper.insert(record);
    }

    @Override
    public int update(WsFlinkKubernetesDeploymentDTO dto) {
        WsFlinkKubernetesDeployment record = WsFlinkKubernetesDeploymentConvert.INSTANCE.toDo(dto);
        return wsFlinkKubernetesDeploymentMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        return wsFlinkKubernetesDeploymentMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return wsFlinkKubernetesDeploymentMapper.deleteBatchIds(ids);
    }
}
