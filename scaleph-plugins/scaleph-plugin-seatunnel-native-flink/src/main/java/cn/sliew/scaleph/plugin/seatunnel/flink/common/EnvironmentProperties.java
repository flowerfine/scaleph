package cn.sliew.scaleph.plugin.seatunnel.flink.common;

import cn.sliew.scaleph.plugin.framework.property.Parsers;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.Validators;

public enum EnvironmentProperties {
    ;

    public static final PropertyDescriptor<Integer> PARALLELISM = new PropertyDescriptor.Builder<String>()
            .name("execution.parallelism")
            .description("default parallelism")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAX_PARALLELISM = new PropertyDescriptor.Builder<String>()
            .name("execution.max-parallelism")
            .description("max parallelism")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> TIME_CHARACTERISTIC = new PropertyDescriptor.Builder<String>()
            .name("execution.time-characteristic")
            .description("time characteristic")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .allowableValues("event-time", "ingestion-time", "processing-time")
            .validateAndBuild();

    public static final PropertyDescriptor<Long> BUFFER_TIMEOUT_MILLIS =new PropertyDescriptor.Builder<String>()
            .name("execution.buffer.timeout")
            .description("buffer buffer")
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.LONG_VALIDATOR)
            .validateAndBuild();

    public static final String CHECKPOINT_INTERVAL = "execution.checkpoint.interval";


    public static final String CHECKPOINT_MODE = "execution.checkpoint.mode";
    public static final String CHECKPOINT_TIMEOUT = "execution.checkpoint.timeout";
    public static final String CHECKPOINT_DATA_URI = "execution.checkpoint.data-uri";
    public static final String MAX_CONCURRENT_CHECKPOINTS = "execution.max-concurrent-checkpoints";
    public static final String CHECKPOINT_CLEANUP_MODE = "execution.checkpoint.cleanup-mode";
    public static final String MIN_PAUSE_BETWEEN_CHECKPOINTS = "execution.checkpoint.min-pause";
    public static final String FAIL_ON_CHECKPOINTING_ERRORS = "execution.checkpoint.fail-on-error";

    public static final String RESTART_STRATEGY = "execution.restart.strategy";
    public static final String RESTART_ATTEMPTS = "execution.restart.attempts";
    public static final String RESTART_DELAY_BETWEEN_ATTEMPTS = "execution.restart.delayBetweenAttempts";
    public static final String RESTART_FAILURE_INTERVAL = "execution.restart.failureInterval";
    public static final String RESTART_FAILURE_RATE = "execution.restart.failureRate";
    public static final String RESTART_DELAY_INTERVAL = "execution.restart.delayInterval";
    public static final String MAX_STATE_RETENTION_TIME = "execution.query.state.max-retention";
    public static final String MIN_STATE_RETENTION_TIME = "execution.query.state.min-retention";

    public static final String STATE_BACKEND = "execution.state.backend";
    public static final String PLANNER = "execution.planner";

}
