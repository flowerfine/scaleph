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

import cn.sliew.scaleph.common.dict.flink.FlinkDeploymentMode;
import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.jackson.polymorphic.Polymorphic;
import cn.sliew.scaleph.common.jackson.polymorphic.PolymorphicResolver;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.artifact.Artifact;
import cn.sliew.scaleph.engine.flink.kubernetes.resource.savepoint.RestoreStrategy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

import java.util.Map;

@JsonTypeIdResolver(Job.JobResolver.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Job extends Polymorphic<FlinkDeploymentMode> {

    String getName();

    Integer getParallelism();

    Artifact getArtifact();

    RestoreStrategy getRestoreStrategy();

    Map<String, String> getFlinkConfiguration();

    final class JobResolver extends PolymorphicResolver<FlinkDeploymentMode> {

        public JobResolver() {
            bindDefault(FlinkDeploymentJob.class);
            bind(FlinkDeploymentMode.APPLICATION, FlinkDeploymentJob.class);
            bind(FlinkDeploymentMode.SESSION, FlinkSessionJob.class);
        }

        @Override
        protected String typeFromSubtype(Object obj) {
            return subTypes.inverse().get(obj.getClass()).getValue();
        }

        @Override
        protected Class<?> subTypeFromType(String id) {
            Class<?> subType = subTypes.get(FlinkJobType.of(id));
            return subType != null ? subType : defaultClass;
        }
    }
}
