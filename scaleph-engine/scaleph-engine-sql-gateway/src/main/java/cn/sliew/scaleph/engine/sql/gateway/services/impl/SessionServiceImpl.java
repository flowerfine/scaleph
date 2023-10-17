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

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkSqlGatewaySession;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkSqlGatewaySessionMapper;
import cn.sliew.scaleph.engine.sql.gateway.services.SessionService;
import cn.sliew.scaleph.engine.sql.gateway.services.dto.FlinkSqlGatewaySession;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.gateway.api.endpoint.EndpointVersion;
import org.apache.flink.table.gateway.api.operation.OperationHandle;
import org.apache.flink.table.gateway.api.session.SessionEnvironment;
import org.apache.flink.table.gateway.api.session.SessionHandle;
import org.apache.flink.table.gateway.api.utils.SqlGatewayException;
import org.apache.flink.table.gateway.rest.util.SqlGatewayRestAPIVersion;
import org.apache.flink.table.gateway.service.context.DefaultContext;
import org.apache.flink.table.gateway.service.context.SessionContext;
import org.apache.flink.table.gateway.service.operation.OperationManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * org.apache.flink.table.gateway.service.session.SessionManager
 */
@Slf4j
@Service
public class SessionServiceImpl implements SessionService, InitializingBean, DisposableBean {

    private ExecutorService operationExecutorService;
    private DefaultContext defaultContext;

    private LoadingCache<SessionHandle, FlinkSqlGatewaySession> sessions = Caffeine.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(Duration.ofDays(3L))
            .evictionListener((SessionHandle sessionHandle, FlinkSqlGatewaySession session, RemovalCause removalCause) -> doCloseSession(sessionHandle, session, removalCause))
            .build(sessionHandle -> doGetSession(sessionHandle));

    @Autowired
    private WsFlinkSqlGatewaySessionMapper wsFlinkSqlGatewaySessionMapper;

