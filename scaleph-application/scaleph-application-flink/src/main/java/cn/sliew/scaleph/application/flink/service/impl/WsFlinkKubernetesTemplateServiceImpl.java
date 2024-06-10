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

import cn.sliew.scaleph.application.flink.factory.FlinkDefaultTemplateFactory;
import cn.sliew.scaleph.application.flink.operator.spec.IngressSpec;
import cn.sliew.scaleph.application.flink.operator.spec.JobManagerSpec;
import cn.sliew.scaleph.application.flink.operator.spec.TaskManagerSpec;
import cn.sliew.scaleph.application.flink.resource.definition.template.FlinkTemplate;
import cn.sliew.scaleph.application.flink.resource.definition.template.FlinkTemplateConverter;
import cn.sliew.scaleph.application.flink.resource.handler.FlinkImageMapping;
import cn.sliew.scaleph.application.flink.resource.handler.FlinkVersionMapping;
import cn.sliew.scaleph.application.flink.service.WsFlinkKubernetesTemplateService;
import cn.sliew.scaleph.application.flink.service.convert.WsFlinkKubernetesTemplateConvert;
import cn.sliew.scaleph.application.flink.service.dto.ChildOption;
import cn.sliew.scaleph.application.flink.service.dto.FlinkImageOption;
import cn.sliew.scaleph.application.flink.service.dto.FlinkVersionOption;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesTemplateDTO;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesTemplateAddParam;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesTemplateListParam;
import cn.sliew.scaleph.application.flink.service.param.WsFlinkKubernetesTemplateUpdateParam;
import cn.sliew.scaleph.application.flink.service.vo.KubernetesOptionsVO;
import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import cn.sliew.scaleph.common.dict.flink.kubernetes.DeploymentKind;
import cn.sliew.scaleph.common.jackson.JsonMerger;
import cn.sliew.scaleph.common.util.UUIDUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkKubernetesTemplate;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkKubernetesTemplateMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.fabric8.kubernetes.api.model.Pod;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WsFlinkKubernetesTemplateServiceImpl implements WsFlinkKubernetesTemplateService {

    @Autowired
    private WsFlinkKubernetesTemplateMapper wsFlinkKubernetesTemplateMapper;

    @Override
    public Page<WsFlinkKubernetesTemplateDTO> list(WsFlinkKubernetesTemplateListParam param) {
        Page<WsFlinkKubernetesTemplate> page = wsFlinkKubernetesTemplateMapper.selectPage(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(WsFlinkKubernetesTemplate.class)
                        .eq(WsFlinkKubernetesTemplate::getProjectId, param.getProjectId())
                        .eq(param.getDeploymentKind() != null, WsFlinkKubernetesTemplate::getDeploymentKind, param.getDeploymentKind())
                        .like(StringUtils.hasText(param.getName()), WsFlinkKubernetesTemplate::getName, param.getName()));
        Page<WsFlinkKubernetesTemplateDTO> result =
                new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<WsFlinkKubernetesTemplateDTO> dtoList = WsFlinkKubernetesTemplateConvert.INSTANCE.toDto(page.getRecords());
        result.setRecords(dtoList);
        return result;
    }

    @Override
    public WsFlinkKubernetesTemplateDTO selectOne(Long id) {
        WsFlinkKubernetesTemplate record = wsFlinkKubernetesTemplateMapper.selectById(id);
        return WsFlinkKubernetesTemplateConvert.INSTANCE.toDto(record);
    }

    @Override
    public List<FlinkVersionOption> getFlinkVersionOptions() {
        List<FlinkVersionOption> options = new ArrayList<>();
        for (FlinkVersionMapping mapping : FlinkVersionMapping.values()) {
            options.add(mapping.toOption());
        }
        return options;
    }

    @Override
    public List<FlinkImageOption> getFlinkImageOptions(FlinkVersion flinkVersion) {
        List<FlinkImageOption> options = new ArrayList<>();
        List<FlinkImageMapping> mappings = FlinkImageMapping.of(flinkVersion);
        Map<FlinkJobType, List<FlinkImageMapping>> mappingMap = mappings.stream().collect(Collectors.groupingBy(FlinkImageMapping::getJobType));
        for (Map.Entry<FlinkJobType, List<FlinkImageMapping>> entry : mappingMap.entrySet()) {
            FlinkImageOption option = new FlinkImageOption();
            option.setLabel(entry.getKey().getLabel());
            List<ChildOption> childOptions = new ArrayList<>();
            for (FlinkImageMapping mapping : entry.getValue()) {
                ChildOption childOption = new ChildOption();
                childOption.setLabel(mapping.getImage());
                childOption.setValue(mapping.getImage());
                childOptions.add(childOption);
            }
            option.setOptions(childOptions);
            options.add(option);
        }
        return options;
    }

    @Override
    public FlinkTemplate asYaml(WsFlinkKubernetesTemplateDTO dto) {
        return FlinkTemplateConverter.INSTANCE.convertTo(dto);
    }

    @Override
    public FlinkTemplate asYamlWithDefault(WsFlinkKubernetesTemplateDTO dto) {
        return FlinkTemplateConverter.INSTANCE.convertTo(mergeDefault(dto));
    }

    @Override
    public WsFlinkKubernetesTemplateDTO mergeDefault(WsFlinkKubernetesTemplateDTO dto) {
        FlinkTemplate customTemplate = FlinkTemplateConverter.INSTANCE.convertTo(dto);
        FlinkTemplate defaultTemplate = FlinkDefaultTemplateFactory.create("default", "default", customTemplate);
        switch (dto.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
            case FLINK_SESSION_JOB:
                return doMergeDefault(FlinkTemplateConverter.INSTANCE.convertFrom(defaultTemplate), getGlobalDefault(dto.getDeploymentKind()));
            default:
                return null;
        }
    }

    private WsFlinkKubernetesTemplateDTO doMergeDefault(WsFlinkKubernetesTemplateDTO dto, WsFlinkKubernetesTemplateDTO globalDefault) {
        WsFlinkKubernetesTemplateDTO result = new WsFlinkKubernetesTemplateDTO();
        result.setName(dto.getName());
        result.setNamespace(StringUtils.hasText(dto.getNamespace()) ? dto.getNamespace() : globalDefault.getNamespace());
        result.setKubernetesOptions(JsonMerger.merge(globalDefault.getKubernetesOptions(), dto.getKubernetesOptions(), KubernetesOptionsVO.class));
        result.setJobManager(JsonMerger.merge(globalDefault.getJobManager(), dto.getJobManager(), JobManagerSpec.class));
        result.setTaskManager(JsonMerger.merge(globalDefault.getTaskManager(), dto.getTaskManager(), TaskManagerSpec.class));
        result.setPodTemplate(JsonMerger.merge(globalDefault.getPodTemplate(), dto.getPodTemplate(), Pod.class));
        result.setFlinkConfiguration(JsonMerger.merge(globalDefault.getFlinkConfiguration(), dto.getFlinkConfiguration(), Map.class));
        result.setLogConfiguration(JsonMerger.merge(globalDefault.getLogConfiguration(), dto.getLogConfiguration(), Map.class));
        result.setIngress(JsonMerger.merge(globalDefault.getIngress(), dto.getIngress(), IngressSpec.class));
        result.setAdditionalDependencies(JsonMerger.merge(globalDefault.getAdditionalDependencies(), dto.getAdditionalDependencies(), List.class));
        result.setRemark(StringUtils.hasText(dto.getRemark()) ? dto.getRemark() : globalDefault.getRemark());
        return result;
    }

    private WsFlinkKubernetesTemplateDTO getGlobalDefault(DeploymentKind deploymentKind) {
        switch (deploymentKind) {
            case FLINK_DEPLOYMENT:
                FlinkTemplate deploymentDefaults = FlinkDefaultTemplateFactory.getDeploymentDefaults();
                return FlinkTemplateConverter.INSTANCE.convertFrom(deploymentDefaults);
            case FLINK_SESSION_JOB:
                FlinkTemplate sessionClusterDefaults = FlinkDefaultTemplateFactory.getSessionClusterDefaults();
                return FlinkTemplateConverter.INSTANCE.convertFrom(sessionClusterDefaults);
            default:
                return null;
        }
    }

    @Override
    public int insert(WsFlinkKubernetesTemplateAddParam param) {
        WsFlinkKubernetesTemplateDTO dto = new WsFlinkKubernetesTemplateDTO();
        BeanUtils.copyProperties(param, dto);
        WsFlinkKubernetesTemplateDTO mergeWithDefault = mergeDefault(dto);
        mergeWithDefault.setAdditionalDependencies(param.getAdditionalDependencies());
        WsFlinkKubernetesTemplate record = WsFlinkKubernetesTemplateConvert.INSTANCE.toDo(mergeWithDefault);
        BeanUtils.copyProperties(param, record);
        record.setTemplateId(UUIDUtil.randomUUId());
        return wsFlinkKubernetesTemplateMapper.insert(record);
    }

    @Override
    public int update(WsFlinkKubernetesTemplateUpdateParam param) {
        WsFlinkKubernetesTemplate record = new WsFlinkKubernetesTemplate();
        BeanUtils.copyProperties(param, record);
        return wsFlinkKubernetesTemplateMapper.updateById(record);
    }

    @Override
    public int updateTemplate(WsFlinkKubernetesTemplateDTO param) {
        WsFlinkKubernetesTemplateDTO mergeWithDefault = mergeDefault(param);
        mergeWithDefault.setAdditionalDependencies(param.getAdditionalDependencies());
        WsFlinkKubernetesTemplate record = WsFlinkKubernetesTemplateConvert.INSTANCE.toDo(mergeWithDefault);
        record.setId(param.getId());
        record.setProjectId(param.getProjectId());
        record.setTemplateId(param.getTemplateId());
        return wsFlinkKubernetesTemplateMapper.updateById(record);
    }

    @Override
    public int deleteById(Long id) {
        return wsFlinkKubernetesTemplateMapper.deleteById(id);
    }

    @Override
    public int deleteBatch(List<Long> ids) {
        return wsFlinkKubernetesTemplateMapper.deleteBatchIds(ids);
    }
}
