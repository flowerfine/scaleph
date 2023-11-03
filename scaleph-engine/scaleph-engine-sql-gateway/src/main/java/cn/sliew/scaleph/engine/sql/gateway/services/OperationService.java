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

import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.flink.table.catalog.ResolvedSchema;
import org.apache.flink.table.gateway.api.SqlGatewayService;
import org.apache.flink.table.gateway.api.operation.OperationHandle;
import org.apache.flink.table.gateway.api.operation.OperationStatus;
import org.apache.flink.table.gateway.api.results.OperationInfo;
import org.apache.flink.table.gateway.api.results.ResultSet;
import org.apache.flink.table.gateway.api.session.SessionHandle;
import org.apache.flink.table.gateway.api.utils.SqlGatewayException;

public interface OperationService {

    Set<OperationInfo> listOperations(SessionHandle sessionHandle) throws SqlGatewayException;

    /**
     * Submit an operation and execute. The {@link SqlGatewayService} will take care of the
     * execution and assign the {@link OperationHandle} for later to retrieve the results.
     *
     * @param sessionHandle handle to identify the session.
     * @param executor      the main logic to get the execution results.
     * @return Returns the handle for later retrieve results.
     */
    OperationHandle submitOperation(SessionHandle sessionHandle, Callable<ResultSet> executor)
            throws SqlGatewayException;

    /**
     * Cancel the operation when it is not in terminal status.
     *
     * <p>It can't cancel an Operation if it is terminated.
     *
     * @param sessionHandle   handle to identify the session.
     * @param operationHandle handle to identify the operation.JarURLConnection
     */
    void cancelOperation(SessionHandle sessionHandle, OperationHandle operationHandle) throws SqlGatewayException;

    /**
     * Close the operation and release all used resource by the operation.
     *
     * @param sessionHandle   handle to identify the session.
     * @param operationHandle handle to identify the operation.
     */
    void closeOperation(SessionHandle sessionHandle, OperationHandle operationHandle) throws SqlGatewayException;

    /**
     * Get the {@link OperationInfo} of the operation.
     *
     * @param sessionHandle   handle to identify the session.
     * @param operationHandle handle to identify the operation.
     */
    OperationInfo getOperationInfo(SessionHandle sessionHandle, OperationHandle operationHandle)
            throws SqlGatewayException;

    /**
     * Get the result schema for the specified Operation.
     *
     * <p>Note: The result schema is available when the Operation is in the {@link
     * OperationStatus#FINISHED}.
     *
     * @param sessionHandle   handle to identify the session.
     * @param operationHandle handle to identify the operation.
     */
    ResolvedSchema getOperationResultSchema(SessionHandle sessionHandle, OperationHandle operationHandle)
            throws SqlGatewayException;
}
