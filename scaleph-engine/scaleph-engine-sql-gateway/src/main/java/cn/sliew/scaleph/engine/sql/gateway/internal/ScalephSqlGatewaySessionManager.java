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

import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.table.gateway.api.results.GatewayInfo;
import org.apache.flink.table.gateway.api.session.SessionEnvironment;
import org.apache.flink.table.gateway.api.session.SessionHandle;
import org.apache.flink.table.gateway.api.utils.ThreadUtils;
import org.apache.flink.table.gateway.rest.util.SqlGatewayRestAPIVersion;
import org.apache.flink.table.gateway.service.context.DefaultContext;
import org.apache.flink.table.gateway.service.context.SessionContext;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.apache.flink.table.gateway.api.config.SqlGatewayServiceConfigOptions.SQL_GATEWAY_SESSION_CHECK_INTERVAL;
import static org.apache.flink.table.gateway.api.config.SqlGatewayServiceConfigOptions.SQL_GATEWAY_SESSION_IDLE_TIMEOUT;
import static org.apache.flink.table.gateway.api.config.SqlGatewayServiceConfigOptions.SQL_GATEWAY_WORKER_KEEPALIVE_TIME;
import static org.apache.flink.table.gateway.api.config.SqlGatewayServiceConfigOptions.SQL_GATEWAY_WORKER_THREADS_MAX;
import static org.apache.flink.table.gateway.api.config.SqlGatewayServiceConfigOptions.SQL_GATEWAY_WORKER_THREADS_MIN;

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
}
