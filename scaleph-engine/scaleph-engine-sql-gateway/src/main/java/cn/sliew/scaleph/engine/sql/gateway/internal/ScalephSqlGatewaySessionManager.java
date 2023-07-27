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

package cn.sliew.scaleph.engine.sql.gateway.internal;

import cn.sliew.scaleph.engine.sql.gateway.dto.catalog.*;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.catalog.*;
import org.apache.flink.table.catalog.exceptions.DatabaseNotExistException;
import org.apache.flink.table.data.StringData;
import org.apache.flink.table.factories.FactoryUtil;
import org.apache.flink.table.gateway.api.operation.OperationHandle;
import org.apache.flink.table.gateway.api.results.FetchOrientation;
import org.apache.flink.table.gateway.api.results.GatewayInfo;
import org.apache.flink.table.gateway.api.results.ResultSet;
import org.apache.flink.table.gateway.api.session.SessionEnvironment;
import org.apache.flink.table.gateway.api.session.SessionHandle;
import org.apache.flink.table.gateway.api.utils.ThreadUtils;
import org.apache.flink.table.gateway.rest.util.SqlGatewayRestAPIVersion;
import org.apache.flink.table.gateway.service.context.DefaultContext;
import org.apache.flink.table.gateway.service.context.SessionContext;
import org.apache.flink.table.gateway.service.operation.OperationManager;
import org.apache.flink.table.module.ModuleManager;
import org.apache.flink.table.resource.ResourceType;
import org.apache.flink.table.resource.ResourceUri;

import java.io.IOException;
import java.net.URI;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.apache.flink.table.gateway.api.config.SqlGatewayServiceConfigOptions.*;

public class ScalephSqlGatewaySessionManager {
    private final Map<SessionHandle, ScalephSqlGatewaySession> sessions = new ConcurrentHashMap<>();
    private final DefaultContext defaultContext;
    private final ReadableConfig readableConfig;
    private ScheduledExecutorService cleanupService;
    private ExecutorService operationExecutorService;

    public ScalephSqlGatewaySessionManager(DefaultContext defaultContext) {
        this.defaultContext = defaultContext;
        this.readableConfig = defaultContext.getFlinkConfig();
    }

    public void start() {
        this.cleanupService = Executors.newSingleThreadScheduledExecutor();
        cleanupService.scheduleAtFixedRate(() -> {
                    for (Map.Entry<SessionHandle, ScalephSqlGatewaySession> entry : sessions.entrySet()) {
                        SessionHandle sessionHandle = entry.getKey();
                        ScalephSqlGatewaySession session = entry.getValue();
                        if (session.isExpired(readableConfig.get(SQL_GATEWAY_SESSION_IDLE_TIMEOUT))) {
                            closeSession(sessionHandle);
                        }
                    }
                }, 0,
                readableConfig.get(SQL_GATEWAY_SESSION_CHECK_INTERVAL).toMillis()
                , TimeUnit.MILLISECONDS);
        this.operationExecutorService =
                ThreadUtils.newThreadPool(
                        readableConfig.get(SQL_GATEWAY_WORKER_THREADS_MIN),
                        readableConfig.get(SQL_GATEWAY_WORKER_THREADS_MAX),
                        readableConfig.get(SQL_GATEWAY_WORKER_KEEPALIVE_TIME).toMillis(),
                        "scaleph-sql-gateway-pool");
    }

    public ScalephSqlGatewaySession openSession() {
        SessionHandle sessionHandle;
        do {
            sessionHandle = SessionHandle.create();
        } while (sessions.containsKey(sessionHandle));
        SessionContext sessionContext = SessionContext.create(defaultContext,
                sessionHandle,
                SessionEnvironment.newBuilder()
                        .setSessionName(UUID.randomUUID().toString())
                        .setSessionEndpointVersion(SqlGatewayRestAPIVersion.V2)
                        .build(),
                operationExecutorService
        );
        ScalephSqlGatewaySession session = ScalephSqlGatewaySession.create(sessionContext);
        sessions.put(sessionHandle, session);
        return session;
    }

    public ScalephSqlGatewaySession getSession(SessionHandle sessionHandle) {
        if (sessions.containsKey(sessionHandle)) {
            ScalephSqlGatewaySession session = sessions.get(sessionHandle);
            session.touch();
            return session;
        } else {
            throw new IllegalArgumentException("Session not exists!");
        }
    }