    /**
     * session 的配置 2 级：sql gateway 实例级和 session 级别
     * DefaultContext 相当于实例级别，而 sessionconfig 相当于 session 级别
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        this.operationExecutorService = Executors.newFixedThreadPool(4);
        this.defaultContext = DefaultContext.load(new Configuration(), Collections.emptyList(), false, false);
        // 加载所有的 session
        List<WsFlinkSqlGatewaySession> wsFlinkSqlGatewaySessions = wsFlinkSqlGatewaySessionMapper.selectList(Wrappers.emptyWrapper());
        wsFlinkSqlGatewaySessions.stream().map(this::convertSession).forEach(session -> sessions.put(session.getSessionHandle(), session));
    }

    @Override
    public void destroy() throws Exception {
        sessions.cleanUp();
    }

    @Override
    public FlinkSqlGatewaySession getSession(SessionHandle sessionHandle) throws SqlGatewayException {
        return sessions.get(sessionHandle);
    }

    public FlinkSqlGatewaySession doGetSession(SessionHandle sessionHandle) throws SqlGatewayException {
        LambdaQueryWrapper<WsFlinkSqlGatewaySession> queryWrapper = Wrappers.lambdaQuery(WsFlinkSqlGatewaySession.class)
                .eq(WsFlinkSqlGatewaySession::getSessionHandler, sessionHandle.toString());
        WsFlinkSqlGatewaySession record = wsFlinkSqlGatewaySessionMapper.selectOne(queryWrapper);
        if (record == null) {
            String msg = String.format("Session '%s' does not exist.", sessionHandle);
            log.warn(msg);
            throw new SqlGatewayException(msg);
        }
        return convertSession(record);
    }

    @Override
    public SessionHandle openSession(SessionEnvironment environment) throws SqlGatewayException {
        return doOpenSession(environment).getSessionHandle();
    }

    public FlinkSqlGatewaySession doOpenSession(SessionEnvironment environment) throws SqlGatewayException {
        SessionHandle sessionId = null;
        boolean exist = true;
        while (exist) {
            sessionId = SessionHandle.create();
            try {
                sessions.get(sessionId);
            } catch (SqlGatewayException ignored) {
                exist = false;
            }
        }

        WsFlinkSqlGatewaySession record = new WsFlinkSqlGatewaySession();
        record.setSessionHandler(sessionId.toString());
        environment.getSessionName().ifPresent(sessionName -> record.setSessionName(sessionName));
        environment.getDefaultCatalog().ifPresent(defaultCatalog -> record.setDefaultCatalog(defaultCatalog));
        if (CollectionUtils.isEmpty(environment.getSessionConfig()) == false) {
            record.setSessionConfig(JacksonUtil.toJsonString(environment.getSessionConfig()));
        }
        wsFlinkSqlGatewaySessionMapper.insert(record);
        log.info("Session {} is opened.", sessionId);

        return getSession(sessionId);
    }

    @Override
    public void closeSession(SessionHandle sessionHandle) throws SqlGatewayException {
        sessions.invalidate(sessionHandle);
    }

    public void doCloseSession(SessionHandle sessionHandle, FlinkSqlGatewaySession session, RemovalCause removalCause) throws SqlGatewayException {
        switch (removalCause) {
            case EXPLICIT:
            case SIZE:
            case REPLACED:
            case COLLECTED:
                return;
            case EXPIRED:
                session.close();
                LambdaQueryWrapper<WsFlinkSqlGatewaySession> queryWrapper = Wrappers.lambdaQuery(WsFlinkSqlGatewaySession.class)
                        .eq(WsFlinkSqlGatewaySession::getSessionHandler, sessionHandle.toString());
                wsFlinkSqlGatewaySessionMapper.delete(queryWrapper);
                log.info("Session: {} is closed.", sessionHandle);
                break;
            default:
        }
    }

    /**
     * forked from SqlGatewayServiceImpl
     */
    @Override
    public void configureSession(SessionHandle sessionHandle, String statement, long executionTimeoutMs) throws SqlGatewayException {
        try {
            if (executionTimeoutMs > 0) {
                // TODO: support the feature in FLINK-27838
                throw new UnsupportedOperationException(
                        "SqlGatewayService doesn't support timeout mechanism now.");
            }

            OperationManager operationManager = getSession(sessionHandle).getSessionContext().getOperationManager();
            OperationHandle operationHandle =
                    operationManager.submitOperation(
                            handle ->
                                    getSession(sessionHandle).getSessionContext().createOperationExecutor(getSession(sessionHandle).getSessionContext().getSessionConf())
                                            .configureSession(handle, statement));
            operationManager.awaitOperationTermination(operationHandle);
            operationManager.closeOperation(operationHandle);
        } catch (Throwable t) {
            log.error("Failed to configure session.", t);
            throw new SqlGatewayException("Failed to configure session.", t);
        }
    }

    @Override
    public Map<String, String> getSessionConfig(SessionHandle sessionHandle) throws SqlGatewayException {
        return getSession(sessionHandle).getSessionContext().getSessionConf().toMap();
    }

    @Override
    public EndpointVersion getSessionEndpointVersion(SessionHandle sessionHandle) throws SqlGatewayException {
        return SqlGatewayRestAPIVersion.V2;
    }

    private FlinkSqlGatewaySession convertSession(WsFlinkSqlGatewaySession record) {
        FlinkSqlGatewaySession session = new FlinkSqlGatewaySession();
        SessionHandle sessionId = new SessionHandle(UUID.fromString(record.getSessionHandler()));
        session.setSessionHandle(sessionId);

        Map<String, String> sessionConfig = Collections.emptyMap();
        if (StringUtils.hasText(record.getSessionConfig())) {
            sessionConfig = JacksonUtil.parseJsonString(record.getSessionConfig(), new TypeReference<Map<String, String>>() {
            });
        }
        session.setSessionConfig(sessionConfig);

        SessionEnvironment environment = SessionEnvironment.newBuilder()
                .setSessionName(record.getSessionName())
                .setDefaultCatalog(record.getDefaultCatalog())
                .addSessionConfig(sessionConfig)
                .build();
        SessionContext sessionContext = SessionContext.create(defaultContext, sessionId, environment, operationExecutorService);
        session.setSessionContext(sessionContext);
        return session;
    }

}
