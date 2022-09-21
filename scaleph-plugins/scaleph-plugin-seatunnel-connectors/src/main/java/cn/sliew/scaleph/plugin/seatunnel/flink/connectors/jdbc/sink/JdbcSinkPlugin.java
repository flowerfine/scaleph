package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.jdbc.sink;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelPluginMapping;
import com.google.auto.service.AutoService;

@AutoService(SeaTunnelConnectorPlugin.class)
public class JdbcSinkPlugin extends SeaTunnelConnectorPlugin {

    public JdbcSinkPlugin() {
        this.pluginInfo = new PluginInfo(getPluginName().getLabel(),
                "Jdbc Source Plugin , input records from jdbc connection.",
                JdbcSinkPlugin.class.getName());
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SINK_JDBC;
    }

}
