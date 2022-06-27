package cn.sliew.scaleph.plugin.datasource.oracle;

import cn.sliew.scaleph.common.enums.DataSourceTypeEnum;
import cn.sliew.scaleph.plugin.datasource.jdbc.JDBCDataSourcePlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyContext;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.*;

public class OracleDataSourcePlugin extends JDBCDataSourcePlugin {

    public OracleDataSourcePlugin() {
        this.pluginInfo = new PluginInfo(DataSourceTypeEnum.ORACLE.getValue(), "Oracle Jdbc Datasource", "11.2.0.4", OracleDataSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(HOST);
        props.add(PORT);
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
        properties.set(JDBC_URL_UNREQUIRED, getJdbcUrl());
        properties.set(DRIVER_CLASS_NAME_UNREQUIRED, getDriverClassNmae());
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:oracle:thin:@//" + properties.get(HOST) + ":" + String.valueOf(properties.get(PORT)) + "/" + properties.get(DATABASE_NAME);
    }

    @Override
    public String getDriverClassNmae() {
        return "oracle.jdbc.driver.OracleDriver";
    }


}
