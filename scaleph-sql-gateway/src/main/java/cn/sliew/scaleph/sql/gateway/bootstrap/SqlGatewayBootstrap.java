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

package cn.sliew.scaleph.sql.gateway.bootstrap;

import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.table.gateway.api.SqlGatewayService;
import org.apache.flink.table.gateway.service.SqlGatewayServiceImpl;
import org.apache.flink.table.gateway.service.context.DefaultContext;
import org.apache.flink.table.gateway.service.session.SessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SqlGatewayBootstrap {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public SessionManager flinkSessionManager() {
        org.apache.flink.configuration.Configuration configuration = GlobalConfiguration.loadConfiguration();
        DefaultContext defaultContext = new DefaultContext(configuration, Collections.emptyList());
        return SessionManager.create(defaultContext);
    }

    @Bean
    public SqlGatewayService flinkSqlGatewayService(SessionManager sessionManager) {
        return new SqlGatewayServiceImpl(sessionManager);
    }
}
