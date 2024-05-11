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

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.common.dict.image.ImagePullPolicy;
import cn.sliew.scaleph.common.util.NetUtils;
import cn.sliew.scaleph.config.kubernetes.resource.ResourceNames;
import cn.sliew.scaleph.config.storage.S3FileSystemProperties;
import cn.sliew.scaleph.application.flink.operator.spec.FlinkDeploymentSpec;
import cn.sliew.scaleph.application.flink.operator.spec.JobManagerSpec;
import cn.sliew.scaleph.resource.service.JarService;
import cn.sliew.scaleph.resource.service.dto.JarDTO;
import io.fabric8.kubernetes.api.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Component
public class FileFetcherHandler {

    private static final Map<String, Quantity> FILE_FETCHER_CONTAINER_CPU = Map.of("cpu", Quantity.parse("250m"));
    private static final Map<String, Quantity> FILE_FETCHER_CONTAINER_MEMORY = Map.of("memory", Quantity.parse("512Mi"));

    private static final String ENDPOINT = "MINIO_ENDPOINT";

    private static final String ENV_JAVA_OPTS_ALL_NAME = "CLASSPATH";

    @Autowired(required = false)
    private S3FileSystemProperties s3FileSystemProperties;
    @Autowired
    private JarService jarService;

    public void handleJarArtifact(WsFlinkKubernetesJobDTO jobDTO, FlinkDeploymentSpec spec) {
        List<FileFetcherParam> files = collectFiles(jobDTO);
        if (CollectionUtils.isEmpty(files)) {
            return;
        }

        PodBuilder podBuilder = Optional.ofNullable(spec.getPodTemplate()).map(pod -> new PodBuilder(pod)).orElse(new PodBuilder());
        handlePodTemplate(podBuilder);
        spec.setPodTemplate(podBuilder.build());

        JobManagerSpec jobManager = Optional.ofNullable(spec.getJobManager()).orElse(new JobManagerSpec());
        handleJobManagerPodTemplate(jobDTO, jobManager, files);
        spec.setJobManager(jobManager);
    }

    private void handlePodTemplate(PodBuilder builder) {
        builder.editOrNewMetadata()
                .withName(ResourceNames.POD_TEMPLATE_NAME)
                .endMetadata();

        PodFluent<PodBuilder>.SpecNested<PodBuilder> spec = builder.editOrNewSpec();

        spec.addAllToVolumes(buildVolume()); // add volumes
        ContainerUtil.findFlinkMainContainer(spec)
                .addAllToEnv(buildJavaOptsEnv())
                .addAllToVolumeMounts(buildVolumeMount()) // add volume mount
                .endContainer();

        spec.endSpec();
    }

    private void handleJobManagerPodTemplate(WsFlinkKubernetesJobDTO jobDTO, JobManagerSpec jobManager, List<FileFetcherParam> files) {
        PodBuilder builder = Optional.of(jobManager).map(JobManagerSpec::getPodTemplate).map(pod -> new PodBuilder(pod)).orElse(new PodBuilder());
        doHandle(builder, files);
        jobManager.setPodTemplate(builder.build());
    }

    private void doHandle(PodBuilder builder, List<FileFetcherParam> files) {
        builder.editOrNewMetadata()
                .withName(ResourceNames.JOB_MANAGER_POD_TEMPLATE_NAME)
                .endMetadata();
        addFileFetcherInitContainers(builder, files);
    }

    private List<FileFetcherParam> collectFiles(WsFlinkKubernetesJobDTO jobDTO) {
        List<FileFetcherParam> result = new ArrayList<>();
        addArtifactJar(jobDTO, result);
        addAdditionalJars(jobDTO, result);
        return result;
    }

    private void addArtifactJar(WsFlinkKubernetesJobDTO jobDTO, List<FileFetcherParam> result) {
        if (jobDTO.getArtifactFlinkJar() == null) {
            return;
        }

        switch (jobDTO.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                result.add(new FileFetcherParam(jobDTO.getArtifactFlinkJar().getPath(), ResourceNames.SCALEPH_JAR_DIRECTORY + jobDTO.getArtifactFlinkJar().getFileName()));
                break;
            case FLINK_SESSION_JOB:
                break;
            default:
        }
    }

    private void addAdditionalJars(WsFlinkKubernetesJobDTO jobDTO, List<FileFetcherParam> result) {
        switch (jobDTO.getDeploymentKind()) {
            case FLINK_DEPLOYMENT:
                doAddAdditionalJars(jobDTO.getFlinkDeployment().getAdditionalDependencies(), result);
                break;
            case FLINK_SESSION_JOB:
                break;
            default:
        }
    }

    private void doAddAdditionalJars(List<Long> additionalDependencies, List<FileFetcherParam> result) {
        if (!CollectionUtils.isEmpty(additionalDependencies) && result != null) {
            for (Long jarId : additionalDependencies) {
                JarDTO jarDTO = jarService.getRaw(jarId);
                result.add(new FileFetcherParam(jarDTO.getPath(), ResourceNames.LIB_DIRECTORY + jarDTO.getFileName()));
            }
        }
    }

    private void addFileFetcherInitContainers(PodBuilder builder, List<FileFetcherParam> files) {
        builder.editOrNewSpec().addToInitContainers(buildInitContainer(files)).endSpec();
    }

    private Container buildInitContainer(List<FileFetcherParam> files) {
        ContainerBuilder builder = new ContainerBuilder();
        builder.withName(ResourceNames.FILE_FETCHER_CONTAINER_NAME);
        builder.withImage(ResourceNames.FILE_FETCHER_CONTAINER_IMAGE);
        builder.withImagePullPolicy(ImagePullPolicy.IF_NOT_PRESENT.getValue());
        builder.withArgs(buildFileFetcherArgs(files));
        builder.withEnv(buildEnvs());
        builder.withResources(buildResource());
        builder.withVolumeMounts(buildVolumeMount());
        builder.withTerminationMessagePath("/dev/termination-log");
        builder.withTerminationMessagePolicy("File");
        return builder.build();
    }

    private List<String> buildFileFetcherArgs(List<FileFetcherParam> files) {
        return Arrays.asList("-file-fetcher-json", JacksonUtil.toJsonString(files));
    }

    private List<EnvVar> buildEnvs() {
        if (s3FileSystemProperties != null) {
            EnvVarBuilder builder = new EnvVarBuilder();
            builder.withName(ENDPOINT);
            builder.withValue(NetUtils.replaceLocalhost(s3FileSystemProperties.getEndpoint()));
            return Arrays.asList(builder.build());
        }
        return Collections.emptyList();
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

    private List<EnvVar> buildJavaOptsEnv() {
        EnvVarBuilder builder = new EnvVarBuilder();
        builder.withName(ENV_JAVA_OPTS_ALL_NAME);
        builder.withValue(ResourceNames.LIB_DIRECTORY);
        return Collections.singletonList(builder.build());
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

    @Getter
    @Setter
    @AllArgsConstructor
    private static class FileFetcherParam {
        private String uri;
        private String path;
    }

}
