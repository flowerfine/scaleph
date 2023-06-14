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

package cn.sliew.scaleph.engine.flink.kubernetes.resource.deployment;

import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.FlinkDeploymentSpec;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.FlinkVersion;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesDeploymentDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.vo.KubernetesOptionsVO;
import cn.sliew.scaleph.kubernetes.Constant;
import cn.sliew.scaleph.kubernetes.ResourceConverter;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

public enum FlinkDeploymentConverter implements ResourceConverter<WsFlinkKubernetesDeploymentDTO, FlinkDeployment> {
    INSTANCE;

    @Override
    public FlinkDeployment convertTo(WsFlinkKubernetesDeploymentDTO source) {
        FlinkDeployment deployment = new FlinkDeployment();
        ObjectMetaBuilder builder = new ObjectMetaBuilder(true);
        String name = StringUtils.hasText(source.getDeploymentId()) ? source.getDeploymentId() : source.getName();
        builder.withName(name);
        builder.withNamespace(source.getNamespace());
        builder.withAdditionalProperties(Map.of(Constant.SCALEPH_NAME, source.getName()));
        deployment.setMetadata(builder.build());
        FlinkDeploymentSpec spec = new FlinkDeploymentSpec();
        KubernetesOptionsVO kuberenetesOptions = source.getKubernetesOptions();
        if (kuberenetesOptions != null) {
            spec.setImage(kuberenetesOptions.getImage());
            spec.setImagePullPolicy(kuberenetesOptions.getImagePullPolicy());
            spec.setServiceAccount(kuberenetesOptions.getServiceAccount());
            spec.setFlinkVersion(EnumUtils.getEnum(FlinkVersion.class, kuberenetesOptions.getFlinkVersion()));
        }
        spec.setJobManager(source.getJobManager());
        spec.setTaskManager(source.getTaskManager());
        spec.setPodTemplate(source.getPodTemplate());
        spec.setFlinkConfiguration(source.getFlinkConfiguration());
        spec.setLogConfiguration(source.getLogConfiguration());
        spec.setIngress(source.getIngress());
        deployment.setSpec(spec);
        return deployment;
    }

    @Override
    public WsFlinkKubernetesDeploymentDTO convertFrom(FlinkDeployment target) {
        WsFlinkKubernetesDeploymentDTO dto = new WsFlinkKubernetesDeploymentDTO();
        String name = target.getMetadata().getName();
        if (target.getMetadata().getAdditionalProperties() != null) {
            Map<String, Object> additionalProperties = target.getMetadata().getAdditionalProperties();
            name = (String) additionalProperties.computeIfAbsent(Constant.SCALEPH_NAME, key -> target.getMetadata().getName());
        }
        dto.setName(name);
        dto.setDeploymentId(target.getMetadata().getName());
        dto.setNamespace(target.getMetadata().getNamespace());
        FlinkDeploymentSpec spec = target.getSpec();
        KubernetesOptionsVO optionsVO = new KubernetesOptionsVO();
        optionsVO.setImage(spec.getImage());
        optionsVO.setImagePullPolicy(spec.getImagePullPolicy());
        optionsVO.setServiceAccount(spec.getServiceAccount());
        if (spec.getFlinkVersion() != null) {
            optionsVO.setFlinkVersion(spec.getFlinkVersion().name());
        }
        dto.setKubernetesOptions(optionsVO);
        dto.setJobManager(spec.getJobManager());
        dto.setTaskManager(spec.getTaskManager());
        dto.setPodTemplate(spec.getPodTemplate());
        dto.setFlinkConfiguration(spec.getFlinkConfiguration());
        dto.setLogConfiguration(spec.getLogConfiguration());
        dto.setIngress(spec.getIngress());
        return dto;
    }
}
