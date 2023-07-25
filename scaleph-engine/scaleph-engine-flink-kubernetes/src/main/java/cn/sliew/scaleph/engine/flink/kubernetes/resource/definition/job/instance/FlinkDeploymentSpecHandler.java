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

package cn.sliew.scaleph.engine.flink.kubernetes.resource.definition.job.instance;

import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.FlinkDeploymentSpec;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.handler.FileSystemPluginHandler;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.handler.FlinkImageHandler;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.handler.FlinkJobServiceHandler;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.handler.FlinkStateStorageHandler;
import cn.sliew.scaleph.engine.flink.kubernetes.service.dto.WsFlinkKubernetesJobInstanceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FlinkDeploymentSpecHandler {

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

    public FlinkDeploymentSpec handle(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec flinkDeploymentSpec) {
        FlinkDeploymentSpec spec = Optional.ofNullable(flinkDeploymentSpec).orElse(new FlinkDeploymentSpec());
        addArtifact(jobInstanceDTO, spec);
        enableFileSystem(jobInstanceDTO, spec);
        enableFlinkStateStore(jobInstanceDTO, spec);
        addService(spec);
        addImage(jobInstanceDTO, spec);
        return spec;
    }

    private void addArtifact(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        artifactConverterFactory.convert(jobInstanceDTO, spec);
    }

    private void enableFileSystem(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        fileSystemPluginHandler.handle(jobInstanceDTO.getWsFlinkKubernetesJob(), spec);
    }

    private void enableFlinkStateStore(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        flinkStateStorageHandler.handle(jobInstanceDTO.getInstanceId(), spec.getFlinkConfiguration());
    }

    private void addService(FlinkDeploymentSpec spec) {
        flinkJobServiceHandler.handle(spec);
    }

    private void addImage(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO, FlinkDeploymentSpec spec) {
        flinkImageHandler.handle(jobInstanceDTO, spec);
    }
}
