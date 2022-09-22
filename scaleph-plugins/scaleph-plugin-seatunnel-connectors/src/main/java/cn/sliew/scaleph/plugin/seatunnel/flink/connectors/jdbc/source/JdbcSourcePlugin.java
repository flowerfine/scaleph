package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.jdbc.source;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelPluginMapping;
import com.google.auto.service.AutoService;

@AutoService(SeaTunnelConnectorPlugin.class)
public class JdbcSourcePlugin extends SeaTunnelConnectorPlugin {

    public JdbcSourcePlugin() {
        this.pluginInfo = new PluginInfo(getPluginName().getLabel(),
                "Jdbc Source Plugin , input records from jdbc connection.",
                JdbcSourcePlugin.class.getName());
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SOURCE_JDBC;
    }

}
