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

import cn.sliew.scaleph.workspace.flink.kubernetes.factory.FlinkDeploymentFactory;
import cn.sliew.scaleph.workspace.flink.kubernetes.resource.definition.deployment.FlinkDeployment;
import cn.sliew.scaleph.workspace.flink.kubernetes.resource.definition.sessioncluster.FlinkSessionCluster;
import cn.sliew.scaleph.workspace.flink.kubernetes.service.FlinkKubernetesOperatorService;
import cn.sliew.scaleph.workspace.flink.kubernetes.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.workspace.flink.kubernetes.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.workspace.flink.kubernetes.service.dto.WsFlinkKubernetesSessionClusterDTO;
import cn.sliew.scaleph.workspace.flink.kubernetes.watch.FlinkDeploymentShardWatcher;
import cn.sliew.scaleph.kubernetes.Constant;
import cn.sliew.scaleph.kubernetes.service.KubernetesService;
import cn.sliew.scaleph.kubernetes.watch.DefaultKubernetesWatcher;
import cn.sliew.scaleph.kubernetes.watch.WatchCallbackHandler;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.NamespacedKubernetesClient;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FlinkKubernetesOperatorServiceImpl implements FlinkKubernetesOperatorService {

    @Autowired
    private KubernetesService kubernetesService;

    @Override
    public Optional<GenericKubernetesResource> getSessionCluster(WsFlinkKubernetesSessionClusterDTO sessionClusterDTO) throws Exception {
        KubernetesClient client = kubernetesService.getClient(sessionClusterDTO.getClusterCredentialId());
        GenericKubernetesResource resource = client.genericKubernetesResources(Constant.API_VERSION, Constant.FLINK_DEPLOYMENT)
                .inNamespace(sessionClusterDTO.getNamespace())
                .withName(sessionClusterDTO.getSessionClusterId())
                .get();
        return Optional.ofNullable(resource);
    }

    @Override
    public void deploySessionCluster(Long clusterCredentialId, FlinkSessionCluster sessionCluster) throws Exception {
        KubernetesClient client = kubernetesService.getClient(clusterCredentialId);
        FlinkDeployment deployment = FlinkDeploymentFactory.fromSessionCluster(sessionCluster);
        // fixme 这里多做了一层转化，用对象会报错
        client.resource(Serialization.asYaml(deployment)).createOrReplace();
    }

    @Override
    public void shutdownSessionCluster(Long clusterCredentialId, FlinkSessionCluster sessionCluster) throws Exception {
        KubernetesClient client = kubernetesService.getClient(clusterCredentialId);
        FlinkDeployment deployment = FlinkDeploymentFactory.fromSessionCluster(sessionCluster);
        client.resource(Serialization.asYaml(deployment)).delete();
    }

    @Override
    public Optional<GenericKubernetesResource> getJob(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO) throws Exception {
        final WsFlinkKubernetesJobDTO jobDto = jobInstanceDTO.getWsFlinkKubernetesJob();
        Long clusterCredentialId = null;
        String namespace = null;
        switch (jobDto.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                clusterCredentialId = jobDto.getFlinkDeployment().getClusterCredentialId();
                namespace = jobDto.getFlinkDeployment().getNamespace();
                break;
            case FLINK_SESSION_JOB:
                clusterCredentialId = jobDto.getFlinkSessionCluster().getClusterCredentialId();
                namespace = jobDto.getFlinkSessionCluster().getNamespace();
                break;
            default:
        }
        KubernetesClient client = kubernetesService.getClient(clusterCredentialId);
        // fixme only support flinkdeployment
        GenericKubernetesResource resource = client.genericKubernetesResources(Constant.API_VERSION, Constant.FLINK_DEPLOYMENT)
                .inNamespace(namespace)
                .withName(jobInstanceDTO.getInstanceId())
                .get();
        return Optional.ofNullable(resource);
    }

    @Override
    public void deployJob(Long clusterCredentialId, String job) throws Exception {
        KubernetesClient client = kubernetesService.getClient(clusterCredentialId);
        client.load(new ByteArrayInputStream((job).getBytes())).createOrReplace();
    }

    @Override
    public void shutdownJob(Long clusterCredentialId, String job) throws Exception {
        KubernetesClient client = kubernetesService.getClient(clusterCredentialId);
        client.load(new ByteArrayInputStream((job).getBytes())).delete();
    }

    @Override
    public void applyJob(Long clusterCredentialId, String job) throws Exception {
        KubernetesClient client = kubernetesService.getClient(clusterCredentialId);
        client.load(new ByteArrayInputStream((job).getBytes())).createOrReplace();
    }

    public <T extends HasMetadata> Watch addWatch(Long clusterCredentialId, String resource, Map<String, String> labels, WatchCallbackHandler<T> callbackHandler) throws Exception {
        KubernetesClient client = kubernetesService.getClient(clusterCredentialId);
        return client.genericKubernetesResources(Constant.API_VERSION, resource)
                .withLabels(labels)
                .withResourceVersion(Constant.KUBERNETES_ZERO_RESOURCE_VERSION)
                .watch(new DefaultKubernetesWatcher(callbackHandler));
    }

    @Override
    public FlinkDeploymentShardWatcher addFlinkDeploymentSharedWatcher(Long clusterCredentialId, String namespace, Map<String, String> labels) throws Exception {
        NamespacedKubernetesClient client = kubernetesService.getClient(clusterCredentialId, namespace);
        return new FlinkDeploymentShardWatcher(client, labels);
    }
}
