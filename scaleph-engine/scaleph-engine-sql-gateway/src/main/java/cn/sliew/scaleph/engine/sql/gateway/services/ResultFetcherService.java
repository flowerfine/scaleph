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

import org.apache.flink.table.gateway.api.operation.OperationHandle;
import org.apache.flink.table.gateway.api.results.FetchOrientation;
import org.apache.flink.table.gateway.api.results.ResultSet;
import org.apache.flink.table.gateway.api.session.SessionHandle;
import org.apache.flink.table.gateway.api.utils.SqlGatewayException;

public interface ResultFetcherService {

    /**
     * Fetch the results from the operation. When maxRows is Integer.MAX_VALUE, it means to fetch
     * all available data.
     *
     * @param sessionHandle   handle to identify the session.
     * @param operationHandle handle to identify the operation.
     * @param token           token to identify results.
     * @param maxRows         max number of rows to fetch.
     * @return Returns the results.
     */
    ResultSet fetchResults(SessionHandle sessionHandle, OperationHandle operationHandle, long token, int maxRows)
            throws SqlGatewayException;

    /**
     * Fetch the results from the operation. When maxRows is Integer.MAX_VALUE, it means to fetch
     * all available data. It promises to return at least one rows if the results is not
     * end-of-stream.
     *
     * @param sessionHandle   handle to identify the session.
     * @param operationHandle handle to identify the operation.
     * @param orientation     orientation to fetch the results.
     * @param maxRows         max number of rows to fetch.
     * @return Returns the results.
     */
    ResultSet fetchResults(
            SessionHandle sessionHandle, OperationHandle operationHandle, FetchOrientation orientation, int maxRows)
            throws SqlGatewayException;
}
