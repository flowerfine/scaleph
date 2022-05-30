package cn.sliew.scaleph.plugin.seatunnel.flink;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.core.PluginSPILoader;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SeatunnelNativeFlinkConnectorManager {

    private PluginSPILoader<SeatunnelNativeFlinkConnector> pluginPluginSPILoader = new PluginSPILoader<>(SeatunnelNativeFlinkConnector.class, SeatunnelNativeFlinkConnector.class.getClassLoader());

    public List<PropertyDescriptor> getSupportedEnvProperties() {
        return Collections.emptyList();
    }

    public Set<PluginInfo> getAvailableConnectors(ConnectorType connectorType) {
        return pluginPluginSPILoader.getServices().values().stream()
                .filter(connector -> connector.getConnectorType().equals(connectorType))
                .map(SeatunnelNativeFlinkConnector::getPluginInfo)
                .collect(Collectors.toSet());
    }

    public List<PropertyDescriptor> getSupportedProperties(PluginInfo pluginInfo) {
        final Optional<SeatunnelNativeFlinkConnector> optional = pluginPluginSPILoader.getPlugin(pluginInfo);
        final SeatunnelNativeFlinkConnector connector = optional.orElseThrow(() -> new IllegalStateException("unknown plugin info for " + pluginInfo));
        return connector.getSupportedProperties();
    }
}
