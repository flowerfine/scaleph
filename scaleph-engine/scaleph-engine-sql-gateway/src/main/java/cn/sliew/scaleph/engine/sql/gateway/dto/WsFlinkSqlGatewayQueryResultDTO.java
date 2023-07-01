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

package cn.sliew.scaleph.engine.sql.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.apache.flink.table.catalog.Column;
import org.apache.flink.table.data.ArrayData;
import org.apache.flink.table.data.MapData;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.gateway.api.results.ResultSet;
import org.apache.flink.table.types.logical.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode
@Schema(name = "Flink Sql gateway 查询结果")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WsFlinkSqlGatewayQueryResultDTO {
    private String resultType;
    private String resultKind;
    private String jobID;
    private Long nextToken;
    private Boolean isQueryResult;
    private List<Map<String, Object>> data;

    public static WsFlinkSqlGatewayQueryResultDTO fromResultSet(ResultSet resultSet) {
        List<Column> columns = resultSet.getResultSchema().getColumns();
        return WsFlinkSqlGatewayQueryResultDTO.builder()
                .resultType(resultSet.getResultType().name())
                .resultKind(resultSet.getResultKind().name())
                .jobID(resultSet.getJobID().toHexString())
                .nextToken(resultSet.getNextToken())
                .isQueryResult(resultSet.isQueryResult())
                .data(resultSet.getData().stream().map(rowData -> {
                    Map<String, Object> map = new HashMap<>();
                    for (int i = 0; i < columns.size(); i++) {
                        Column column = columns.get(i);
                        try {
                            map.put(column.getName(), getDataFromRow(rowData, column.getDataType().getLogicalType(), i));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return map;
                }).collect(Collectors.toList()))
                .build();
    }

    /**
     * Get origin value from `RowData`
     *
     * @param rowData     {@link RowData},{@link ArrayData}
     * @param logicalType LogicalType of the column by index
     * @param index       Index of the column
     * @return Origin data
     * @throws Exception Reflect exceptions
     */
    private static Object getDataFromRow(Object rowData, LogicalType logicalType, int index) throws Exception {
        if (!(rowData instanceof RowData) || ! (rowData instanceof ArrayData)) {
            throw new IllegalArgumentException(rowData.getClass() + " is not supported row data type!");
        }
        Class<?> dataClass = rowData.getClass();
        switch (logicalType.getTypeRoot()) {
            case VARCHAR:
            case CHAR:
                return dataClass.getDeclaredMethod("getString", Integer.class).invoke(rowData, index);
            case TINYINT:
            case SMALLINT:
                return dataClass.getDeclaredMethod("getShort", Integer.class).invoke(rowData, index);
            case INTEGER:
                return dataClass.getDeclaredMethod("getInt", Integer.class).invoke(rowData, index);
            case FLOAT:
                return dataClass.getDeclaredMethod("getFloat", Integer.class).invoke(rowData, index);
            case DOUBLE:
                return dataClass.getDeclaredMethod("getDouble", Integer.class).invoke(rowData, index);
            case DECIMAL:
                DecimalType decimalType = (DecimalType) logicalType;
                return dataClass.getDeclaredMethod("getDecimal", Integer.class, Integer.class, Integer.class)
                        .invoke(rowData, index, decimalType.getPrecision(), decimalType.getScale());
            case BIGINT:
                return dataClass.getDeclaredMethod("getLong", Integer.class).invoke(rowData, index);
            case BOOLEAN:
                return dataClass.getDeclaredMethod("getBoolean", Integer.class).invoke(rowData, index);
            case NULL:
                return null;
            case BINARY:
                return dataClass.getDeclaredMethod("getBinary", Integer.class).invoke(rowData, index);
            case ROW:
                RowType rowType = (RowType) logicalType;
                RowData row = (RowData) dataClass.getDeclaredMethod("getRow", Integer.class, Integer.class).invoke(rowData, index, rowType.getFieldCount());
                Map<String, Object> mapInRow = new HashMap<>();
                for (RowType.RowField rowField : rowType.getFields()) {
                    String fieldName = rowField.getName();
                    LogicalType fieldType = rowField.getType();
                    mapInRow.put(fieldName, getDataFromRow(row, fieldType, rowType.getFieldIndex(fieldName)));
                }
                return mapInRow;
            case MAP:
                MapType mapType = (MapType) logicalType;
                LogicalType keyValueType = mapType.getKeyType();
                LogicalType valueValueType = mapType.getValueType();
                MapData mapData = (MapData) dataClass.getDeclaredMethod("getMap", Integer.class).invoke(rowData, index);
                ArrayData keyArray = mapData.keyArray();
                ArrayData valueArray = mapData.valueArray();
                Map<Object, Object> mapInMap = new HashMap<>();
                for (int i = 0; i < mapData.size(); i++) {
                    Object key = getDataFromRow(keyArray, keyValueType, i);
                    Object value = getDataFromRow(valueArray, valueValueType, i);
                    mapInMap.put(key, value);
                }
                return mapInMap;
            case ARRAY:
                ArrayType arrayType = (ArrayType) logicalType;
                ArrayData arrayData = (ArrayData) dataClass.getDeclaredMethod("getArray", Integer.class).invoke(rowData, index);
                LogicalType elementType = arrayType.getElementType();
                List<Object> list = new ArrayList<>();
                for (int i = 0; i < arrayData.size(); i++) {
                    list.add(getDataFromRow(arrayData, elementType, i));
                }
                return list;
            case DATE:
            case TIME_WITHOUT_TIME_ZONE:
            case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
            case TIMESTAMP_WITH_TIME_ZONE:
            case TIMESTAMP_WITHOUT_TIME_ZONE:
                return dataClass.getDeclaredMethod("getTimestamp", Integer.class, Integer.class).invoke(rowData, index, ((TimeType) logicalType).getPrecision());
            default:
                throw new IllegalArgumentException("DataType: " + logicalType + " not supported now!");
        }
    }
}
