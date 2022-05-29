package cn.sliew.scaleph.plugin.seatunnel.flink;

import cn.sliew.scaleph.plugin.framework.core.Plugin;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.converter.ConfConverter;

import java.util.List;
import java.util.Properties;

public interface SeatunnelNativeFlinkConnector extends Plugin {

    ConfConverter getConverter(Properties properties);

    /**
     * For example: flink-connector-jdbc requires mysql jdbc jar.
     * todo may we need a new ResourceDescriptor?
     */
    List<PropertyDescriptor> additionalResources();

}
