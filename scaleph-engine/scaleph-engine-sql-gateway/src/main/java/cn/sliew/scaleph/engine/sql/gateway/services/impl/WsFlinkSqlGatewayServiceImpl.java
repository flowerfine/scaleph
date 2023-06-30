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

package cn.sliew.scaleph.engine.sql.gateway.services.impl;

import cn.sliew.scaleph.engine.sql.gateway.dto.WsFlinkSqlGatewayQueryParamsDTO;
import cn.sliew.scaleph.engine.sql.gateway.services.WsFlinkSqlGatewayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.DeploymentOptions;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.kubernetes.configuration.KubernetesConfigOptions;
import org.apache.flink.table.catalog.CatalogBaseTable;
import org.apache.flink.table.gateway.api.SqlGatewayService;
import org.apache.flink.table.gateway.api.operation.OperationHandle;
import org.apache.flink.table.gateway.api.results.*;
import org.apache.flink.table.gateway.api.session.SessionEnvironment;
import org.apache.flink.table.gateway.api.session.SessionHandle;
import org.apache.flink.table.gateway.rest.util.SqlGatewayRestAPIVersion;
import org.apache.flink.table.gateway.service.SqlGatewayServiceImpl;
import org.apache.flink.table.gateway.service.context.DefaultContext;
import org.apache.flink.table.gateway.service.session.SessionManager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Slf4j
@Service
public class WsFlinkSqlGatewayServiceImpl implements WsFlinkSqlGatewayService {

    /**
     * Store {@link SqlGatewayService}s in this map. </br>
     * In case multi {@link SqlGatewayService}s can be enabled in the future
     */
    private static final Map<String, SqlGatewayService> SERVICE_MAP = new ConcurrentHashMap<>();

    /**
     * Create a new {@link SessionEnvironment} with random UUID session name
     *
     * @return
     */
    private static SessionEnvironment newSessionEnv() {
        return SessionEnvironment
                .newBuilder()
                .setSessionEndpointVersion(SqlGatewayRestAPIVersion.V2)
                .setSessionName(UUID.randomUUID().toString())
                .build();
    }

    /**
     * {@inheritDoc}
     *
     * @param clusterId Flink K8S session cluster id
     * @return
     */
    @Override
    public Optional<SqlGatewayService> getSqlGatewayService(String clusterId) {
        return Optional.ofNullable(SERVICE_MAP.get(clusterId));
    }

