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
import cn.sliew.scaleph.engine.sql.gateway.services.WsFlinkSqlGatewayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.flink.table.catalog.CatalogBaseTable;
import org.apache.flink.table.gateway.api.results.FunctionInfo;
import org.apache.flink.table.gateway.api.results.GatewayInfo;
import org.apache.flink.table.gateway.api.results.ResultSet;
import org.apache.flink.table.gateway.api.results.TableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> closeSession(@PathVariable("clusterId") String clusterId, @PathVariable("sessionHandleId") String sessionHandleId) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.closeSession(clusterId, sessionHandleId));
    }

    @GetMapping("{clusterId}/{sessionHandleId}/listCatalogs")
    @Operation(summary = "列出Catalog", description = "列出当前Session下所有的Catalog")
    public ResponseEntity<Set<String>> listCatalogs(@PathVariable("clusterId") String clusterId, @PathVariable("sessionHandleId") String sessionHandleId) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.listCatalogs(clusterId, sessionHandleId));
    }

    @GetMapping("{clusterId}/{sessionHandleId}/listDatabases")
    @Operation(summary = "列出Catalog下所有的数据库", description = "列出Catalog下所有的数据库，如果Catalog为空，则列出当前Catalog下的数据库")
    public ResponseEntity<Set<String>> listDatabases(@PathVariable("clusterId") String clusterId,
                                                     @PathVariable("sessionHandleId") String sessionHandleId,
                                                     @RequestParam("catalog") String catalog) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.listDatabases(clusterId, sessionHandleId, catalog));
    }

    @GetMapping("{clusterId}/{sessionHandleId}/listTables")
    @Operation(summary = "列出Catalog/Database下所有的表", description = "列出Catalog/Database下所有表和视图")
    public ResponseEntity<Set<TableInfo>> listTables(@PathVariable("clusterId") String clusterId,
                                                     @PathVariable("sessionHandleId") String sessionHandleId,
                                                     @RequestParam("catalog") String catalog,
                                                     @RequestParam("database") String database) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.listTables(clusterId, sessionHandleId,
                catalog, database, Set.of(CatalogBaseTable.TableKind.values())));
    }

    @GetMapping("{clusterId}/{sessionHandleId}/listSystemFunctions")
    @Operation(summary = "列出系统方法", description = "列出系统方法")
    public ResponseEntity<Set<FunctionInfo>> listSystemFunctions(@PathVariable("clusterId") String clusterId,
                                                                 @PathVariable("sessionHandleId") String sessionHandleId) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.listSystemFunctions(clusterId, sessionHandleId));
    }

    @GetMapping("{clusterId}/{sessionHandleId}/listUserDefinedFunctions")
    @Operation(summary = "列出用户自定义方法", description = "列出用户自定义方法")
    public ResponseEntity<Set<FunctionInfo>> listUserDefinedFunctions(@PathVariable("clusterId") String clusterId,
                                                                      @PathVariable("sessionHandleId") String sessionHandleId,
                                                                      @RequestParam("catalog") String catalog,
                                                                      @RequestParam("database") String database) {
        return ResponseEntity.ok(wsFlinkSqlGatewayService.listUserDefinedFunctions(clusterId, sessionHandleId, catalog, database));
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

}
