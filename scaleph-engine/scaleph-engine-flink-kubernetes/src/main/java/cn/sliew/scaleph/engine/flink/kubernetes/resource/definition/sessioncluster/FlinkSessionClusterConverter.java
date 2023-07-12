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

package cn.sliew.scaleph.engine.flink.kubernetes.resource.definition.sessioncluster;

import cn.sliew.scaleph.config.resource.ResourceLabels;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.FlinkSessionClusterSpec;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.FlinkVersion;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.KubernetesDeploymentMode;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesSessionClusterDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.vo.KubernetesOptionsVO;
import cn.sliew.scaleph.kubernetes.resource.ResourceConverter;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.util.StringUtils;

import java.util.Map;

public enum FlinkSessionClusterConverter implements ResourceConverter<WsFlinkKubernetesSessionClusterDTO, FlinkSessionCluster> {
    INSTANCE;

    @Override
    public FlinkSessionCluster convertTo(WsFlinkKubernetesSessionClusterDTO source) {
        FlinkSessionCluster sessionCluster = new FlinkSessionCluster();
        ObjectMetaBuilder builder = new ObjectMetaBuilder(true);
        String name = StringUtils.hasText(source.getSessionClusterId()) ? source.getSessionClusterId() : source.getName();
        builder.withName(name);
        builder.withNamespace(source.getNamespace());
        builder.withLabels(Map.of(ResourceLabels.SCALEPH_LABEL_NAME, source.getName()));
        sessionCluster.setMetadata(builder.build());
        FlinkSessionClusterSpec spec = new FlinkSessionClusterSpec();
        KubernetesOptionsVO kuberenetesOptions = source.getKubernetesOptions();
        if (kuberenetesOptions != null) {
            spec.setImage(kuberenetesOptions.getImage());
            spec.setImagePullPolicy(kuberenetesOptions.getImagePullPolicy());
            spec.setServiceAccount(kuberenetesOptions.getServiceAccount());
            spec.setFlinkVersion(EnumUtils.getEnum(FlinkVersion.class, kuberenetesOptions.getFlinkVersion()));
        }
        spec.setFlinkConfiguration(source.getFlinkConfiguration());
        spec.setJobManager(source.getJobManager());
        spec.setTaskManager(source.getTaskManager());
        spec.setPodTemplate(source.getPodTemplate());
        spec.setIngress(source.getIngress());
        spec.setMode(KubernetesDeploymentMode.NATIVE);
        sessionCluster.setSpec(spec);
        return sessionCluster;
    }

    @Override
    public WsFlinkKubernetesSessionClusterDTO convertFrom(FlinkSessionCluster target) {
        WsFlinkKubernetesSessionClusterDTO dto = new WsFlinkKubernetesSessionClusterDTO();
        String name = target.getMetadata().getName();
        if (target.getMetadata().getLabels() != null) {
            Map<String, String> labels = target.getMetadata().getLabels();
            name = labels.computeIfAbsent(ResourceLabels.SCALEPH_LABEL_NAME, key -> target.getMetadata().getName());
        }
        dto.setName(name);
        dto.setSessionClusterId(target.getMetadata().getName());
        dto.setNamespace(target.getMetadata().getNamespace());
        dto.setSessionClusterId(target.getMetadata().getUid());
        FlinkSessionClusterSpec spec = target.getSpec();
        KubernetesOptionsVO kuberenetesOptions = new KubernetesOptionsVO();
        if (kuberenetesOptions != null) {
            kuberenetesOptions.setImage(spec.getImage());
            kuberenetesOptions.setImagePullPolicy(spec.getImagePullPolicy());
            kuberenetesOptions.setServiceAccount(spec.getServiceAccount());
            if (spec.getFlinkVersion() != null) {
                kuberenetesOptions.setFlinkVersion(spec.getFlinkVersion().name());
            }
        }
        dto.setKubernetesOptions(kuberenetesOptions);
        dto.setFlinkConfiguration(spec.getFlinkConfiguration());
        dto.setJobManager(spec.getJobManager());
        dto.setTaskManager(spec.getTaskManager());
        dto.setPodTemplate(spec.getPodTemplate());
        dto.setIngress(spec.getIngress());
        return dto;
    }
}
