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

import cn.sliew.scaleph.application.flink.operator.spec.FlinkSessionJobSpec;
import cn.sliew.scaleph.application.flink.operator.spec.JobSpec;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.dict.flink.kubernetes.DeploymentKind;
import cn.sliew.scaleph.dao.entity.master.ws.WsArtifactFlinkJar;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class FlinkSessionJobArtifactHandler implements ArtifactHandler {

    @Override
    public boolean support(DeploymentKind deploymentKind) {
        return deploymentKind == DeploymentKind.FLINK_SESSION_JOB;
    }

    @Override
    public boolean support(FlinkJobType flinkJobType) {
        switch (flinkJobType) {
            case JAR:
                return true;
            default:
                return false;
        }
    }

    public void handle(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, Object spec) {
        FlinkSessionJobSpec flinkSessionJobSpec = (FlinkSessionJobSpec) spec;
        switch (jobInstanceDTO.getWsFlinkKubernetesJob().getType()) {
            case JAR:
                addJarArtifact(jobInstanceDTO, flinkSessionJobSpec);
                break;
            default:
        }
    }

    private void addJarArtifact(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkSessionJobSpec spec) {
        WsArtifactFlinkJar artifactFlinkJar = jobInstanceDTO.getWsFlinkKubernetesJob().getArtifactFlinkJar();
        JobSpec jobSpec = new JobSpec();
        jobSpec.setJarURI(artifactFlinkJar.getPath());
        jobSpec.setEntryClass(artifactFlinkJar.getEntryClass());
        jobSpec.setArgs(StringUtils.split(artifactFlinkJar.getJarParams(), " "));
        spec.setJob(jobSpec);
    }
}
