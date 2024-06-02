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

import cn.sliew.scaleph.application.flink.resource.definition.sessioncluster.FlinkSessionCluster;
import cn.sliew.scaleph.application.flink.operator.spec.AbstractFlinkSpec;
import cn.sliew.scaleph.application.flink.operator.spec.FlinkSessionJobSpec;
import cn.sliew.scaleph.application.flink.resource.handler.FileSystemParamHandler;
import cn.sliew.scaleph.application.flink.resource.handler.FlinkRuntimeModeHandler;
import cn.sliew.scaleph.application.flink.resource.handler.FlinkStateStorageHandler;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobInstanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FlinkSessionJobSpecHandler {

    @Autowired
    private FlinkRuntimeModeHandler flinkRuntimeModeHandler;
    @Autowired
    private ArtifactConverterFactory artifactConverterFactory;
    @Autowired
    private FlinkStateStorageHandler flinkStateStorageHandler;
    @Autowired
    private FileSystemParamHandler fileSystemParamHandler;

    public FlinkSessionJobSpec handle(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkSessionCluster flinkSessionCluster, FlinkSessionJobSpec flinkSessionJobSpec) throws Exception {
        FlinkSessionJobSpec spec = Optional.ofNullable(flinkSessionJobSpec).orElse(new FlinkSessionJobSpec());
        addDeploymentName(spec, flinkSessionCluster);
        setRuntimeMode(jobInstanceDTO, spec);
        addArtifact(jobInstanceDTO, spec);
        enableFlinkStateStore(jobInstanceDTO, spec);
        setFileSystemParam(spec);
        return spec;
    }

    private void addDeploymentName(FlinkSessionJobSpec spec, FlinkSessionCluster flinkSessionCluster) {
        spec.setDeploymentName(flinkSessionCluster.getMetadata().getName());
    }

    private void setRuntimeMode(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, AbstractFlinkSpec spec) {
        flinkRuntimeModeHandler.handle(jobInstanceDTO.getWsFlinkKubernetesJob(), spec);
    }

    private void addArtifact(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkSessionJobSpec spec) throws Exception {
        artifactConverterFactory.convert(jobInstanceDTO, spec);
    }

    private void enableFlinkStateStore(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkSessionJobSpec spec) {
        flinkStateStorageHandler.handle(jobInstanceDTO.getInstanceId(), spec);
    }

    private void setFileSystemParam(FlinkSessionJobSpec spec) {
        fileSystemParamHandler.handle(spec);
    }

}
