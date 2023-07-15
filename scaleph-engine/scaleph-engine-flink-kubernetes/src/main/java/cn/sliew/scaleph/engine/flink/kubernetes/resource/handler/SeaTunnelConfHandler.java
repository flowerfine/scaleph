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

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.config.resource.ResourceNames;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.definition.job.FlinkDeploymentJob;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConfigService;
import cn.sliew.scaleph.engine.seatunnel.service.WsDiJobService;
import cn.sliew.scaleph.engine.seatunnel.service.dto.WsDiJobDTO;
import io.fabric8.kubernetes.api.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SeaTunnelConfHandler {

    @Autowired
    private SeatunnelConfigService seatunnelConfigService;
    @Autowired
    private WsDiJobService wsDiJobService;

    public ConfigMap customize(WsFlinkKubernetesJobDTO jobDTO, FlinkDeploymentJob job) throws Exception {
        PodBuilder podBuilder = Optional.ofNullable(job.getSpec().getPodTemplate()).map(pod -> new PodBuilder(pod)).orElse(new PodBuilder());
        cusomizePodTemplate(jobDTO, podBuilder);
        job.getSpec().setPodTemplate(podBuilder.build());
        return buildSeaTunnelConf(jobDTO, job);
    }

    public ConfigMap buildSeaTunnelConf(WsFlinkKubernetesJobDTO jobDTO, FlinkDeploymentJob job) throws Exception {
        WsDiJobDTO wsDiJobDTO = wsDiJobService.queryJobGraph(jobDTO.getWsDiJob().getId());
        String prettyJson = seatunnelConfigService.buildConfig(wsDiJobDTO);
        String plainJson = JacksonUtil.toJsonNode(prettyJson).toString();

        ConfigMapBuilder builder = new ConfigMapBuilder();
        builder.withNewMetadataLike(job.getMetadata())
                .withName(jobDTO.getJobId() + "-seatunnel-configmap")
                .endMetadata()
                .withData(Map.of(ResourceNames.SEATUNNEL_CONF_FILE, plainJson));
        return builder.build();
    }

    private void cusomizePodTemplate(WsFlinkKubernetesJobDTO jobDTO, PodBuilder builder) {
        builder.editOrNewMetadata().withName(ResourceNames.POD_TEMPLATE_NAME)
                .endMetadata();
        PodFluent.SpecNested<PodBuilder> spec = builder.editOrNewSpec();
        spec.addAllToVolumes(buildVolume(jobDTO)); // add volumes
        if (spec.hasMatchingContainer(containerBuilder -> containerBuilder.getName().equals(ResourceNames.FLINK_MAIN_CONTAINER_NAME))) {
            spec.editMatchingContainer((containerBuilder -> containerBuilder.getName().equals(ResourceNames.FLINK_MAIN_CONTAINER_NAME)))
                    .addAllToVolumeMounts(buildVolumeMount()) // add volume mount
                    .endContainer();
        } else {
            spec.addNewContainer()
                    .withName(ResourceNames.FLINK_MAIN_CONTAINER_NAME)
                    .addAllToVolumeMounts(buildVolumeMount()) // add volume mount
                    .endContainer();
        }
        spec.endSpec();
    }

    private List<VolumeMount> buildVolumeMount() {
        VolumeMountBuilder seatunnelConf = new VolumeMountBuilder();
        seatunnelConf.withName(ResourceNames.SEATUNNEL_CONF_VOLUME_NAME);
        seatunnelConf.withMountPath(ResourceNames.SEATUNNEL_CONF_FILE_PATH);
        seatunnelConf.withSubPath(ResourceNames.SEATUNNEL_CONF_FILE);
        return Arrays.asList(seatunnelConf.build());
    }

    private List<Volume> buildVolume(WsFlinkKubernetesJobDTO jobDTO) {
        VolumeBuilder seatunnelConf = new VolumeBuilder();
        seatunnelConf.withName(ResourceNames.SEATUNNEL_CONF_VOLUME_NAME);
        ConfigMapVolumeSourceBuilder configMap = new ConfigMapVolumeSourceBuilder();
        KeyToPath keyToPath = new KeyToPathBuilder()
                .withKey(ResourceNames.SEATUNNEL_CONF_FILE)
                .withPath(ResourceNames.SEATUNNEL_CONF_FILE)
                .build();
        configMap.withName(jobDTO.getJobId() + "-seatunnel-configmap")
                .withItems(keyToPath);
        seatunnelConf.withConfigMap(configMap.build());
        return Arrays.asList(seatunnelConf.build());
    }
}
