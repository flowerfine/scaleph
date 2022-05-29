package cn.sliew.scaleph.plugin.seatunnel.flink.converter.common;

import cn.sliew.scaleph.plugin.framework.property.Parsers;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.framework.property.Validators;

public enum CommonOptions {
    ;

    public static final PropertyDescriptor<String> RESULT_TABLE_NAME =
        new PropertyDescriptor.Builder<String>()
            .name("result_table_name")
            .description(
                "The data processed by plugin will be registered as a data set (dataStream/dataset) that can be directly accessed by other plugins, or called a temporary table (table) . ")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SOURCE_TABLE_NAME =
        new PropertyDescriptor.Builder<String>()
            .name("source_table_name")
            .description("The data set processed by previous plugin")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> FIELD_NAME =
        new PropertyDescriptor.Builder<String>()
            .name("field_name")
            .description(
                "When the data is obtained from the upper-level plug-in, you can specify the name of the obtained field, which is convenient for use in subsequent sql plugins.")
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();
}
