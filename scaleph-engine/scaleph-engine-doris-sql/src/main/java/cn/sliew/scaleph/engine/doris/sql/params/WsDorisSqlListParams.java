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

package cn.sliew.scaleph.engine.doris.sql.params;

import cn.sliew.scaleph.engine.doris.sql.dto.enums.TableType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Doris 数据库信息获取参数", description = "Doris 数据库信息获取参数")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WsDorisSqlListParams {
    @Schema(description = "Doris Operator Instance Id")
    private Long instanceId;
    @Schema(description = "Catalog名称")
    private String catalogName;
    @Schema(description = "Schema名称")
    private String schemaName;
    @Schema(description = "表名")
    private String tableName;
    @Schema(description = "表类型，用于查询Doris Table列表时使用")
    private TableType[] tableTypes;
}
