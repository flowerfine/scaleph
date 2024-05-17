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
import cn.sliew.scaleph.config.kubernetes.resource.ResourceNames;
import io.fabric8.kubernetes.api.model.ContainerPort;
import io.fabric8.kubernetes.api.model.ContainerPortBuilder;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.PodFluent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class FlinkMetricsHandler {

    public void handle(FlinkDeploymentSpec spec) {
        PodBuilder podBuilder = Optional.ofNullable(spec.getPodTemplate()).map(pod -> new PodBuilder(pod)).orElse(new PodBuilder());
        handlePodTemplate(podBuilder);
        spec.setPodTemplate(podBuilder.build());
    }

    private void handlePodTemplate(PodBuilder builder) {
        builder.editOrNewMetadata().withName(ResourceNames.POD_TEMPLATE_NAME)
                .endMetadata();
        PodFluent<PodBuilder>.SpecNested<PodBuilder> spec = builder.editOrNewSpec();

        ContainerUtil.findFlinkMainContainer(spec)
                .addAllToPorts(buildMetricsPorts())
                .endContainer();

        spec.endSpec();
    }

    private List<ContainerPort> buildMetricsPorts() {
        List<ContainerPort> ports = new ArrayList<>();
        ports.add(new ContainerPortBuilder().withName("jmx-metrics").withContainerPort(8789).withProtocol("TCP").build());
        ports.add(new ContainerPortBuilder().withName("prom-metrics").withContainerPort(9249).withProtocol("TCP").build());
        return ports;
    }

}
