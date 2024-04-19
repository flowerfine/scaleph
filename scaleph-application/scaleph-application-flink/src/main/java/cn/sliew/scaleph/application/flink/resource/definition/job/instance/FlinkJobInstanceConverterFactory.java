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

import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobDTO;
import cn.sliew.scaleph.application.flink.service.dto.WsFlinkKubernetesJobInstanceDTO;
import cn.sliew.scaleph.common.dict.flink.FlinkVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class FlinkJobInstanceConverterFactory {

    @Autowired
    private Map<String, FlinkJobInstanceConverter> registry;

    public String convert(WsFlinkKubernetesJobInstanceDTO jobInstanceDTO) {
        return findConverter(jobInstanceDTO.getWsFlinkKubernetesJob())
                .map(converter -> converter.convert(jobInstanceDTO))
                .orElseThrow();
    }

    private Optional<FlinkJobInstanceConverter> findConverter(WsFlinkKubernetesJobDTO jobDTO) {
        return registry.values().stream()
                .filter(converter -> converter.support(jobDTO.getDeploymentKind()))
                .filter(converter -> converter.support(jobDTO.getType()))
                .filter(converter -> converter.support(getFlinkVersion(jobDTO)))
                .findAny();
    }

    public static FlinkVersion getFlinkVersion(WsFlinkKubernetesJobDTO jobDTO) {
        switch (jobDTO.getType()) {
            case JAR:
                return jobDTO.getArtifactFlinkJar().getFlinkVersion();
            case SQL:
                return jobDTO.getArtifactFlinkSql().getFlinkVersion();
            case FLINK_CDC:
                return jobDTO.getArtifactFlinkCDC().getFlinkVersion();
            case SEATUNNEL:
                return jobDTO.getArtifactSeaTunnel().getFlinkVersion();
            default:
                return FlinkVersion.V_1_17_1;
        }
    }

}
