package cn.sliew.scaleph.engine.seatunnel.service;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.ConnectorType;

import java.util.List;
import java.util.Set;

/**
 * todo connector crud
 */
public interface SeatunnelConnectorService {

    List<PropertyDescriptor> getSupportedEnvProperties();

    Set<PluginInfo> getAvailableConnectors(ConnectorType type);

    List<PropertyDescriptor> getSupportedProperties(PluginInfo pluginInfo);

}
