package cn.sliew.scaleph.plugin.seatunnel.flink.connector.jdbc.source;

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
import java.util.Properties;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connector.jdbc.JdbcProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connector.jdbc.source.JdbcSourceProperties.*;

public class JdbcSourceConnector extends AbstractPlugin implements SeatunnelNativeFlinkConnector {

    private static final List<PropertyDescriptor> supportedProperties;

    static {
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(URL);
        props.add(DRIVER);
        props.add(USERNAME);
        props.add(PASSWORD);
        props.add(QUERY);
        props.add(FETCH_SIZE);
        props.add(PARALLELISM);
        props.add(PARTITION_COLUMN);
        props.add(PARTITION_UPPER_BOUND);
        props.add(PARTITION_LOWER_BOUND);

        props.add(CommonOptions.RESULT_TABLE_NAME);
        props.add(CommonOptions.FIELD_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    private final Properties properties;
    private final PluginInfo pluginInfo;

    public JdbcSourceConnector(Properties properties) {
        this.properties = properties;
        this.pluginInfo = new PluginInfo("JdbcSource", "jdbc source connector", "2.1.1", JdbcSourceConnector.class.getName());
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
        return ConnectorType.SOURCE;
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
