package cn.sliew.scaleph.plugin.seatunnel.flink.jdbc;

import cn.sliew.scaleph.plugin.framework.property.Parsers;
import cn.sliew.scaleph.plugin.framework.property.Property;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.Validators;

public enum JdbcProperties {
    ;

    public static final PropertyDescriptor<String> URL = new PropertyDescriptor.Builder<String>()
            .name("url")
            .description("jdbc url")
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .build();

    public static final PropertyDescriptor<String> DRIVER = new PropertyDescriptor.Builder<String>()
            .name("driver")
            .description("jdbc class name")
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .build();

    public static final PropertyDescriptor<String> USERNAME = new PropertyDescriptor.Builder<String>()
            .name("username")
            .description("jdbc username")
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .build();

    public static final PropertyDescriptor<String> PASSWORD = new PropertyDescriptor.Builder<String>()
            .name("password")
            .description("jdbc password")
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .build();

    public static final PropertyDescriptor<String> QUERY = new PropertyDescriptor.Builder<String>()
            .name("query")
            .description("query statement")
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .build();

    public static final PropertyDescriptor<Integer> FETCH_SIZE = new PropertyDescriptor.Builder<String>()
            .name("fetch_size")
            .description("result set fetch size")
            .defaultValue(descriptor -> "1024")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .properties(Property.Required)
            .build();

    public static final PropertyDescriptor<String> PARALLELISM = new PropertyDescriptor.Builder<String>()
            .name("parallelism")
            .description("The flink operator parallelism")
            .defaultValue(descriptor -> "1")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .properties(Property.Required)
            .build();

    public static final PropertyDescriptor<String> PARTITION_COLUMN = new PropertyDescriptor.Builder<String>()
            .name("partition_column")
            .description("The column name for parallelism's partition, only support numeric type.")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .build();

    public static final PropertyDescriptor<Long> PARTITION_UPPER_BOUND = new PropertyDescriptor.Builder<String>()
            .name("partition_upper_bound")
            .description("The partition_column max value for scan, if not set SeaTunnel will query database get max value.")
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.LONG_VALIDATOR)
            .build();

    public static final PropertyDescriptor<Long> PARTITION_LOWER_BOUND = new PropertyDescriptor.Builder<String>()
            .name("partition_lower_bound")
            .description("The partition_column min value for scan, if not set SeaTunnel will query database get min value.")
            .parser(Parsers.LONG_PARSER)
            .addValidator(Validators.LONG_VALIDATOR)
            .build();
}
