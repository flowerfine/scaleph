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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.catalog.*;
import org.apache.flink.table.functions.FunctionDefinition;
import org.apache.flink.table.gateway.api.SqlGatewayService;
import org.apache.flink.table.gateway.api.endpoint.EndpointVersion;
import org.apache.flink.table.gateway.api.operation.OperationHandle;
import org.apache.flink.table.gateway.api.results.*;
import org.apache.flink.table.gateway.api.session.SessionEnvironment;
import org.apache.flink.table.gateway.api.session.SessionHandle;
import org.apache.flink.table.gateway.api.utils.SqlGatewayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.sliew.scaleph.engine.sql.gateway.services.CatalogService;
import cn.sliew.scaleph.engine.sql.gateway.services.OperationService;
import cn.sliew.scaleph.engine.sql.gateway.services.ResultFetcherService;
import cn.sliew.scaleph.engine.sql.gateway.services.SessionService;
import cn.sliew.scaleph.engine.sql.gateway.services.SqlService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ScalephSqlGatewayService implements SqlGatewayService {

    private final SessionService sessionService;
    private final CatalogService catalogService;
    private final OperationService operationService;
    private final SqlService sqlService;
    private final ResultFetcherService resultFetcherService;

    @Autowired
    public ScalephSqlGatewayService(
            SessionService sessionService,
            CatalogService catalogService,
            OperationService operationService,
            SqlService sqlService,
            ResultFetcherService resultFetcherService) {
        this.sessionService = sessionService;
        this.catalogService = catalogService;
        this.operationService = operationService;
        this.sqlService = sqlService;
        this.resultFetcherService = resultFetcherService;
    }

    @Override
    public SessionHandle openSession(SessionEnvironment sessionEnvironment) throws SqlGatewayException {
        return sessionService.openSession(sessionEnvironment);
    }

    @Override
    public void closeSession(SessionHandle sessionHandle) throws SqlGatewayException {
        sessionService.closeSession(sessionHandle);
    }

    @Override
    public void configureSession(SessionHandle sessionHandle, String statement, long executionTimeoutMs)
            throws SqlGatewayException {
        sessionService.configureSession(sessionHandle, statement, executionTimeoutMs);
    }

    @Override
    public Map<String, String> getSessionConfig(SessionHandle sessionHandle) throws SqlGatewayException {
        return sessionService.getSessionConfig(sessionHandle);
    }

    @Override
    public EndpointVersion getSessionEndpointVersion(SessionHandle sessionHandle) throws SqlGatewayException {
        return sessionService.getSessionEndpointVersion(sessionHandle);
    }

    @Override
    public OperationHandle submitOperation(SessionHandle sessionHandle, Callable<ResultSet> callable)
            throws SqlGatewayException {
        return operationService.submitOperation(sessionHandle, callable);
    }

    @Override
    public void cancelOperation(SessionHandle sessionHandle, OperationHandle operationHandle)
            throws SqlGatewayException {
        operationService.cancelOperation(sessionHandle, operationHandle);
    }

    @Override
    public void closeOperation(SessionHandle sessionHandle, OperationHandle operationHandle)
            throws SqlGatewayException {
        operationService.closeOperation(sessionHandle, operationHandle);
    }

    @Override
    public OperationInfo getOperationInfo(SessionHandle sessionHandle, OperationHandle operationHandle)
            throws SqlGatewayException {
        return operationService.getOperationInfo(sessionHandle, operationHandle);
    }

    @Override
    public ResolvedSchema getOperationResultSchema(SessionHandle sessionHandle, OperationHandle operationHandle)
            throws SqlGatewayException {
        return operationService.getOperationResultSchema(sessionHandle, operationHandle);
    }

    @Override
    public OperationHandle executeStatement(
            SessionHandle sessionHandle, String statement, long executionTimeoutMs, Configuration configuration)
            throws SqlGatewayException {
        return sqlService.executeStatement(sessionHandle, statement, executionTimeoutMs, configuration);
    }

    @Override
    public ResultSet fetchResults(SessionHandle sessionHandle, OperationHandle operationHandle, long token, int maxRows)
            throws SqlGatewayException {
        return resultFetcherService.fetchResults(sessionHandle, operationHandle, token, maxRows);
    }

    @Override
    public ResultSet fetchResults(
            SessionHandle sessionHandle,
            OperationHandle operationHandle,
            FetchOrientation fetchOrientation,
            int maxRows)
            throws SqlGatewayException {
        return resultFetcherService.fetchResults(sessionHandle, operationHandle, fetchOrientation, maxRows);
    }

    @Override
    public String getCurrentCatalog(SessionHandle sessionHandle) throws SqlGatewayException {
        return catalogService.getCurrentCatalog(sessionHandle);
    }

    @Override
    public Set<String> listCatalogs(SessionHandle sessionHandle) throws SqlGatewayException {
        return catalogService.listCatalogs(sessionHandle);
    }

    @Override
    public Set<String> listDatabases(SessionHandle sessionHandle, String catalogName) throws SqlGatewayException {
        return catalogService.listDatabases(sessionHandle, catalogName);
    }

    @Override
    public Set<TableInfo> listTables(
            SessionHandle sessionHandle,
            String catalogName,
            String databaseName,
            Set<CatalogBaseTable.TableKind> tableKinds)
            throws SqlGatewayException {
        return catalogService.listTables(sessionHandle, catalogName, databaseName, tableKinds);
    }

    @Override
    public ResolvedCatalogBaseTable<?> getTable(SessionHandle sessionHandle, ObjectIdentifier tableIdentifier)
            throws SqlGatewayException {
        return catalogService.getTable(sessionHandle, tableIdentifier);
    }

    @Override
    public Set<FunctionInfo> listUserDefinedFunctions(
            SessionHandle sessionHandle, String catalogName, String databaseName) throws SqlGatewayException {
        return catalogService.listUserDefinedFunctions(sessionHandle, catalogName, databaseName);
    }

    @Override
    public Set<FunctionInfo> listSystemFunctions(SessionHandle sessionHandle) throws SqlGatewayException {
        return catalogService.listSystemFunctions(sessionHandle);
    }

    @Override
    public FunctionDefinition getFunctionDefinition(
            SessionHandle sessionHandle, UnresolvedIdentifier functionIdentifier) throws SqlGatewayException {
        return catalogService.getFunctionDefinition(sessionHandle, functionIdentifier);
    }

    @Override
    public GatewayInfo getGatewayInfo() {
        return GatewayInfo.INSTANCE;
    }

    @Override
    public List<String> completeStatement(SessionHandle sessionHandle, String statement, int position)
            throws SqlGatewayException {
        return sqlService.completeStatement(sessionHandle, statement, position);
    }
}
