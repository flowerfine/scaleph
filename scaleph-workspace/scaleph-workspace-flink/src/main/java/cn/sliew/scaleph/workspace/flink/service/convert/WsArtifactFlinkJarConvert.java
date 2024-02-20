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

package cn.sliew.scaleph.workspace.flink.service.convert;

import cn.sliew.scaleph.common.convert.BaseConvert;
import cn.sliew.scaleph.dao.entity.master.ws.WsArtifactFlinkJar;
import cn.sliew.scaleph.workspace.flink.service.dto.WsArtifactFlinkJarDTO;
import cn.sliew.scaleph.workspace.project.service.convert.WsArtifactConvert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {WsArtifactConvert.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WsArtifactFlinkJarConvert extends BaseConvert<WsArtifactFlinkJar, WsArtifactFlinkJarDTO> {
    WsArtifactFlinkJarConvert INSTANCE = Mappers.getMapper(WsArtifactFlinkJarConvert.class);

    @Override
    @Mapping(source = "artifact.id", target = "artifactId")
    WsArtifactFlinkJar toDo(WsArtifactFlinkJarDTO dto);
}
