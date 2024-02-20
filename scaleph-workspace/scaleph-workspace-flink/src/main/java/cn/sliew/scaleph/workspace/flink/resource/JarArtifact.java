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

package cn.sliew.scaleph.workspace.flink.resource;

import cn.sliew.scaleph.kubernetes.Constant;
import cn.sliew.scaleph.kubernetes.resource.Resource;
import io.fabric8.kubernetes.client.CustomResource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
public class JarArtifact extends CustomResource implements Resource {

    private JarArtifactSpec spec;

    @Override
    public String getKind() {
        return Constant.JAR_ARTIFACT;
    }

    @Override
    public String getApiVersion() {
        return Constant.API_VERSION;
    }

    @Data
    @EqualsAndHashCode
    public static class JarArtifactSpec {

        private String flinkVersion;

        private String name;

        private String jarUri;

        private String entryClass;

        private String mainArgs;

        private List<String> additionalDependencies;
    }
}
