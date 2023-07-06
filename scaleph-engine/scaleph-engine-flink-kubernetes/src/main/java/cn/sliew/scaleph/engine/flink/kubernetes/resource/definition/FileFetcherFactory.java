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
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.JobManagerSpec;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.job.FlinkDeploymentJob;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.kubernetes.resource.definition.ResourceCustomizer;
import cn.sliew.scaleph.system.snowflake.utils.NetUtils;
import io.fabric8.kubernetes.api.model.*;

import java.util.*;

public enum FileFetcherFactory implements ResourceCustomizer<WsFlinkKubernetesJobDTO, FlinkDeploymentJob> {
    INSTANCE;

    private static final String FLINK_MAIN_CONTAINER_NAME = "flink-main-container";
    private static final String FILE_FETCHER_CONTAINER_NAME = "scaleph-file-fetcher";
    //    private static final String FILE_FETCHER_CONTAINER_IMAGE = "ghcr.io/flowerfine/scaleph/scaleph-file-fetcher:latest";
    private static final String FILE_FETCHER_CONTAINER_IMAGE = "scaleph-file-fetcher:dev";

    private static final Map<String, Quantity> FILE_FETCHER_CONTAINER_CPU = Map.of("cpu", Quantity.parse("250m"));
    private static final Map<String, Quantity> FILE_FETCHER_CONTAINER_MEMORY = Map.of("memory", Quantity.parse("512Mi"));

    private static final String FILE_FETCHER_VOLUME_NAME = "file-fetcher-volume";
    public static final String TARGET_DIRECTORY = "/flink/usrlib/";
    public static final String LOCAL_SCHEMA = "local://";
    public static final String LOCAL_PATH = LOCAL_SCHEMA + TARGET_DIRECTORY;

    @Override
    public void customize(WsFlinkKubernetesJobDTO jobDTO, FlinkDeploymentJob job) {
        PodBuilder podBuilder = Optional.ofNullable(job.getSpec().getPodTemplate()).map(pod -> new PodBuilder(pod)).orElse(new PodBuilder());
        cusomizePodTemplate(podBuilder);
        job.getSpec().setPodTemplate(podBuilder.build());

        JobManagerSpec jobManager = Optional.ofNullable(job.getSpec().getJobManager()).orElse(new JobManagerSpec());
        cusomizeJobManagerPodTemplate(jobDTO, jobManager);
        job.getSpec().setJobManager(jobManager);
    }

    private void cusomizePodTemplate(PodBuilder builder) {
        builder.editOrNewMetadata()
                .withName("pod-template")
                .endMetadata();
        PodFluent.SpecNested<PodBuilder> spec = builder.editOrNewSpec();
        spec.addToVolumes(buildVolume()); // add volumes
        if (spec.hasMatchingContainer(containerBuilder -> containerBuilder.getName().equals(FLINK_MAIN_CONTAINER_NAME))) {
            spec.editMatchingContainer((containerBuilder -> containerBuilder.getName().equals(FLINK_MAIN_CONTAINER_NAME)))
                    .addToVolumeMounts(buildVolumeMount()) // add volume mount
                    .endContainer();
        } else {
            spec.addNewContainer()
                    .withName(FLINK_MAIN_CONTAINER_NAME)
                    .addToVolumeMounts(buildVolumeMount()) // add volume mount
                    .endContainer();
        }
        spec.endSpec();
    }

    private void cusomizeJobManagerPodTemplate(WsFlinkKubernetesJobDTO jobDTO, JobManagerSpec jobManager) {
        PodBuilder builder = Optional.of(jobManager).map(JobManagerSpec::getPodTemplate).map(pod -> new PodBuilder(pod)).orElse(new PodBuilder());
        doCustomize(jobDTO, builder);
        jobManager.setPodTemplate(builder.build());
    }

    private void doCustomize(WsFlinkKubernetesJobDTO jobDTO, PodBuilder builder) {
        builder.editOrNewMetadata()
                .withName("job-manager-pod-template")
                .endMetadata();
        addArtifactJar(jobDTO, builder);
        addAdditionalJars(jobDTO, builder);
    }

    private void addArtifactJar(WsFlinkKubernetesJobDTO jobDTO, PodBuilder builder) {
        switch (jobDTO.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                doAddJars(jobDTO.getFlinkArtifactJar(), builder);
                return;
            case FLINK_SESSION_JOB:
            default:
        }
    }

    private void addAdditionalJars(WsFlinkKubernetesJobDTO jobDTO, PodBuilder builder) {
        switch (jobDTO.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
//                doAddJars(builder);
                return;
            case FLINK_SESSION_JOB:
            default:
        }
    }

    private void doAddJars(WsFlinkArtifactJar jarArtifact, PodBuilder builder) {
        builder.editOrNewSpec().addToInitContainers(addJarArtifact(jarArtifact)).endSpec();
    }

    private Container addJarArtifact(WsFlinkArtifactJar jarArtifact) {
        ContainerBuilder builder = new ContainerBuilder();
        builder.withName(FILE_FETCHER_CONTAINER_NAME);
        builder.withImage(FILE_FETCHER_CONTAINER_IMAGE);
        builder.withImagePullPolicy(ImagePullPolicy.IF_NOT_PRESENT.getValue());
        builder.withArgs(buildFileFetcherArgs(jarArtifact));
        builder.withEnv(buildEnvs());
        builder.withResources(buildResource());
        builder.withVolumeMounts(buildVolumeMount());
        builder.withTerminationMessagePath("/dev/termination-log");
        builder.withTerminationMessagePolicy("File");
        return builder.build();
    }

    private void addAdditionalJars(PodFluent.SpecNested<PodBuilder> spec, WsFlinkArtifactJar jarArtifact) {

    }

    private List<String> buildFileFetcherArgs(WsFlinkArtifactJar jarArtifact) {
        return Arrays.asList("-uri", jarArtifact.getPath(),
                "-path", TARGET_DIRECTORY + jarArtifact.getFileName());
    }

    private List<EnvVar> buildEnvs() {
        EnvVarBuilder builder = new EnvVarBuilder();
        builder.withName("MINIO_ENDPOINT");
        final String localAddress = NetUtils.getLocalIP();
        builder.withValue(String.format("http://%s:9000", localAddress));
        return Arrays.asList(builder.build());
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

    private Volume buildVolume() {
        VolumeBuilder builder = new VolumeBuilder();
        builder.withName(FILE_FETCHER_VOLUME_NAME);
        builder.withEmptyDir(new EmptyDirVolumeSource());
        return builder.build();
    }

}
