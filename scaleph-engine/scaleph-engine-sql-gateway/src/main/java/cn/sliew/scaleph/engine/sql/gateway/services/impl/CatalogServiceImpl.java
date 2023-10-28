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

package cn.sliew.scaleph.engine.sql.gateway.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.flink.table.catalog.Catalog;
import org.apache.flink.table.catalog.CatalogBaseTable;
import org.apache.flink.table.catalog.CatalogManager;
import org.apache.flink.table.catalog.ContextResolvedFunction;
import org.apache.flink.table.catalog.ContextResolvedTable;
import org.apache.flink.table.catalog.FunctionCatalog;
import org.apache.flink.table.catalog.ObjectIdentifier;
import org.apache.flink.table.catalog.ObjectPath;
import org.apache.flink.table.catalog.ResolvedCatalogBaseTable;
import org.apache.flink.table.catalog.UnresolvedIdentifier;
import org.apache.flink.table.catalog.exceptions.TableNotExistException;
import org.apache.flink.table.functions.FunctionDefinition;
import org.apache.flink.table.functions.FunctionIdentifier;
import org.apache.flink.table.gateway.api.results.FunctionInfo;
import org.apache.flink.table.gateway.api.results.TableInfo;
import org.apache.flink.table.gateway.api.session.SessionHandle;
import org.apache.flink.table.gateway.api.utils.SqlGatewayException;
import org.apache.flink.table.gateway.service.context.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.sliew.scaleph.engine.sql.gateway.services.CatalogService;
import cn.sliew.scaleph.engine.sql.gateway.services.SessionService;
import cn.sliew.scaleph.engine.sql.gateway.services.dto.FlinkSqlGatewaySession;
import cn.sliew.scaleph.engine.sql.gateway.services.dto.catalog.CatalogInfo;
import cn.sliew.scaleph.engine.sql.gateway.util.CatalogUtil;

@Service
public class CatalogServiceImpl implements CatalogService {

    private final SessionService sessionService;

