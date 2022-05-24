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

    public static final PropertyDescriptor<String> PARALLELISM = new PropertyDescriptor.Builder<String>()
            .name("parallelism")
            .description("The flink operator parallelism")
            .defaultValue(descriptor -> "1")
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .properties(Property.Required)
            .build();
}
