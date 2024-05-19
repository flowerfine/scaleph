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

package cn.sliew.scaleph.workspace.seatunnel.service;

import cn.sliew.scaleph.workspace.seatunnel.service.dto.WsArtifactSeaTunnelDTO;
import cn.sliew.scaleph.workspace.seatunnel.service.param.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Optional;

public interface WsArtifactSeaTunnelService {

    Page<WsArtifactSeaTunnelDTO> list(WsArtifactSeaTunnelListParam param);

    Page<WsArtifactSeaTunnelDTO> listByArtifact(WsArtifactSeaTunnelArtifactParam param);

    List<WsArtifactSeaTunnelDTO> listAll(WsArtifactSeaTunnelSelectListParam param);

    List<WsArtifactSeaTunnelDTO> listAllByArtifact(Long artifactId);

    WsArtifactSeaTunnelDTO selectOne(Long id);

    WsArtifactSeaTunnelDTO selectCurrent(Long artifactId);

    String buildConfig(Long id, Optional<String> jobName, Optional<String> jobMode) throws Exception;

    WsArtifactSeaTunnelDTO insert(WsArtifactSeaTunnelAddParam param);

    int update(WsArtifactSeaTunnelUpdateParam param);

    void updateGraph(WsArtifactSeaTunnelGraphParam param);

    int delete(Long id);

    int deleteBatch(List<Long> ids);

    int deleteArtifact(Long artifactId);

}
