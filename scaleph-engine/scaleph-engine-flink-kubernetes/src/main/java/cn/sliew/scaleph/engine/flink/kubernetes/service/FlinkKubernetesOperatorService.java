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

package cn.sliew.scaleph.engine.flink.kubernetes.service;

import cn.sliew.scaleph.engine.flink.kubernetes.resource.definition.sessioncluster.FlinkSessionCluster;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesSessionClusterDTO;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;

import java.util.Optional;

public interface FlinkKubernetesOperatorService {

    Optional<GenericKubernetesResource> getSessionCluster(WsFlinkKubernetesSessionClusterDTO sessionClusterDTO) throws Exception;

    void deploySessionCluster(Long clusterCredentialId, FlinkSessionCluster sessionCluster) throws Exception;

    void shutdownSessionCluster(Long clusterCredentialId, FlinkSessionCluster sessionCluster) throws Exception;

    Optional<GenericKubernetesResource> getJob(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO) throws Exception;

    void deployJob(Long clusterCredentialId, String job) throws Exception;

    void shutdownJob(Long clusterCredentialId, String job) throws Exception;

    void restartJob(Long clusterCredentialId, String job) throws Exception;

    void triggerSavepoint(Long clusterCredentialId, String job) throws Exception;

    void suspendJob(Long clusterCredentialId, String job) throws Exception;

    void resumeJob(Long clusterCredentialId, String job) throws Exception;

}
