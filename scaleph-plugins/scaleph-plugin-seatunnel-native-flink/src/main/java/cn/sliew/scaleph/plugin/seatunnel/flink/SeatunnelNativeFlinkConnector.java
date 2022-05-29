package cn.sliew.scaleph.plugin.seatunnel.flink;

import java.util.List;

import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import com.fasterxml.jackson.databind.node.ObjectNode;

public interface SeatunnelNativeFlinkConnector {

    ObjectNode appendOptions(ObjectNode conf);

    /**
     * For example: flink-connector-jdbc requires mysql jdbc jar.
     * todo may we need a new ResourceDescriptor?
     */
    List<PropertyDescriptor> additionalResources();

}
