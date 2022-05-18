package cn.sliew.scaleph.plugin.datasource.mysql;

import cn.sliew.scaleph.plugin.datasource.DataSourcePlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.Property;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.ValidationResult;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.*;

public class JDBCDataSourcePlugin implements DataSourcePlugin {

    private static final String PLUGIN_NAME = "MySQLDataSource";
    private static final String PLUGIN_DESCRIPTION = "MySQL DataSource";
    private static final String PLUGIN_VERSION = "1.0.0";

    public static final PropertyDescriptor JDBC_URL = new PropertyDescriptor.Builder()
            .name("HikariCP-jdbc-url")
            .description("database connection url")
            .defaultValue(null)
            .properties(Property.Required)
            .build();
    public static final PropertyDescriptor DRIVER_NAME = new PropertyDescriptor.Builder()
            .name("HikariCP-driver-classname")
            .description("fully-qualified class name of the JDBC driver. Example: com.mysql.cj.jdbc.Driver")
            .defaultValue(null)
            .properties(Property.Required)
            .build();
    public static final PropertyDescriptor USERNAME = new PropertyDescriptor.Builder()
            .name("HikariCP-username")
            .description("database username")
            .defaultValue(null)
            .properties(Property.Required)
            .build();
    public static final PropertyDescriptor PASSWORD = new PropertyDescriptor.Builder()
            .name("HikariCP-password")
            .description("password for the database user")
            .defaultValue(null)
            .properties(Property.Required, Property.Sensitive)
            .build();

    private static final List<PropertyDescriptor> properties;

    static {
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(JDBC_URL);
        props.add(DRIVER_NAME);
        props.add(USERNAME);
        props.add(PASSWORD);
        properties = Collections.unmodifiableList(props);
    }

    private final PluginInfo pluginInfo;
    private volatile HikariDataSource dataSource;

    public JDBCDataSourcePlugin() {
        this.pluginInfo = new PluginInfo(PLUGIN_NAME, PLUGIN_DESCRIPTION, PLUGIN_VERSION, this.getClass().getName());
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return pluginInfo;
    }

    @Override
    public PropertyDescriptor getPropertyDescriptor(String name) {
        return null;
    }

    @Override
    public List<PropertyDescriptor> getSupportedProperties() {
        return properties;
    }

    @Override
    public Collection<ValidationResult> validate(Properties properties) {
        return null;
    }
}
