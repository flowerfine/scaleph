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
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.plugin.datasource.DatasourcePlugin;
import cn.sliew.scaleph.plugin.framework.core.AbstractPlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyContext;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.ValidationResult;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.MeterRegistry;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.*;

import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.*;

public class JDBCDataSourcePlugin extends AbstractPlugin implements DatasourcePlugin<DataSource> {

    private static final List<PropertyDescriptor> supportedProperties;

    static {
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(JDBC_URL);
        props.add(DRIVER_CLASS_NAME);
        props.add(USERNAME);
        props.add(PASSWORD);
        props.add(MININUM_IDLE);
        props.add(MAXIMUM_POOL_SIZE);
        props.add(IDLE_TIMEOUT);
        props.add(VALIDATION_QUERY);
        supportedProperties = Collections.unmodifiableList(props);
    }

    private final PluginInfo pluginInfo;
    private MeterRegistry meterRegistry;
    private volatile Properties additionalProperties;
    private volatile HikariDataSource dataSource;

    public JDBCDataSourcePlugin() {
        PluginInfo pluginInfo = null;
        try (InputStream resourceAsStream = JDBCDataSourcePlugin.class.getResourceAsStream(
                "/" + PluginInfo.PLUGIN_PROPERTIES)) {
            Properties pluginProperties = new Properties();
            pluginProperties.load(resourceAsStream);
            pluginInfo = PluginInfo.readFromProperties(pluginProperties);
        } catch (Exception e) {
            Rethrower.throwAs(e);
        }
        this.pluginInfo = pluginInfo;
    }

    @Override
    public void configure(PropertyContext properties) {
        super.configure(properties);

        final Collection<ValidationResult> validate = validate(properties);
        final Optional<ValidationResult> validationResult =
                validate.stream().filter(result -> result.isValid() == false).findAny();
        if (validationResult.isPresent()) {
            throw new IllegalArgumentException(JacksonUtil.toJsonString(validationResult.get()));
        }
    }

    @Override
    public void start() {
        if (Optional.ofNullable(properties).isPresent() == false) {
            throw new IllegalStateException("jdbc datasource plugin not initialized!");
        }
        final Properties jdbcProperties = new Properties();
        properties.addAllToProperties(jdbcProperties);
        HikariConfig config = new HikariConfig(jdbcProperties);
        config.setDataSourceProperties(additionalProperties);
        config.setMetricRegistry(meterRegistry);
        this.dataSource = new HikariDataSource(config);
    }

    @Override
    public void shutdown() {
        dataSource.close();
    }

    @Override
    public DataSource getDatasource() {
        return dataSource;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return pluginInfo;
    }

    @Override
    public List<PropertyDescriptor> getSupportedProperties() {
        return supportedProperties;
    }

    @Override
    public void setAdditionalProperties(Properties properties) {
        this.additionalProperties = properties;
    }

    @Override
    public void setMeterRegistry(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
}
