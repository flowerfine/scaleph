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

package cn.sliew.scaleph.engine.flink.kubernetes.resource.definition;

import cn.sliew.scaleph.common.dict.image.ImagePullPolicy;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkArtifactJar;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.kubernetes.resource.definition.ResourceCustomizer;
import cn.sliew.scaleph.kubernetes.resource.definition.ResourceCustomizerFactory;
import io.fabric8.kubernetes.api.model.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileFetcherFactory implements ResourceCustomizerFactory<PodBuilder> {

    private static final String FILE_FETCHER_CONTAINER_NAME = "scaleph-file-fetcher";
//    private static final String FILE_FETCHER_CONTAINER_IMAGE = "ghcr.io/flowerfine/scaleph/scaleph-file-fetcher:latest";
    private static final String FILE_FETCHER_CONTAINER_IMAGE = "scaleph-file-fetcher:dev";

    private static final Map<String, Quantity> FILE_FETCHER_CONTAINER_CPU = Map.of("cpu", Quantity.parse("250m"));
    private static final Map<String, Quantity> FILE_FETCHER_CONTAINER_MEMORY = Map.of("memory", Quantity.parse("512Mi"));

    private static final String FILE_FETCHER_VOLUME_NAME = "file-fetcher-volume";
    private static final String TARGET_DIRECTORY = "/flink/usrlib/";

    private String fileSystemSchema;
    private WsFlinkKubernetesJobDTO wsFlinkKubernetesJob;

    public FileFetcherFactory(String fileSystemSchema, WsFlinkKubernetesJobDTO wsFlinkKubernetesJob) {
        this.fileSystemSchema = fileSystemSchema;
        this.wsFlinkKubernetesJob = wsFlinkKubernetesJob;
    }

    @Override
    public ResourceCustomizer<PodBuilder> create() {
        return builder -> {
            addAdditionalJars(builder);
            addArtifactJar(builder);
        };
    }

    private void addArtifactJar(PodBuilder builder) {
        switch (wsFlinkKubernetesJob.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                doAddJars(builder);
                return;
            case FLINK_SESSION_JOB:
            default:
        }
    }

    private void addAdditionalJars(PodBuilder builder) {
        switch (wsFlinkKubernetesJob.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                doAddJars(builder);
                return;
            case FLINK_SESSION_JOB:
            default:
        }
    }

    private void doAddJars(PodBuilder builder) {
        wsFlinkKubernetesJob.getFlinkArtifactJar();
        PodFluent.SpecNested<PodBuilder> spec = builder.editOrNewSpec();
        spec.addToInitContainers(addJarArtifact(wsFlinkKubernetesJob.getFlinkArtifactJar()));
    }

    private Container addJarArtifact(WsFlinkArtifactJar jarArtifact) {
        ContainerBuilder builder = new ContainerBuilder();
        builder.withName(FILE_FETCHER_CONTAINER_NAME);
        builder.withImage(FILE_FETCHER_CONTAINER_IMAGE);
        builder.withImagePullPolicy(ImagePullPolicy.IF_NOT_PRESENT.getValue());
        builder.withArgs(buildFileFetcherArgs(jarArtifact));
        builder.withResources(buildResource());
        builder.withVolumeMounts(buildVolumeMount());
        builder.withTerminationMessagePath("/dev/termination-log");
        builder.withTerminationMessagePolicy("File");
        return builder.build();
    }

    private void addAdditionalJars(PodFluent.SpecNested<PodBuilder> spec, WsFlinkArtifactJar jarArtifact) {
        // add init container
        // add volume
    }

    private List<String> buildFileFetcherArgs(WsFlinkArtifactJar jarArtifact) {
        return Arrays.asList("-uri", fileSystemSchema + jarArtifact.getPath(),
                "-path", TARGET_DIRECTORY + jarArtifact.getFileName());
    }

    private ResourceRequirements buildResource() {
        ResourceRequirementsBuilder resourceRequirementsBuilder = new ResourceRequirementsBuilder();
        Map resource = new HashMap();
        resource.putAll(FILE_FETCHER_CONTAINER_CPU);
        resource.putAll(FILE_FETCHER_CONTAINER_MEMORY);
        resourceRequirementsBuilder.addToRequests(resource);
        resourceRequirementsBuilder.addToLimits(resource);
        return resourceRequirementsBuilder.build();
    }

    private VolumeMount buildVolumeMount() {
        VolumeMountBuilder builder = new VolumeMountBuilder();
        builder.withName(FILE_FETCHER_VOLUME_NAME);
        builder.withMountPath(TARGET_DIRECTORY);
        return builder.build();
    }

}
