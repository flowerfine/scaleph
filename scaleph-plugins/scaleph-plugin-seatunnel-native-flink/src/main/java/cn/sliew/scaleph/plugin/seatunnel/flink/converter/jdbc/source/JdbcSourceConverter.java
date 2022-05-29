package cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.JdbcProperties.DRIVER;
import static cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.JdbcProperties.PARALLELISM;
import static cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.JdbcProperties.PASSWORD;
import static cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.JdbcProperties.URL;
import static cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.JdbcProperties.USERNAME;
import static cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.source.JdbcSourceProperties.FETCH_SIZE;
import static cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.source.JdbcSourceProperties.PARTITION_COLUMN;
import static cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.source.JdbcSourceProperties.PARTITION_LOWER_BOUND;
import static cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.source.JdbcSourceProperties.PARTITION_UPPER_BOUND;
import static cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.source.JdbcSourceProperties.QUERY;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.plugin.framework.property.PropertyContext;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.converter.SourceConverter;
import cn.sliew.scaleph.plugin.seatunnel.flink.converter.common.CommonOptions;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JdbcSourceConverter implements SourceConverter {

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

    @Override
    public String getPluginName() {
        return "JdbcSource";
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
