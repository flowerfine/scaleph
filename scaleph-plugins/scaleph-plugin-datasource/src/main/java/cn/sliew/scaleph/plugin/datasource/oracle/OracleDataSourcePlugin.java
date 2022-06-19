package cn.sliew.scaleph.plugin.datasource.oracle;

import cn.sliew.scaleph.plugin.datasource.jdbc.JDBCDataSourcePlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.datasource.jdbc.JdbcPoolProperties.*;

public class OracleDataSourcePlugin extends JDBCDataSourcePlugin {
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

    public OracleDataSourcePlugin() {
        PluginInfo info = new PluginInfo("Oracle", "Oracle Jdbc Datasource", "11.2.0.4", OracleDataSourcePlugin.class.getName());
        this.setPluginInfo(info);
    }

    @Override
    protected String getJdbcUrl() {
        return "jdbc:oracle:thin:@//" + properties.get(HOST) + ":" + properties.get(PORT) + "/" + properties.get(DATABASE_NAME);
    }

    @Override
    protected String getDriverClassNmae() {
        return "oracle.jdbc.driver.OracleDriver";
    }
}
