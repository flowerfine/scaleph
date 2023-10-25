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

package cn.sliew.scaleph.engine.sql.gateway.internal;

import java.io.IOException;
import java.net.URI;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.catalog.Catalog;
import org.apache.flink.table.catalog.CatalogManager;
import org.apache.flink.table.data.StringData;
import org.apache.flink.table.factories.FactoryUtil;
import org.apache.flink.table.gateway.api.operation.OperationHandle;
import org.apache.flink.table.gateway.api.results.FetchOrientation;
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
import org.springframework.util.CollectionUtils;

import cn.sliew.scaleph.engine.sql.gateway.services.dto.catalog.CatalogInfo;
import cn.sliew.scaleph.engine.sql.gateway.services.dto.catalog.FunctionInfo;
import cn.sliew.scaleph.engine.sql.gateway.util.CatalogUtil;

import static org.apache.flink.table.gateway.api.config.SqlGatewayServiceConfigOptions.SQL_GATEWAY_WORKER_KEEPALIVE_TIME;
import static org.apache.flink.table.gateway.api.config.SqlGatewayServiceConfigOptions.SQL_GATEWAY_WORKER_THREADS_MAX;
import static org.apache.flink.table.gateway.api.config.SqlGatewayServiceConfigOptions.SQL_GATEWAY_WORKER_THREADS_MIN;

public class ScalephCatalogManager implements AutoCloseable {

    private final SessionContext sessionContext;

    public ScalephCatalogManager(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    public static ScalephCatalogManager create(Configuration configuration) {
        DefaultContext defaultContext = new DefaultContext(configuration, Collections.emptyList());
        SessionEnvironment sessionEnvironment = SessionEnvironment.newBuilder()
                .setSessionEndpointVersion(SqlGatewayRestAPIVersion.V2)
                .build();
        ThreadPoolExecutor executor = ThreadUtils.newThreadPool(
                configuration.get(SQL_GATEWAY_WORKER_THREADS_MIN),
                configuration.get(SQL_GATEWAY_WORKER_THREADS_MAX),
                configuration.get(SQL_GATEWAY_WORKER_KEEPALIVE_TIME).toMillis(),
                "scaleph-catalog-manager-pool");
        SessionContext sessionContext =
                SessionContext.create(defaultContext, SessionHandle.create(), sessionEnvironment, executor);
        return new ScalephCatalogManager(sessionContext);
    }

    public Set<CatalogInfo> getCatalogInfo(boolean includeSystemFunctions) {
        SessionContext.SessionState sessionState = sessionContext.getSessionState();
        CatalogManager catalogManager = sessionState.catalogManager;
        ModuleManager moduleManager = sessionState.moduleManager;
        Set<FunctionInfo> systemFunctions = moduleManager.listFunctions().stream()
                .flatMap(functionName -> moduleManager.getFunctionDefinition(functionName).stream()
                        .map(functionDefinition -> CatalogUtil.createFunctionInfo(functionName, functionDefinition)))
                .collect(Collectors.toSet());
        return catalogManager.listCatalogs().stream()
                .map(catalogName -> {
                    CatalogInfo catalogInfo = CatalogUtil.createCatalogInfo(sessionContext, catalogName);
                    if (includeSystemFunctions) {
                        catalogInfo.setSystemFunctions(systemFunctions);
                    }
                    return catalogInfo;
                })
                .collect(Collectors.toSet());
    }

    public ResultSet fetchResults(OperationHandle operationHandle, FetchOrientation fetchOrientation, int maxRows) {
        return sessionContext.getOperationManager().fetchResults(operationHandle, fetchOrientation, maxRows);
    }

    public ResultSet fetchResults(OperationHandle operationHandle, Long token, int maxRows) {
        return sessionContext.getOperationManager().fetchResults(operationHandle, token, maxRows);
    }

    public void cancelOperation(OperationHandle operationHandle) {
        sessionContext.getOperationManager().cancelOperation(operationHandle);
    }

    public List<String> completeStatement(String statement, int position) throws Exception {
        OperationManager operationManager = sessionContext.getOperationManager();
        Configuration sessionConf = sessionContext.getSessionConf();
        OperationHandle operationHandle = operationManager.submitOperation(handle ->
                sessionContext.createOperationExecutor(sessionConf).getCompletionHints(handle, statement, position));
        operationManager.awaitOperationTermination(operationHandle);

        ResultSet resultSet = fetchResults(operationHandle, 0L, Integer.MAX_VALUE);
        return resultSet.getData().stream()
                .map(data -> data.getString(0))
                .map(StringData::toString)
                .collect(Collectors.toList());
    }

    public String executeStatement(String sql, Map<String, String> configuration) {
        Configuration sessionConf = new Configuration(sessionContext.getSessionConf());
        if (!CollectionUtils.isEmpty(configuration)) {
            configuration.forEach(sessionConf::setString);
        }
        return sessionContext
                .getOperationManager()
                .submitOperation(handle ->
                        sessionContext.createOperationExecutor(sessionConf).executeStatement(handle, sql))
                .getIdentifier()
                .toString();
    }

    public void addDependencies(List<URI> jars) {
        List<ResourceUri> uris = jars.stream()
                .map(uri -> new ResourceUri(ResourceType.JAR, uri.toString()))
                .collect(Collectors.toList());
        try {
            sessionContext.getSessionState().resourceManager.registerJarResources(uris);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addCatalog(String catalogName, Map<String, String> options) {
        Configuration sessionConf = sessionContext.getSessionConf();
        SessionContext.SessionState sessionState = sessionContext.getSessionState();
        URLClassLoader userClassLoader = sessionState.resourceManager.getUserClassLoader();
        Catalog catalog = FactoryUtil.createCatalog(catalogName, options, sessionConf, userClassLoader);
        sessionState.catalogManager.registerCatalog(catalogName, catalog);
    }

    public void removeCatalog(String catalogName) {
        sessionContext.getSessionState().catalogManager.unregisterCatalog(catalogName, true);
    }

    @Override
    public void close() throws Exception {
        sessionContext.close();
    }
}
