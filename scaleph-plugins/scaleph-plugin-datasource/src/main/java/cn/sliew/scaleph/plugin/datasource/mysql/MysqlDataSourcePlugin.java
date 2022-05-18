package cn.sliew.scaleph.plugin.datasource.mysql;

import cn.sliew.scaleph.plugin.datasource.DataSourcePlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.ValidationResult;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class MysqlDataSourcePlugin implements DataSourcePlugin {

    private static final String PLUGIN_NAME = "MySQLDataSource";
    private static final String PLUGIN_DESCRIPTION = "MySQL DataSource";
    private static final String PLUGIN_VERSION = "1.0.0";

    private final PluginInfo pluginInfo;

    public MysqlDataSourcePlugin() {
        this.pluginInfo = new PluginInfo(PLUGIN_NAME, PLUGIN_DESCRIPTION, PLUGIN_VERSION, this.getClass().getName());
    }

    @Override
    public DataSource getDataSource() {
        return null;
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
        return null;
    }

    @Override
    public Collection<ValidationResult> validate(Properties properties) {
        return null;
    }
}
