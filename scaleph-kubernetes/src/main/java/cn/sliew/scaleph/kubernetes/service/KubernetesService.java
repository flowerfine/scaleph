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

package cn.sliew.scaleph.kubernetes.service;

import cn.sliew.scaleph.resource.service.dto.ClusterCredentialDTO;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.NamespacedKubernetesClient;

import java.io.IOException;
import java.nio.file.Path;

public interface KubernetesService {

    KubernetesClient getClient(Long clusterCredentialId);

    NamespacedKubernetesClient getClient(Long clusterCredentialId, String namespace);

    Path downloadConfig(ClusterCredentialDTO clusterCredential) throws IOException;

    Config getConfig(Long clusterCredentialId) throws IOException;

}
