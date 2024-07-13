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

package cn.sliew.scaleph.workspace.flink.cdc.service;

import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.workspace.flink.cdc.service.dto.WsArtifactFlinkCDCDTO;
import cn.sliew.scaleph.workspace.flink.cdc.service.param.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Optional;

public interface WsArtifactFlinkCDCService {

    Page<WsArtifactFlinkCDCDTO> list(WsArtifactFlinkCDCListParam param);

    Page<WsArtifactFlinkCDCDTO> listByArtifact(WsArtifactFlinkCDCArtifactParam param);

    List<WsArtifactFlinkCDCDTO> listAll(WsArtifactFlinkCDCSelectListParam param);

    List<WsArtifactFlinkCDCDTO> listAllByArtifact(Long artifactId);

    WsArtifactFlinkCDCDTO selectOne(Long id);

    WsArtifactFlinkCDCDTO selectCurrent(Long artifactId);

    JsonNode buildConfig(WsArtifactFlinkCDCDTO dto) throws Exception;

    WsArtifactFlinkCDCDTO insert(WsArtifactFlinkCDCAddParam param);

    int update(WsArtifactFlinkCDCUpdateParam param);

    int delete(Long id) throws ScalephException;

    int deleteBatch(List<Long> ids) throws ScalephException;

    int deleteArtifact(Long artifactId) throws ScalephException;
}