    public boolean sessionExists(SessionHandle sessionHandle) {
        return sessions.containsKey(sessionHandle);
    }

    public void closeSession(SessionHandle sessionHandle) {
        if (sessionExists(sessionHandle)) {
            ScalephSqlGatewaySession session = getSession(sessionHandle);
            session.close();
            sessions.remove(sessionHandle);
        }
    }

    public void stop() {
        for (Map.Entry<SessionHandle, ScalephSqlGatewaySession> entry : sessions.entrySet()) {
            closeSession(entry.getKey());
        }
        if (this.cleanupService != null) {
            this.cleanupService.shutdown();
        }
        if (this.operationExecutorService != null) {
            this.operationExecutorService.shutdown();
        }
    }

    public GatewayInfo getGatewayInfo() {
        return GatewayInfo.INSTANCE;
    }

    private TableInfo getTableInfo(CatalogManager catalogManager, ObjectIdentifier tableName) {
        TableInfo.TableInfoBuilder tableInfoBuilder = TableInfo.builder();
        tableInfoBuilder.tableName(tableName.getObjectName());
        catalogManager.getTable(tableName).ifPresent(contextResolvedTable -> {
            CatalogBaseTable table = contextResolvedTable.getTable();
            tableInfoBuilder.tableKind(table.getTableKind());
            table.getDescription().ifPresent(tableInfoBuilder::description);
            tableInfoBuilder.comment(table.getComment());
            tableInfoBuilder.properties(table.getOptions());
            Schema unresolvedSchema = table.getUnresolvedSchema();
            ResolvedSchema schema = unresolvedSchema.resolve(catalogManager.getSchemaResolver());
            List<ColumnInfo> columns = schema.getColumns().stream().map(column -> {
                ColumnInfo.ColumnInfoBuilder columnInfoBuilder = ColumnInfo.builder()
                        .columnName(column.getName())
                        .dataType(column.getDataType().toString())
                        .isPersist(column.isPersisted())
                        .isPhysical(column.isPhysical());
                column.getComment().ifPresent(columnInfoBuilder::comment);
                return columnInfoBuilder.build();
            }).collect(Collectors.toList());
            tableInfoBuilder.schema(columns);
        });
        return tableInfoBuilder.build();
    }

    public Set<CatalogInfo> getCatalogInfo(SessionHandle sessionHandle, boolean includeSystemFunctions) {
        ScalephSqlGatewaySession session = getSession(sessionHandle);
        SessionContext sessionContext = session.getSessionContext();
        SessionContext.SessionState sessionState = sessionContext.getSessionState();
        CatalogManager catalogManager = sessionState.catalogManager;
        ModuleManager moduleManager = sessionState.moduleManager;
        Set<FunctionInfo> systemFunctions = moduleManager.listFunctions()
                .stream()
                .map(functionName -> {
                    FunctionInfo.FunctionInfoBuilder functionInfoBuilder = FunctionInfo.builder();
                    functionInfoBuilder.functionName(functionName);
                    moduleManager.getFunctionDefinition(functionName)
                            .ifPresent(functionDefinition -> functionInfoBuilder.functionKind(functionDefinition.getKind()));
                    return functionInfoBuilder.build();
                }).collect(Collectors.toSet());
        return catalogManager.listCatalogs()
                .stream()
                .map(catalogName -> {
                    CatalogInfo.CatalogInfoBuilder catalogInfoBuilder = CatalogInfo.builder();
                    if (includeSystemFunctions) {
                        catalogInfoBuilder.systemFunctions(systemFunctions);
                    }
                    catalogInfoBuilder.catalogName(catalogName);
                    catalogManager.getCatalog(catalogName).ifPresent(catalog -> {
                        catalog.getFactory().ifPresent(factory -> {
                            Map<String, String> properties = new HashMap<>();
                            properties.put("factory", factory.factoryIdentifier());
                            catalogInfoBuilder.properties(properties);
                        });
                        Set<DatabaseInfo> databaseInfos = catalog.listDatabases().stream().map(databaseName -> {
                                    DatabaseInfo.DatabaseInfoBuilder databaseInfoBuilder = DatabaseInfo.builder();
                                    databaseInfoBuilder.databaseName(databaseName);
                                    try {
                                        CatalogDatabase database = catalog.getDatabase(databaseName);
                                        database.getDescription().ifPresent(databaseInfoBuilder::description);
                                        databaseInfoBuilder.comment(database.getComment());
                                        Set<TableInfo> views = catalogManager.listViews().stream()
                                                .map(viewName -> getTableInfo(catalogManager, ObjectIdentifier.of(catalogName, databaseName, viewName)))
                                                .collect(Collectors.toSet());
                                        Set<TableInfo> tables = catalogManager.listTables(catalogName, databaseName).stream()
                                                .map(tableName -> getTableInfo(catalogManager, ObjectIdentifier.of(catalogName, databaseName, tableName)))
                                                .collect(Collectors.toSet());
                                        databaseInfoBuilder.tables(tables);
                                        databaseInfoBuilder.views(views);
                                        databaseInfoBuilder.properties(database.getProperties());
                                    } catch (DatabaseNotExistException e) {
                                        throw new RuntimeException(e);
                                    }
                                    return databaseInfoBuilder.build();
                                })
                                .collect(Collectors.toSet());
                        catalogInfoBuilder.databases(databaseInfos);
                    });

                    return catalogInfoBuilder.build();
                }).collect(Collectors.toSet());
    }