    /**
     * {@inheritDoc}
     *
     * @param clusterId Flink K8S session cluster id
     * @return
     * @throws Exception
     */
    @Override
    public Optional<SqlGatewayService> createSqlGatewayService(String clusterId) {
        try {
            Configuration configuration = GlobalConfiguration.loadConfiguration();
            configuration.set(KubernetesConfigOptions.CLUSTER_ID, clusterId);
            configuration.set(DeploymentOptions.TARGET, "kubernetes-session");
            DefaultContext defaultContext = new DefaultContext(configuration, Collections.emptyList());
            SessionManager sessionManager = SessionManager.create(defaultContext);
            sessionManager.start();
            SqlGatewayServiceImpl sqlGatewayService = new SqlGatewayServiceImpl(sessionManager);
            log.info("Stated sql-gateway for session cluster {}", clusterId);
            SERVICE_MAP.put(clusterId, sqlGatewayService);
            return Optional.of(sqlGatewayService);
        } catch (Exception e) {
            log.error("Error create SqlGateway for session id: " + clusterId, e);
        }
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     *
     * @param clusterId Flink K8S session cluster id
     * @throws Exception
     */
    @Override
    public void destroySqlGatewayService(String clusterId) {
        getSqlGatewayService(clusterId).ifPresent(sqlGatewayService -> {
            Class<?> sqlGatewayServiceClass = sqlGatewayService.getClass();
            Field sessionManagerField = null;
            try {
                sessionManagerField = sqlGatewayServiceClass.getDeclaredField("sessionManager");
                sessionManagerField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
            try {
                SessionManager sessionManager = (SessionManager) sessionManagerField.get(sqlGatewayService);
                sessionManager.stop();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            SERVICE_MAP.remove(clusterId);
        });
    }

    @Override
    public GatewayInfo getGatewayInfo(String clusterId) {
        try {
            return getSqlGatewayService(clusterId)
                    .orElseThrow((Supplier<Throwable>) () -> new IllegalArgumentException("Sql gateway not start!"))
                    .getGatewayInfo();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String openSession(String clusterId) {
        return getSqlGatewayService(clusterId)
                .orElseThrow()
                .openSession(newSessionEnv())
                .getIdentifier()
                .toString();
    }

    /**
     * {@inheritDoc}
     *
     * @param clusterId Session cluster id
     * @return Set of catalogs
     */
    @Override
    public Set<String> listCatalogs(String clusterId, String sessionHandleId) {
        SessionHandle sessionHandle = new SessionHandle(UUID.fromString(sessionHandleId));
        return getSqlGatewayService(clusterId).orElseThrow()
                .listCatalogs(sessionHandle);
    }

    /**
     * {@inheritDoc}
     *
     * @param clusterId       Flink K8S session cluster id
     * @param sessionHandleId Session handler id
     * @param catalog         Catalog name
     * @return
     */
    @Override
    public Set<String> listDatabases(String clusterId, String sessionHandleId, String catalog) {
        SessionHandle sessionHandle = new SessionHandle(UUID.fromString(sessionHandleId));
        SqlGatewayService sqlGatewayService = getSqlGatewayService(clusterId).orElseThrow();
        if (!StringUtils.hasText(catalog)) {
            catalog = sqlGatewayService.getCurrentCatalog(sessionHandle);
        }
        return sqlGatewayService.listDatabases(sessionHandle, catalog);
    }

    /**
     * {@inheritDoc}
     *
     * @param clusterId       Flink K8S session cluster id
     * @param sessionHandleId Session handler id
     * @param catalog         Catalog name
     * @param database        Database name
     * @param tableKinds      {@link  org.apache.flink.table.catalog.CatalogBaseTable.TableKind}
     * @return
     */
    @Override
    public Set<TableInfo> listTables(String clusterId, String sessionHandleId,
                                     String catalog, String database,
                                     Set<CatalogBaseTable.TableKind> tableKinds) {
        SessionHandle sessionHandle = new SessionHandle(UUID.fromString(sessionHandleId));
        return getSqlGatewayService(clusterId).orElseThrow()
                .listTables(sessionHandle, catalog, database, tableKinds);
    }

    /**
     * {@inheritDoc}
     *
     * @param clusterId       Flink K8S session cluster id
     * @param sessionHandleId Session handler id
     * @return
     */
    @Override
    public Set<FunctionInfo> listSystemFunctions(String clusterId, String sessionHandleId) {
        SessionHandle sessionHandle = new SessionHandle(UUID.fromString(sessionHandleId));
        return getSqlGatewayService(clusterId).orElseThrow()
                .listSystemFunctions(sessionHandle);
    }

    /**
     * {@inheritDoc}
     *
     * @param clusterId       Flink K8S session cluster id
     * @param sessionHandleId Session handler id
     * @param catalog         Catalog name
     * @param database        Database name
     * @return
     */
    @Override
    public Set<FunctionInfo> listUserDefinedFunctions(String clusterId, String sessionHandleId, String catalog, String database) {
        SessionHandle sessionHandle = new SessionHandle(UUID.fromString(sessionHandleId));
        return getSqlGatewayService(clusterId).orElseThrow()
                .listUserDefinedFunctions(sessionHandle, catalog, database);
    }

    /**
     * {@inheritDoc}
     *
     * @param clusterId       Flink K8S session cluster id
     * @param sessionHandleId Session handler id
     * @return
     */
    @Override
    public String closeSession(String clusterId, String sessionHandleId) {
        SessionHandle sessionHandle = new SessionHandle(UUID.fromString(sessionHandleId));
        getSqlGatewayService(clusterId).orElseThrow()
                .closeSession(sessionHandle);
        return sessionHandleId;
    }

    /**
     * {@inheritDoc}
     *
     * @param clusterId       Flink K8S session cluster id
     * @param sessionHandleId Session handler id
     * @param params          Sql query params
     * @return
     */
    @Override
    public String executeSql(String clusterId, String sessionHandleId, WsFlinkSqlGatewayQueryParamsDTO params) {
        SessionHandle sessionHandle = new SessionHandle(UUID.fromString(sessionHandleId));
        Configuration configuration = GlobalConfiguration.loadConfiguration();
        params.getConfiguration().forEach(configuration::setString);
        return getSqlGatewayService(clusterId).orElseThrow()
                .executeStatement(sessionHandle, params.getSql(), 0L /*Only <=0 is supported now*/,
                        configuration)
                .getIdentifier().toString();
    }

    /**
     * {@inheritDoc}
     *
     * @param clusterId         Flink K8S session cluster id
     * @param sessionHandleId   Session handler id
     * @param operationHandleId Operation handle id
     * @param token             token
     * @param maxRows           Max rows to fetch
     * @return
     */
    @Override
    public ResultSet fetchResults(String clusterId, String sessionHandleId,
                                  String operationHandleId,
                                  Long token, int maxRows) {
        SessionHandle sessionHandle = new SessionHandle(UUID.fromString(sessionHandleId));
        OperationHandle operationHandle = new OperationHandle(UUID.fromString(operationHandleId));
        SqlGatewayService sqlGatewayService = getSqlGatewayService(clusterId).orElseThrow();
        ResultSet resultSet;
        if (token == null || token < 0) {
            resultSet = sqlGatewayService.fetchResults(sessionHandle, operationHandle, FetchOrientation.FETCH_NEXT, maxRows);
        } else {
            resultSet = sqlGatewayService.fetchResults(sessionHandle, operationHandle, token, maxRows);
        }
        return resultSet;
    }

    @Override
    public Boolean cancel(String clusterId, String sessionHandleId, String operationHandleId) {
        try {
            getSqlGatewayService(clusterId).orElseThrow()
                    .cancelOperation(new SessionHandle(UUID.fromString(sessionHandleId)), new OperationHandle(UUID.fromString(operationHandleId)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
