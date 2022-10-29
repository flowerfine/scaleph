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

package cn.sliew.scaleph.plugin.datasource.jdbc;

import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.scaleph.common.enums.DataSourceTypeEnum;
import cn.sliew.scaleph.plugin.datasource.DatasourcePlugin;
import cn.sliew.scaleph.plugin.datasource.util.JdbcUtil;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.PropertiesUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.*;

@Slf4j
@AutoService(DatasourcePlugin.class)
public class JDBCDataSourcePlugin extends DatasourcePlugin<Connection> {

    protected volatile Connection connection;

    public JDBCDataSourcePlugin() {
        this.pluginInfo = new PluginInfo(DataSourceTypeEnum.JDBC.getValue(), "Generic Jdbc DataSource", JDBCDataSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(JDBC_URL);
        props.add(DRIVER_CLASS_NAME);
        props.add(USERNAME);
        props.add(PASSWORD);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public void start() {
        if (!Optional.ofNullable(properties).isPresent()) {
            throw new IllegalStateException("jdbc datasource plugin not initialized!");
        }
        final Properties jdbcProperties = new Properties();
        properties.addAllToProperties(jdbcProperties);
        String jdbcUrl = getJdbcUrl();
        String driver = getDriverClassName();
        String userName = jdbcProperties.getProperty(USERNAME.getName());
        String password = jdbcProperties.getProperty(PASSWORD.getName());
        try {
            Class.forName(driver);
            this.connection = DriverManager.getConnection(jdbcUrl, userName, password);
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
        return this.connection;
    }

    @Override
    public boolean testConnection() {
        return this.connection != null;
    }

    public String getJdbcUrl() {
        return properties.get(JDBC_URL);
    }

    public String getDriverClassName() {
        return properties.get(DRIVER_CLASS_NAME);
    }

    public String getUsername() {
        return properties.get(USERNAME);
    }

    public String getPassword() {
        return properties.get(PASSWORD);
    }

    protected String getAdditionalProps() {
        return JdbcUtil.formatAdditionalProps(additionalProperties);
    }

}
