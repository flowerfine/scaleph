package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.kudu;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum KuduProperties {
    ;
    public static final PropertyDescriptor<String> KUDU_MASTER = new PropertyDescriptor.Builder()
            .name("kudu_master")
            .description("The address of kudu master")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> KUDU_TABLE = new PropertyDescriptor.Builder()
            .name("kudu_table")
            .description("The name of kudu table")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> COLUMNS_LIST = new PropertyDescriptor.Builder()
            .name("columnsList")
            .description("Specifies the column names of the table")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SAVE_MODE = new PropertyDescriptor.Builder()
            .name("save_mode")
            .description("Storage mode, we need support overwrite and append")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();
}
