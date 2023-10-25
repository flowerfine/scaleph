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

package cn.sliew.scaleph.engine.sql.gateway.services.impl;

import org.apache.flink.table.gateway.api.operation.OperationHandle;
import org.apache.flink.table.gateway.api.results.FetchOrientation;
import org.apache.flink.table.gateway.api.results.ResultSet;
import org.apache.flink.table.gateway.api.session.SessionHandle;
import org.apache.flink.table.gateway.api.utils.SqlGatewayException;
import org.apache.flink.table.gateway.service.context.SessionContext;
import org.springframework.stereotype.Service;

import cn.sliew.scaleph.engine.sql.gateway.services.ResultFetcherService;
import cn.sliew.scaleph.engine.sql.gateway.services.SessionService;

@Service
public class ResultFetcherServiceImpl implements ResultFetcherService {

    private final SessionService sessionService;

    public ResultFetcherServiceImpl(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public ResultSet fetchResults(SessionHandle sessionHandle, OperationHandle operationHandle, long token, int maxRows)
            throws SqlGatewayException {
        SessionContext sessionContext = sessionService.getSession(sessionHandle).getSessionContext();
        return sessionContext.getOperationManager().fetchResults(operationHandle, token, maxRows);
    }

    @Override
    public ResultSet fetchResults(
            SessionHandle sessionHandle, OperationHandle operationHandle, FetchOrientation orientation, int maxRows)
            throws SqlGatewayException {
        SessionContext sessionContext = sessionService.getSession(sessionHandle).getSessionContext();
        return sessionContext.getOperationManager().fetchResults(operationHandle, orientation, maxRows);
    }
}
