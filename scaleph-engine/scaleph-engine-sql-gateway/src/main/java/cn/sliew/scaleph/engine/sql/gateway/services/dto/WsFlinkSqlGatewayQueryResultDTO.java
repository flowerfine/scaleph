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

package cn.sliew.scaleph.engine.sql.gateway.services.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Hex;
import org.apache.flink.table.api.ResultKind;
import org.apache.flink.table.catalog.Column;
import org.apache.flink.table.data.*;
import org.apache.flink.table.gateway.api.results.ResultSet;
import org.apache.flink.table.types.logical.*;

import cn.sliew.scaleph.engine.sql.gateway.services.dto.catalog.ColumnInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@EqualsAndHashCode
@Schema(name = "Flink Sql gateway 查询结果")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WsFlinkSqlGatewayQueryResultDTO {

    @Schema(
            description = "SQL 执行状态。NOT_READY: 未就绪，需轮询重试, PAYLOAD: 可查询, EOS: 数据查询已至末尾，后续无数据，不在调用",
            allowableValues = {"PAYLOAD", "NOT_READY", "EOS"})
    private ResultSet.ResultType resultType;

    @Schema(
            description = "结果类型。SUCCESS: 执行成功, SUCCESS_WITH_CONTENT: 执行成功并可获取数据列表",
            allowableValues = {"SUCCESS", "SUCCESS_WITH_CONTENT"})
    private ResultKind resultKind;

    @Schema(description = "任务 id")
    private String jobID;

    @Schema(description = "分页参数。查询下一页数据参数")
    private Long nextToken;

    @Schema(description = "是否支持查询数据")
    private Boolean isQueryResult;

    @Schema(description = "数据类型信息")
    private List<ColumnInfo> columns;

    @Schema(description = "数据")
    private List<Map<String, Object>> data;

    public static WsFlinkSqlGatewayQueryResultDTO fromResultSet(ResultSet resultSet) {
        WsFlinkSqlGatewayQueryResultDTOBuilder builder =
                WsFlinkSqlGatewayQueryResultDTO.builder().resultType(resultSet.getResultType());
        if (resultSet.getResultType() != ResultSet.ResultType.NOT_READY) {
            if (resultSet.getJobID() != null) {
                builder.jobID(resultSet.getJobID().toHexString());
            }
            builder.resultKind(resultSet.getResultKind());
            if (resultSet.isQueryResult()) {
                List<Column> columns = resultSet.getResultSchema().getColumns();
                builder.columns(columns.stream()
                                .map(column -> {
                                    ColumnInfo.ColumnInfoBuilder columnInfoBuilder = ColumnInfo.builder()
                                            .columnName(column.getName())
                                            .dataType(column.getDataType()
                                                    .getLogicalType()
                                                    .toString())
                                            .isPersist(column.isPersisted())
                                            .isPhysical(column.isPhysical());
                                    column.getComment().ifPresent(columnInfoBuilder::comment);
                                    return columnInfoBuilder.build();
                                })
                                .collect(Collectors.toList()))
                        .data(resultSet.getData().stream()
                                .map(rowData -> {
                                    Map<String, Object> map = new HashMap<>();
                                    for (int i = 0; i < columns.size(); i++) {
                                        Column column = columns.get(i);
                                        try {
                                            map.put(
                                                    column.getName(),
                                                    getDataFromRow(
                                                            rowData,
                                                            column.getDataType().getLogicalType(),
                                                            i));
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    return map;
                                })
                                .collect(Collectors.toList()));
                builder.nextToken(resultSet.getNextToken());
            }
        }
        return builder.build();
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
        Class<?> dataClass = rowData.getClass();
        if (!RowData.class.isAssignableFrom(dataClass) && !ArrayData.class.isAssignableFrom(dataClass)) {
            throw new IllegalArgumentException(dataClass + " is not supported row data type!");
        }
        switch (logicalType.getTypeRoot()) {
            case VARCHAR:
            case CHAR:
                return dataClass
                        .getDeclaredMethod("getString", int.class)
                        .invoke(rowData, index)
                        .toString();
            case TINYINT:
            case SMALLINT:
                return dataClass.getDeclaredMethod("getShort", int.class).invoke(rowData, index);
            case INTEGER:
            case DATE:
            case TIME_WITHOUT_TIME_ZONE:
            case INTERVAL_YEAR_MONTH:
                return dataClass.getDeclaredMethod("getInt", int.class).invoke(rowData, index);
            case FLOAT:
                return dataClass.getDeclaredMethod("getFloat", int.class).invoke(rowData, index);
            case DOUBLE:
                return dataClass.getDeclaredMethod("getDouble", int.class).invoke(rowData, index);
            case DECIMAL:
                DecimalType decimalType = (DecimalType) logicalType;
                DecimalData decimalData = (DecimalData) dataClass
                        .getDeclaredMethod("getDecimal", int.class, int.class, int.class)
                        .invoke(rowData, index, decimalType.getPrecision(), decimalType.getScale());
                return decimalData.toBigDecimal().doubleValue();
            case BIGINT:
            case INTERVAL_DAY_TIME:
                return dataClass.getDeclaredMethod("getLong", int.class).invoke(rowData, index);
            case BOOLEAN:
                return dataClass.getDeclaredMethod("getBoolean", int.class).invoke(rowData, index);
            case NULL:
                return null;
            case BINARY:
            case VARBINARY:
                byte[] binary = (byte[])
                        dataClass.getDeclaredMethod("getBinary", int.class).invoke(rowData, index);
                return Hex.encodeHexString(binary);
            case ROW:
            case STRUCTURED_TYPE:
                RowType rowType = (RowType) logicalType;
                RowData row = (RowData) dataClass
                        .getDeclaredMethod("getRow", int.class, int.class)
                        .invoke(rowData, index, rowType.getFieldCount());
                Map<String, Object> mapInRow = new HashMap<>();
                for (RowType.RowField rowField : rowType.getFields()) {
                    String fieldName = rowField.getName();
                    LogicalType fieldType = rowField.getType();
                    mapInRow.put(fieldName, getDataFromRow(row, fieldType, rowType.getFieldIndex(fieldName)));
                }
                return mapInRow;
            case MAP:
            case MULTISET:
                MapType mapType = (MapType) logicalType;
                LogicalType keyValueType = mapType.getKeyType();
                LogicalType valueValueType = mapType.getValueType();
                MapData mapData = (MapData)
                        dataClass.getDeclaredMethod("getMap", int.class).invoke(rowData, index);
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
                ArrayData arrayData = (ArrayData)
                        dataClass.getDeclaredMethod("getArray", int.class).invoke(rowData, index);
                LogicalType elementType = arrayType.getElementType();
                List<Object> list = new ArrayList<>();
                for (int i = 0; i < arrayData.size(); i++) {
                    list.add(getDataFromRow(arrayData, elementType, i));
                }
                return list;
            case TIMESTAMP_WITH_LOCAL_TIME_ZONE:
            case TIMESTAMP_WITHOUT_TIME_ZONE:
                TimestampData timestampData = (TimestampData) dataClass
                        .getDeclaredMethod("getTimestamp", int.class, int.class)
                        .invoke(rowData, index, ((TimestampType) logicalType).getPrecision());
                return timestampData.toTimestamp();
            case DISTINCT_TYPE:
                DistinctType distinctType = (DistinctType) logicalType;
                LogicalType sourceType = distinctType.getSourceType();
                return getDataFromRow(rowData, sourceType, index);
            case RAW:
            case TIMESTAMP_WITH_TIME_ZONE:
            case SYMBOL:
            case UNRESOLVED:
            default:
                throw new IllegalArgumentException("DataType: " + logicalType + " not supported now!");
        }
    }
}
