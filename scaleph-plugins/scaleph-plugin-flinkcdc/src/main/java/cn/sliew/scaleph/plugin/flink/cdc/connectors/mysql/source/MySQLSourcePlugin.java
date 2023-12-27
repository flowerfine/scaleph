package cn.sliew.scaleph.plugin.flink.cdc.connectors.mysql.source;

import cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCPluginMapping;
import cn.sliew.scaleph.plugin.flink.cdc.FlinkCDCPipilineConnectorPlugin;
import cn.sliew.scaleph.plugin.flink.cdc.connectors.CommonProperties;
import cn.sliew.scaleph.plugin.flink.cdc.transform.RoutePlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AutoService(FlinkCDCPipilineConnectorPlugin.class)
public class MySQLSourcePlugin extends FlinkCDCPipilineConnectorPlugin {

    public MySQLSourcePlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "mysql",
                RoutePlugin.class.getName());
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(CommonProperties.NAME);
        props.add(CommonProperties.TYPE);
        props.add(CommonProperties.DESCRIPTION);
        this.supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected FlinkCDCPluginMapping getPluginMapping() {
        return FlinkCDCPluginMapping.SOURCE_MYSQL;
    }
}
