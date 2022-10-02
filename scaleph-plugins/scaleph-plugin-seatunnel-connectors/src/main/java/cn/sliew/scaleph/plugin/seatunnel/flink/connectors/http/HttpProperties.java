package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.http;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum HttpProperties {
    ;

    public static final PropertyDescriptor<String> URL = new PropertyDescriptor.Builder<String>()
            .name("url")
            .description("http request url")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> HEADERS = new PropertyDescriptor.Builder<String>()
            .name("headers")
            .description("http headers")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PARAMS = new PropertyDescriptor.Builder<String>()
            .name("params")
            .description("http params")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> RETRY = new PropertyDescriptor.Builder<Integer>()
            .name("retry")
            .description("request http api interval(millis) in stream mode")
            .type(PropertyType.INT)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> RETRY_BACKOFF_MULTIPLIER_MS = new PropertyDescriptor.Builder<Long>()
            .name("retry_backoff_multiplier_ms")
            .description("The retry-backoff times(millis) multiplier if request http failed")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> RETRY_BACKOFF_MAX_MS = new PropertyDescriptor.Builder<Long>()
            .name("retry_backoff_max_ms")
            .description("The maximum retry-backoff times(millis) if request http failed")
            .type(PropertyType.INT)
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.POSITIVE_LONG_VALIDATOR)
            .validateAndBuild();
}
