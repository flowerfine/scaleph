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
import cn.sliew.scaleph.engine.sql.gateway.dto.catalog.CatalogInfo;
import cn.sliew.scaleph.engine.sql.gateway.internal.ScalephSqlGatewaySessionManager;
import cn.sliew.scaleph.engine.sql.gateway.services.WsFlinkSqlGatewayService;
import cn.sliew.scaleph.kubernetes.service.KubernetesService;
import cn.sliew.scaleph.resource.service.ClusterCredentialService;
import cn.sliew.scaleph.resource.service.dto.ClusterCredentialDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.DeploymentOptions;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.kubernetes.configuration.KubernetesConfigOptions;
import org.apache.flink.table.gateway.api.operation.OperationHandle;
import org.apache.flink.table.gateway.api.results.FetchOrientation;
import org.apache.flink.table.gateway.api.results.GatewayInfo;
import org.apache.flink.table.gateway.api.results.ResultSet;
import org.apache.flink.table.gateway.api.session.SessionEnvironment;
import org.apache.flink.table.gateway.api.session.SessionHandle;
import org.apache.flink.table.gateway.rest.util.SqlGatewayRestAPIVersion;
import org.apache.flink.table.gateway.service.context.DefaultContext;
import org.apache.flink.table.gateway.service.context.SessionContext;
import org.apache.flink.table.gateway.service.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Slf4j
@Service
public class WsFlinkSqlGatewayServiceImpl implements WsFlinkSqlGatewayService {

    /**
     * Store {@link ScalephSqlGatewaySessionManager}s in this map. </br>
     * In case multi {@link ScalephSqlGatewaySessionManager}s can be enabled in the future
     */
    private static final Map<String, ScalephSqlGatewaySessionManager> SERVICE_MAP =
            new ConcurrentHashMap<>();
    private KubernetesService kubernetesService;
    private ClusterCredentialService clusterCredentialService;

    @Autowired
    public WsFlinkSqlGatewayServiceImpl(KubernetesService kubernetesService,
                                        ClusterCredentialService clusterCredentialService) {
        this.kubernetesService = kubernetesService;
        this.clusterCredentialService = clusterCredentialService;
    }

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
    public Optional<ScalephSqlGatewaySessionManager> getSessionManager(String clusterId) {
        return Optional.ofNullable(SERVICE_MAP.get(clusterId));
    }

    /**
     * {@inheritDoc}
     *
     * @param kubeCredentialId Cluster credential id
     * @param clusterId        Flink K8S session cluster id
     * @return
     * @throws Exception
     */
    @Override
    public Optional<ScalephSqlGatewaySessionManager> createSessionManager(Long kubeCredentialId, String clusterId) {
        try {
            ClusterCredentialDTO clusterCredential = clusterCredentialService.selectOne(kubeCredentialId);
            Path path = kubernetesService.downloadConfig(clusterCredential);
            Configuration configuration = GlobalConfiguration.loadConfiguration();
            configuration.set(KubernetesConfigOptions.CLUSTER_ID, clusterId);
            configuration.set(KubernetesConfigOptions.KUBE_CONFIG_FILE, path.toString());
            if (StringUtils.hasText(clusterCredential.getContext())) {
                configuration.set(KubernetesConfigOptions.CONTEXT, clusterCredential.getContext());
            }
            configuration.set(DeploymentOptions.TARGET, "kubernetes-session");
            DefaultContext defaultContext = new DefaultContext(configuration, Collections.emptyList());
            ScalephSqlGatewaySessionManager sessionManager = new ScalephSqlGatewaySessionManager(defaultContext);
            sessionManager.start();
            log.info("Stated sql-gateway for session cluster {}", clusterId);
            SERVICE_MAP.put(clusterId, sessionManager);
            return Optional.of(sessionManager);
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
    public void destroySessionManager(String clusterId) {
        getSessionManager(clusterId).ifPresent(sessionManager -> {
            sessionManager.stop();
            SERVICE_MAP.remove(clusterId);
        });
    }

    @Override
    public GatewayInfo getGatewayInfo(String clusterId) {
        try {
            return getSessionManager(clusterId)
                    .orElseThrow((Supplier<Throwable>) () -> new IllegalArgumentException("Sql gateway not start!"))
                    .getGatewayInfo();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String openSession(String clusterId) {
        return getSessionManager(clusterId)
                .orElseThrow()
                .openSession()
                .getIdentifier()
                .toString();
    }

    /**
     * {@inheritDoc}
     *
     * @param clusterId       Flink K8S session cluster id
     * @param sessionHandleId Session handler id
     * @return
     */
    @Override
    public Set<CatalogInfo> getCatalogInfo(String clusterId, String sessionHandleId) {
        SessionHandle sessionHandle = new SessionHandle(UUID.fromString(sessionHandleId));
        return getSessionManager(clusterId).orElseThrow()
                .getCatalogInfo(sessionHandle);
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
        getSessionManager(clusterId).orElseThrow()
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
        SessionContext sessionContext = getSessionManager(clusterId).orElseThrow()
                .getSession(sessionHandle)
                .getSessionContext();
        Configuration sessionConf = sessionContext.getSessionConf();
        params.getConfiguration().forEach(sessionConf::setString);
        return sessionContext.getOperationManager()
                .submitOperation(handle ->
                        sessionContext.createOperationExecutor(sessionConf)
                                .executeStatement(handle, params.getSql())
                )
                .getIdentifier()
                .toString();
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
        ScalephSqlGatewaySessionManager sessionManager = getSessionManager(clusterId).orElseThrow();
        ResultSet resultSet;
        if (token == null || token < 0) {
            resultSet = sessionManager.fetchResults(sessionHandle, operationHandle, FetchOrientation.FETCH_NEXT, maxRows);
        } else {
            resultSet = sessionManager.fetchResults(sessionHandle, operationHandle, token, maxRows);
        }
        return resultSet;
    }

    @Override
    public Boolean cancel(String clusterId, String sessionHandleId, String operationHandleId) {
        try {
            getSessionManager(clusterId).orElseThrow()
                    .cancelOperation(new SessionHandle(UUID.fromString(sessionHandleId)), new OperationHandle(UUID.fromString(operationHandleId)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<String> completeStatement(String clusterId, String sessionId, String statement, int position) throws Exception {
        return getSessionManager(clusterId).orElseThrow()
                .completeStatement(new SessionHandle(UUID.fromString(sessionId)), statement, position);
    }
}
