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

import cn.sliew.scaleph.common.dict.flink.ServiceExposedType;
import cn.sliew.scaleph.engine.flink.kubernetes.factory.FlinkTemplateFactory;
import cn.sliew.scaleph.engine.flink.kubernetes.service.FlinkJobManagerEndpointService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.WsFlinkKubernetesJobService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.WsFlinkKubernetesSessionClusterService;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesDeploymentDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesSessionClusterDTO;
import cn.sliew.scaleph.kubernetes.service.KubernetesService;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@Component
public class FlinkJobManagerEndpointServiceImpl implements FlinkJobManagerEndpointService {

    @Autowired
    private WsFlinkKubernetesSessionClusterService wsFlinkKubernetesSessionClusterService;
    @Autowired
    private WsFlinkKubernetesJobService wsFlinkKubernetesJobService;
    @Autowired
    private KubernetesService kubernetesService;

    @Override
    public URI getSessionClusterJobManagerEndpoint(Long sessionClusterId) {
        WsFlinkKubernetesSessionClusterDTO sessionClusterDTO = wsFlinkKubernetesSessionClusterService.selectOne(sessionClusterId);
        return getJobManagerEndpoint(sessionClusterDTO.getNamespace(), sessionClusterDTO.getSessionClusterId(), sessionClusterDTO.getClusterCredentialId()).orElse(null);
    }

    @Override
    public URI getJobManagerEndpoint(Long jobId) {
        WsFlinkKubernetesJobDTO jobDTO = wsFlinkKubernetesJobService.selectOne(jobId);
        String name = jobDTO.getJobId();
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
        KubernetesClient client = kubernetesService.getClient(clusterCredentialId);
        return getEndpointByIngress(namespace, name, client).or(() -> getEndpointByService(namespace, name, client));
    }

    /**
     * @see FlinkTemplateFactory#createIngressSpec()
     */
    private Optional<URI> getEndpointByIngress(String namespace, String name, KubernetesClient client) {
        Resource<Ingress> ingressResource = client.resources(Ingress.class)
                .inNamespace(namespace)
                .withName(name);
        if (ingressResource != null && ingressResource.isReady()) {
            Ingress ingress = ingressResource.get();
            String format = "https://${host}/${namespace}/${name}/";
            Optional<String> host = formatHost(ingress.getStatus().getLoadBalancer());
            if (host.isEmpty()) {
                return Optional.empty();
            }
            Map<String, String> variables = Map.of("host", host.get(), "namespace", namespace, "name", name);
            StrSubstitutor substitutor = new StrSubstitutor(variables);
            return Optional.of(URI.create(substitutor.replace(format)));
        }
        return Optional.empty();
    }

    /**
     * @see FlinkTemplateFactory#createServiceConfiguration()
     */
    private Optional<URI> getEndpointByService(String namespace, String name, KubernetesClient client) {
        Resource<Service> serviceResource = client.services()
                .inNamespace(namespace)
                .withName(String.format("%s-rest", name));
        if (serviceResource != null && serviceResource.isReady()) {
            Service service = serviceResource.get();
            ServiceExposedType type = ServiceExposedType.of(service.getSpec().getType());
            switch (type) {
                case NODE_PORT:
                    return doGetEndpointByNodePort(service);
                case LOAD_BALANCER:
                    return doGetEndpointByLoadBalancer(service);
                default:
                    return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private Optional<URI> doGetEndpointByLoadBalancer(Service service) {
        String format = "http://${host}:${port}/";
        Optional<String> host = formatHost(service.getStatus().getLoadBalancer());
        if (host.isEmpty()) {
            return Optional.empty();
        }
        Optional<Integer> port = formatPort(service.getSpec());
        if (port.isEmpty()) {
            return Optional.empty();
        }
        Map<String, String> variables = Map.of("host", host.get(), "port", port.get().toString());
        StrSubstitutor substitutor = new StrSubstitutor(variables);
        return Optional.of(URI.create(substitutor.replace(format)));
    }

    private Optional<URI> doGetEndpointByNodePort(Service service) {
        String format = "http://${host}:${nodePort}/";
        Optional<String> host = formatHost(service.getStatus().getLoadBalancer());
        if (host.isEmpty()) {
            return Optional.empty();
        }
        Optional<Integer> nodePort = formatNodePort(service.getSpec());
        if (nodePort.isEmpty()) {
            return Optional.empty();
        }
        Map<String, String> variables = Map.of("host", host.get(), "nodePort", nodePort.get().toString());
        StrSubstitutor substitutor = new StrSubstitutor(variables);
        return Optional.of(URI.create(substitutor.replace(format)));
    }

    private Optional<String> formatHost(LoadBalancerStatus loadBalancer) {
        if (loadBalancer == null) {
            return Optional.empty();
        }
        for (LoadBalancerIngress ingress : loadBalancer.getIngress()) {
            // Get by ip firstly
            String hostOrIp = ingress.getIp();
            if (!StringUtils.hasText(hostOrIp)) {
                // If ip is empty, get by hostname
                hostOrIp = ingress.getHostname();
            }
            for (PortStatus portStatus : ingress.getPorts()) {
                return Optional.of(String.format("%s:%d", hostOrIp, portStatus.getPort()));
            }
            return Optional.ofNullable(hostOrIp);
        }
        return Optional.empty();
    }

    private Optional<Integer> formatPort(ServiceSpec service) {
        for (ServicePort servicePort : service.getPorts()) {
            if (servicePort.getName().equals("rest")) {
                return Optional.of(servicePort.getPort());
            }
        }
        return Optional.empty();
    }

    private Optional<Integer> formatNodePort(ServiceSpec service) {
        for (ServicePort servicePort : service.getPorts()) {
            if (servicePort.getName().equals("rest")) {
                return Optional.of(servicePort.getNodePort());
            }
        }
        return Optional.empty();
    }
}
