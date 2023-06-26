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

import cn.sliew.scaleph.engine.sql.gateway.services.FlinkSqlGatewayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.DeploymentOptions;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.kubernetes.configuration.KubernetesConfigOptions;
import org.apache.flink.table.gateway.api.SqlGatewayService;
import org.apache.flink.table.gateway.api.results.GatewayInfo;
import org.apache.flink.table.gateway.service.SqlGatewayServiceImpl;
import org.apache.flink.table.gateway.service.context.DefaultContext;
import org.apache.flink.table.gateway.service.session.SessionManager;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Slf4j
@Service
public class FlinkSqlGatewayServiceImpl implements FlinkSqlGatewayService {

    /**
     * Store {@link SqlGatewayService}s in this map. </br>
     * In case multi {@link SqlGatewayService}s can be enabled in the future
     */
    private static final Map<String, SqlGatewayService> SERVICE_MAP = new ConcurrentHashMap<>();

    private Optional<SqlGatewayService> getSqlGatewayService(String clusterId) throws Exception {
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
    public Optional<SqlGatewayService> getOrCreateSqlGatewayService(String clusterId) throws Throwable {
        return Optional.ofNullable(SERVICE_MAP.compute(clusterId, (id, sqlGatewayService) -> {
            try {
                Configuration configuration = GlobalConfiguration.loadConfiguration();
                configuration.set(KubernetesConfigOptions.CLUSTER_ID, id);
                configuration.set(DeploymentOptions.TARGET, "kubernetes-session");
                DefaultContext defaultContext = new DefaultContext(configuration, Collections.emptyList());
                SessionManager sessionManager = SessionManager.create(defaultContext);
                sessionManager.start();
                return new SqlGatewayServiceImpl(sessionManager);
            } catch (Exception e) {
                log.error("Error create SqlGateway for session id: " + clusterId, e);
            }
            return null;
        }));
    }

    /**
     * {@inheritDoc}
     *
     * @param clusterId Flink K8S session cluster id
     * @throws Exception
     */
    @Override
    public void destroySqlGatewayService(String clusterId) throws Throwable {
        getOrCreateSqlGatewayService(clusterId).ifPresent(sqlGatewayService -> {
            Class<?> sqlGatewayServiceClass = sqlGatewayService.getClass();
            Field sessionManagerField = null;
            try {
                sessionManagerField = sqlGatewayServiceClass.getDeclaredField("sessionManager");
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
}
