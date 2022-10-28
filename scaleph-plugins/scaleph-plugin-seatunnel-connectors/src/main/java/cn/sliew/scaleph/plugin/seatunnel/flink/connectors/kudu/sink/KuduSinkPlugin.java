package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.kudu.sink;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.kudu.KuduProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class KuduSinkPlugin extends SeaTunnelConnectorPlugin {

    public KuduSinkPlugin() {
        this.pluginInfo = new PluginInfo(getPluginName().getLabel(),
                "Write data to Kudu.",
                KuduSinkPlugin.class.getName());
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(KUDU_MASTER);
        props.add(KUDU_TABLE);
        props.add(SAVE_MODE);
        props.add(CommonProperties.SOURCE_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SINK_KUDU;
    }
}
