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
package cn.sliew.scaleph.engine.flink.kubernetes.service.impl;

import cn.sliew.scaleph.engine.flink.kubernetes.service.FlinkJobManagerEndpointService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.WsFlinkKubernetesJobService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.WsFlinkKubernetesSessionClusterService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesDeploymentDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesSessionClusterDTO;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.Map;

@Service
public class FlinkJobManagerEndpointServiceImpl implements FlinkJobManagerEndpointService {

    @Autowired
    private WsFlinkKubernetesSessionClusterService wsFlinkKubernetesSessionClusterService;
    @Autowired
    private WsFlinkKubernetesJobService wsFlinkKubernetesJobService;

    @Override
    public URI getSessionClusterJobManagerEndpoint(Long sessionClusterId) {
        WsFlinkKubernetesSessionClusterDTO sessionClusterDTO = wsFlinkKubernetesSessionClusterService.selectOne(sessionClusterId);
        return getJobManagerEndpoint(sessionClusterDTO);
    }

    @Override
    public URI getJobManagerEndpoint(Long jobId) {
        final WsFlinkKubernetesJobDTO jobDTO = wsFlinkKubernetesJobService.selectOne(jobId);
        switch (jobDTO.getDeploymentKind()) {
            case FLINK_SESSION_JOB:
                return getJobManagerEndpoint(jobDTO.getFlinkSessionCluster());
            case FLINK_DEPLOYMENT:
                return getJobManagerEndpoint(jobDTO.getFlinkDeployment());
            default:

        }
        return null;
    }

    private URI getJobManagerEndpoint(WsFlinkKubernetesSessionClusterDTO sessionCluster) {
        String format = "https://${host}/${namespace}/${name}/";
        String host = getIngressHosts(sessionCluster.getClusterCredentialId(), sessionCluster.getName());
        String name = StringUtils.hasText(sessionCluster.getSessionClusterId()) ? sessionCluster.getSessionClusterId() : sessionCluster.getName();
        Map<String, String> variables = Map.of("host", host, "namespace", sessionCluster.getNamespace(), "name", name);
        StrSubstitutor substitutor = new StrSubstitutor(variables);
        return URI.create(substitutor.replace(format));
    }

    private URI getJobManagerEndpoint(WsFlinkKubernetesDeploymentDTO deployment) {
        String format = "https://${host}/${namespace}/${name}/";
        String host = getIngressHosts(deployment.getClusterCredentialId(), deployment.getName());
        String name = StringUtils.hasText(deployment.getDeploymentId()) ? deployment.getDeploymentId() : deployment.getName();
        Map<String, String> variables = Map.of("host", host, "namespace", deployment.getNamespace(), "name", name);
        StrSubstitutor substitutor = new StrSubstitutor(variables);
        return URI.create(substitutor.replace(format));
    }

    private static String getIngressHosts(Long clusterCredentialId, String name) {
        return "localhost";
    }
}
