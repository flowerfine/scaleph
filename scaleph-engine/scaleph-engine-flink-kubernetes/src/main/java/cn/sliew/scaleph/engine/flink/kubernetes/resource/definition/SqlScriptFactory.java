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

import cn.sliew.scaleph.engine.flink.kubernetes.resource.job.FlinkDeploymentJob;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.kubernetes.resource.custom.ResourceCustomizer;
import io.fabric8.kubernetes.api.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum SqlScriptFactory implements ResourceCustomizer<WsFlinkKubernetesJobDTO, FlinkDeploymentJob> {
    INSTANCE;

    private static final String SQL_SCRIPTS_VOLUME_NAME = "sql-scripts-volume";
    private static final String SQL_DIRECTORY = "/scaleph/sql/";
    private static final String SQL_SCRIPTS_DIRECTORY = "/scaleph/sql-scripts/";

    public static final String SQL_LOCAL_PATH = FileFetcherFactory.LOCAL_SCHEMA + SQL_DIRECTORY;

    @Override
    public void customize(WsFlinkKubernetesJobDTO jobDTO, FlinkDeploymentJob job) {
        PodBuilder podBuilder = Optional.ofNullable(job.getSpec().getPodTemplate()).map(pod -> new PodBuilder(pod)).orElse(new PodBuilder());
        cusomizePodTemplate(podBuilder);
        job.getSpec().setPodTemplate(podBuilder.build());
    }

    private void cusomizePodTemplate(PodBuilder builder) {
        builder.editOrNewMetadata()
                .withName("pod-template")
                .endMetadata();
        PodFluent.SpecNested<PodBuilder> spec = builder.editOrNewSpec();
        spec.addAllToVolumes(buildVolume()); // add volumes
        if (spec.hasMatchingContainer(containerBuilder -> containerBuilder.getName().equals(FileFetcherFactory.FLINK_MAIN_CONTAINER_NAME))) {
            spec.editMatchingContainer((containerBuilder -> containerBuilder.getName().equals(FileFetcherFactory.FLINK_MAIN_CONTAINER_NAME)))
                    .addAllToVolumeMounts(buildVolumeMount()) // add volume mount
                    .endContainer();
        } else {
            spec.addNewContainer()
                    .withName(FileFetcherFactory.FLINK_MAIN_CONTAINER_NAME)
                    .addAllToVolumeMounts(buildVolumeMount()) // add volume mount
                    .endContainer();
        }
        spec.endSpec();
    }

    private List<VolumeMount> buildVolumeMount() {
        VolumeMountBuilder sqlScripts = new VolumeMountBuilder();
        sqlScripts.withName(SQL_SCRIPTS_VOLUME_NAME);
        sqlScripts.withMountPath(SQL_SCRIPTS_VOLUME_NAME);
        return Arrays.asList(sqlScripts.build());
    }

    private List<Volume> buildVolume() {
        VolumeBuilder sqlScripts = new VolumeBuilder();
        sqlScripts.withName(SQL_SCRIPTS_VOLUME_NAME);
//        sqlScripts.withConfi
        sqlScripts.withEmptyDir(new EmptyDirVolumeSource());
        return Arrays.asList(sqlScripts.build());
    }

}
