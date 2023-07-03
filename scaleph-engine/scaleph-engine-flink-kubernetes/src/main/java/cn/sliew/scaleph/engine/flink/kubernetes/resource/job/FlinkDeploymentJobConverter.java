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

package cn.sliew.scaleph.engine.flink.kubernetes.resource.job;

import cn.sliew.scaleph.dao.entity.master.ws.WsDiJob;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkArtifactJar;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkArtifactSql;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.FlinkDeploymentSpec;
import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.JobSpec;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.definition.FileFetcherFactory;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.deployment.FlinkDeployment;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.deployment.FlinkDeploymentConverter;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.kubernetes.Constant;
import cn.sliew.scaleph.kubernetes.resource.ResourceConverter;
import cn.sliew.scaleph.kubernetes.resource.definition.ResourceCustomizer;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;
import io.fabric8.kubernetes.api.model.PodBuilder;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum FlinkDeploymentJobConverter implements ResourceConverter<WsFlinkKubernetesJobDTO, FlinkDeploymentJob> {
    INSTANCE;

    @Override
    public FlinkDeploymentJob convertTo(WsFlinkKubernetesJobDTO source) {
        FlinkDeploymentJob deployment = new FlinkDeploymentJob();
        FlinkDeployment flinkDeployment = FlinkDeploymentConverter.INSTANCE.convertTo(source.getFlinkDeployment());
        ObjectMetaBuilder builder = new ObjectMetaBuilder(flinkDeployment.getMetadata(), true);
        String name = StringUtils.hasText(source.getJobId()) ? source.getJobId() : source.getName();
        builder.withName(name);
        builder.withLabels(Map.of(Constant.SCALEPH_NAME, source.getName()));
        deployment.setMetadata(builder.build());
        FlinkDeploymentSpec spec = flinkDeployment.getSpec();
        if (source.getFlinkArtifactJar() != null) {
            WsFlinkArtifactJar flinkArtifactJar = source.getFlinkArtifactJar();
            JobSpec jobSpec = new JobSpec();
            jobSpec.setJarURI(flinkArtifactJar.getFileName());
            jobSpec.setEntryClass(flinkArtifactJar.getEntryClass());
            jobSpec.setArgs(StringUtils.split(flinkArtifactJar.getJarParams(), " "));
            spec.setJob(jobSpec);
        }
        if (source.getFlinkArtifactSql() != null) {
            WsFlinkArtifactSql flinkArtifactSql = source.getFlinkArtifactSql();
            JobSpec jobSpec = new JobSpec();
            jobSpec.setJarURI("local:///sql-runner.jar");
            jobSpec.setEntryClass("cn.sliew.engine.flink.sql.SqlRunner");
            List<String> args = Arrays.asList("--script", flinkArtifactSql.getScript());
            jobSpec.setArgs(args.toArray(new String[2]));
            spec.setJob(jobSpec);
        }
        if (source.getWsDiJob() != null) {
            WsDiJob wsDiJob = source.getWsDiJob();
            JobSpec jobSpec = new JobSpec();
            jobSpec.setJarURI("local:///opt/seatunnel/starter/seatunnel-flink-starter.jar");
            jobSpec.setEntryClass("org.apache.seatunnel.core.starter.flink.SeatunnelFlink");
            List<String> args = Arrays.asList("--config", "todo config");
            jobSpec.setArgs(args.toArray(new String[2]));
            spec.setJob(jobSpec);
        }

        FileFetcherFactory fileFetcherFactory = new FileFetcherFactory("scaleph://scaleph", source);
        ResourceCustomizer<PodBuilder> podBuilderResourceCustomizer = fileFetcherFactory.create();
        PodBuilder podBuilder = new PodBuilder(spec.getPodTemplate());
        podBuilderResourceCustomizer.customize(new PodBuilder());
        spec.setPodTemplate(podBuilder.build());
        deployment.setSpec(spec);
        return deployment;
    }

    @Override
    public WsFlinkKubernetesJobDTO convertFrom(FlinkDeploymentJob target) {
        WsFlinkKubernetesJobDTO dto = new WsFlinkKubernetesJobDTO();

        return dto;
    }
}
