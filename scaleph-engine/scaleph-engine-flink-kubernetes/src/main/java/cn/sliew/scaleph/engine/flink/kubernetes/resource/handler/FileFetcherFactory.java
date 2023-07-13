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

import cn.sliew.scaleph.common.dict.image.ImagePullPolicy;
import cn.sliew.scaleph.config.resource.ResourceNames;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkArtifactJar;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.JobManagerSpec;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.definition.job.FlinkDeploymentJob;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.system.snowflake.utils.NetUtils;
import io.fabric8.kubernetes.api.model.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FileFetcherFactory {

    private static final Map<String, Quantity> FILE_FETCHER_CONTAINER_CPU = Map.of("cpu", Quantity.parse("250m"));
    private static final Map<String, Quantity> FILE_FETCHER_CONTAINER_MEMORY = Map.of("memory", Quantity.parse("512Mi"));

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
                .withName(ResourceNames.POD_TEMPLATE_NAME)
                .endMetadata();
        PodFluent.SpecNested<PodBuilder> spec = builder.editOrNewSpec();

        spec.addAllToVolumes(buildVolume()); // add volumes
        ContainerUtil.findFlinkMainContainer(spec)
                .addAllToVolumeMounts(buildVolumeMount()) // add volume mount
                .endContainer();
        
        spec.endSpec();
    }

    private void cusomizeJobManagerPodTemplate(WsFlinkKubernetesJobDTO jobDTO, JobManagerSpec jobManager) {
        PodBuilder builder = Optional.of(jobManager).map(JobManagerSpec::getPodTemplate).map(pod -> new PodBuilder(pod)).orElse(new PodBuilder());
        doCustomize(jobDTO, builder);
        jobManager.setPodTemplate(builder.build());
    }

    private void doCustomize(WsFlinkKubernetesJobDTO jobDTO, PodBuilder builder) {
        builder.editOrNewMetadata()
                .withName(ResourceNames.JOB_MANAGER_POD_TEMPLATE_NAME)
                .endMetadata();
        addArtifactJar(jobDTO, builder);
        addAdditionalJars(jobDTO, builder);
    }

    private void addArtifactJar(WsFlinkKubernetesJobDTO jobDTO, PodBuilder builder) {
        if (jobDTO.getFlinkArtifactJar() == null) {
            return;
        }

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
                addAdditionalJars();
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
        builder.withName(ResourceNames.FILE_FETCHER_CONTAINER_NAME);
        builder.withImage(ResourceNames.FILE_FETCHER_CONTAINER_IMAGE);
        builder.withImagePullPolicy(ImagePullPolicy.IF_NOT_PRESENT.getValue());
        builder.withArgs(buildFileFetcherArgs(jarArtifact));
        builder.withEnv(buildEnvs());
        builder.withResources(buildResource());
        builder.withVolumeMounts(buildVolumeMount());
        builder.withTerminationMessagePath("/dev/termination-log");
        builder.withTerminationMessagePolicy("File");
        return builder.build();
    }

    private void addAdditionalJars() {

    }

    private List<String> buildFileFetcherArgs(WsFlinkArtifactJar jarArtifact) {
        return Arrays.asList("-uri", jarArtifact.getPath(),
                "-path", ResourceNames.SCALEPH_JAR_DIRECTORY + jarArtifact.getFileName());
    }

    private List<EnvVar> buildEnvs() {
        EnvVarBuilder builder = new EnvVarBuilder();
        builder.withName("MINIO_ENDPOINT");
        builder.withValue(String.format("http://%s:9000", NetUtils.getLocalIP()));
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


    private List<VolumeMount> buildVolumeMount() {
        VolumeMountBuilder scalephLib = new VolumeMountBuilder();
        scalephLib.withName(ResourceNames.FILE_FETCHER_SCALEPH_VOLUME_NAME);
        scalephLib.withMountPath(ResourceNames.SCALEPH_JAR_DIRECTORY);

        VolumeMountBuilder flinkLib = new VolumeMountBuilder();
        flinkLib.withName(ResourceNames.FILE_FETCHER_FLINK_VOLUME_NAME);
        flinkLib.withMountPath(ResourceNames.LIB_DIRECTORY);
        return Arrays.asList(scalephLib.build(), flinkLib.build());
    }

    private List<Volume> buildVolume() {
        VolumeBuilder scalephLib = new VolumeBuilder();
        scalephLib.withName(ResourceNames.FILE_FETCHER_SCALEPH_VOLUME_NAME);
        scalephLib.withEmptyDir(new EmptyDirVolumeSource());

        VolumeBuilder flinkLib = new VolumeBuilder();
        flinkLib.withName(ResourceNames.FILE_FETCHER_FLINK_VOLUME_NAME);
        flinkLib.withEmptyDir(new EmptyDirVolumeSource());
        return Arrays.asList(scalephLib.build(), flinkLib.build());
    }

}
