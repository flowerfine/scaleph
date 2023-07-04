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

import cn.sliew.scaleph.engine.sql.gateway.dto.WsFlinkSqlGatewayQueryParamsDTO;
import org.apache.flink.table.catalog.CatalogBaseTable;
import org.apache.flink.table.gateway.api.SqlGatewayService;
import org.apache.flink.table.gateway.api.results.FunctionInfo;
import org.apache.flink.table.gateway.api.results.GatewayInfo;
import org.apache.flink.table.gateway.api.results.ResultSet;
import org.apache.flink.table.gateway.api.results.TableInfo;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.Set;

public interface WsFlinkSqlGatewayService {

    /**
     * Get a {@link SqlGatewayService}
     *
     * @param clusterId Flink K8S session cluster id
     * @return Optional {@link SqlGatewayService}
     */
    Optional<SqlGatewayService> getSqlGatewayService(String clusterId);

    /**
     * Get a {@link SqlGatewayService} by given params.
     * <p>
     * If not exists, create a new one and store in memory.
     * </p>
     *
     * @param kubeCredentialId Cluster credential id
     * @param clusterId Flink K8S session cluster id
     * @return an Optional instance of {@link SqlGatewayService}
     */
    Optional<SqlGatewayService> createSqlGatewayService(Long kubeCredentialId, String clusterId);

    /**
     * Destroy a {@link SqlGatewayService} by cluster id
     *
     * @param clusterId Flink K8S session cluster id
     */
    void destroySqlGatewayService(String clusterId);

    /**
     * Get sql gate way info
     *
     * <p>
     * Note: This function only returns the sql-gateway info started on local, NOT the remote.
     * </p>
     *
     * @param clusterId Flink K8S session cluster id
     * @return Sql gateway info
     */
    GatewayInfo getGatewayInfo(String clusterId);

    /**
     * Create a new session
     *
     * @param clusterId Flink K8S session cluster id
     * @return session handler id {@link org.apache.flink.table.gateway.api.session.SessionHandle}
     */
    String openSession(String clusterId);

    /**
     * List catalogs
     *
     * @param clusterId       Flink K8S session cluster id
     * @param sessionHandleId Session handler id
     * @return Set of catalogs
     */
    Set<String> listCatalogs(String clusterId, String sessionHandleId);

    /**
     * List databases.
     *
     * <p>If catalog name is empty, use default catalog.</p>
     *
     * @param clusterId       Flink K8S session cluster id
     * @param sessionHandleId Session handler id
     * @param catalog         Catalog name
     * @return Set of databases
     */
    Set<String> listDatabases(String clusterId, String sessionHandleId, String catalog);

    /**
     * List tables
     *
     * @param clusterId       Flink K8S session cluster id
     * @param sessionHandleId Session handler id
     * @param catalog         Catalog name
     * @param database        Database name
     * @param tableKinds      {@link  org.apache.flink.table.catalog.CatalogBaseTable.TableKind}
     * @return A set of {@link TableInfo}
     */
    Set<TableInfo> listTables(String clusterId, String sessionHandleId,
                              String catalog, String database,
                              Set<CatalogBaseTable.TableKind> tableKinds);

    /**
     * List system functions
     *
     * @param clusterId       Flink K8S session cluster id
     * @param sessionHandleId Session handler id
     * @return A set of {@link FunctionInfo}
     */
    Set<FunctionInfo> listSystemFunctions(String clusterId, String sessionHandleId);

    /**
     * List user defined functions
     *
     * @param clusterId       Flink K8S session cluster id
     * @param sessionHandleId Session handler id
     * @param catalog         Catalog name
     * @param database        Database name
     * @return A set of {@link FunctionInfo}
     */
    Set<FunctionInfo> listUserDefinedFunctions(String clusterId, String sessionHandleId, String catalog, String database);

    /**
     * Close a session
     *
     * @param clusterId       Flink K8S session cluster id
     * @param sessionHandleId Session handler id
     * @return Session handle id
     */
    String closeSession(String clusterId, String sessionHandleId);

    /**
     * Execute a sql
     *
     * @param clusterId       Flink K8S session cluster id
     * @param sessionHandleId Session handler id
     * @param params          Sql query params
     * @return Operation handle id {@link org.apache.flink.table.gateway.api.operation.OperationHandle}
     */
    String executeSql(String clusterId, String sessionHandleId, WsFlinkSqlGatewayQueryParamsDTO params);

    /**
     * Execute a sql
     *
     * @param clusterId         Flink K8S session cluster id
     * @param sessionHandleId   Session handler id
     * @param operationHandleId Operation handle id
     * @param token             token
     * @param maxRows           Max rows to fetch
     * @return Operation handle id {@link org.apache.flink.table.gateway.api.results.ResultSet}
     */
    ResultSet fetchResults(String clusterId, String sessionHandleId,
                           String operationHandleId,
                           Long token, int maxRows);

    /**
     * Cancel running jobs
     *
     * @param clusterId         Flink K8S session cluster id
     * @param sessionHandleId   Session handler id
     * @param operationHandleId Operation handle id
     * @return Success or not
     */
    Boolean cancel(String clusterId, String sessionHandleId, String operationHandleId);
}