    public ResultSet fetchResults(SessionHandle sessionHandle, OperationHandle operationHandle, FetchOrientation fetchOrientation, int maxRows) {
        return getSession(sessionHandle)
                .getSessionContext()
                .getOperationManager()
                .fetchResults(operationHandle, fetchOrientation, maxRows);
    }

    public ResultSet fetchResults(SessionHandle sessionHandle, OperationHandle operationHandle, Long token, int maxRows) {
        return getSession(sessionHandle)
                .getSessionContext()
                .getOperationManager()
                .fetchResults(operationHandle, token, maxRows);
    }

    public void cancelOperation(SessionHandle sessionHandle, OperationHandle operationHandle) {
        getSession(sessionHandle)
                .getSessionContext()
                .getOperationManager()
                .cancelOperation(operationHandle);
    }

    public List<String> completeStatement(SessionHandle sessionHandle, String statement, int position) throws Exception {
        SessionContext sessionContext = getSession(sessionHandle).getSessionContext();
        OperationManager operationManager = sessionContext.getOperationManager();
        Configuration sessionConf = sessionContext.getSessionConf();
        OperationHandle operationHandle =
                operationManager.submitOperation(
                        handle ->
                                getSession(sessionHandle)
                                        .getSessionContext()
                                        .createOperationExecutor(sessionConf)
                                        .getCompletionHints(handle, statement, position));
        operationManager.awaitOperationTermination(operationHandle);

        ResultSet resultSet =
                fetchResults(sessionHandle, operationHandle, 0L, Integer.MAX_VALUE);
        return resultSet.getData().stream()
                .map(data -> data.getString(0))
                .map(StringData::toString)
                .collect(Collectors.toList());
    }

    public String executeStatement(SessionHandle sessionHandle, Map<String, String> configuration, String sql) {
        SessionContext sessionContext = getSession(sessionHandle).getSessionContext();
        Configuration sessionConf = new Configuration(sessionContext.getSessionConf());
        configuration.forEach(sessionConf::setString);
        return sessionContext.getOperationManager()
                .submitOperation(handle ->
                        sessionContext.createOperationExecutor(sessionConf)
                                .executeStatement(handle, sql)
                )
                .getIdentifier()
                .toString();
    }

    public void addDependencies(SessionHandle sessionHandle, List<URI> jars) {
        List<ResourceUri> uris = jars.stream().map(uri -> new ResourceUri(ResourceType.JAR, uri.toString()))
                .collect(Collectors.toList());
        try {
            getSession(sessionHandle)
                    .getSessionContext()
                    .getSessionState()
                    .resourceManager
                    .registerJarResources(uris);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addCatalog(SessionHandle sessionHandle, String catalogName, Map<String, String> options) {
        SessionContext sessionContext = getSession(sessionHandle)
                .getSessionContext();
        Configuration sessionConf = sessionContext.getSessionConf();
        SessionContext.SessionState sessionState = sessionContext
                .getSessionState();
        URLClassLoader userClassLoader = sessionState.resourceManager.getUserClassLoader();
        Catalog catalog = FactoryUtil.createCatalog(catalogName, options, sessionConf, userClassLoader);
        sessionState
                .catalogManager
                .registerCatalog(catalogName, catalog);
    }
}
