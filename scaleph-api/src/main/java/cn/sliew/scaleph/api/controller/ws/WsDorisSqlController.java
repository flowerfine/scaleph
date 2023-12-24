/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.api.controller.ws;

import cn.sliew.scaleph.api.annotation.Logging;
import cn.sliew.scaleph.engine.doris.sql.WsDorisSqlService;
import cn.sliew.scaleph.engine.doris.sql.dto.Function;
import cn.sliew.scaleph.engine.doris.sql.dto.QueryResult;
import cn.sliew.scaleph.engine.doris.sql.dto.Table;
import cn.sliew.scaleph.engine.doris.sql.params.WsDorisSqlExecutionParam;
import cn.sliew.scaleph.engine.doris.sql.params.WsDorisSqlListParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Doris Sql")
@RestController
@RequestMapping(path = "/api/doris/sql")
public class WsDorisSqlController {

    @Autowired
    private WsDorisSqlService dorisSqlService;

    @Logging
    @PostMapping("/listCatalogs")
    @Operation(summary = "查询Doris Catalog列表", description = "查询Doris Catalog列表")
    public ResponseEntity<List<String>> listCatalogs(@RequestBody WsDorisSqlListParams listParams) {
        List<String> catalogs = dorisSqlService.listCatalogs(listParams.getInstanceId());
        return new ResponseEntity<>(catalogs, HttpStatus.OK);
    }

    @Logging
    @PostMapping("/listSchemas")
    @Operation(summary = "查询Doris Schema列表", description = "查询Doris Schema列表")
    public ResponseEntity<List<String>> listSchemas(@RequestBody WsDorisSqlListParams listParams) {
        List<String> schemas = dorisSqlService.listSchemas(listParams.getInstanceId(), listParams.getCatalogName());
        return new ResponseEntity<>(schemas, HttpStatus.OK);
    }

    @Logging
    @PostMapping("/listTables")
    @Operation(summary = "查询Doris Table列表", description = "查询Doris Table列表")
    public ResponseEntity<List<String>> listTables(@RequestBody WsDorisSqlListParams listParams) {
        List<String> tables = dorisSqlService.listTables(
                listParams.getInstanceId(),
                listParams.getCatalogName(),
                listParams.getSchemaName(),
                listParams.getTableTypes());
        return new ResponseEntity<>(tables, HttpStatus.OK);
    }

    @Logging
    @PostMapping("/getTableInfo")
    @Operation(summary = "查询Doris Table详情", description = "查询Doris Table详情")
    public ResponseEntity<Table> getTableInfo(@RequestBody WsDorisSqlListParams listParams) {
        Table table = dorisSqlService.getTableInfo(
                listParams.getInstanceId(),
                listParams.getCatalogName(),
                listParams.getSchemaName(),
                listParams.getTableName());
        return new ResponseEntity<>(table, HttpStatus.OK);
    }

    @Logging
    @PostMapping("/listFunctions")
    @Operation(summary = "查询Doris Function列表", description = "查询Doris Function列表")
    public ResponseEntity<List<Function>> listFunctions(@RequestBody WsDorisSqlListParams listParams) {
        List<Function> functions = dorisSqlService.listFunctions(listParams.getInstanceId(), listParams.getCatalogName(), listParams.getSchemaName());
        return new ResponseEntity<>(functions, HttpStatus.OK);
    }



    @Logging
    @PostMapping("/executeSql")
    @Operation(summary = "执行sql", description = "执行slq")
    public ResponseEntity<QueryResult> executeSql(@RequestBody WsDorisSqlExecutionParam executionParam) {
        QueryResult queryResult = dorisSqlService.executeSql(executionParam);
        return new ResponseEntity<>(queryResult, HttpStatus.OK);
    }
}
