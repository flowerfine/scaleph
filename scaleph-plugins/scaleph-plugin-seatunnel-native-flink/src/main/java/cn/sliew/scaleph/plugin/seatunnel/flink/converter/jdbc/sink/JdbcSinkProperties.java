package cn.sliew.scaleph.plugin.seatunnel.flink.converter.jdbc.sink;

import cn.sliew.scaleph.plugin.framework.property.Parsers;
import cn.sliew.scaleph.plugin.framework.property.Property;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.Validators;

public enum JdbcSinkProperties {
    ;

    public static final PropertyDescriptor<String> QUERY = new PropertyDescriptor.Builder<String>()
            .name("query")
            .description("upsert statement")
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> BATCH_SIZE = new PropertyDescriptor.Builder<String>()
            .name("batch_size")
            .description("The number of records writen per batch")
            .defaultValue(descriptor -> "1024")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .properties(Property.Required)
            .validateAndBuild();
}
