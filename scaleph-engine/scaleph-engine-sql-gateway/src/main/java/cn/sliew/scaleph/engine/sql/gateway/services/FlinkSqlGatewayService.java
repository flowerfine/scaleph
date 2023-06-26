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

import org.apache.flink.table.gateway.api.SqlGatewayService;
import org.apache.flink.table.gateway.api.results.GatewayInfo;

import java.util.Optional;

public interface FlinkSqlGatewayService {

    /**
     * Get a {@link SqlGatewayService} by given params.
     * <p>
     * If not exists, create a new one and store in memory.
     * </p>
     * <p>
     *
     * </p>
     *
     * @param clusterId Flink K8S session cluster id
     * @return an Optional instance of {@link SqlGatewayService}
     */
    Optional<SqlGatewayService> getOrCreateSqlGatewayService(String clusterId) throws Throwable;

    /**
     * Destroy a {@link SqlGatewayService} by cluster id
     *
     * @param clusterId Flink K8S session cluster id
     */
    void destroySqlGatewayService(String clusterId) throws Throwable;

    GatewayInfo getGatewayInfo(String clusterId);
}
