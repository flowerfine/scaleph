package cn.sliew.scaleph.plugin.seatunnel.flink.common;

import cn.sliew.scaleph.plugin.framework.property.Parsers;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.Validators;

import java.util.Arrays;
import java.util.List;

public enum IdleStateRetentionProperties {
    ;

    public static final PropertyDescriptor<Integer> MIN_STATE_RETENTION_TIME = new PropertyDescriptor.Builder<>()
            .name("execution.query.state.min-retention")
            .description("Specifies a minimum time interval for how long idle state (i.e. state which was not updated), will be retained")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> MAX_STATE_RETENTION_TIME = new PropertyDescriptor.Builder<>()
            .name("execution.query.state.max-retention")
            .description("Specifies a maximum time interval for how long idle state (i.e. state which was not updated), will be retained")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final List<PropertyDescriptor> SUPPORTED_PROPERTIES = Arrays.asList(MIN_STATE_RETENTION_TIME, MAX_STATE_RETENTION_TIME);

}
