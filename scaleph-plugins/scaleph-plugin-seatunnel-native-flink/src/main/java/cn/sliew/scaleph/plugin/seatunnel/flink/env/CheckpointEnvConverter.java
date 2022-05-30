package cn.sliew.scaleph.plugin.seatunnel.flink.env;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.plugin.framework.property.PropertyContext;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.converter.EnvConverter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static cn.sliew.scaleph.plugin.seatunnel.flink.common.CheckpointProperties.*;

public class CheckpointEnvConverter implements EnvConverter {

    private static final List<PropertyDescriptor> supportedProperties;

    static {
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(CHECKPOINT_INTERVAL);
        props.add(CHECKPOINT_MODE);
        props.add(CHECKPOINT_DATA_URI);
        props.add(CHECKPOINT_TIMEOUT);
        props.add(MAX_CONCURRENT_CHECKPOINTS);
        props.add(CHECKPOINT_CLEANUP_MODE);
        props.add(MIN_PAUSE_BETWEEN_CHECKPOINTS);
        props.add(FAIL_ON_CHECKPOINTING_ERRORS);
        supportedProperties = Collections.unmodifiableList(props);
    }

    private final Properties properties;

    public CheckpointEnvConverter(Properties properties) {
        this.properties = properties;
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
}
