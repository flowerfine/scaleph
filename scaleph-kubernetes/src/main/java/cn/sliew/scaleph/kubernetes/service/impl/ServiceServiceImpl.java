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

import cn.sliew.scaleph.common.dict.flink.ServiceExposedType;
import cn.sliew.scaleph.kubernetes.service.KubernetesService;
import cn.sliew.scaleph.kubernetes.service.ServiceService;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import io.fabric8.kubernetes.api.model.networking.v1.IngressLoadBalancerIngress;
import io.fabric8.kubernetes.api.model.networking.v1.IngressLoadBalancerStatus;
import io.fabric8.kubernetes.api.model.networking.v1.IngressPortStatus;
import io.fabric8.kubernetes.client.NamespacedKubernetesClient;
import io.fabric8.kubernetes.client.dsl.Resource;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private KubernetesService kubernetesService;

    @Override
    public Optional<URI> getIngress(Long clusterCredentialId, String namespace, String name) {
        NamespacedKubernetesClient client = kubernetesService.getClient(clusterCredentialId, namespace);
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

    @Override
    public Optional<Map<String, URI>> getService(Long clusterCredentialId, String namespace, String name) {
        NamespacedKubernetesClient client = kubernetesService.getClient(clusterCredentialId, namespace);
        Resource<io.fabric8.kubernetes.api.model.Service> serviceResource = client.services()
                .inNamespace(namespace)
                .withName(name);
        if (serviceResource != null && serviceResource.isReady()) {
            io.fabric8.kubernetes.api.model.Service service = serviceResource.get();
            ServiceExposedType type = ServiceExposedType.of(service.getSpec().getType());
            switch (type) {
                case NODE_PORT:
                    return getNodePort(client.getMasterUrl().getHost(), service);
                case LOAD_BALANCER:
                    return getLoadBalancer(service);
                default:
                    return Optional.empty();
            }
        }
        return Optional.empty();
    }


    private Optional<Map<String, URI>> getLoadBalancer(io.fabric8.kubernetes.api.model.Service service) {
        String format = "http://${host}:${port}/";
        Optional<String> host = formatHost(service.getStatus().getLoadBalancer());
        if (host.isEmpty()) {
            return Optional.empty();
        }
        Map<String, URI> uris = new HashMap<>();
        for (ServicePort servicePort : service.getSpec().getPorts()) {
            Map<String, String> variables = Map.of("host", host.get(), "port", servicePort.getPort().toString());
            StrSubstitutor substitutor = new StrSubstitutor(variables);
            URI uri = URI.create(substitutor.replace(format));
            uris.put(servicePort.getName(), uri);
        }
        return Optional.of(uris);
    }

    private Optional<Map<String, URI>> getNodePort(String masterHost, io.fabric8.kubernetes.api.model.Service service) {
        String format = "http://${host}:${nodePort}/";
        Optional<String> host = formatHost(service.getStatus().getLoadBalancer());
        if (host.isEmpty()) {
            host = Optional.of(masterHost);
        }

        Map<String, URI> uris = new HashMap<>();
        for (ServicePort servicePort : service.getSpec().getPorts()) {
            Map<String, String> variables = Map.of("host", host.get(), "nodePort", servicePort.getNodePort().toString());
            StrSubstitutor substitutor = new StrSubstitutor(variables);
            URI uri = URI.create(substitutor.replace(format));
            uris.put(servicePort.getName(), uri);
        }
        return Optional.of(uris);
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

    private Optional<String> formatHost(IngressLoadBalancerStatus loadBalancer) {
        if (loadBalancer == null) {
            return Optional.empty();
        }
        for (IngressLoadBalancerIngress ingress : loadBalancer.getIngress()) {
            // Get by ip firstly
            String hostOrIp = ingress.getIp();
            if (!StringUtils.hasText(hostOrIp)) {
                // If ip is empty, get by hostname
                hostOrIp = ingress.getHostname();
            }
            for (IngressPortStatus portStatus : ingress.getPorts()) {
                return Optional.of(String.format("%s:%d", hostOrIp, portStatus.getPort()));
            }
            return Optional.ofNullable(hostOrIp);
        }
        return Optional.empty();
    }

    private Map<String, Integer> formatPort(ServiceSpec service) {
        Map<String, Integer> ports = new HashMap<>();
        for (ServicePort servicePort : service.getPorts()) {
            ports.put(servicePort.getName(), servicePort.getPort());
        }
        return ports;
    }
}
