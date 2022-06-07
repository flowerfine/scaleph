package cn.sliew.scaleph.plugin.seatunnel.flink.connector.file.source;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum FileSourceProperties {
    ;

    public static final PropertyDescriptor<String> FORMAT_TYPE = new PropertyDescriptor.Builder<String>()
            .name("format.type")
            .description("The format for reading files from the file system")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .allowableValues("csv", "json", "parquet", "orc", "text")
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> PATH = new PropertyDescriptor.Builder<String>()
            .name("path")
            .description("The files path. support hdfs:// or file://")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> SCHEMA = new PropertyDescriptor.Builder<String>()
            .name("schema")
            .description("https://seatunnel.apache.org/docs/2.1.1/connector/source/File")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<Integer> PARALLELISM = new PropertyDescriptor.Builder()
            .name("parallelism")
            .description("The flink operator parallelism")
            .type(PropertyType.INT)
            .defaultValue(1)
            .parser(Parsers.INTEGER_PARSER)
            .addValidator(Validators.POSITIVE_INTEGER_VALIDATOR)
            .validateAndBuild();
}
