package cn.sliew.scaleph.plugin.seatunnel.flink.jdbc;

import cn.sliew.scaleph.plugin.framework.core.AbstractPlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyContext;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeatunnelNativeFlinkConnector;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static cn.sliew.scaleph.plugin.seatunnel.flink.jdbc.JdbcProperties.*;

public class JdbcSource extends AbstractPlugin implements SeatunnelNativeFlinkConnector {

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
        supportedProperties = Collections.unmodifiableList(props);
    }

    private PropertyContext propertyContext;

    @Override
    public void initialize(Properties properties) {
        this.propertyContext = PropertyContext.fromProperties(properties);
    }

    @Override
    public ObjectNode appendOptions(ObjectNode conf) {
        for (PropertyDescriptor descriptor : getSupportedProperties()) {
            final String name = descriptor.getName();
            final String value = propertyContext.getString(descriptor);
            conf.put(name, value);
        }
        return conf;
    }

    @Override
    public List<PropertyDescriptor> additionalResources() {
        return null;
    }

    @Override
    public PluginInfo getPluginInfo() {
        return null;
    }

    @Override
    public List<PropertyDescriptor> getSupportedProperties() {
        return supportedProperties;
    }
}