    @Autowired
    public CatalogServiceImpl(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public Set<CatalogInfo> getCatalogs(SessionHandle sessionHandle) throws SqlGatewayException {
        FlinkSqlGatewaySession flinkSqlGatewaySession = sessionService.getSession(sessionHandle);
        SessionContext sessionContext = flinkSqlGatewaySession.getSessionContext();
        SessionContext.SessionState sessionState = sessionContext.getSessionState();
        CatalogManager catalogManager = sessionState.catalogManager;
        return catalogManager.listCatalogs().stream()
                .map(catalogName -> CatalogUtil.createCatalogInfo(sessionContext, catalogName))
                .collect(Collectors.toSet());
    }

    @Override
    public String getCurrentCatalog(SessionHandle sessionHandle) throws SqlGatewayException {
        FlinkSqlGatewaySession flinkSqlGatewaySession = sessionService.getSession(sessionHandle);
        return flinkSqlGatewaySession
                .getSessionContext()
                .getSessionState()
                .catalogManager
                .getCurrentCatalog();
    }

    @Override
    public Set<String> listCatalogs(SessionHandle sessionHandle) throws SqlGatewayException {
        FlinkSqlGatewaySession flinkSqlGatewaySession = sessionService.getSession(sessionHandle);
        return flinkSqlGatewaySession
                .getSessionContext()
                .getSessionState()
                .catalogManager
                .listCatalogs();
    }

    @Override
    public Set<String> listDatabases(SessionHandle sessionHandle, String catalogName) throws SqlGatewayException {
        FlinkSqlGatewaySession flinkSqlGatewaySession = sessionService.getSession(sessionHandle);
        List<String> databases = flinkSqlGatewaySession
                .getSessionContext()
                .getSessionState()
                .catalogManager
                .getCatalogOrThrowException(catalogName)
                .listDatabases();
        return new HashSet<>(databases);
    }

    @Override
    public Set<TableInfo> listTables(
            SessionHandle sessionHandle,
            String catalogName,
            String databaseName,
            Set<CatalogBaseTable.TableKind> tableKinds)
            throws SqlGatewayException {
        FlinkSqlGatewaySession flinkSqlGatewaySession = sessionService.getSession(sessionHandle);
        CatalogManager catalogManager =
                flinkSqlGatewaySession.getSessionContext().getSessionState().catalogManager;
        HashSet<TableInfo> tableInfoSet = new HashSet<>();
        if (tableKinds.contains(CatalogBaseTable.TableKind.TABLE)) {
            Set<TableInfo> tables = catalogManager.listTables(catalogName, databaseName).stream()
                    .map(tableName -> {
                        ContextResolvedTable table = catalogManager.getTableOrError(
                                ObjectIdentifier.of(catalogName, databaseName, tableName));
                        return new TableInfo(table.getIdentifier(), CatalogBaseTable.TableKind.TABLE);
                    })
                    .collect(Collectors.toSet());
            tableInfoSet.addAll(tables);
        }
        if (tableKinds.contains(CatalogBaseTable.TableKind.VIEW)) {
            Set<TableInfo> views = catalogManager.listViews(catalogName, databaseName).stream()
                    .map(tableName -> {
                        ContextResolvedTable table = catalogManager.getTableOrError(
                                ObjectIdentifier.of(catalogName, databaseName, tableName));
                        return new TableInfo(table.getIdentifier(), CatalogBaseTable.TableKind.VIEW);
                    })
                    .collect(Collectors.toSet());
            tableInfoSet.addAll(views);
        }
        return tableInfoSet;
    }

    @Override
    public ResolvedCatalogBaseTable<?> getTable(SessionHandle sessionHandle, ObjectIdentifier tableIdentifier)
            throws SqlGatewayException {
        FlinkSqlGatewaySession flinkSqlGatewaySession = sessionService.getSession(sessionHandle);
        CatalogManager catalogManager =
                flinkSqlGatewaySession.getSessionContext().getSessionState().catalogManager;
        Catalog catalog = catalogManager.getCatalogOrThrowException(tableIdentifier.getCatalogName());
        try {
            ObjectPath tablePath = new ObjectPath(tableIdentifier.getDatabaseName(), tableIdentifier.getObjectName());
            CatalogBaseTable table = catalog.getTable(tablePath);
            return catalogManager.resolveCatalogBaseTable(table);
        } catch (TableNotExistException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<FunctionInfo> listUserDefinedFunctions(
            SessionHandle sessionHandle, String catalogName, String databaseName) throws SqlGatewayException {
        FlinkSqlGatewaySession flinkSqlGatewaySession = sessionService.getSession(sessionHandle);
        SessionContext.SessionState sessionState =
                flinkSqlGatewaySession.getSessionContext().getSessionState();
        FunctionCatalog functionCatalog = sessionState.functionCatalog;
        return functionCatalog.getUserDefinedFunctions(catalogName, databaseName).stream()
                .map(FunctionInfo::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<FunctionInfo> listSystemFunctions(SessionHandle sessionHandle) throws SqlGatewayException {
        FlinkSqlGatewaySession flinkSqlGatewaySession = sessionService.getSession(sessionHandle);
        SessionContext.SessionState sessionState =
                flinkSqlGatewaySession.getSessionContext().getSessionState();
        return sessionState.moduleManager.listFunctions().stream()
                .map(functionName -> sessionState
                        .moduleManager
                        .getFunctionDefinition(functionName)
                        .map(functionDefinition ->
                                new FunctionInfo(FunctionIdentifier.of(functionName), functionDefinition.getKind()))
                        .orElse(new FunctionInfo(FunctionIdentifier.of(functionName))))
                .collect(Collectors.toSet());
    }

    @Override
    public FunctionDefinition getFunctionDefinition(
            SessionHandle sessionHandle, UnresolvedIdentifier functionIdentifier) throws SqlGatewayException {
        FlinkSqlGatewaySession flinkSqlGatewaySession = sessionService.getSession(sessionHandle);
        SessionContext.SessionState sessionState =
                flinkSqlGatewaySession.getSessionContext().getSessionState();
        Optional<ContextResolvedFunction> contextResolvedFunction =
                sessionState.functionCatalog.lookupFunction(functionIdentifier);
        return contextResolvedFunction
                .map(ContextResolvedFunction::getDefinition)
                .orElse(null);
    }
}
