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

package cn.sliew.scaleph.application.doris.service.resource.cluster;

import cn.sliew.scaleph.config.kubernetes.resource.ResourceLabels;
import cn.sliew.scaleph.application.doris.operator.DorisCluster;
import cn.sliew.scaleph.application.doris.operator.spec.DorisClusterSpec;
import cn.sliew.scaleph.application.doris.service.dto.WsDorisOperatorInstanceDTO;
import cn.sliew.scaleph.kubernetes.resource.ResourceConverter;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import org.springframework.util.StringUtils;

import java.util.Map;

public enum DorisClusterConverter implements ResourceConverter<WsDorisOperatorInstanceDTO, DorisCluster> {
    INSTANCE;

    @Override
    public DorisCluster convertTo(WsDorisOperatorInstanceDTO source) {
        DorisCluster cluster = new DorisCluster();
        ObjectMetaBuilder builder = new ObjectMetaBuilder();
        String name = StringUtils.hasText(source.getInstanceId()) ? source.getInstanceId() : source.getName();
        builder.withName(name);
        builder.withNamespace(source.getNamespace());
        builder.addToLabels(ResourceLabels.SCALEPH_LABEL_NAME, source.getName());
        builder.addToLabels(ResourceLabels.DORIS_APP_NAME, ResourceLabels.DORIS_APP_NAME_VALUE);
        builder.addToLabels(ResourceLabels.DORIS_APP_INSTANCE, name);
        builder.addToLabels(ResourceLabels.DORIS_APP_PART_OF, ResourceLabels.DORIS_APP_PART_OF_VALUE);
        cluster.setMetadata(builder.build());
        DorisClusterSpec spec = new DorisClusterSpec();
        spec.setFeSpec(source.getFeSpec());
        spec.setBeSpec(source.getBeSpec());
        spec.setCnSpec(source.getCnSpec());
        spec.setBrokerSpec(source.getBrokerSpec());
        spec.setAdminUser(source.getAdmin());
        cluster.setSpec(spec);
        return cluster;
    }

    @Override
    public WsDorisOperatorInstanceDTO convertFrom(DorisCluster target) {
        WsDorisOperatorInstanceDTO dto = new WsDorisOperatorInstanceDTO();
        String name = target.getMetadata().getName();
        if (target.getMetadata().getLabels() != null) {
            Map<String, String> labels = target.getMetadata().getLabels();
            name = labels.computeIfAbsent(ResourceLabels.SCALEPH_LABEL_NAME, key -> target.getMetadata().getName());
        }
        dto.setName(name);
        dto.setInstanceId(target.getMetadata().getName());
        dto.setNamespace(target.getMetadata().getNamespace());

        DorisClusterSpec spec = target.getSpec();
        dto.setFeSpec(spec.getFeSpec());
        dto.setBeSpec(spec.getBeSpec());
        dto.setCnSpec(spec.getCnSpec());
        dto.setBrokerSpec(spec.getBrokerSpec());
        dto.setAdmin(spec.getAdminUser());
        return dto;
    }
}
