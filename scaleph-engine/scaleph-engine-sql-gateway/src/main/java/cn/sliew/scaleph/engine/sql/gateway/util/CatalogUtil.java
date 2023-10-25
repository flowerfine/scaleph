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

package cn.sliew.scaleph.engine.sql.gateway.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.catalog.Catalog;
import org.apache.flink.table.catalog.CatalogBaseTable;
import org.apache.flink.table.catalog.CatalogDatabase;
import org.apache.flink.table.catalog.CatalogManager;
import org.apache.flink.table.catalog.FunctionCatalog;
import org.apache.flink.table.catalog.ObjectIdentifier;
import org.apache.flink.table.catalog.ResolvedSchema;
import org.apache.flink.table.catalog.UnresolvedIdentifier;
import org.apache.flink.table.catalog.exceptions.DatabaseNotExistException;
import org.apache.flink.table.factories.FactoryUtil;
import org.apache.flink.table.functions.FunctionDefinition;
import org.apache.flink.table.gateway.service.context.SessionContext;

import com.fasterxml.jackson.core.type.TypeReference;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkSqlGatewayCatalog;
import cn.sliew.scaleph.engine.sql.gateway.services.dto.catalog.CatalogInfo;
import cn.sliew.scaleph.engine.sql.gateway.services.dto.catalog.ColumnInfo;
import cn.sliew.scaleph.engine.sql.gateway.services.dto.catalog.DatabaseInfo;
import cn.sliew.scaleph.engine.sql.gateway.services.dto.catalog.FunctionInfo;
import cn.sliew.scaleph.engine.sql.gateway.services.dto.catalog.TableInfo;

public class CatalogUtil {

    public static Catalog createCatalog(
            WsFlinkSqlGatewayCatalog wsFlinkSqlGatewayCatalog,
            Map<String, String> sessionConfig,
            ClassLoader classLoader) {
        String catalogName = wsFlinkSqlGatewayCatalog.getCatalogName();
        Map<String, String> options = JacksonUtil.parseJsonString(
                wsFlinkSqlGatewayCatalog.getCatalogOptions(), new TypeReference<HashMap<String, String>>() {});
        return FactoryUtil.createCatalog(
                catalogName,
                options,
                Configuration.fromMap(sessionConfig),
                // TODO Classloader should load session added jars.
                classLoader == null ? Thread.currentThread().getContextClassLoader() : classLoader);
    }

    public static CatalogInfo createCatalogInfo(SessionContext sessionContext, String catalogName) {
        Catalog catalog = sessionContext.getSessionState().catalogManager.getCatalogOrThrowException(catalogName);
        CatalogInfo.CatalogInfoBuilder catalogInfoBuilder = CatalogInfo.builder();
        catalogInfoBuilder.catalogName(catalogName);
        catalog.getFactory().ifPresent(factory -> {
            Map<String, String> properties = new HashMap<>();
            properties.put("factory", factory.factoryIdentifier());
            catalogInfoBuilder.properties(properties);
        });
        Set<DatabaseInfo> databaseInfos = catalog.listDatabases().stream()
                .map(databaseName -> createDatabaseInfo(sessionContext, catalogName, databaseName))
                .collect(Collectors.toSet());
        catalogInfoBuilder.databases(databaseInfos);
        return catalogInfoBuilder.build();
    }

