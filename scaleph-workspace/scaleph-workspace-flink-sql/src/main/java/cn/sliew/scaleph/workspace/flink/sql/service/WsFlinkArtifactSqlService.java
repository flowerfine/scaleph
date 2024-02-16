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

package cn.sliew.scaleph.workspace.flink.sql.service;

import cn.sliew.scaleph.common.exception.ScalephException;
import cn.sliew.scaleph.workspace.flink.sql.service.dto.WsFlinkArtifactSqlDTO;
import cn.sliew.scaleph.engine.sql.service.param.*;
import cn.sliew.scaleph.workspace.flink.sql.service.param.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface WsFlinkArtifactSqlService {

    Page<WsFlinkArtifactSqlDTO> list(WsFlinkArtifactSqlListParam param);

    Page<WsFlinkArtifactSqlDTO> listByArtifact(WsFlinkArtifactSqlHistoryParam param);

    List<WsFlinkArtifactSqlDTO> listAll(WsFlinkArtifactSqlSelectListParam param);

    List<WsFlinkArtifactSqlDTO> listAllByArtifact(Long artifactId);

    WsFlinkArtifactSqlDTO selectOne(Long id);

    WsFlinkArtifactSqlDTO selectCurrent(Long artifactId);

    void insert(WsFlinkArtifactSqlInsertParam param);

    int update(WsFlinkArtifactSqlUpdateParam params);

    int updateScript(WsFlinkArtifactSqlScriptUpdateParam param);

    int deleteOne(Long id) throws ScalephException;

    int deleteAll(Long flinkArtifactId) throws ScalephException;
}
