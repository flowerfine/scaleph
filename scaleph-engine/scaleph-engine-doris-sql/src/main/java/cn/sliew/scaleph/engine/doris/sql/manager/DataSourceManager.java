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

import cn.sliew.scaleph.engine.doris.service.DorisClusterEndpointService;
import cn.sliew.scaleph.engine.doris.service.dto.DorisClusterFeEndpoint;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.mysql.cj.jdbc.Driver;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;

@Slf4j
@Component
public class DataSourceManager {

    private final DorisClusterEndpointService endpointService;

    private final LoadingCache<Long, HikariDataSource> dataSources;

    @Autowired
    public DataSourceManager(DorisClusterEndpointService endpointService) {
        this.endpointService = endpointService;
        this.dataSources = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(Duration.ofMinutes(15))
                .evictionListener((RemovalListener<Long, HikariDataSource>) (key, value, cause) -> {
                    if (!value.isClosed()) {
                        value.close();
                    }
                })
                .build(this::getDataSource);
    }

    public Connection getConnection(Long dorisInstanceId) {
        HikariDataSource dataSource = dataSources.get(dorisInstanceId);
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new IllegalArgumentException("Error get connection, sql state = " + e.getSQLState(), e);
        }
    }

    public HikariDataSource getDataSource(Long dorisInstanceId) {
        DorisClusterFeEndpoint feEndpoint = endpointService.getFEEndpoint(dorisInstanceId);
        if (feEndpoint == null) {
            return null;
        } else {
            URI query = feEndpoint.getQuery();
            String host = query.getHost();
            int port = query.getPort();
            String jdbcUrl = String.format("jdbc:mysql://%s:%d", host, port);
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(jdbcUrl);
            hikariConfig.setDriverClassName(Driver.class.getName());
            hikariConfig.setUsername("root");
            // hikariConfig.setPassword();
            return new HikariDataSource(hikariConfig);
        }
    }


    public <T> T actionWithConnection(Long dorisInstanceId, JdbcConnectionFunction<T> action) {
        try (Connection connection = getConnection(dorisInstanceId)) {
            return action.apply(connection);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @FunctionalInterface
    public interface JdbcConnectionFunction<T> {
        public T apply(Connection connection) throws SQLException;
    }

}
