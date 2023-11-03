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

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.apache.flink.table.catalog.ResolvedSchema;
import org.apache.flink.table.gateway.api.operation.OperationHandle;
import org.apache.flink.table.gateway.api.results.OperationInfo;
import org.apache.flink.table.gateway.api.results.ResultSet;
import org.apache.flink.table.gateway.api.session.SessionHandle;
import org.apache.flink.table.gateway.api.utils.SqlGatewayException;
import org.apache.flink.table.gateway.service.context.SessionContext;
import org.apache.flink.table.gateway.service.operation.OperationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.sliew.scaleph.engine.sql.gateway.services.OperationService;
import cn.sliew.scaleph.engine.sql.gateway.services.SessionService;
import cn.sliew.scaleph.engine.sql.gateway.services.dto.FlinkSqlGatewaySession;

@Service
public class OperationServiceImpl implements OperationService {

    private final SessionService sessionService;

    @Autowired
    public OperationServiceImpl(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public Set<OperationInfo> listOperations(SessionHandle sessionHandle) throws SqlGatewayException {
        SessionContext sessionContext = sessionService.getSession(sessionHandle).getSessionContext();
        try {
            OperationManager operationManager = sessionContext.getOperationManager();
            Class<? extends OperationManager> operationManagerClass = operationManager.getClass();
            Field field = operationManagerClass.getDeclaredField("submittedOperations");
            field.setAccessible(true);
            Map<OperationHandle, OperationManager.Operation> map =
                    (Map<OperationHandle, OperationManager.Operation>) field.get(operationManager);
            return map.values().stream()
                    .map(OperationManager.Operation::getOperationInfo)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new SqlGatewayException(e);
        }
    }

    @Override
    public OperationHandle submitOperation(SessionHandle sessionHandle, Callable<ResultSet> executor)
            throws SqlGatewayException {
        FlinkSqlGatewaySession session = sessionService.getSession(sessionHandle);
        SessionContext sessionContext = session.getSessionContext();
        return sessionContext.getOperationManager().submitOperation(executor);
    }

    @Override
    public void cancelOperation(SessionHandle sessionHandle, OperationHandle operationHandle)
            throws SqlGatewayException {
        FlinkSqlGatewaySession session = sessionService.getSession(sessionHandle);
        SessionContext sessionContext = session.getSessionContext();
        sessionContext.getOperationManager().cancelOperation(operationHandle);
    }

    @Override
    public void closeOperation(SessionHandle sessionHandle, OperationHandle operationHandle)
            throws SqlGatewayException {
        FlinkSqlGatewaySession session = sessionService.getSession(sessionHandle);
        SessionContext sessionContext = session.getSessionContext();
        sessionContext.getOperationManager().closeOperation(operationHandle);
    }

    @Override
    public OperationInfo getOperationInfo(SessionHandle sessionHandle, OperationHandle operationHandle)
            throws SqlGatewayException {
        FlinkSqlGatewaySession session = sessionService.getSession(sessionHandle);
        SessionContext sessionContext = session.getSessionContext();
        return sessionContext.getOperationManager().getOperationInfo(operationHandle);
    }

    @Override
    public ResolvedSchema getOperationResultSchema(SessionHandle sessionHandle, OperationHandle operationHandle)
            throws SqlGatewayException {
        FlinkSqlGatewaySession session = sessionService.getSession(sessionHandle);
        SessionContext sessionContext = session.getSessionContext();
        try {
            return sessionContext.getOperationManager().getOperationResultSchema(operationHandle);
        } catch (Exception e) {
            throw new SqlGatewayException(e);
        }
    }
}
