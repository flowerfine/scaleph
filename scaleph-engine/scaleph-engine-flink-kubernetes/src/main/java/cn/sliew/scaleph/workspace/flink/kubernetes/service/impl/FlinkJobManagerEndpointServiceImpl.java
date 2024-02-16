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

package cn.sliew.scaleph.workspace.flink.kubernetes.service.impl;

import cn.sliew.scaleph.workspace.flink.kubernetes.factory.FlinkDefaultTemplateFactory;
import cn.sliew.scaleph.workspace.flink.kubernetes.service.FlinkJobManagerEndpointService;
import cn.sliew.scaleph.workspace.flink.kubernetes.service.WsFlinkKubernetesJobInstanceService;
import cn.sliew.scaleph.workspace.flink.kubernetes.service.WsFlinkKubernetesSessionClusterService;
import cn.sliew.scaleph.workspace.flink.kubernetes.service.dto.WsFlinkKubernetesDeploymentDTO;
import cn.sliew.scaleph.workspace.flink.kubernetes.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.workspace.flink.kubernetes.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.workspace.flink.kubernetes.service.dto.WsFlinkKubernetesSessionClusterDTO;
import cn.sliew.scaleph.kubernetes.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@Component
public class FlinkJobManagerEndpointServiceImpl implements FlinkJobManagerEndpointService {

    @Autowired
    private WsFlinkKubernetesSessionClusterService wsFlinkKubernetesSessionClusterService;
    @Autowired
    private WsFlinkKubernetesJobInstanceService wsFlinkKubernetesJobInstanceService;
    @Autowired
    private ServiceService serviceService;

    @Override
    public URI getSessionClusterJobManagerEndpoint(Long sessionClusterId) {
        WsFlinkKubernetesSessionClusterDTO sessionClusterDTO = wsFlinkKubernetesSessionClusterService.selectOne(sessionClusterId);
        return getJobManagerEndpoint(sessionClusterDTO.getNamespace(), sessionClusterDTO.getSessionClusterId(), sessionClusterDTO.getClusterCredentialId()).orElse(null);
    }

    @Override
    public URI getJobManagerEndpoint(Long jobInstanceId) {
        WsFlinkKubernetesJobInstanceDTO jobInstanceDTO = wsFlinkKubernetesJobInstanceService.selectOne(jobInstanceId);
        WsFlinkKubernetesJobDTO jobDTO = jobInstanceDTO.getWsFlinkKubernetesJob();
        String name = jobInstanceDTO.getInstanceId();
        switch (jobDTO.getDeploymentKind()) {
            case FLINK_SESSION_JOB:
                WsFlinkKubernetesSessionClusterDTO sessionClusterDTO = jobDTO.getFlinkSessionCluster();
                return getJobManagerEndpoint(sessionClusterDTO.getNamespace(), name, sessionClusterDTO.getClusterCredentialId()).orElse(null);
            case FLINK_DEPLOYMENT:
                WsFlinkKubernetesDeploymentDTO deploymentDTO = jobDTO.getFlinkDeployment();
                return getJobManagerEndpoint(deploymentDTO.getNamespace(), name, deploymentDTO.getClusterCredentialId()).orElse(null);
            default:
        }
        return null;
    }

    private Optional<URI> getJobManagerEndpoint(String namespace, String name, Long clusterCredentialId) {
        return getEndpointByIngress(clusterCredentialId, namespace, name).or(() -> getEndpointByService(clusterCredentialId, namespace, name));
    }

    /**
     * @see FlinkDefaultTemplateFactory#createIngressSpec()
     */
    private Optional<URI> getEndpointByIngress(Long clusterCredentialId, String namespace, String name) {
        return serviceService.getIngress(clusterCredentialId, namespace, name);
    }

    /**
     * @see FlinkDefaultTemplateFactory#createServiceConfiguration()
     */
    private Optional<URI> getEndpointByService(Long clusterCredentialId, String namespace, String name) {
        String serviceName = String.format("%s-rest", name);
        Optional<Map<String, URI>> optional = serviceService.getService(clusterCredentialId, namespace, serviceName);
        if (optional.isPresent()) {
            Map<String, URI> uris = optional.get();
            if (uris.containsKey("rest")) {
                return Optional.of(uris.get("rest"));
            }
        }
        return Optional.empty();
    }
}
