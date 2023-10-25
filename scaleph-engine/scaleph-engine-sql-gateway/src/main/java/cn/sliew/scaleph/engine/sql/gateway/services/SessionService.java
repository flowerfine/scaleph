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

import java.util.Map;

import org.apache.flink.table.gateway.api.endpoint.EndpointVersion;
import org.apache.flink.table.gateway.api.session.SessionEnvironment;
import org.apache.flink.table.gateway.api.session.SessionHandle;
import org.apache.flink.table.gateway.api.utils.SqlGatewayException;

import cn.sliew.scaleph.engine.sql.gateway.services.dto.FlinkSqlGatewaySession;

public interface SessionService {

    FlinkSqlGatewaySession getSession(SessionHandle sessionHandle) throws SqlGatewayException;

    /**
     * Open the {@code Session}.
     *
     * @param environment Environment to initialize the Session.
     * @return Returns a handle that used to identify the Session.
     */
    SessionHandle openSession(SessionEnvironment environment) throws SqlGatewayException;

    /**
     * Close the {@code Session}.
     *
     * @param sessionHandle handle to identify the Session needs to be closed.
     */
    void closeSession(SessionHandle sessionHandle) throws SqlGatewayException;

    /**
     * Using the statement to initialize the Session. It's only allowed to execute
     * SET/RESET/CREATE/DROP/USE/ALTER/LOAD MODULE/UNLOAD MODULE/ADD JAR.
     *
     * <p>It returns until the execution finishes.
     *
     * @param sessionHandle      handle to identify the session.
     * @param statement          the statement used to configure the session.
     * @param executionTimeoutMs the execution timeout. Please use non-positive value to forbid the
     *                           timeout mechanism.
     */
    void configureSession(SessionHandle sessionHandle, String statement, long executionTimeoutMs)
            throws SqlGatewayException;

    /**
     * Get the current configuration of the {@code Session}.
     *
     * @param sessionHandle handle to identify the session.
     * @return Returns configuration of the session.
     */
    Map<String, String> getSessionConfig(SessionHandle sessionHandle) throws SqlGatewayException;

    /**
     * Get endpoint version that is negotiated in the openSession.
     *
     * @param sessionHandle handle to identify the session.
     * @return Returns the version.
     */
    EndpointVersion getSessionEndpointVersion(SessionHandle sessionHandle) throws SqlGatewayException;
}
