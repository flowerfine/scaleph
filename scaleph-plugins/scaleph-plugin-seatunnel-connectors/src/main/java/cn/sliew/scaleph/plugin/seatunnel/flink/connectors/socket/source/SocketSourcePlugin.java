package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.socket.source;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.seatunnel.flink.connectors.socket.SocketProperties;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AutoService(SeaTunnelConnectorPlugin.class)
public class SocketSourcePlugin extends SeaTunnelConnectorPlugin {

    public SocketSourcePlugin() {
        this.pluginInfo = new PluginInfo(getPluginName().getLabel(),
                "Socket Source Plugin,Used to read data from Socket",
                SocketSourcePlugin.class.getName());
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(SocketProperties.HOST);
        props.add(SocketProperties.PORT);
        props.add(CommonProperties.FIELD_NAME);
        props.add(CommonProperties.RESULT_TABLE_NAME);
        this.supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SOURCE_SOCKET;
    }
}
