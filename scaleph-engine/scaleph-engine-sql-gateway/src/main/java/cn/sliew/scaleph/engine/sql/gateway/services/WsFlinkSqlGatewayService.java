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
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.flink.table.gateway.api.results.GatewayInfo;
import org.apache.flink.table.gateway.api.results.ResultSet;

import cn.sliew.scaleph.engine.sql.gateway.internal.ScalephCatalogManager;
import cn.sliew.scaleph.engine.sql.gateway.services.dto.catalog.CatalogInfo;
import cn.sliew.scaleph.engine.sql.gateway.services.param.WsFlinkSqlGatewayQueryParam;

public interface WsFlinkSqlGatewayService {

    /**
     * Get a {@link ScalephCatalogManager}.
     *
     * @param clusterId Flink K8S session cluster id
     * @return Optional {@link ScalephCatalogManager}
     */
    Optional<ScalephCatalogManager> getCatalogManager(String clusterId);

    /**
     * Create a {@link ScalephCatalogManager}.
     *
     * @param clusterCredentialId K8S cluster credential id
     * @param sessionClusterId    Flink K8S session cluster id
     * @return Optional {@link ScalephCatalogManager}
     */
    Optional<ScalephCatalogManager> createCatalogManager(Long clusterCredentialId, String sessionClusterId);

    /**
     * Destroy a {@link ScalephCatalogManager} by cluster id
     *
     * @param clusterId Flink K8S session cluster id
     */
    void destroyCatalogManager(String clusterId);

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
     * List catalogs
     *
     * @param clusterId              Flink K8S session cluster id
     * @param includeSystemFunctions Whether show system function of catalog
     * @return Set of catalog informations
     */
    Set<CatalogInfo> getCatalogInfo(String clusterId, boolean includeSystemFunctions);

    /**
     * Execute a sql
     *
     * @param clusterId Flink K8S session cluster id
     * @param params    Sql query params
     * @return Operation handle id {@link org.apache.flink.table.gateway.api.operation.OperationHandle}
     */
    String executeSql(String clusterId, WsFlinkSqlGatewayQueryParam params);

    /**
     * Fetch sql result
     *
     * @param clusterId         Flink K8S session cluster id
     * @param operationHandleId Operation handle id
     * @param token             token
     * @param maxRows           Max rows to fetch
     * @return Operation handle id {@link org.apache.flink.table.gateway.api.results.ResultSet}
     */
    ResultSet fetchResults(String clusterId, String operationHandleId, Long token, int maxRows);

    /**
     * Cancel running jobs
     *
     * @param clusterId         Flink K8S session cluster id
     * @param operationHandleId Operation handle id
     * @return Success or not
     */
    Boolean cancel(String clusterId, String operationHandleId);

    /**
     * Complete sql statement
     *
     * @param clusterId Flink K8S session cluster id
     * @param statement Sql statement
     * @param position  Position of the sql statement
     * @return A list of strings
     * @throws Exception
     */
    List<String> completeStatement(String clusterId, String statement, int position) throws Exception;

    /**
     * Validate sql statement
     *
     * @param clusterId
     * @param statement
     * @throws Exception
     */
    //    void validStatement(String clusterId, String statement) throws Exception;

    /**
     * Add dependency jars to the sql-gateway
     *
     * @param clusterId Flink K8S session cluster id
     * @param jarIdList List of jar ids
     * @return true if success
     */
    Boolean addDependencies(String clusterId, List<Long> jarIdList);

    /**
     * Add a catalog
     *
     * @param clusterId   Flink K8S session cluster id
     * @param catalogName Catalog name
     * @param options     Catalog options
     * @return true if success
     */
    Boolean addCatalog(String clusterId, String catalogName, Map<String, String> options);

    /**
     * Remove a catalog
     *
     * @param clusterId   Flink K8S session cluster id
     * @param catalogName Catalog name
     * @return
     */
    Boolean removeCatalog(String clusterId, String catalogName);
}
