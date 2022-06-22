package cn.sliew.scaleph.plugin.datasource.postgre;

import cn.sliew.scaleph.common.enums.DataSourceTypeEnum;
import cn.sliew.scaleph.plugin.datasource.jdbc.JDBCDataSourcePlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyContext;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.datasource.postgre.PostgreDataSourceProperties.*;

public class PostgreDataSourcePlugin extends JDBCDataSourcePlugin {
    private static final List<PropertyDescriptor> supportedProperties;

    static {
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(HOST);
        props.add(PORT);
        props.add(DATABASE_NAME);
        props.add(USERNAME);
        props.add(PASSWORD);
        props.add(JDBC_URL);
        props.add(DRIVER_CLASS_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    public PostgreDataSourcePlugin() {
        PluginInfo info = new PluginInfo(DataSourceTypeEnum.POSTGRESQL.getValue(), "PostGre SQL Jdbc Datasource", "42.4.0", PostgreDataSourcePlugin.class.getName());
        this.setPluginInfo(info);
    }

    @Override
    public void configure(PropertyContext props) {
        super.configure(props);
        properties.set(JDBC_URL, getJdbcUrl());
        properties.set(DRIVER_CLASS_NAME, getDriverClassNmae());
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:postgresql://" + properties.get(HOST) + ":" + String.valueOf(properties.get(PORT)) + "/" + properties.get(DATABASE_NAME);
    }

    @Override
    public String getDriverClassNmae() {
        return "org.postgresql.Driver";
    }

    @Override
    public String getUsername() {
        return properties.get(USERNAME);
    }

    @Override
    public String getPassword() {
        return properties.get(PASSWORD);
    }

    @Override
    public List<PropertyDescriptor> getSupportedProperties() {
        return supportedProperties;
    }
}
