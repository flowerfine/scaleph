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

import cn.sliew.scaleph.engine.sql.gateway.services.FlinkSqlGatewayService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.flink.table.gateway.api.results.GatewayInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Flink Sql Gateway")
@RestController
@RequestMapping(path = "/api/flink/sql-gateway")
public class WsFlinkSqlGatewayController {

    private final FlinkSqlGatewayService flinkSqlGatewayService;

    @Autowired
    public WsFlinkSqlGatewayController(FlinkSqlGatewayService flinkSqlGatewayService) {
        this.flinkSqlGatewayService = flinkSqlGatewayService;
    }

    @GetMapping("info/{clusterId}")
    public ResponseEntity<GatewayInfo> getClusterInfo(@PathVariable("clusterId") String clusterId) {
        return ResponseEntity.ok(flinkSqlGatewayService.getGatewayInfo(clusterId));
    }

}
