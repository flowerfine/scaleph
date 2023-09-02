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

package cn.sliew.scaleph.engine.sql.gateway.services.dto.catalog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.apache.flink.table.catalog.CatalogBaseTable;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode
@Schema(name = "SqlGateway 表信息", description = "SqlGateway 表信息")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableInfo {

    @Schema(description = "table 名称")
    private String tableName;

    @Schema(description = "table 类型。TABLE: 表, VIEW: 试图")
    private CatalogBaseTable.TableKind tableKind;

    @Schema(description = "table 结构")
    private List<ColumnInfo> schema;

    @Schema(description = "table 描述")
    private String description;

    @Schema(description = "table 备注")
    private String comment;

    @Schema(description = "table 属性")
    private Map<String, String> properties;
}
