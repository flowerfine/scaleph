package cn.sliew.scaleph.plugin.seatunnel.flink;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class SeatunnelNativeFlinkConnectorManagerTest {

    @Test
    void testLoadConnectorPlugins() throws Exception {
        SeatunnelNativeFlinkConnectorManager manager = new SeatunnelNativeFlinkConnectorManager();
        final Set<PluginInfo> sourceConnectors = manager.getAvailableConnectors(ConnectorType.SOURCE);
        assertThat(sourceConnectors).isNotEmpty();
    }
}
