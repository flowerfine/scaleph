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

package cn.sliew.scaleph.application.flink.resource.definition.template;

import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesTemplateDTO;
import cn.sliew.scaleph.application.flink.service.vo.KubernetesOptionsVO;
import cn.sliew.scaleph.common.dict.flink.kubernetes.OperatorFlinkVersion;
import cn.sliew.scaleph.config.kubernetes.resource.ResourceLabels;
import cn.sliew.scaleph.application.flink.operator.spec.FlinkVersion;
import cn.sliew.scaleph.application.flink.resource.handler.FlinkVersionMapping;
import cn.sliew.scaleph.kubernetes.resource.ResourceConverter;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

public enum FlinkTemplateConverter implements ResourceConverter<WsFlinkKubernetesTemplateDTO, FlinkTemplate> {
    INSTANCE;

    @Override
    public FlinkTemplate convertTo(WsFlinkKubernetesTemplateDTO source) {
        FlinkTemplate template = new FlinkTemplate();
        ObjectMetaBuilder builder = new ObjectMetaBuilder();
        FlinkTemplateSpec spec = new FlinkTemplateSpec();

        String name = StringUtils.hasText(source.getTemplateId()) ? source.getTemplateId() : source.getName();
        builder.withName(name);
        builder.withNamespace(source.getNamespace());
        builder.addToLabels(ResourceLabels.SCALEPH_LABEL_NAME, source.getName());

        KubernetesOptionsVO kuberenetesOptions = source.getKubernetesOptions();
        if (kuberenetesOptions != null) {
            spec.setImage(kuberenetesOptions.getImage());
            spec.setImagePullPolicy(kuberenetesOptions.getImagePullPolicy());
            spec.setServiceAccount(kuberenetesOptions.getServiceAccount());
            if (StringUtils.hasLength(kuberenetesOptions.getFlinkVersion())) {
                builder.addToLabels(ResourceLabels.SCALEPH_LABEL_FLINK_VERSION, kuberenetesOptions.getFlinkVersion());
            }
            FlinkVersionMapping flinkVersionMapping = FlinkVersionMapping.of(cn.sliew.scaleph.common.dict.flink.FlinkVersion.of(kuberenetesOptions.getFlinkVersion()));
            spec.setFlinkVersion(EnumUtils.getEnum(FlinkVersion.class, flinkVersionMapping.getMajorVersion().getValue()));
        }
        spec.setFlinkConfiguration(source.getFlinkConfiguration());
        spec.setJobManager(source.getJobManager());
        spec.setTaskManager(source.getTaskManager());
        spec.setPodTemplate(source.getPodTemplate());
        spec.setIngress(source.getIngress());

        template.setMetadata(builder.build());
        template.setSpec(spec);
        return template;
    }

    @Override
    public WsFlinkKubernetesTemplateDTO convertFrom(FlinkTemplate target) {
        WsFlinkKubernetesTemplateDTO dto = new WsFlinkKubernetesTemplateDTO();
        ObjectMeta metadata = target.getMetadata();
        FlinkTemplateSpec spec = target.getSpec();
        String name = metadata.getName();
        String flinkVersion = null;
        if (metadata.getLabels() != null) {
            Map<String, String> labels = metadata.getLabels();
            name = labels.computeIfAbsent(ResourceLabels.SCALEPH_LABEL_NAME, key -> metadata.getName());
            flinkVersion = labels.get(ResourceLabels.SCALEPH_LABEL_FLINK_VERSION);
        }
        dto.setName(name);
        dto.setTemplateId(metadata.getName());
        dto.setNamespace(metadata.getNamespace());

        KubernetesOptionsVO optionsVO = new KubernetesOptionsVO();
        optionsVO.setImage(spec.getImage());
        optionsVO.setImagePullPolicy(spec.getImagePullPolicy());
        optionsVO.setServiceAccount(spec.getServiceAccount());
        cn.sliew.scaleph.common.dict.flink.FlinkVersion version = null;
        if (StringUtils.hasLength(flinkVersion)) {
            version = cn.sliew.scaleph.common.dict.flink.FlinkVersion.of(flinkVersion);
        } else if (spec.getFlinkVersion() != null) {
            FlinkVersionMapping flinkVersionMapping = FlinkVersionMapping.of(OperatorFlinkVersion.of(spec.getFlinkVersion().name()));
            version = flinkVersionMapping.getDefaultVersion();
        }
        if (version != null) {
            optionsVO.setFlinkVersion(version.getValue());
        }
        dto.setKubernetesOptions(optionsVO);
        dto.setFlinkConfiguration(spec.getFlinkConfiguration());
        dto.setJobManager(spec.getJobManager());
        dto.setTaskManager(spec.getTaskManager());
        dto.setPodTemplate(spec.getPodTemplate());
        dto.setIngress(spec.getIngress());
        return dto;
    }
}
