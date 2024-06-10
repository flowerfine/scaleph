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

import cn.sliew.scaleph.application.flink.operator.spec.*;
import cn.sliew.scaleph.application.flink.resource.handler.*;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.common.jackson.JsonMerger;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class FlinkDeploymentSpecHandler {

    @Autowired
    private FlinkRuntimeModeHandler flinkRuntimeModeHandler;
    @Autowired
    private ArtifactConverterFactory artifactConverterFactory;
    @Autowired
    private FileSystemPluginHandler fileSystemPluginHandler;
    @Autowired
    private FlinkStateStorageHandler flinkStateStorageHandler;
    @Autowired
    private FlinkJobServiceHandler flinkJobServiceHandler;
    @Autowired
    private FlinkImageHandler flinkImageHandler;
    @Autowired
    private LoggingHandler loggingHandler;
    @Autowired
    private FlinkMainContainerHandler flinkMainContainerHandler;
    @Autowired
    private PodTemplateHandler podTemplateHandler;

    public FlinkDeploymentSpec handle(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec flinkDeploymentSpec) {
        FlinkDeploymentSpec spec = Optional.ofNullable(flinkDeploymentSpec).orElse(new FlinkDeploymentSpec());
        setRuntimeMode(jobInstanceDTO, spec);
        setPodTemplate(jobInstanceDTO, spec);
        addArtifact(jobInstanceDTO, spec);
        enableFileSystem(jobInstanceDTO, spec);
        enableFlinkStateStore(jobInstanceDTO, spec);
        addService(spec);
        addImage(jobInstanceDTO, spec);
        addLogging(jobInstanceDTO, spec);
        customFlinkMainContainer(jobInstanceDTO, spec);

        mergeJobInstance(jobInstanceDTO, spec);
        return spec;
    }

    private void setRuntimeMode(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        flinkRuntimeModeHandler.handle(jobInstanceDTO.getWsFlinkKubernetesJob(), spec);
    }

    private void setPodTemplate(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        podTemplateHandler.handle(jobInstanceDTO.getWsFlinkKubernetesJob(), spec);
    }

    private void addArtifact(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        artifactConverterFactory.convert(jobInstanceDTO, spec);
    }

    private void enableFileSystem(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        fileSystemPluginHandler.handle(jobInstanceDTO.getWsFlinkKubernetesJob(), spec);
    }

    private void enableFlinkStateStore(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        flinkStateStorageHandler.handle(jobInstanceDTO.getInstanceId(), spec);
    }

    private void addService(FlinkDeploymentSpec spec) {
        flinkJobServiceHandler.handle(spec);
    }

    private void addImage(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        flinkImageHandler.handle(jobInstanceDTO, spec);
    }

    private void addLogging(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        loggingHandler.handle(jobInstanceDTO.getWsFlinkKubernetesJob().getFlinkDeployment().getLogConfiguration(), spec);
    }

    private void customFlinkMainContainer(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        flinkMainContainerHandler.handle(jobInstanceDTO, spec);
    }

    private void mergeJobInstance(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        spec.setJobManager(JsonMerger.merge(spec.getJobManager(), jobInstanceDTO.getJobManager(), JobManagerSpec.class));
        spec.setTaskManager(JsonMerger.merge(spec.getTaskManager(), jobInstanceDTO.getTaskManager(), TaskManagerSpec.class));
        spec.setFlinkConfiguration(JsonMerger.merge(spec.getFlinkConfiguration(), jobInstanceDTO.getUserFlinkConfiguration(), Map.class));
        JobSpec job = spec.getJob();
        if (jobInstanceDTO.getParallelism() != null) {
            job.setParallelism(jobInstanceDTO.getParallelism());
        }
        if (jobInstanceDTO.getUpgradeMode() != null) {
            job.setUpgradeMode(EnumUtils.getEnum(UpgradeMode.class, jobInstanceDTO.getUpgradeMode().name()));
        }
        job.setAllowNonRestoredState(jobInstanceDTO.getAllowNonRestoredState());
    }
}
