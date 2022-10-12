package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.kudu.source;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.kudu.KuduProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class KuduSourcePlugin extends SeaTunnelConnectorPlugin {

    public KuduSourcePlugin() {
        this.pluginInfo = new PluginInfo(getPluginName().getLabel(),
                "Used to read data from Kudu.",
                KuduSourcePlugin.class.getName());
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(KUDU_MASTER);
        props.add(KUDU_TABLE);
        props.add(COLUMNS_LIST);
        props.add(CommonProperties.RESULT_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SOURCE_KUDU;
    }
}
