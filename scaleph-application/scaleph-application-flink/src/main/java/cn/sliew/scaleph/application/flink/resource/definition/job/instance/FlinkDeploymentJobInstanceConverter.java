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

package cn.sliew.scaleph.application.flink.resource.definition.job.instance;

import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.scaleph.application.flink.resource.definition.deployment.FlinkDeployment;
import cn.sliew.scaleph.application.flink.resource.definition.deployment.FlinkDeploymentConverter;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.common.dict.flink.kubernetes.DeploymentKind;
import cn.sliew.scaleph.application.flink.resource.definition.job.FlinkDeploymentJob;
import cn.sliew.scaleph.application.flink.resource.handler.SeaTunnelConfHandler;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FlinkDeploymentJobInstanceConverter implements FlinkJobInstanceConverter {

    @Autowired
    private MetadataHandler metadataHandler;
    @Autowired
    private FlinkDeploymentSpecHandler flinkDeploymentSpecHandler;
    @Autowired
    private SeaTunnelConfHandler seaTunnelConfHandler;

    @Override
    public boolean support(DeploymentKind deploymentKind) {
        return deploymentKind == DeploymentKind.FLINK_DEPLOYMENT;
    }

    @Override
    public String convert(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO) {
        try {
            FlinkDeploymentJob deployment = new FlinkDeploymentJob();
            FlinkDeployment flinkDeployment = FlinkDeploymentConverter.INSTANCE.convertTo(jobInstanceDTO.getWsFlinkKubernetesJob().getFlinkDeployment());
            deployment.setMetadata(metadataHandler.handle(jobInstanceDTO, flinkDeployment.getMetadata()));
            deployment.setSpec(flinkDeploymentSpecHandler.handle(jobInstanceDTO, flinkDeployment.getSpec()));
            switch (jobInstanceDTO.getWsFlinkKubernetesJob().getType()) {
                case JAR:
                    return Serialization.asYaml(deployment);
                case SQL:
                    return Serialization.asYaml(deployment);
                case FLINK_CDC:
                    return Serialization.asYaml(deployment);
                case SEATUNNEL:
                    ConfigMap configMap = seaTunnelConfHandler.buildSeaTunnelConf(jobInstanceDTO.getInstanceId(), jobInstanceDTO.getWsFlinkKubernetesJob().getArtifactSeaTunnel().getId(), jobInstanceDTO.getWsFlinkKubernetesJob().getExecutionMode(), deployment.getMetadata());
                    return Serialization.asYaml(deployment) + Serialization.asYaml(configMap);
                default:
                    throw new IllegalStateException("unknown flink job type for " + jobInstanceDTO.getWsFlinkKubernetesJob().getType().getValue());
            }
        } catch (Exception e) {
            Rethrower.throwAs(e);
            return null;
        }
    }
}
