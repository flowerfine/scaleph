package cn.sliew.scaleph.plugin.seatunnel.flink.env;

import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.scaleph.plugin.framework.property.Parsers;
import cn.sliew.scaleph.plugin.framework.property.PropertyContext;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.Validators;
import cn.sliew.scaleph.plugin.seatunnel.flink.EnvConverter;
import com.fasterxml.jackson.databind.node.ObjectNode;

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

    public static final PropertyDescriptor<String> FAIL_ON_CHECKPOINTING_ERRORS = new PropertyDescriptor.Builder<>()
            .name("execution.checkpoint.data-uri")
            .description("execution.checkpoint.data-uri")
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
            .description("execution.checkpoint.cleanup-mode")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MIN_PAUSE_BETWEEN_CHECKPOINTS = new PropertyDescriptor.Builder<>()
            .name("execution.checkpoint.min-pause")
            .description("The minimal pause between checkpointing attempts")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FAIL_ON_CHECKPOINTING_ERRORS = new PropertyDescriptor.Builder<>()
            .name("execution.checkpoint.fail-on-error")
            .description("execution.checkpoint.fail-on-error")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    @Override
    public ObjectNode create(Properties properties) {
        ObjectNode objectNode = JacksonUtil.createObjectNode();
        PropertyContext propertyContext = PropertyContext.fromProperties(properties);
        if (propertyContext.contains(CHECKPOINT_INTERVAL)) {
            objectNode.put(CHECKPOINT_INTERVAL.getName(), propertyContext.get(CHECKPOINT_INTERVAL));
        }


        return objectNode;
    }
}
