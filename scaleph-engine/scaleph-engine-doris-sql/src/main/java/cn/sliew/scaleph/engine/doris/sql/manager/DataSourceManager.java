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
 *
 */

package cn.sliew.scaleph.engine.doris.sql.manager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Slf4j
@Component
public class DataSourceManager {

    private final Map<Long, HikariDataSource> dataSourceMap;

    public DataSourceManager() {
        this.dataSourceMap = new ConcurrentHashMap<>();
    }

    public boolean hasCluster(Long clusterCredentialId) {
        return dataSourceMap.containsKey(clusterCredentialId);
    }

    public Connection getConnection(Long clusterCredentialId) {
        if (hasCluster(clusterCredentialId)) {
            HikariDataSource dataSource = dataSourceMap.get(clusterCredentialId);
            try {
                return dataSource.getConnection();
            } catch (SQLException e) {
                throw new IllegalArgumentException("Error get connection, sql state = " + e.getSQLState(), e);
            }
        }
        throw new IllegalArgumentException("Datasource of cluster id " + clusterCredentialId + " not exists!");
    }

    public boolean addDataSource(Long clusterCredentialId, HikariConfig hikariConfig, boolean overwrite) {
        if (hasCluster(clusterCredentialId) && !overwrite) {
            log.info("Cluster datasource already exists!");
            return false;
        }
        // Destroy exist data source
        destroyDataSource(clusterCredentialId);
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        dataSourceMap.put(clusterCredentialId, dataSource);
        return true;
    }

    public void destroyDataSource(Long clusterCredentialId) {
        if (!hasCluster(clusterCredentialId)) {
            return;
        }
        HikariDataSource removedDataSource = dataSourceMap.remove(clusterCredentialId);
        removedDataSource.close();
    }

    public <T> T actionWithConnection(Long clusterCredentialId, Function<Connection, T> action) {
        try (Connection connection = getConnection(clusterCredentialId)) {
            return action.apply(connection);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
