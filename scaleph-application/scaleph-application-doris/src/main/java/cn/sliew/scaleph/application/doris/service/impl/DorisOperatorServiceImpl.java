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

package cn.sliew.scaleph.application.doris.service.impl;

import cn.sliew.scaleph.application.doris.service.DorisOperatorService;
import cn.sliew.scaleph.application.doris.service.dto.WsDorisOperatorInstanceDTO;
import cn.sliew.scaleph.config.kubernetes.resource.ResourceLabels;
import cn.sliew.scaleph.kubernetes.service.KubernetesService;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Optional;

@Service
public class DorisOperatorServiceImpl implements DorisOperatorService {

    @Autowired
    private KubernetesService kubernetesService;

    @Override
    public Optional<GenericKubernetesResource> get(WsDorisOperatorInstanceDTO instanceDTO) throws Exception {
        KubernetesClient client = kubernetesService.getClient(instanceDTO.getClusterCredentialId());
        GenericKubernetesResource resource = client.genericKubernetesResources(ResourceLabels.DORIS_API_VERSION, ResourceLabels.DORIS_CLUSTER)
                .inNamespace(instanceDTO.getNamespace())
                .withName(instanceDTO.getInstanceId())
                .get();
        return Optional.ofNullable(resource);
    }

    @Override
    public void deploy(Long clusterCredentialId, String dorisCluster) {
        KubernetesClient client = kubernetesService.getClient(clusterCredentialId);
        client.load(new ByteArrayInputStream((dorisCluster).getBytes())).createOrReplace();
    }

    @Override
    public void apply(Long clusterCredentialId, String dorisCluster) {
        KubernetesClient client = kubernetesService.getClient(clusterCredentialId);
        client.load(new ByteArrayInputStream((dorisCluster).getBytes())).createOrReplace();
    }

    @Override
    public void shutdown(Long clusterCredentialId, String dorisCluster) {
        KubernetesClient client = kubernetesService.getClient(clusterCredentialId);
        client.load(new ByteArrayInputStream((dorisCluster).getBytes())).delete();
    }
}
