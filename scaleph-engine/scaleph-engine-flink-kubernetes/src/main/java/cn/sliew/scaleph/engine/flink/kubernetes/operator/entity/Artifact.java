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

import cn.sliew.scaleph.common.dict.flink.FlinkJobType;
import cn.sliew.scaleph.common.jackson.polymorphic.Polymorphic;
import cn.sliew.scaleph.common.jackson.polymorphic.PolymorphicResolver;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

@JsonTypeIdResolver(Artifact.ArtifactResolver.class)
public interface Artifact extends Polymorphic<FlinkJobType> {

    class JarArtifact implements Artifact {

        private String jarURI;

        private String entryClass;

        private String[] args;

        private int parallelism;

        @Override
        public FlinkJobType getType() {
            return FlinkJobType.JAR;
        }
    }

    class SQLArtifact implements Artifact {

        private String jarURI;

        private String entryClass;

        private int parallelism;

        @Override
        public FlinkJobType getType() {
            return FlinkJobType.SQL;
        }
    }

    class SeaTunnelArtifact implements Artifact {

        private String jarURI;

        private String entryClass;

        private String[] args;

        private int parallelism;

        @Override
        public FlinkJobType getType() {
            return FlinkJobType.SEATUNNEL;
        }
    }

    final class ArtifactResolver extends PolymorphicResolver<FlinkJobType> {
        public ArtifactResolver() {
            bindDefault(JarArtifact.class);
            bind(FlinkJobType.JAR, JarArtifact.class);
            bind(FlinkJobType.SQL, SQLArtifact.class);
            bind(FlinkJobType.SEATUNNEL, SeaTunnelArtifact.class);
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
