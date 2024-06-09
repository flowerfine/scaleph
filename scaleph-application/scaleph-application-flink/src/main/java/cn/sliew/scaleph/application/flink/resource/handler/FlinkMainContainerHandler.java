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

package cn.sliew.scaleph.application.flink.resource.handler;

import cn.sliew.scaleph.application.flink.operator.spec.FlinkDeploymentSpec;
import cn.sliew.scaleph.application.flink.operator.spec.FlinkSessionClusterSpec;
import cn.sliew.scaleph.application.flink.resource.definition.job.instance.MetadataHandler;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesSessionClusterDTO;
import cn.sliew.scaleph.config.kubernetes.resource.ResourceAnnotations;
import cn.sliew.scaleph.config.kubernetes.resource.ResourceNames;
import io.fabric8.kubernetes.api.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FlinkMainContainerHandler {

    @Autowired
    private MetadataHandler metadataHandler;

    public void handle(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        PodBuilder podBuilder = Optional.ofNullable(spec.getPodTemplate()).map(pod -> new PodBuilder(pod)).orElse(new PodBuilder());
        podBuilder.editOrNewMetadata().withName(ResourceNames.POD_TEMPLATE_NAME)
                .addToAnnotations(buildAnnotations())
                .addToLabels(metadataHandler.generateLables(jobInstanceDTO))
                .endMetadata();

        handlePodTemplate(podBuilder);
        spec.setPodTemplate(podBuilder.build());
    }

    public void handle(WsFlinkKubernetesSessionClusterDTO sessionClusterDTO, FlinkSessionClusterSpec spec) {
        PodBuilder podBuilder = Optional.ofNullable(spec.getPodTemplate()).map(pod -> new PodBuilder(pod)).orElse(new PodBuilder());
        podBuilder.editOrNewMetadata().withName(ResourceNames.POD_TEMPLATE_NAME)
                .addToAnnotations(buildAnnotations())
                .addToLabels(metadataHandler.generateLables(sessionClusterDTO))
                .endMetadata();
        handlePodTemplate(podBuilder);
        spec.setPodTemplate(podBuilder.build());
    }

    private void handlePodTemplate(PodBuilder builder) {
        PodFluent<PodBuilder>.SpecNested<PodBuilder> spec = builder.editOrNewSpec();

        ContainerUtil.findFlinkMainContainer(spec)
                .addAllToPorts(buildMetricsPorts())
                .addAllToEnv(buildEnv())
                .endContainer();

        spec.endSpec();
    }

    private Map<String, String> buildAnnotations() {
        Map<String, String> annotations = new HashMap<>();
        annotations.put(ResourceAnnotations.PROMETHEUS_ANNOTATION_PORT, ResourceAnnotations.PROMETHEUS_ANNOTATION_PORT_VALUE);
        annotations.put(ResourceAnnotations.PROMETHEUS_ANNOTATION_SCRAPE, ResourceAnnotations.PROMETHEUS_ANNOTATION_SCRAPE_VALUE);
        return Collections.emptyMap();
    }

    private List<ContainerPort> buildMetricsPorts() {
        List<ContainerPort> ports = new ArrayList<>();
        ports.add(new ContainerPortBuilder().withName("jmx-metrics").withContainerPort(8789).withProtocol("TCP").build());
        ports.add(new ContainerPortBuilder().withName("prom-metrics").withContainerPort(9249).withProtocol("TCP").build());
        return ports;
    }

    private List<EnvVar> buildEnv() {
        List<EnvVar> envs = new ArrayList<>();
        envs.add(new EnvVarBuilder().withName("TZ").withValue("Asia/Shanghai").build());
        return envs;
    }
}
