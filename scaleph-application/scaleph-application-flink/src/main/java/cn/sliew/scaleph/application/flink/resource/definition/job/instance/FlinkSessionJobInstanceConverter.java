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
import cn.sliew.scaleph.application.flink.resource.definition.sessioncluster.FlinkSessionCluster;
import cn.sliew.scaleph.application.flink.resource.definition.sessioncluster.FlinkSessionClusterConverter;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.common.dict.flink.kubernetes.DeploymentKind;
import cn.sliew.scaleph.application.flink.operator.spec.FlinkSessionJobSpec;
import cn.sliew.scaleph.application.flink.resource.definition.job.FlinkSessionJob;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FlinkSessionJobInstanceConverter implements FlinkJobInstanceConverter {

    @Autowired
    private MetadataHandler metadataHandler;
    @Autowired
    private FlinkSessionJobSpecHandler flinkSessionJobSpecHandler;

    @Override
    public boolean support(DeploymentKind deploymentKind) {
        return deploymentKind == DeploymentKind.FLINK_SESSION_JOB;
    }

    @Override
    public String convert(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO) {
        try {
            FlinkSessionJob flinkSessionJob = new FlinkSessionJob();
            FlinkSessionCluster flinkSessionCluster = FlinkSessionClusterConverter.INSTANCE.convertTo(jobInstanceDTO.getWsFlinkKubernetesJob().getFlinkSessionCluster());
            flinkSessionJob.setMetadata(metadataHandler.handle(jobInstanceDTO, flinkSessionCluster.getMetadata()));
            flinkSessionJob.setSpec(flinkSessionJobSpecHandler.handle(jobInstanceDTO, flinkSessionCluster, new FlinkSessionJobSpec()));
            return Serialization.asYaml(flinkSessionJob);
        } catch (Exception e) {
            Rethrower.throwAs(e);
            return null;
        }
    }
}
