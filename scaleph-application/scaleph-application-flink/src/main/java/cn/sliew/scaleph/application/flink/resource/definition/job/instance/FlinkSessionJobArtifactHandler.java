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
import cn.sliew.scaleph.common.util.SeaTunnelReleaseUtil;
import cn.sliew.scaleph.config.kubernetes.resource.ResourceNames;
import cn.sliew.scaleph.dao.entity.master.ws.WsArtifactFlinkJar;
import cn.sliew.scaleph.dao.entity.master.ws.WsArtifactSeaTunnel;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

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
            case SEATUNNEL:
                return true;
            case SQL:
                return false;
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
            case SEATUNNEL:
                addSeaTunnelArtifact(jobInstanceDTO, flinkSessionJobSpec);
                break;
            case SQL:
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

    private void addSeaTunnelArtifact(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkSessionJobSpec spec) {
        WsArtifactSeaTunnel artifactSeaTunnel = jobInstanceDTO.getWsFlinkKubernetesJob().getArtifactSeaTunnel();
        JobSpec jobSpec = new JobSpec();
        jobSpec.setJarURI(SeaTunnelReleaseUtil.seatunnelStarterUrl(SeaTunnelReleaseUtil.STARTER_REPO_URL, artifactSeaTunnel.getSeaTunnelVersion().getValue()));
        jobSpec.setEntryClass(SeaTunnelReleaseUtil.SEATUNNEL_MAIN_CLASS);
        List<String> args = Arrays.asList("--config", ResourceNames.SEATUNNEL_CONF_FILE_PATH);
        jobSpec.setArgs(args.toArray(new String[2]));
        spec.setJob(jobSpec);
    }
}
