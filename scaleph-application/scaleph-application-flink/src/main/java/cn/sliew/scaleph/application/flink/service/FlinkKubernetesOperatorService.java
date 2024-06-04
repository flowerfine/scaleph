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

package cn.sliew.scaleph.application.flink.service;

import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesSessionClusterDTO;
import cn.sliew.scaleph.application.flink.resource.definition.sessioncluster.FlinkSessionCluster;
import cn.sliew.scaleph.application.flink.watch.FlinkDeploymentShardWatcher;
import cn.sliew.scaleph.kubernetes.watch.watch.WatchCallbackHandler;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.Watch;

import java.util.Map;
import java.util.Optional;

public interface FlinkKubernetesOperatorService {

    Optional<GenericKubernetesResource> getSessionCluster(WsFlinkKubernetesSessionClusterDTO sessionClusterDTO) throws Exception;

    void deploySessionCluster(Long clusterCredentialId, FlinkSessionCluster sessionCluster) throws Exception;

    void shutdownSessionCluster(Long clusterCredentialId, FlinkSessionCluster sessionCluster) throws Exception;

    Optional<GenericKubernetesResource> getJob(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO) throws Exception;

    Optional<GenericKubernetesResource> getFlinkDeployment(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO) throws Exception;

    Optional<GenericKubernetesResource> getFlinkSessionJob(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO) throws Exception;

    void shutdownJob(Long clusterCredentialId, String job) throws Exception;

    void applyJob(Long clusterCredentialId, String job) throws Exception;

    /**
     * todo when and how to close watch
     */
    <T extends HasMetadata> Watch addWatch(Long clusterCredentialId, String resource, Map<String, String> labels, WatchCallbackHandler<T> callbackHandler) throws Exception;

    FlinkDeploymentShardWatcher addFlinkDeploymentSharedWatcher(Long clusterCredentialId, String namespace, Map<String, String> labels) throws Exception;

}
