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

package cn.sliew.scaleph.engine.sql.gateway.services;

import java.util.List;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.gateway.api.operation.OperationHandle;
import org.apache.flink.table.gateway.api.session.SessionHandle;
import org.apache.flink.table.gateway.api.utils.SqlGatewayException;

public interface SqlService {

    Object validate(SessionHandle sessionHandle, String statement, Configuration executionConfig) throws Exception;

    /**
     * Returns a list of completion hints for the given statement at the given position.
     *
     * @param sessionHandle handle to identify the session.
     * @param statement     sql statement to be completed.
     * @param position      position of where need completion hints.
     * @return completion hints.
     */
    List<String> completeStatement(SessionHandle sessionHandle, String statement, int position)
            throws SqlGatewayException;

    /**
     * Execute the submitted statement.
     *
     * @param sessionHandle      handle to identify the session.
     * @param statement          the SQL to execute.
     * @param executionTimeoutMs the execution timeout. Please use non-positive value to forbid the
     *                           timeout mechanism.
     * @param executionConfig    execution config for the statement.
     * @return handle to identify the operation.
     */
    OperationHandle executeStatement(
            SessionHandle sessionHandle, String statement, long executionTimeoutMs, Configuration executionConfig)
            throws SqlGatewayException;

    OperationHandle previewStatement(
            SessionHandle sessionHandle, String statement, long executionTimeoutMs, Configuration executionConfig)
            throws SqlGatewayException;

    /**
     * Preview the submitted statement.
     *
     * <p>NOTE</p>
     * <p>
     * Only `INSERT` or `SELECT` statement is supported
     * </p>
     *
     * @param sessionHandle   handle to identify the session.
     * @param statement       the SQL to execute.
     * @param executionConfig execution config for the statement.
     * @param limit           limitation of the rows to statement
     * @return handle to identify the operation.
     * @throws SqlGatewayException
     */
    OperationHandle previewStatement(
            SessionHandle sessionHandle, String statement, Configuration executionConfig, long limit)
            throws SqlGatewayException;
}
