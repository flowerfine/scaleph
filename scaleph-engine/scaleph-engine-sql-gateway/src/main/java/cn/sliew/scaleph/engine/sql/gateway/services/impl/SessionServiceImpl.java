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

import cn.sliew.sakura.common.util.JacksonUtil;
import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkSqlGatewaySession;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkSqlGatewaySessionMapper;
import cn.sliew.scaleph.engine.sql.gateway.services.SessionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.type.TypeReference;
import org.apache.flink.table.gateway.api.endpoint.EndpointVersion;
import org.apache.flink.table.gateway.api.operation.OperationHandle;
import org.apache.flink.table.gateway.api.session.SessionEnvironment;
import org.apache.flink.table.gateway.api.session.SessionHandle;
import org.apache.flink.table.gateway.api.utils.SqlGatewayException;
import org.apache.flink.table.gateway.rest.util.SqlGatewayRestAPIVersion;
import org.apache.flink.table.gateway.service.context.DefaultContext;
import org.apache.flink.table.gateway.service.context.SessionContext;
import org.apache.flink.table.gateway.service.operation.OperationManager;
import org.apache.flink.table.gateway.service.session.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * org.apache.flink.table.gateway.service.session.SessionManager
 */
@Slf4j
@Service
public class SessionServiceImpl implements SessionService, InitializingBean {

    private ExecutorService operationExecutorService;
    private DefaultContext defaultContext;
    private ConcurrentMap<SessionHandle, Session> sessions = new ConcurrentHashMap<>();

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
        wsFlinkSqlGatewaySessions.forEach(this::convertSession);
    }

    @Override
    public SessionHandle openSession(SessionEnvironment environment) throws SqlGatewayException {
        SessionHandle sessionId = null;
        do {
            sessionId = SessionHandle.create();
        } while (sessions.containsKey(sessionId));

        Session session = buildSession(sessionId, environment);
        log.info("Session {} is opened.", session.getSessionHandle());



        return session.getSessionHandle();
    }

    @Override
    public void closeSession(SessionHandle sessionHandle) throws SqlGatewayException {
        Session session = sessions.remove(sessionHandle);
        session.close();
        LambdaQueryWrapper<WsFlinkSqlGatewaySession> queryWrapper = Wrappers.lambdaQuery(WsFlinkSqlGatewaySession.class)
                .eq(WsFlinkSqlGatewaySession::getSessionHandler, sessionHandle.toString());
        wsFlinkSqlGatewaySessionMapper.delete(queryWrapper);
        log.info("Session: {} is closed.", sessionHandle);
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

            OperationManager operationManager = getSession(sessionHandle).getOperationManager();
            OperationHandle operationHandle =
                    operationManager.submitOperation(
                            handle ->
                                    getSession(sessionHandle)
                                            .createExecutor()
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
        return getSession(sessionHandle).getSessionConfig();
    }

    @Override
    public EndpointVersion getSessionEndpointVersion(SessionHandle sessionHandle) throws SqlGatewayException {
        return SqlGatewayRestAPIVersion.V2;
    }

    private Session getSession(SessionHandle sessionHandle) {
        Session session = sessions.get(sessionHandle);
        if (session == null) {
            log.warn("Session '%s' does not exist in memeory, try load from database", sessionHandle);
            return loadSession(sessionHandle);
        }
        session.touch();
        return session;
    }

    private Session loadSession(SessionHandle sessionHandle) {
        LambdaQueryWrapper<WsFlinkSqlGatewaySession> queryWrapper = Wrappers.lambdaQuery(WsFlinkSqlGatewaySession.class)
                .eq(WsFlinkSqlGatewaySession::getSessionHandler, sessionHandle.toString());
        WsFlinkSqlGatewaySession record = wsFlinkSqlGatewaySessionMapper.selectOne(queryWrapper);
        return convertSession(record);
    }

    private Session convertSession(WsFlinkSqlGatewaySession record) {
        SessionHandle sessionId = new SessionHandle(UUID.fromString(record.getSessionHandler()));

        Map<String, String> sessionConfig = Collections.emptyMap();
        if (StringUtils.hasText(record.getSessionConfig())) {
            sessionConfig = JacksonUtil.parseJsonString(record.getSessionConfig(), new TypeReference<Map<String, String>>() {
            });
        }
        SessionEnvironment environment = SessionEnvironment.newBuilder()
                .addSessionConfig(sessionConfig)
                .build();

        return buildSession(sessionId, environment);
    }

    private Session buildSession(SessionHandle sessionId, SessionEnvironment environment) {
        SessionContext sessionContext = SessionContext.create(defaultContext, sessionId, environment, operationExecutorService);
        Session session = new Session(sessionContext);
        sessions.put(sessionId, session);
        return session;
    }

}
