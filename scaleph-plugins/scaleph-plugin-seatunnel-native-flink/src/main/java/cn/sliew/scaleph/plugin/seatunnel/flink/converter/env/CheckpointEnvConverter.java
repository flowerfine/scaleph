package cn.sliew.scaleph.plugin.seatunnel.flink.converter.env;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.plugin.framework.property.Parsers;
import cn.sliew.scaleph.plugin.framework.property.PropertyContext;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.Validators;
import cn.sliew.scaleph.plugin.seatunnel.flink.converter.EnvConverter;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class CheckpointEnvConverter implements EnvConverter {

    public static final PropertyDescriptor<Integer> CHECKPOINT_INTERVAL = new PropertyDescriptor.Builder<>()
            .name("execution.checkpoint.interval")
            .description("This setting defines the base interval")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CHECKPOINT_MODE = new PropertyDescriptor.Builder<>()
            .name("execution.checkpoint.mode")
            .description("The checkpointing mode (exactly-once vs. at-least-once).")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CHECKPOINT_DATA_URI = new PropertyDescriptor.Builder<>()
            .name("execution.checkpoint.data-uri")
            .description("The directory to write checkpoints to")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> CHECKPOINT_TIMEOUT = new PropertyDescriptor.Builder<>()
            .name("execution.checkpoint.timeout")
            .description("The maximum time that a checkpoint may take before being discarded.")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAX_CONCURRENT_CHECKPOINTS = new PropertyDescriptor.Builder<>()
            .name("execution.max-concurrent-checkpoints")
            .description("The maximum number of checkpoint attempts that may be in progress at the same time. If this value is n, then no checkpoints will be triggered while n checkpoint attempts are currently in flight. For the next checkpoint to be triggered, one checkpoint attempt would need to finish or expire.")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> CHECKPOINT_CLEANUP_MODE = new PropertyDescriptor.Builder<>()
            .name("execution.checkpoint.cleanup-mode")
            .description("The mode defines how an externalized checkpoint should be cleaned up on job cancellation")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MIN_PAUSE_BETWEEN_CHECKPOINTS = new PropertyDescriptor.Builder<>()
            .name("execution.checkpoint.min-pause")
            .description("The minimal pause between checkpointing attempts")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> FAIL_ON_CHECKPOINTING_ERRORS = new PropertyDescriptor.Builder<>()
            .name("execution.checkpoint.fail-on-error")
            .description("The tolerable checkpoint consecutive failure number. If set to 0, that means we do not tolerance any checkpoint failure.")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.INTEGER_VALIDATOR)
            .validateAndBuild();

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
