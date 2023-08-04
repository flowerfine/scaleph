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

import cn.sliew.scaleph.engine.sql.gateway.dto.WsFlinkSqlGatewayCreateCatalogParamsDTO;
import cn.sliew.scaleph.engine.sql.gateway.dto.WsFlinkSqlGatewayQueryParamsDTO;
import cn.sliew.scaleph.engine.sql.gateway.dto.WsFlinkSqlGatewayQueryResultDTO;
import cn.sliew.scaleph.engine.sql.gateway.dto.catalog.CatalogInfo;
import cn.sliew.scaleph.engine.sql.gateway.services.WsFlinkSqlGatewayService;
import cn.sliew.scaleph.system.model.PaginationParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
    @Parameters(@Parameter(name = "clusterId", description = "flink kubernetes session-cluster 的 sessionClusterId"))
    public ResponseEntity<GatewayInfo> getClusterInfo(@PathVariable("clusterId") String clusterId) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.getGatewayInfo(clusterId));
    }

    @PutMapping("{clusterId}/openSession")
    @Operation(summary = "新建Session", description = "新建Session")
    @Parameters(@Parameter(name = "clusterId", description = "flink kubernetes session-cluster 的 sessionClusterId"))
    public ResponseEntity<String> openSession(@PathVariable("clusterId") String clusterId) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.openSession(clusterId));
    }

    @DeleteMapping("{clusterId}/{sessionHandleId}")
    @Operation(summary = "关闭Session", description = "关闭Session")
    @Parameters({
            @Parameter(name = "clusterId", description = "flink kubernetes session-cluster 的 sessionClusterId"),
            @Parameter(name = "sessionHandleId", description = "openSession() 返回结果")
    })
    public ResponseEntity<String> closeSession(@PathVariable("clusterId") String clusterId,
                                               @PathVariable("sessionHandleId") String sessionHandleId) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.closeSession(clusterId, sessionHandleId));
    }

    @GetMapping("{clusterId}/{sessionHandleId}/getCatalogInfo")
    @Operation(summary = "获取所有Catalog信息", description = "获取所有Catalog信息")
    @Parameters({
            @Parameter(name = "clusterId", description = "flink kubernetes session-cluster 的 sessionClusterId"),
            @Parameter(name = "sessionHandleId", description = "openSession() 返回结果"),
            @Parameter(name = "includeSystemFunctions", description = "是否返回系统函数")
    })
    public ResponseEntity<Set<CatalogInfo>> getCatalogInfo(@PathVariable("clusterId") String clusterId,
                                                           @PathVariable("sessionHandleId") String sessionHandleId,
                                                           @RequestParam(value = "includeSystemFunctions", defaultValue = "false") boolean includeSystemFunctions) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.getCatalogInfo(clusterId, sessionHandleId, includeSystemFunctions));
    }


    @PostMapping("{clusterId}/{sessionHandleId}/executeSql")
    @Operation(summary = "执行FlinkSql", description = "执行FlinkSql")
    @Parameters({
            @Parameter(name = "clusterId", description = "flink kubernetes session-cluster 的 sessionClusterId"),
            @Parameter(name = "sessionHandleId", description = "openSession() 返回结果")
    })
    public ResponseEntity<String> executeSql(@PathVariable("clusterId") String clusterId,
                                             @PathVariable("sessionHandleId") String sessionHandleId,
                                             @RequestBody WsFlinkSqlGatewayQueryParamsDTO params) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.executeSql(clusterId, sessionHandleId, params));
    }

    @GetMapping("{clusterId}/{sessionHandleId}/{operationHandleId}/results")
    @Operation(summary = "获取Sql执行结果", description = "获取Sql执行结果")
    @Parameters({
            @Parameter(name = "clusterId", description = "flink kubernetes session-cluster 的 sessionClusterId"),
            @Parameter(name = "sessionHandleId", description = "openSession() 返回结果"),
            @Parameter(name = "operationHandleId", description = "executeSql() 返回结果")
    })
    public ResponseEntity<WsFlinkSqlGatewayQueryResultDTO> fetchResults(@PathVariable("clusterId") String clusterId,
                                                                        @PathVariable("sessionHandleId") String sessionHandleId,
                                                                        @PathVariable("operationHandleId") String operationHandleId,
                                                                        PaginationParam param) {
        ResultSet resultSet = wsFlinkSqlGatewayService.fetchResults(clusterId, sessionHandleId, operationHandleId, param.getCurrent(), param.getPageSize().intValue());
        try {
            WsFlinkSqlGatewayQueryResultDTO wsFlinkSqlGatewayQueryResultDTO = WsFlinkSqlGatewayQueryResultDTO.fromResultSet(resultSet);
            return ResponseEntity.ok(wsFlinkSqlGatewayQueryResultDTO);
        } catch (Exception e) {
            return ResponseEntity.of(Optional.empty());
        }
    }

    @DeleteMapping("{clusterId}/{sessionHandleId}/{operationHandleId}")
    @Operation(summary = "取消执行的任务", description = "取消执行的任务")
    @Parameters({
            @Parameter(name = "clusterId", description = "flink kubernetes session-cluster 的 sessionClusterId"),
            @Parameter(name = "sessionHandleId", description = "openSession() 返回结果"),
            @Parameter(name = "operationHandleId", description = "executeSql() 返回结果")
    })
    public ResponseEntity<Boolean> cancel(@PathVariable("clusterId") String clusterId,
                                          @PathVariable("sessionHandleId") String sessionHandleId,
                                          @PathVariable("operationHandleId") String operationHandleId) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.cancel(clusterId, sessionHandleId, operationHandleId));
    }

    @GetMapping("{clusterId}/{sessionHandleId}/completeStatement")
    @Operation(summary = "获取Sql提示", description = "获取Sql提示")
    @Parameters({
            @Parameter(name = "clusterId", description = "flink kubernetes session-cluster 的 sessionClusterId"),
            @Parameter(name = "sessionHandleId", description = "openSession() 返回结果"),
            @Parameter(name = "statement", description = "sql 片段"),
            @Parameter(name = "position", description = "position")
    })
    public ResponseEntity<List<String>> completeStatement(@PathVariable("clusterId") String clusterId,
                                                          @PathVariable("sessionHandleId") String sessionHandleId,
                                                          @RequestParam("statement") String statement,
                                                          @RequestParam("position") int position) {
        try {
            return ResponseEntity.ok(wsFlinkSqlGatewayService.completeStatement(clusterId, sessionHandleId, statement, position));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("{clusterId}/{sessionHandleId}/addDependencies")
    @Operation(summary = "添加依赖jar包", description = "添加依赖jar包")
    @Parameters({
            @Parameter(name = "clusterId", description = "flink kubernetes session-cluster 的 sessionClusterId"),
            @Parameter(name = "sessionHandleId", description = "openSession() 返回结果"),
            @Parameter(name = "jarIdList", description = "jar ids")
    })
    public ResponseEntity<Boolean> addDependencies(@PathVariable("clusterId") String clusterId,
                                                   @PathVariable("sessionHandleId") String sessionHandleId,
                                                   @RequestParam("jarIdList") List<Long> jarIdList) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.addDependencies(clusterId, sessionHandleId, jarIdList));
    }

    @PostMapping("{clusterId}/{sessionHandleId}/addCatalog")
    @Operation(summary = "添加catalog", description = "添加catalog")
    @Parameters({
            @Parameter(name = "clusterId", description = "flink kubernetes session-cluster 的 sessionClusterId"),
            @Parameter(name = "sessionHandleId", description = "openSession() 返回结果")
    })
    public ResponseEntity<Boolean> addCatalog(@PathVariable("clusterId") String clusterId,
                                              @PathVariable("sessionHandleId") String sessionHandleId,
                                              @RequestBody WsFlinkSqlGatewayCreateCatalogParamsDTO params) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.addCatalog(clusterId, sessionHandleId, params.getCatalogName(), params.getOptions()));
    }

}
