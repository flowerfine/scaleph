package cn.sliew.scaleph.plugin.seatunnel.flink.connector.jdbc.sink;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.plugin.framework.core.AbstractPlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyContext;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.ConnectorType;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeatunnelNativeFlinkConnector;
import cn.sliew.scaleph.plugin.seatunnel.flink.common.CommonOptions;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connector.jdbc.JdbcProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connector.jdbc.sink.JdbcSinkProperties.BATCH_SIZE;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connector.jdbc.sink.JdbcSinkProperties.QUERY;

public class JdbcSinkConnector extends AbstractPlugin implements SeatunnelNativeFlinkConnector {

    private static final List<PropertyDescriptor> supportedProperties;

    static {
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(URL);
        props.add(DRIVER);
        props.add(USERNAME);
        props.add(PASSWORD);
        props.add(QUERY);
        props.add(BATCH_SIZE);
        props.add(PARALLELISM);

        props.add(CommonOptions.SOURCE_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    private final PluginInfo pluginInfo;

    public JdbcSinkConnector() {
        this.pluginInfo = new PluginInfo("JdbcSink", "jdbc sink connector", "2.1.1", JdbcSinkConnector.class.getName());
    }

    @Override
    public ObjectNode create() {
        ObjectNode objectNode = JacksonUtil.createObjectNode();
        PropertyContext propertyContext = PropertyContext.fromProperties(properties);
        for (PropertyDescriptor descriptor : supportedProperties) {
            if (propertyContext.contains(descriptor)) {
                objectNode.put(descriptor.getName(), propertyContext.getValue(descriptor));
            }
        }
        return objectNode;
    }

    @Override
    public ConnectorType getConnectorType() {
        return ConnectorType.SINK;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return pluginInfo;
    }

    @Override
    public List<PropertyDescriptor> getSupportedProperties() {
        return supportedProperties;
    }

    @Override
    public List<PropertyDescriptor> additionalResources() {
        return Collections.emptyList();
    }
}
