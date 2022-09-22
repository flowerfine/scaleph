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

package cn.sliew.scaleph.plugin.datasource.druid;

import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.scaleph.common.enums.DataSourceTypeEnum;
import cn.sliew.scaleph.plugin.datasource.DatasourcePlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import static cn.sliew.scaleph.plugin.datasource.druid.DruidProperties.JDBC_URL;

@Slf4j
@AutoService(DatasourcePlugin.class)
public class DruidJdbcDataSourcePlugin extends DatasourcePlugin<Connection> {

    private Connection connection;

    public DruidJdbcDataSourcePlugin() {
        this.pluginInfo = new PluginInfo(DataSourceTypeEnum.DRUID.getValue(), "Druid JDBC Datasource",  DruidJdbcDataSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(JDBC_URL);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public void start() {
        if (!Optional.ofNullable(properties).isPresent()) {
            throw new IllegalStateException("druid datasource plugin not initialized!");
        }
        String jdbcUrl = properties.getString(JDBC_URL);
        final Properties jdbcProperties = new Properties();
        properties.addAllToProperties(jdbcProperties);
        try {
            Class.forName("org.apache.calcite.avatica.remote.Driver");
            this.connection = DriverManager.getConnection(jdbcUrl, jdbcProperties);
        } catch (ClassNotFoundException | SQLException e) {
            Rethrower.throwAs(e);
        }
    }

    @Override
    public void shutdown() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public Connection getDatasource() {
        return connection;
    }

    @Override
    public boolean testConnection() {
        return connection != null;
    }
}
