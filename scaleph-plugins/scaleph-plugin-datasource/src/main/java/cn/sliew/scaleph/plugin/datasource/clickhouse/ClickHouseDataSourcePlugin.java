package cn.sliew.scaleph.plugin.datasource.clickhouse;

import static cn.sliew.scaleph.plugin.datasource.clickhouse.ClickhouseProperties.DATABASE;
import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.DATABASE_NAME;
import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.DRIVER_CLASS_NAME_UNREQUIRED;
import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.HOST;
import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.JDBC_URL_UNREQUIRED;
import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.PASSWORD;
import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.USERNAME;

import cn.sliew.scaleph.common.enums.DataSourceTypeEnum;
import cn.sliew.scaleph.plugin.datasource.jdbc.JDBCDataSourcePlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyContext;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClickHouseDataSourcePlugin extends JDBCDataSourcePlugin {

    public ClickHouseDataSourcePlugin() {
        this.pluginInfo = new PluginInfo(DataSourceTypeEnum.CLICKHOUSE.getValue(), "ClickHouse Jdbc Datasource", "0.2", ClickHouseDataSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(HOST);
        props.add(DATABASE_NAME);
        props.add(USERNAME);
        props.add(PASSWORD);
        props.add(JDBC_URL_UNREQUIRED);
        props.add(DRIVER_CLASS_NAME_UNREQUIRED);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public void configure(PropertyContext props) {
        super.configure(props);
        properties.set(DATABASE, properties.getString(DATABASE_NAME));
        properties.set(JDBC_URL_UNREQUIRED, getJdbcUrl());
        properties.set(DRIVER_CLASS_NAME_UNREQUIRED, getDriverClassNmae());
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:clickhouse://" + properties.getString(HOST)+ "/" + properties.getString(DATABASE_NAME) + "?" + getAdditionalProps();
    }

    @Override
    public String getDriverClassNmae() {
        return "ru.yandex.clickhouse.ClickHouseDriver";
    }

}
