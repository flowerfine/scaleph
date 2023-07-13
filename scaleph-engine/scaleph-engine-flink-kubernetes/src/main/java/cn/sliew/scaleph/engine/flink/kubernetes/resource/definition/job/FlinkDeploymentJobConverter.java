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

package cn.sliew.scaleph.engine.flink.kubernetes.resource.definition.job;

import cn.sliew.scaleph.common.util.SeaTunnelReleaseUtil;
import cn.sliew.scaleph.config.resource.ResourceLabels;
import cn.sliew.scaleph.config.resource.ResourceNames;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkArtifactJar;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkArtifactSql;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.FlinkDeploymentSpec;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.JobSpec;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.definition.deployment.FlinkDeployment;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.definition.deployment.FlinkDeploymentConverter;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.handler.FileFetcherFactory;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.handler.FileSystemPluginHandler;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.handler.FlinkStateStorageHandler;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.handler.SeaTunnelConfHandler;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.kubernetes.resource.ResourceConverter;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.client.utils.Serialization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class FlinkDeploymentJobConverter implements ResourceConverter<WsFlinkKubernetesJobDTO, String> {

    @Autowired
    private FileFetcherFactory fileFetcherFactory;
    @Autowired
    private SeaTunnelConfHandler seaTunnelConfHandler;
    @Autowired
    private FileSystemPluginHandler fileSystemPluginHandler;
    @Autowired
    private FlinkStateStorageHandler flinkStateStorageHandler;

    @Override
    public String convertTo(WsFlinkKubernetesJobDTO source) throws Exception {
        FlinkDeploymentJob deployment = new FlinkDeploymentJob();
        FlinkDeployment flinkDeployment = FlinkDeploymentConverter.INSTANCE.convertTo(source.getFlinkDeployment());
        ObjectMetaBuilder builder = new ObjectMetaBuilder(flinkDeployment.getMetadata(), true);
        String name = StringUtils.hasText(source.getJobId()) ? source.getJobId() : source.getName();
        builder.withName(name);
        builder.withLabels(Map.of(ResourceLabels.SCALEPH_LABEL_NAME, source.getName()));
        deployment.setMetadata(builder.build());
        FlinkDeploymentSpec spec = flinkDeployment.getSpec();
        deployment.setSpec(spec);
        fileSystemPluginHandler.customize(source, deployment);
        flinkStateStorageHandler.customize(deployment);
        if (source.getFlinkArtifactJar() != null) {
            WsFlinkArtifactJar flinkArtifactJar = source.getFlinkArtifactJar();
            JobSpec jobSpec = new JobSpec();
            jobSpec.setJarURI(ResourceNames.JAR_LOCAL_PATH + flinkArtifactJar.getFileName());
            jobSpec.setEntryClass(flinkArtifactJar.getEntryClass());
            jobSpec.setArgs(StringUtils.split(flinkArtifactJar.getJarParams(), " "));
            spec.setJob(jobSpec);
            fileFetcherFactory.customize(source, deployment);

            return Serialization.asYaml(deployment);
        }
        if (source.getFlinkArtifactSql() != null) {
            WsFlinkArtifactSql flinkArtifactSql = source.getFlinkArtifactSql();
            JobSpec jobSpec = new JobSpec();
            jobSpec.setJarURI(ResourceNames.SQL_LOCAL_PATH + "sql-runner.jar");
            jobSpec.setEntryClass("cn.sliew.scaleph.engine.sql.SqlRunner");
            List<String> args = Arrays.asList(SqlUtil.format(flinkArtifactSql.getScript()));
            jobSpec.setArgs(args.toArray(new String[1]));
            spec.setJob(jobSpec);
            return Serialization.asYaml(deployment);
        }
        if (source.getWsDiJob() != null) {
            JobSpec jobSpec = new JobSpec();
            jobSpec.setJarURI(ResourceNames.LOCAL_SCHEMA + "/opt/seatunnel/starter/" + SeaTunnelReleaseUtil.STARTER_JAR_NAME);
            jobSpec.setEntryClass(SeaTunnelReleaseUtil.SEATUNNEL_MAIN_CLASS);
            List<String> args = Arrays.asList("--config", ResourceNames.SEATUNNEL_CONF_LOCAL_PATH);
            jobSpec.setArgs(args.toArray(new String[2]));
            spec.setJob(jobSpec);
            ConfigMap seatunnelConfConfigMap = seaTunnelConfHandler.customize(source, deployment);
            return Serialization.asYaml(deployment) + Serialization.asYaml(seatunnelConfConfigMap);
        }
        return Serialization.asYaml(deployment);
    }

    @Override
    public WsFlinkKubernetesJobDTO convertFrom(String target) {
        throw new UnsupportedOperationException();
    }
}
