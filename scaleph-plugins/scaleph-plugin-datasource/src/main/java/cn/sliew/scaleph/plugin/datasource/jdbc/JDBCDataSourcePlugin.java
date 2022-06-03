package cn.sliew.scaleph.plugin.datasource.jdbc;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.DRIVER_CLASS_NAME;
import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.IDLE_TIMEOUT;
import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.JDBC_URL;
import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.MAXIMUM_POOL_SIZE;
import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.MININUM_IDLE;
import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.PASSWORD;
import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.USERNAME;
import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.VALIDATION_QUERY;

import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.plugin.datasource.DatasourcePlugin;
import cn.sliew.scaleph.plugin.framework.core.AbstractPlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.ValidationResult;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.MeterRegistry;

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
    private volatile Properties properties;
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
    public void configure(Properties properties) {
        final Collection<ValidationResult> validate = validate(properties);
        final Optional<ValidationResult> validationResult =
            validate.stream().filter(result -> result.isValid() == false).findAny();
        if (validationResult.isPresent()) {
            throw new IllegalArgumentException(JacksonUtil.toJsonString(validationResult.get()));
        }
        this.properties = properties;
    }

    @Override
    public void start() {
        if (Optional.ofNullable(properties).isPresent() == false) {
            throw new IllegalStateException("jdbc datasource plugin not initialized!");
        }
        HikariConfig config = new HikariConfig(properties);
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
