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
import cn.sliew.scaleph.workspace.flink.cdc.service.dto.WsFlinkArtifactCDCDTO;
import cn.sliew.scaleph.workspace.flink.cdc.service.param.WsFlinkArtifactCDCAddParam;
import cn.sliew.scaleph.workspace.flink.cdc.service.param.WsFlinkArtifactCDCListParam;
import cn.sliew.scaleph.workspace.flink.cdc.service.param.WsFlinkArtifactCDCSelectListParam;
import cn.sliew.scaleph.workspace.flink.cdc.service.param.WsFlinkArtifactCDCUpdateParam;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface FlinkCDCJobService {

    Page<WsFlinkArtifactCDCDTO> listByPage(WsFlinkArtifactCDCListParam param);

    List<WsFlinkArtifactCDCDTO> listAll(WsFlinkArtifactCDCSelectListParam param);

    List<WsFlinkArtifactCDCDTO> listAllByArtifact(Long artifactId);

    WsFlinkArtifactCDCDTO selectOne(Long id);

    WsFlinkArtifactCDCDTO selectCurrent(Long artifactId);

    String preview(Long id) throws Exception;

    WsFlinkArtifactCDCDTO insert(WsFlinkArtifactCDCAddParam param);

    int update(WsFlinkArtifactCDCUpdateParam param);

    int delete(Long id) throws ScalephException;

    int deleteBatch(List<Long> ids) throws ScalephException;

    int deleteAll(Long flinkArtifactId) throws ScalephException;
}
