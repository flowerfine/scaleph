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

import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.common.dict.flink.kubernetes.DeploymentKind;
import cn.sliew.scaleph.common.util.SeaTunnelReleaseUtil;
import cn.sliew.scaleph.config.kubernetes.resource.ResourceNames;
import cn.sliew.scaleph.dao.entity.master.ws.WsArtifactFlinkCDC;
import cn.sliew.scaleph.dao.entity.master.ws.WsArtifactFlinkJar;
import cn.sliew.scaleph.dao.entity.master.ws.WsArtifactFlinkSql;
import cn.sliew.scaleph.application.flink.operator.spec.FlinkDeploymentSpec;
import cn.sliew.scaleph.application.flink.operator.spec.JobSpec;
import cn.sliew.scaleph.application.flink.resource.handler.FileFetcherHandler;
import cn.sliew.scaleph.application.flink.resource.handler.SeaTunnelConfHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Component
public class FlinkDeploymentArtifactHandler implements ArtifactHandler {

    @Autowired
    private FileFetcherHandler fileFetcherHandler;
    @Autowired
    private SeaTunnelConfHandler seaTunnelConfHandler;

    @Override
    public boolean support(DeploymentKind deploymentKind) {
        return deploymentKind == DeploymentKind.FLINK_DEPLOYMENT;
    }

    public void handle(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, Object spec) {
        FlinkDeploymentSpec flinkDeploymentSpec = (FlinkDeploymentSpec) spec;
        switch (jobInstanceDTO.getWsFlinkKubernetesJob().getType()) {
            case JAR:
                addJarArtifact(jobInstanceDTO, flinkDeploymentSpec);
                break;
            case SQL:
                addSQLArtifact(jobInstanceDTO, flinkDeploymentSpec);
                break;
            case FLINK_CDC:
                addFlinkCDCArtifact(jobInstanceDTO, flinkDeploymentSpec);
                break;
            case SEATUNNEL:
                addSeaTunnelArtifact(jobInstanceDTO, flinkDeploymentSpec);
                break;
            default:
        }
    }

    private void addJarArtifact(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        WsArtifactFlinkJar artifactFlinkJar = jobInstanceDTO.getWsFlinkKubernetesJob().getArtifactFlinkJar();
        JobSpec jobSpec = new JobSpec();
        jobSpec.setJarURI(ResourceNames.JAR_LOCAL_PATH + artifactFlinkJar.getFileName());
        jobSpec.setEntryClass(artifactFlinkJar.getEntryClass());
        jobSpec.setArgs(StringUtils.split(artifactFlinkJar.getJarParams(), " "));
        spec.setJob(jobSpec);
        fileFetcherHandler.handleJarArtifact(jobInstanceDTO.getWsFlinkKubernetesJob(), spec);
    }

    private void addSQLArtifact(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        WsArtifactFlinkSql artifactFlinkSql = jobInstanceDTO.getWsFlinkKubernetesJob().getArtifactFlinkSql();
        JobSpec jobSpec = new JobSpec();
        jobSpec.setJarURI(ResourceNames.SQL_RUNNER_LOCAL_PATH);
        jobSpec.setEntryClass(ResourceNames.SQL_RUNNER_ENTRY_CLASS);
        List<String> args = Arrays.asList(SqlUtil.format(artifactFlinkSql.getScript()));
        jobSpec.setArgs(args.toArray(new String[1]));
        spec.setJob(jobSpec);
    }

    private void addFlinkCDCArtifact(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        WsArtifactFlinkCDC artifactFlinkCDC = jobInstanceDTO.getWsFlinkKubernetesJob().getArtifactFlinkCDC();
        JobSpec jobSpec = new JobSpec();
        jobSpec.setJarURI(ResourceNames.SQL_RUNNER_LOCAL_PATH);
        jobSpec.setEntryClass(ResourceNames.SQL_RUNNER_ENTRY_CLASS);
//        List<String> args = Arrays.asList(SqlUtil.format(artifactFlinkCDC.getScript()));
//        jobSpec.setArgs(args.toArray(new String[1]));
        spec.setJob(jobSpec);
    }

    private void addSeaTunnelArtifact(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        JobSpec jobSpec = new JobSpec();
        jobSpec.setJarURI(ResourceNames.SEATUNNEL_STARTER_PATH + SeaTunnelReleaseUtil.STARTER_JAR_NAME);
        jobSpec.setEntryClass(SeaTunnelReleaseUtil.SEATUNNEL_MAIN_CLASS);
        List<String> args = Arrays.asList("--config", ResourceNames.SEATUNNEL_CONF_FILE_PATH);
        jobSpec.setArgs(args.toArray(new String[2]));
        spec.setJob(jobSpec);
        seaTunnelConfHandler.handle(jobInstanceDTO, spec);
    }
}
