package cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.sink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.JdbcProperties.DRIVER;
import static cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.JdbcProperties.PARALLELISM;
import static cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.JdbcProperties.PASSWORD;
import static cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.JdbcProperties.URL;
import static cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.JdbcProperties.USERNAME;
import static cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.sink.JdbcSinkProperties.BATCH_SIZE;
import static cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.sink.JdbcSinkProperties.QUERY;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.plugin.framework.property.PropertyContext;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.converter.SinkConverter;
import cn.sliew.scaleph.plugin.seatunnel.flink.converter.common.CommonOptions;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JdbcSinkConverter implements SinkConverter {

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

    @Override
    public String getPluginName() {
        return "JdbcSink";
    }

    @Override
    public ObjectNode create(Properties properties) {
        ObjectNode objectNode = JacksonUtil.createObjectNode();
        PropertyContext propertyContext = PropertyContext.fromProperties(properties);
        for (PropertyDescriptor descriptor : supportedProperties) {
            if (propertyContext.contains(descriptor)) {
                objectNode.put(descriptor.getName(), propertyContext.getValue(descriptor));
            }
        }
        return objectNode;
    }
}
