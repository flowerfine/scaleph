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

import cn.sliew.scaleph.engine.sql.gateway.dto.WsFlinkSqlGatewayQueryParamsDTO;
import cn.sliew.scaleph.engine.sql.gateway.dto.WsFlinkSqlGatewayQueryResultDTO;
import cn.sliew.scaleph.engine.sql.gateway.dto.catalog.CatalogInfo;
import cn.sliew.scaleph.engine.sql.gateway.services.WsFlinkSqlGatewayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.flink.table.gateway.api.results.GatewayInfo;
import org.apache.flink.table.gateway.api.results.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Tag(name = "Flink Sql Gateway")
@RestController
@RequestMapping(path = "/api/flink/sql-gateway")
public class WsFlinkSqlGatewayController {

    private final WsFlinkSqlGatewayService wsFlinkSqlGatewayService;

    @Autowired
    public WsFlinkSqlGatewayController(WsFlinkSqlGatewayService wsFlinkSqlGatewayService) {
        this.wsFlinkSqlGatewayService = wsFlinkSqlGatewayService;
    }

    @GetMapping("{clusterId}/info")
    @Operation(summary = "获取SqlGateway信息", description = "获取SqlGateway信息")
    public ResponseEntity<GatewayInfo> getClusterInfo(@PathVariable("clusterId") String clusterId) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.getGatewayInfo(clusterId));
    }

    @RequestMapping(value = "{clusterId}/openSession", method = {RequestMethod.PUT, RequestMethod.POST})
    @Operation(summary = "新建Session", description = "新建Session")
    public ResponseEntity<String> openSession(@PathVariable("clusterId") String clusterId) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.openSession(clusterId));
    }

    @DeleteMapping("{clusterId}/{sessionHandleId}")
    @Operation(summary = "关闭Session", description = "关闭Session")
    public ResponseEntity<String> closeSession(
            @PathVariable("clusterId") String clusterId,
            @PathVariable("sessionHandleId") String sessionHandleId) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.closeSession(clusterId, sessionHandleId));
    }

    @GetMapping("{clusterId}/{sessionHandleId}/getCatalogInfo")
    @Operation(summary = "获取所有Catalog信息", description = "获取所有Catalog信息")
    public ResponseEntity<Set<CatalogInfo>> getCatalogInfo(
            @PathVariable("clusterId") String clusterId,
            @PathVariable("sessionHandleId") String sessionHandleId) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.getCatalogInfo(clusterId, sessionHandleId));
    }


    @RequestMapping(value = "{clusterId}/{sessionHandleId}/executeSql", method = {RequestMethod.POST, RequestMethod.PUT})
    @Operation(summary = "执行FlinkSql", description = "执行FlinkSql")
    public ResponseEntity<String> executeSql(@PathVariable("clusterId") String clusterId,
                                             @PathVariable("sessionHandleId") String sessionHandleId,
                                             @RequestBody WsFlinkSqlGatewayQueryParamsDTO params) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.executeSql(clusterId, sessionHandleId, params));
    }

    @GetMapping("{clusterId}/{sessionHandleId}/{operationHandleId}/results/{token}")
    @Operation(summary = "获取Sql执行结果", description = "获取Sql执行结果")
    public ResponseEntity<WsFlinkSqlGatewayQueryResultDTO> fetchResults(@PathVariable("clusterId") String clusterId,
                                                                        @PathVariable("sessionHandleId") String sessionHandleId,
                                                                        @PathVariable("operationHandleId") String operationHandleId,
                                                                        @PathVariable(value = "token", required = false) Long token,
                                                                        @RequestParam(value = "maxRows", required = false, defaultValue = "100") int maxRows) {
        ResultSet resultSet = wsFlinkSqlGatewayService.fetchResults(clusterId, sessionHandleId, operationHandleId, token, maxRows);
        try {
            WsFlinkSqlGatewayQueryResultDTO wsFlinkSqlGatewayQueryResultDTO = WsFlinkSqlGatewayQueryResultDTO.fromResultSet(resultSet);
            return ResponseEntity.ok(wsFlinkSqlGatewayQueryResultDTO);
        } catch (Exception e) {
            return ResponseEntity.of(Optional.empty());
        }
    }

    @DeleteMapping("{clusterId}/{sessionHandleId}/{operationHandleId}")
    @Operation(summary = "取消执行的任务", description = "取消执行的任务")
    public ResponseEntity<Boolean> cancel(@PathVariable("clusterId") String clusterId,
                                          @PathVariable("sessionHandleId") String sessionHandleId,
                                          @PathVariable("operationHandleId") String operationHandleId) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.cancel(clusterId, sessionHandleId, operationHandleId));
    }

    @GetMapping("{clusterId}/{sessionHandleId}/completeStatement")
    @Operation(summary = "获取Sql提示", description = "获取Sql提示")
    public ResponseEntity<List<String>> completeStatement(
            @PathVariable("clusterId") String clusterId,
            @PathVariable("sessionHandleId") String sessionHandleId,
            @RequestParam("statement") String statement,
            @RequestParam("position") int position) {
        try {
            return ResponseEntity.ok(wsFlinkSqlGatewayService.completeStatement(clusterId, sessionHandleId, statement, position));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
