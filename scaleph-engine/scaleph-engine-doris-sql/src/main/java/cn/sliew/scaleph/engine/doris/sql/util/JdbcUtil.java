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

package cn.sliew.scaleph.engine.doris.sql.util;

import cn.sliew.scaleph.engine.doris.sql.dialect.SqlDialect;
import cn.sliew.scaleph.engine.doris.sql.dto.BaseTableInfo;
import cn.sliew.scaleph.engine.doris.sql.dto.IndexColumn;
import cn.sliew.scaleph.engine.doris.sql.dto.IndexInfo;
import cn.sliew.scaleph.engine.doris.sql.dto.IndexType;
import cn.sliew.scaleph.engine.doris.sql.dto.Order;
import cn.sliew.scaleph.engine.doris.sql.dto.PrimaryKey;
import cn.sliew.scaleph.engine.doris.sql.dto.TableColumn;
import cn.sliew.scaleph.engine.doris.sql.dto.TableInfo;
import cn.sliew.scaleph.engine.doris.sql.dto.TableSchema;
import cn.sliew.scaleph.engine.doris.sql.dto.TableType;

import javax.annotation.Nonnull;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class JdbcUtil {

    /**
     * Private constructor for a Util.
     */
    private JdbcUtil() {
    }

    public static List<String> listDatabases(DatabaseMetaData databaseMetaData) throws SQLException {
        List<String> catalogs = new ArrayList<>();
        try (ResultSet rs = databaseMetaData.getCatalogs()) {
            while (rs.next()) {
                String catalogName = rs.getString("TABLE_CAT");
                catalogs.add(catalogName);
            }
        }
        return catalogs;
    }

    public static List<String> listSchemas(DatabaseMetaData databaseMetaData, String catalogName) throws SQLException {
        List<String> schemas = new ArrayList<>();
        try (ResultSet rs = databaseMetaData.getSchemas(catalogName, null)) {
            while (rs.next()) {
                String schemaName = rs.getString("TABLE_SCHEM");
                schemas.add(schemaName);
            }
        }
        return schemas;
    }

    public static List<BaseTableInfo> listTables(DatabaseMetaData databaseMetaData, String catalogName, String schemaName, TableType[] tableTypes) throws SQLException {
        List<BaseTableInfo> baseTableInfoList = new ArrayList<>();
        String[] tableTypeStrings;
        if (tableTypes == null) {
            tableTypeStrings = null;
        } else {
            tableTypeStrings = Arrays.stream(tableTypes)
                    .map(TableType::getTypeName)
                    .toArray(String[]::new);
        }
        try (ResultSet rs = databaseMetaData.getTables(catalogName, schemaName, null, tableTypeStrings)) {
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                String tableTypeString = rs.getString("TABLE_TYPE");
                String remarks = rs.getString("REMARKS");
                BaseTableInfo baseTableInfo = BaseTableInfo.builder()
                        .tableName(tableName)
                        .tableType(TableType.fromTypeName(tableTypeString))
                        .comment(remarks)
                        .build();
                baseTableInfoList.add(baseTableInfo);
            }
        }
        return baseTableInfoList;
    }

    public List<TableColumn> getTableColumns(DatabaseMetaData databaseMetaData, String catalogName, String schemaName, @Nonnull String tableName, SqlDialect sqlDialect) throws SQLException {
        List<TableColumn> tableColumns = new ArrayList<>();
        try (ResultSet rs = databaseMetaData.getColumns(catalogName, schemaName, tableName, null)) {
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                int nullable = rs.getInt("NULLABLE");
                int dataTypeInt = rs.getInt("DATA_TYPE");
                String typeName = rs.getString("TYPE_NAME");
                String comment = rs.getString("REMARKS");
                int columnSize = rs.getInt("COLUMN_SIZE");
                int decimalDigits = rs.getInt("DECIMAL_DIGITS");
                String dataTypeSummaryString = sqlDialect.getDataTypeSummaryString(typeName, dataTypeInt, columnSize, decimalDigits);
                TableColumn tableColumn = TableColumn.builder()
                        .columnName(columnName)
                        .dataType(dataTypeSummaryString)
                        .nullable(nullable != 0)
                        .comment(comment)
                        .build();
                tableColumns.add(tableColumn);
            }
        }
        return tableColumns;
    }

    public List<IndexInfo> getTableIndices(DatabaseMetaData databaseMetaData, String catalogName, String schemaName, @Nonnull String tableName, SqlDialect sqlDialect) throws SQLException {
        Map<String, TreeMap<Short, IndexColumn>> map = new HashMap<>();
        Map<String, IndexType> indexTypeMap = new HashMap<>();
        try (ResultSet rs = databaseMetaData.getIndexInfo(catalogName, schemaName, tableName, false, false)) {
            while (rs.next()) {
                String indexName = rs.getString("INDEX_NAME");
                String columnName = rs.getString("COLUMN_NAME");
                IndexType indexType = IndexType.fromIndexType(rs.getShort("TYPE"));
                short ordinalPosition = rs.getShort("ORDINAL_POSITION");
                String ascOrDesc = rs.getString("ASC_OR_DESC");
                Order order = ascOrDesc.equalsIgnoreCase("A") ? Order.ASC : Order.DESC;
                IndexColumn indexColumn = IndexColumn.builder()
                        .columnName(columnName)
                        .order(order)
                        .ordinalPosition(ordinalPosition)
                        .build();
                indexTypeMap.putIfAbsent(indexName, indexType);
                map.compute(indexName, (s, treeMap) -> {
                    if (treeMap == null) {
                        treeMap = new TreeMap<>();
                    }
                    treeMap.put(ordinalPosition, indexColumn);
                    return treeMap;
                });
            }
        }
        return map.entrySet().stream().map(entry -> {
            String indexName = entry.getKey();
            TreeMap<Short, IndexColumn> treeMap = entry.getValue();
            List<IndexColumn> indexColumns = new ArrayList<>(treeMap.values());
            return IndexInfo.builder()
                    .indexName(indexName)
                    .indexType(indexTypeMap.get(indexName))
                    .indexColumns(indexColumns)
                    .build();
        }).collect(Collectors.toList());
    }

    public List<PrimaryKey> getTablePrimaryKeys(DatabaseMetaData databaseMetaData, String catalogName, String schemaName, @Nonnull String tableName, SqlDialect sqlDialect) throws SQLException {
        try (ResultSet rs = databaseMetaData.getPrimaryKeys(catalogName, schemaName, tableName)) {
            while (rs.next()) {
                String pkName = rs.getString("PK_NAME");
                String columnName = rs.getString("COLUMN_NAME");
                short keySeq = rs.getShort("KEY_SEQ");
            }
        }
        return null;
    }

    public TableInfo getTableInfo(DatabaseMetaData databaseMetaData, String catalogName, String schemaName, @Nonnull String tableName, SqlDialect sqlDialect) throws SQLException {
        BaseTableInfo baseTableInfo = null;
        try (ResultSet rs = databaseMetaData.getTables(catalogName, schemaName, tableName, null)) {
            if (rs.next()) {
                String tableTypeString = rs.getString("TABLE_TYPE");
                String remarks = rs.getString("REMARKS");
                baseTableInfo = BaseTableInfo.builder()
                        .tableName(tableName)
                        .tableType(TableType.fromTypeName(tableTypeString))
                        .comment(remarks)
                        .build();
            } else {
                return null;
            }
        }
        if (baseTableInfo == null) {
            return null;
        }
        List<TableColumn> columns = getTableColumns(databaseMetaData, catalogName, schemaName, tableName, sqlDialect);
        List<IndexInfo> indices = getTableIndices(databaseMetaData, catalogName, schemaName, tableName, sqlDialect);
        List<PrimaryKey> primaryKeys = getTablePrimaryKeys(databaseMetaData, catalogName, schemaName, tableName, sqlDialect);
        TableInfo tableInfo = new TableInfo(baseTableInfo);
        TableSchema tableSchema = TableSchema.builder()
                .columns(columns)
                .primaryKeys(primaryKeys)
                .indices(indices)
                .build();
        tableInfo.setTableSchema(tableSchema);
        return tableInfo;
    }

}
