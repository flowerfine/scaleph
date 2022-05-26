package cn.sliew.scaleph.plugin.seatunnel.flink.jdbc.source;

import cn.sliew.scaleph.plugin.framework.property.Parsers;
import cn.sliew.scaleph.plugin.framework.property.Property;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.Validators;

public enum JdbcSourceProperties {
    ;

    public static final PropertyDescriptor<String> QUERY = new PropertyDescriptor.Builder<String>()
            .name("query")
            .description("query statement")
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> FETCH_SIZE = new PropertyDescriptor.Builder<String>()
            .name("fetch_size")
            .description("result set fetch size")
            .defaultValue(descriptor -> "1024")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .properties(Property.Required)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PARTITION_COLUMN = new PropertyDescriptor.Builder<String>()
            .name("partition_column")
            .description("The column name for parallelism's partition, only support numeric type.")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> PARTITION_UPPER_BOUND = new PropertyDescriptor.Builder<String>()
            .name("partition_upper_bound")
            .description("The partition_column max value for scan, if not set SeaTunnel will query database get max value.")
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.LONG_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Long> PARTITION_LOWER_BOUND = new PropertyDescriptor.Builder<String>()
            .name("partition_lower_bound")
            .description("The partition_column min value for scan, if not set SeaTunnel will query database get min value.")
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.LONG_VALIDATOR)
            .validateAndBuild();
}
