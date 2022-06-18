package cn.sliew.scaleph.plugin.datasource.postgre;

import cn.sliew.scaleph.plugin.datasource.jdbc.JDBCDataSourcePlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.*;

public class PostgreDataSourcePlugin extends JDBCDataSourcePlugin {
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

    public PostgreDataSourcePlugin() {
        PluginInfo info = new PluginInfo("PostGreSQL", "PostGre SQL Jdbc Datasource", "42.4.0", PostgreDataSourcePlugin.class.getName());
        this.setPluginInfo(info);
    }

    @Override
    protected String getJdbcUrl() {
        return "jdbc:postgresql://" + properties.get(HOST) + ":" + properties.get(PORT) + "/" + properties.get(DATABASE_NAME);
    }

    @Override
    protected String getDriverClassNmae() {
        return "org.postgresql.Driver";
    }
}
