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
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkKubernetesTemplate;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkKubernetesTemplateMapper;
import cn.sliew.scaleph.engine.flink.kubernetes.factory.FlinkTemplateFactory;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.IngressSpec;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.JobManagerSpec;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.TaskManagerSpec;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.util.TemplateMerger;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.template.FlinkTemplate;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.template.FlinkTemplateConverter;
import cn.sliew.scaleph.engine.flink.kubernetes.service.WsFlinkKubernetesTemplateService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.convert.WsFlinkKubernetesTemplateConvert;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesTemplateDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.param.WsFlinkKubernetesTemplateAddParam;
import cn.sliew.scaleph.engine.flink.kubernetes.service.param.WsFlinkKubernetesTemplateListParam;
import cn.sliew.scaleph.engine.flink.kubernetes.service.param.WsFlinkKubernetesTemplateUpdateParam;
import cn.sliew.scaleph.engine.flink.kubernetes.service.vo.KubernetesOptionsVO;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.fabric8.kubernetes.api.model.Pod;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

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
        FlinkTemplate defaultTemplate = FlinkTemplateFactory.create("default", "default", customTemplate);
        return doMergeDefault(FlinkTemplateConverter.INSTANCE.convertFrom(defaultTemplate));
    }

    private WsFlinkKubernetesTemplateDTO doMergeDefault(WsFlinkKubernetesTemplateDTO dto) {
        WsFlinkKubernetesTemplateDTO globalDefault = getGlobalDefault();
        WsFlinkKubernetesTemplateDTO result = new WsFlinkKubernetesTemplateDTO();
        result.setName(dto.getName());
        result.setNamespace(StringUtils.hasText(dto.getNamespace()) ? dto.getNamespace() : globalDefault.getNamespace());
        result.setKubernetesOptions(TemplateMerger.merge(globalDefault.getKubernetesOptions(), dto.getKubernetesOptions(), KubernetesOptionsVO.class));
        result.setJobManager(TemplateMerger.merge(globalDefault.getJobManager(), dto.getJobManager(), JobManagerSpec.class));
        result.setTaskManager(TemplateMerger.merge(globalDefault.getTaskManager(), dto.getTaskManager(), TaskManagerSpec.class));
        result.setPodTemplate(TemplateMerger.merge(globalDefault.getPodTemplate(), dto.getPodTemplate(), Pod.class));
        result.setFlinkConfiguration(TemplateMerger.merge(globalDefault.getFlinkConfiguration(), dto.getFlinkConfiguration(), Map.class));
        result.setLogConfiguration(TemplateMerger.merge(globalDefault.getLogConfiguration(), dto.getLogConfiguration(), Map.class));
        result.setIngress(TemplateMerger.merge(globalDefault.getIngress(), dto.getIngress(), IngressSpec.class));
        result.setRemark(StringUtils.hasText(dto.getRemark()) ? dto.getRemark() : globalDefault.getRemark());
        return result;
    }

    private WsFlinkKubernetesTemplateDTO getGlobalDefault() {
        FlinkTemplate template = FlinkTemplateFactory.getDefaults();
        return FlinkTemplateConverter.INSTANCE.convertFrom(template);
    }

    @Override
    public int insert(WsFlinkKubernetesTemplateAddParam param) {
        WsFlinkKubernetesTemplate record = new WsFlinkKubernetesTemplate();
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
