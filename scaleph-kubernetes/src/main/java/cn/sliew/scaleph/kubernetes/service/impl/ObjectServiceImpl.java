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

package cn.sliew.scaleph.kubernetes.service.impl;

import cn.sliew.scaleph.kubernetes.service.KubernetesService;
import cn.sliew.scaleph.kubernetes.service.ObjectService;
import cn.sliew.scaleph.kubernetes.service.param.VersionGroupKind;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.StatusDetails;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ObjectServiceImpl implements ObjectService {

    @Autowired
    private KubernetesService kubernetesService;

    @Override
    public List<HasMetadata> applyResource(Long clusterCredentialId, String resource) throws Exception {
        KubernetesClient client = kubernetesService.getClient(clusterCredentialId);
        return client.load(new ByteArrayInputStream((resource).getBytes())).createOrReplace();
    }

    @Override
    public void deleteResource(Long clusterCredentialId, String resource) throws Exception {
        KubernetesClient client = kubernetesService.getClient(clusterCredentialId);
        List<StatusDetails> detailsList = client.load(new ByteArrayInputStream((resource).getBytes())).delete();
    }

    @Override
    public Optional<GenericKubernetesResource> getResource(Long clusterCredentialId, VersionGroupKind versionAndGroup) throws Exception {
        KubernetesClient client = kubernetesService.getClient(clusterCredentialId);
        GenericKubernetesResource resource = client.genericKubernetesResources(versionAndGroup.getApiVersion(), versionAndGroup.getKind())
                .inNamespace(versionAndGroup.getNamespace())
                .withName(versionAndGroup.getName())
                .get();
        return Optional.ofNullable(resource);
    }
}