    public static DatabaseInfo createDatabaseInfo(
            SessionContext sessionContext, String catalogName, String databaseName) {
        SessionContext.SessionState sessionState = sessionContext.getSessionState();
        CatalogManager catalogManager = sessionState.catalogManager;
        FunctionCatalog functionCatalog = sessionState.functionCatalog;
        Catalog catalog = catalogManager.getCatalogOrThrowException(catalogName);
        DatabaseInfo.DatabaseInfoBuilder databaseInfoBuilder = DatabaseInfo.builder();
        databaseInfoBuilder.databaseName(databaseName);
        try {
            CatalogDatabase database = catalog.getDatabase(databaseName);
            database.getDescription().ifPresent(databaseInfoBuilder::description);
            databaseInfoBuilder.comment(database.getComment());
            Set<TableInfo> views = catalogManager.listViews().stream()
                    .map(viewName -> createTableInfo(sessionContext, catalogName, databaseName, viewName))
                    .collect(Collectors.toSet());
            Set<TableInfo> tables = catalogManager.listTables(catalogName, databaseName).stream()
                    .map(tableName -> createTableInfo(sessionContext, catalogName, databaseName, tableName))
                    .collect(Collectors.toSet());
            Set<FunctionInfo> userDefinedFunctions =
                    functionCatalog.getUserDefinedFunctions(catalogName, databaseName).stream()
                            .flatMap(functionIdentifier -> {
                                ObjectIdentifier objectIdentifier = functionIdentifier
                                        .getIdentifier()
                                        .orElse(ObjectIdentifier.of(
                                                catalogName, databaseName, functionIdentifier.getFunctionName()));
                                UnresolvedIdentifier unresolvedIdentifier = UnresolvedIdentifier.of(objectIdentifier);
                                return functionCatalog
                                        .lookupFunction(unresolvedIdentifier)
                                        .map(contextResolvedFunction -> {
                                            String functionName = objectIdentifier.getObjectName();
                                            FunctionDefinition functionDefinition =
                                                    contextResolvedFunction.getDefinition();
                                            return createFunctionInfo(functionName, functionDefinition);
                                        })
                                        .stream();
                            })
                            .collect(Collectors.toSet());
            databaseInfoBuilder.tables(tables);
            databaseInfoBuilder.views(views);
            databaseInfoBuilder.userDefinedFunctions(userDefinedFunctions);
            databaseInfoBuilder.properties(database.getProperties());
        } catch (DatabaseNotExistException e) {
            throw new RuntimeException(e);
        }
        return databaseInfoBuilder.build();
    }

    public static TableInfo createTableInfo(
            SessionContext sessionContext, String catalogName, String databaseName, String tableName) {
        SessionContext.SessionState sessionState = sessionContext.getSessionState();
        CatalogManager catalogManager = sessionState.catalogManager;
        TableInfo.TableInfoBuilder tableInfoBuilder = TableInfo.builder();
        tableInfoBuilder.tableName(tableName);
        ObjectIdentifier tableId = ObjectIdentifier.of(catalogName, databaseName, tableName);
        catalogManager.getTable(tableId).ifPresent(contextResolvedTable -> {
            CatalogBaseTable table = contextResolvedTable.getTable();
            tableInfoBuilder.tableKind(table.getTableKind());
            table.getDescription().ifPresent(tableInfoBuilder::description);
            tableInfoBuilder.comment(table.getComment());
            tableInfoBuilder.properties(table.getOptions());
            Schema unresolvedSchema = table.getUnresolvedSchema();
            ResolvedSchema schema = unresolvedSchema.resolve(catalogManager.getSchemaResolver());
            List<ColumnInfo> columns = schema.getColumns().stream()
                    .map(column -> {
                        ColumnInfo.ColumnInfoBuilder columnInfoBuilder = ColumnInfo.builder()
                                .columnName(column.getName())
                                .dataType(column.getDataType().getLogicalType().toString())
                                .isPersist(column.isPersisted())
                                .isPhysical(column.isPhysical());
                        column.getComment().ifPresent(columnInfoBuilder::comment);
                        return columnInfoBuilder.build();
                    })
                    .collect(Collectors.toList());
            tableInfoBuilder.schema(columns);
        });
        return tableInfoBuilder.build();
    }

    public static FunctionInfo createFunctionInfo(String functionName, FunctionDefinition functionDefinition) {
        FunctionInfo.FunctionInfoBuilder functionBuilder = FunctionInfo.builder();
        functionBuilder.functionName(functionName);
        functionBuilder.functionKind(functionDefinition.getKind());
        return functionBuilder.build();
    }
}
