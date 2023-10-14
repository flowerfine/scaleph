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

import cn.sliew.scaleph.engine.sql.gateway.services.SessionService;
import lombok.extern.slf4j.Slf4j;
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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

@Slf4j
public class ScalephSqlGatewayService implements SqlGatewayService {

    private SessionService sessionService;

    public ScalephSqlGatewayService(SessionService sessionService) {
        this.sessionService = sessionService;
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
    public void configureSession(SessionHandle sessionHandle, String statement, long executionTimeoutMs) throws SqlGatewayException {
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
    public OperationHandle submitOperation(SessionHandle sessionHandle, Callable<ResultSet> callable) throws SqlGatewayException {
        return null;
    }

    @Override
    public void cancelOperation(SessionHandle sessionHandle, OperationHandle operationHandle) throws SqlGatewayException {

    }

    @Override
    public void closeOperation(SessionHandle sessionHandle, OperationHandle operationHandle) throws SqlGatewayException {

    }

    @Override
    public OperationInfo getOperationInfo(SessionHandle sessionHandle, OperationHandle operationHandle) throws SqlGatewayException {
        return null;
    }

    @Override
    public ResolvedSchema getOperationResultSchema(SessionHandle sessionHandle, OperationHandle operationHandle) throws SqlGatewayException {
        return null;
    }

    @Override
    public OperationHandle executeStatement(SessionHandle sessionHandle, String s, long l, Configuration configuration) throws SqlGatewayException {
        return null;
    }

    @Override
    public ResultSet fetchResults(SessionHandle sessionHandle, OperationHandle operationHandle, long l, int i) throws SqlGatewayException {
        return null;
    }

    @Override
    public ResultSet fetchResults(SessionHandle sessionHandle, OperationHandle operationHandle, FetchOrientation fetchOrientation, int i) throws SqlGatewayException {
        return null;
    }

    @Override
    public String getCurrentCatalog(SessionHandle sessionHandle) throws SqlGatewayException {
        return null;
    }

    @Override
    public Set<String> listCatalogs(SessionHandle sessionHandle) throws SqlGatewayException {
        return null;
    }

    @Override
    public Set<String> listDatabases(SessionHandle sessionHandle, String s) throws SqlGatewayException {
        return null;
    }

    @Override
    public Set<TableInfo> listTables(SessionHandle sessionHandle, String s, String s1, Set<CatalogBaseTable.TableKind> set) throws SqlGatewayException {
        return null;
    }

    @Override
    public ResolvedCatalogBaseTable<?> getTable(SessionHandle sessionHandle, ObjectIdentifier objectIdentifier) throws SqlGatewayException {
        return null;
    }

    @Override
    public Set<FunctionInfo> listUserDefinedFunctions(SessionHandle sessionHandle, String s, String s1) throws SqlGatewayException {
        return null;
    }

    @Override
    public Set<FunctionInfo> listSystemFunctions(SessionHandle sessionHandle) throws SqlGatewayException {
        return null;
    }

    @Override
    public FunctionDefinition getFunctionDefinition(SessionHandle sessionHandle, UnresolvedIdentifier unresolvedIdentifier) throws SqlGatewayException {
        return null;
    }

    @Override
    public GatewayInfo getGatewayInfo() {
        return GatewayInfo.INSTANCE;
    }

    @Override
    public List<String> completeStatement(SessionHandle sessionHandle, String s, int i) throws SqlGatewayException {
        return null;
    }
}
