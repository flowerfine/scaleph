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

package cn.sliew.scaleph.engine.flink.kubernetes.operator.entity;

import cn.sliew.scaleph.engine.flink.kubernetes.operator.spec.FlinkDeploymentSpec;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"kind", "apiVersion", "metadata", "spec", "status"})
public class Deployment {

    private final String kind = "Deployment";
    private final String apiVersion = "v1";
    private DeploymentMetadata metadata;
    private FlinkDeploymentSpec spec;

    @Data
    @EqualsAndHashCode
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPropertyOrder({"name", "namespace", "labels", "annotations"})
    public static final class DeploymentMetadata {

        private String name;
        private String namespace;
        private Map<String, String> labels;
        private Map<String, String> annotations;
    }
}
