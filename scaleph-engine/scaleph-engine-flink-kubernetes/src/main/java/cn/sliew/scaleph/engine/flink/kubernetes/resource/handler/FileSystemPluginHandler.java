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

package cn.sliew.scaleph.engine.flink.kubernetes.resource.handler;

import cn.sliew.scaleph.config.resource.ResourceNames;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.definition.job.FlinkDeploymentJob;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobDTO;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.PodFluent;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class FileSystemPluginHandler {

    public void customize(WsFlinkKubernetesJobDTO jobDTO, FlinkDeploymentJob job) throws Exception {
        PodBuilder podBuilder = Optional.ofNullable(job.getSpec().getPodTemplate()).map(pod -> new PodBuilder(pod)).orElse(new PodBuilder());
        cusomizePodTemplate(jobDTO, podBuilder);
        addFileSystemConfigOption(jobDTO, job);
        job.getSpec().setPodTemplate(podBuilder.build());
    }

    private void cusomizePodTemplate(WsFlinkKubernetesJobDTO jobDTO, PodBuilder builder) {
        builder.editOrNewMetadata().withName(ResourceNames.POD_TEMPLATE_NAME)
                .endMetadata();
        PodFluent.SpecNested<PodBuilder> spec = builder.editOrNewSpec();

        ContainerUtil.findFlinkMainContainer(spec)
                .addAllToEnv(buildEnableFileSystemEnv())
                .endContainer();

        spec.endSpec();
    }

    private void addFileSystemConfigOption(WsFlinkKubernetesJobDTO jobDTO, FlinkDeploymentJob job) {

    }

    private List<EnvVar> buildEnableFileSystemEnv() {
        EnvVarBuilder builder = new EnvVarBuilder();
        builder.withName("ENABLE_BUILT_IN_PLUGINS");
        builder.withValue("flink-s3-fs-hadoop");
        return Collections.singletonList(builder.build());
    }

}
