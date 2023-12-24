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
 *
 */

package cn.sliew.scaleph.engine.doris.sql.dto;

import cn.sliew.scaleph.engine.doris.sql.dialect.SqlDialect;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@SuperBuilder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "查询结果", description = "查询结果")
public class QueryResult {
    private boolean hasResult;
    @Singular
    private List<TableColumn> tableColumns = Collections.emptyList();
    @Singular("sheep")
    private List<Map<String, Object>> dataList = Collections.emptyList();

    public static QueryResult fromResultSet(ResultSet resultSet, SqlDialect sqlDialect) throws SQLException {
        if (resultSet == null) {
            return QueryResult.builder()
                    .hasResult(false)
                    .build();
        }
        List<TableColumn> tableColumns = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            String columnTypeName = metaData.getColumnTypeName(i);
            int dataType = metaData.getColumnType(i);
            int precision = metaData.getPrecision(i);
            int scale = metaData.getScale(i);
            String dataTypeSummaryString = sqlDialect.getDataTypeSummaryString(columnTypeName, dataType, precision, scale);
            TableColumn tableColumn = TableColumn.builder()
                    .columnName(columnName)
                    .dataType(dataTypeSummaryString)
                    .build();
            tableColumns.add(tableColumn);
        }
        List<Map<String, Object>> dataList = new ArrayList<>();
        while (resultSet.next()) {
            Map<String, Object> data = new HashMap<>();
            for (TableColumn tableColumn : tableColumns) {
                String columnName = tableColumn.getColumnName();
                Object columnData = resultSet.getObject(columnName);
                data.put(columnName, columnData);
            }
            dataList.add(data);
        }
        return QueryResult.builder()
                .tableColumns(tableColumns)
                .dataList(dataList)
                .build();
    }
}
