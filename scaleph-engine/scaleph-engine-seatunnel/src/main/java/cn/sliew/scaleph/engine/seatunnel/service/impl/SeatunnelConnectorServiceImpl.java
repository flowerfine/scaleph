package cn.sliew.scaleph.engine.seatunnel.service.impl;

import cn.sliew.scaleph.engine.seatunnel.service.SeatunnelConnectorService;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.ConnectorType;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeatunnelNativeFlinkConnectorManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class SeatunnelConnectorServiceImpl implements SeatunnelConnectorService {

    private final SeatunnelNativeFlinkConnectorManager connectorManager = new SeatunnelNativeFlinkConnectorManager();

    @Override
    public List<PropertyDescriptor> getSupportedEnvProperties() {
        return connectorManager.getSupportedEnvProperties();
    }

    @Override
    public Set<PluginInfo> getAvailableConnectors(ConnectorType type) {
        return connectorManager.getAvailableConnectors(type);
    }

    @Override
    public List<PropertyDescriptor> getSupportedProperties(PluginInfo pluginInfo) {
        return connectorManager.getSupportedProperties(pluginInfo);
    }
}
