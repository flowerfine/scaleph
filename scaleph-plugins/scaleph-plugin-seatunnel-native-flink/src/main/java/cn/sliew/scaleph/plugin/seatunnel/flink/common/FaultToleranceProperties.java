package cn.sliew.scaleph.plugin.seatunnel.flink.common;

import cn.sliew.scaleph.plugin.framework.property.Parsers;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.Validators;

import java.util.Arrays;
import java.util.List;

public enum FaultToleranceProperties {
    ;

    public static final PropertyDescriptor<String> RESTART_STRATEGY = new PropertyDescriptor.Builder<>()
            .name("execution.restart.strategy")
            .description("Defines the restart strategy to use in case of job failures")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .allowableValues("none", "off", "disable", "fixeddelay", "fixed-delay", "failurerate", "failure-rate", "exponentialdelay", "exponential-delay")
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> RESTART_ATTEMPTS = new PropertyDescriptor.Builder<>()
            .name("execution.restart.attempts")
            .description("The number of times that Flink retries the execution before the job is declared as failed if restart-strategy has been set to fixed-delay.")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> RESTART_DELAY_BETWEEN_ATTEMPTS = new PropertyDescriptor.Builder<>()
            .name("execution.restart.delayBetweenAttempts")
            .description("Delay between two consecutive restart attempts if restart-strategy has been set to fixed-delay")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> RESTART_FAILURE_RATE = new PropertyDescriptor.Builder<>()
            .name("execution.restart.failureRate")
            .description("Maximum number of restarts in given time interval before failing a job if restart-strategy has been set to failure-rate")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> RESTART_FAILURE_INTERVAL = new PropertyDescriptor.Builder<>()
            .name("execution.restart.failureInterval")
            .description("Time interval for measuring failure rate if restart-strategy has been set to failure-rate.")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> RESTART_DELAY_INTERVAL = new PropertyDescriptor.Builder<>()
            .name("execution.restart.delayInterval")
            .description("Delay between two consecutive restart attempts if restart-strategy has been set to failure-rate.")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final List<PropertyDescriptor> SUPPORTED_PROPERTIES = Arrays.asList(RESTART_STRATEGY,
            RESTART_ATTEMPTS, RESTART_DELAY_BETWEEN_ATTEMPTS, RESTART_FAILURE_RATE, RESTART_FAILURE_INTERVAL,
            RESTART_DELAY_INTERVAL);
}
