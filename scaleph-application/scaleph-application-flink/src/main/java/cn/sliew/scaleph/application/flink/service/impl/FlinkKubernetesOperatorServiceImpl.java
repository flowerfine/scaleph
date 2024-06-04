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

package cn.sliew.scaleph.application.flink.service.impl;

import cn.sliew.scaleph.application.flink.factory.FlinkDeploymentFactory;
import cn.sliew.scaleph.application.flink.resource.definition.deployment.FlinkDeployment;
import cn.sliew.scaleph.application.flink.resource.definition.sessioncluster.FlinkSessionCluster;
import cn.sliew.scaleph.application.flink.service.FlinkKubernetesOperatorService;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesDeploymentDTO;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesSessionClusterDTO;
import cn.sliew.scaleph.application.flink.watch.FlinkDeploymentShardWatcher;
import cn.sliew.scaleph.kubernetes.Constant;
import cn.sliew.scaleph.kubernetes.service.KubernetesService;
import cn.sliew.scaleph.kubernetes.service.ObjectService;
import cn.sliew.scaleph.kubernetes.service.param.VersionGroupKind;
import cn.sliew.scaleph.kubernetes.watch.watch.DefaultKubernetesWatcher;
import cn.sliew.scaleph.kubernetes.watch.watch.WatchCallbackHandler;
import io.fabric8.kubernetes.api.model.GenericKubernetesResource;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.NamespacedKubernetesClient;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FlinkKubernetesOperatorServiceImpl implements FlinkKubernetesOperatorService {

    @Autowired
    private KubernetesService kubernetesService;
    @Autowired
    private ObjectService objectService;

    @Override
    public Optional<GenericKubernetesResource> getSessionCluster(WsFlinkKubernetesSessionClusterDTO sessionClusterDTO) throws Exception {
        VersionGroupKind versionAndGroup = new VersionGroupKind();
        versionAndGroup.setNamespace(sessionClusterDTO.getNamespace());
        versionAndGroup.setApiVersion(Constant.API_VERSION);
        versionAndGroup.setKind(Constant.FLINK_DEPLOYMENT);
        versionAndGroup.setName(sessionClusterDTO.getSessionClusterId());
        return objectService.getResource(sessionClusterDTO.getClusterCredentialId(), versionAndGroup);
    }

    @Override
    public void deploySessionCluster(Long clusterCredentialId, FlinkSessionCluster sessionCluster) throws Exception {
        FlinkDeployment deployment = FlinkDeploymentFactory.fromSessionCluster(sessionCluster);
        objectService.applyResource(clusterCredentialId, Serialization.asYaml(deployment));
    }

    @Override
    public void shutdownSessionCluster(Long clusterCredentialId, FlinkSessionCluster sessionCluster) throws Exception {
        FlinkDeployment deployment = FlinkDeploymentFactory.fromSessionCluster(sessionCluster);
        objectService.deleteResource(clusterCredentialId, Serialization.asYaml(deployment));
    }

    @Override
    public Optional<GenericKubernetesResource> getJob(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO) throws Exception {
        WsFlinkKubernetesJobDTO jobDto = jobInstanceDTO.getWsFlinkKubernetesJob();
        switch (jobDto.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                return getFlinkDeployment(jobInstanceDTO);
            case FLINK_SESSION_JOB:
                return getFlinkSessionJob(jobInstanceDTO);
            default:
                return Optional.empty();
        }
    }

    @Override
    public Optional<GenericKubernetesResource> getFlinkDeployment(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO) throws Exception {
        WsFlinkKubernetesDeploymentDTO flinkDeployment = jobInstanceDTO.getWsFlinkKubernetesJob().getFlinkDeployment();
        VersionGroupKind versionAndGroup = new VersionGroupKind();
        versionAndGroup.setApiVersion(Constant.API_VERSION);
        versionAndGroup.setKind(Constant.FLINK_DEPLOYMENT);
        versionAndGroup.setName(jobInstanceDTO.getInstanceId());
        versionAndGroup.setNamespace(flinkDeployment.getNamespace());
        return objectService.getResource(flinkDeployment.getClusterCredentialId(), versionAndGroup);
    }

    @Override
    public Optional<GenericKubernetesResource> getFlinkSessionJob(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO) throws Exception {
        WsFlinkKubernetesSessionClusterDTO flinkSessionCluster = jobInstanceDTO.getWsFlinkKubernetesJob().getFlinkSessionCluster();
        VersionGroupKind versionAndGroup = new VersionGroupKind();
        versionAndGroup.setApiVersion(Constant.API_VERSION);
        versionAndGroup.setKind(Constant.FLINK_SESSION_JOB);
        versionAndGroup.setName(jobInstanceDTO.getInstanceId());
        versionAndGroup.setNamespace(flinkSessionCluster.getNamespace());
        return objectService.getResource(flinkSessionCluster.getClusterCredentialId(), versionAndGroup);
    }

    @Override
    public void shutdownJob(Long clusterCredentialId, String job) throws Exception {
        objectService.deleteResource(clusterCredentialId, job);
    }

    @Override
    public void applyJob(Long clusterCredentialId, String job) throws Exception {
        List<HasMetadata> hasMetadataList = objectService.applyResource(clusterCredentialId, job);
        // 增加 watch 功能
    }

    @Override
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
