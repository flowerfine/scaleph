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

package cn.sliew.scaleph.api.controller.ws;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelEngineType;
import cn.sliew.scaleph.dag.xflow.dnd.DndDTO;
import cn.sliew.scaleph.workspace.seatunnel.service.SeaTunnelDagService;
import cn.sliew.scaleph.plugin.framework.exception.PluginException;
import cn.sliew.scaleph.system.model.ResponseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "SeaTunnel")
@RestController
@RequestMapping(path = {"/api/seatunnel", "/api/seatunnel"})
public class WsSeaTunnelController {

    @Autowired
    private SeaTunnelDagService seaTunnelDagService;

    @Logging
    @GetMapping("/dag/dnd/{type}")
    @Operation(summary = "查询DAG节点元信息", description = "后端统一返回节点信息")
    public ResponseEntity<ResponseVO<List<DndDTO>>> loadNodeMeta(@PathVariable("type") SeaTunnelEngineType type) throws PluginException {
        List<DndDTO> dnds = seaTunnelDagService.getDnds(type);
        return new ResponseEntity<>(ResponseVO.success(dnds), HttpStatus.OK);
    }
}