package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.clickhosue.source;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.clickhosue.source.ClickHouseSourceProperties.*;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.google.auto.service.AutoService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AutoService(SeaTunnelConnectorPlugin.class)
public class ClickHouseSourcePlugin extends SeaTunnelConnectorPlugin {

    public ClickHouseSourcePlugin() {
        this.pluginInfo = new PluginInfo(getPluginName().getLabel(),
            "Jdbc Source Plugin , input records from jdbc connection.",
            ClickHouseSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(HOST);
        props.add(DATABASE);
        props.add(SQL);
        props.add(USERNAME);
        props.add(PASSWORD);
        props.add(CommonProperties.FIELD_NAME);
        props.add(CommonProperties.RESULT_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SOURCE_CLICKHOUSE;
    }

}
