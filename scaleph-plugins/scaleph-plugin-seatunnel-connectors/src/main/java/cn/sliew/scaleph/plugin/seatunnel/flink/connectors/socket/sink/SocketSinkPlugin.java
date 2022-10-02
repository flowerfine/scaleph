package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.socket.sink;

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
public class SocketSinkPlugin extends SeaTunnelConnectorPlugin {

    public SocketSinkPlugin() {
        this.pluginInfo = new PluginInfo(getPluginName().getLabel(),
                "Socket Sink Plugin,Used to send data to Socket Server. Both support streaming and batch mode.",
                SocketSinkPlugin.class.getName());
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(SocketProperties.HOST);
        props.add(SocketProperties.PORT);
        props.add(SocketProperties.MAX_RETRIES);
        props.add(CommonProperties.FIELD_NAME);
        props.add(CommonProperties.SOURCE_TABLE_NAME);
        this.supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SINK_SOCKET;
    }
}
