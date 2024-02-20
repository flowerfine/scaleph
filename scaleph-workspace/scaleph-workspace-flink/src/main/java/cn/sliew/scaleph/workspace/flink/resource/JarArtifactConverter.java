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

import cn.sliew.scaleph.workspace.flink.service.dto.WsArtifactFlinkJarDTO;
import cn.sliew.scaleph.kubernetes.resource.ResourceConverter;

public enum JarArtifactConverter implements ResourceConverter<WsArtifactFlinkJarDTO, JarArtifact> {
    INSTANCE;

    @Override
    public JarArtifact convertTo(WsArtifactFlinkJarDTO source) {
        JarArtifact artifact = new JarArtifact();
        JarArtifact.JarArtifactSpec spec = new JarArtifact.JarArtifactSpec();
        spec.setFlinkVersion(source.getFlinkVersion().getValue());
        spec.setName(source.getArtifact().getName());
        spec.setJarUri(source.getPath());
        spec.setEntryClass(source.getEntryClass());
        spec.setMainArgs(source.getJarParams());
        artifact.setSpec(spec);
        return artifact;
    }

    @Override
    public WsArtifactFlinkJarDTO convertFrom(JarArtifact target) {
       throw new UnsupportedOperationException();
    }
}
