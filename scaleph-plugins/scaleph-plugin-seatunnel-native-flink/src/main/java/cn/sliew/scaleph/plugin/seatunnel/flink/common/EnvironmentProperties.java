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

    public static final PropertyDescriptor<String> STATE_BACKEND = new PropertyDescriptor.Builder<String>()
            .name("execution.state.backend")
            .description("The state backend to use. This defines the data structure mechanism for taking snapshots.")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .allowableValues("filesystem", "rocksdb")
            .validateAndBuild();

    public static final PropertyDescriptor<String> PLANNER = new PropertyDescriptor.Builder<String>()
            .name("execution.planner")
            .description("The planner to use. default is blink")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .allowableValues("blink")
            .validateAndBuild();

}
