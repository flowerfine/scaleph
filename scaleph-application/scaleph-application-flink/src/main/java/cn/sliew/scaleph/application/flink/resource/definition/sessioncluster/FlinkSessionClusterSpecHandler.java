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

package cn.sliew.scaleph.application.flink.resource.definition.sessioncluster;

import cn.sliew.scaleph.application.flink.operator.spec.FlinkSessionClusterSpec;
import cn.sliew.scaleph.application.flink.resource.handler.FileSystemPluginHandler;
import cn.sliew.scaleph.application.flink.resource.handler.FlinkMainContainerHandler;
import cn.sliew.scaleph.application.flink.resource.handler.LoggingHandler;
import cn.sliew.scaleph.application.flink.resource.handler.PodTemplateHandler;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesSessionClusterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FlinkSessionClusterSpecHandler {

    @Autowired
    private FileSystemPluginHandler fileSystemPluginHandler;
    @Autowired
    private LoggingHandler loggingHandler;
    @Autowired
    private FlinkMainContainerHandler flinkMainContainerHandler;
    @Autowired
    private PodTemplateHandler podTemplateHandler;

    public FlinkSessionClusterSpec handle(WsFlinkKubernetesSessionClusterDTO sessionClusterDTO, FlinkSessionClusterSpec flinkSessionClusterSpec) {
        FlinkSessionClusterSpec spec = Optional.ofNullable(flinkSessionClusterSpec).orElse(new FlinkSessionClusterSpec());
        setPodTemplate(sessionClusterDTO, spec);
        enableFileSystem(sessionClusterDTO, spec);
        addLogging(sessionClusterDTO, spec);
        customFlinkMainContainer(sessionClusterDTO, spec);
        return spec;
    }

    private void setPodTemplate(WsFlinkKubernetesSessionClusterDTO sessionClusterDTO, FlinkSessionClusterSpec spec) {
        podTemplateHandler.handle(sessionClusterDTO, spec);
    }

    private void enableFileSystem(WsFlinkKubernetesSessionClusterDTO sessionClusterDTO, FlinkSessionClusterSpec spec) {
        fileSystemPluginHandler.handle(sessionClusterDTO, spec);
    }

    private void addLogging(WsFlinkKubernetesSessionClusterDTO sessionClusterDTO, FlinkSessionClusterSpec spec) {
        loggingHandler.handle(sessionClusterDTO.getLogConfiguration(), spec);
    }

    private void customFlinkMainContainer(WsFlinkKubernetesSessionClusterDTO sessionClusterDTO, FlinkSessionClusterSpec spec) {
        flinkMainContainerHandler.handle(sessionClusterDTO, spec);
    }
}
