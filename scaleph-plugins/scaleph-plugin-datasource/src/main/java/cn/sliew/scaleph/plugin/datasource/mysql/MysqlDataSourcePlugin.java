package cn.sliew.scaleph.plugin.datasource.mysql;

import cn.sliew.scaleph.plugin.datasource.jdbc.JDBCDataSourcePlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.*;

public class MysqlDataSourcePlugin extends JDBCDataSourcePlugin {

    private static final List<PropertyDescriptor> supportedProperties;

    static {
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(HOST);
        props.add(PORT);
        props.add(DATABASE_NAME);
        props.add(USERNAME);
        props.add(PASSWORD);
        supportedProperties = Collections.unmodifiableList(props);
    }

    public MysqlDataSourcePlugin() {
        PluginInfo info = new PluginInfo("Mysql", "Mysql Jdbc Datasource", "8.0.25", MysqlDataSourcePlugin.class.getName());
        this.setPluginInfo(info);
    }

    @Override
    protected String getJdbcUrl() {
        return "jdbc:mysql://" + properties.get(HOST) + ":" + properties.get(PORT) + "/" + properties.get(DATABASE_NAME) + "?" + getAdditionalProps();
    }

    @Override
    protected String getDriverClassNmae() {
        return "com.mysql.cj.jdbc.Driver";
    }
}
