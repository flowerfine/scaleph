package cn.sliew.scaleph.plugin.seatunnel.flink.connector.druid.source;

import cn.sliew.scaleph.plugin.framework.property.*;

public enum DruidSourceProperties {
    ;

    public static final PropertyDescriptor<String> JDBC_URL = new PropertyDescriptor.Builder<String>()
            .name("jdbc_url")
            .description("Apache Druid jdbc url")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> DATASOURCE = new PropertyDescriptor.Builder<String>()
            .name("datasource")
            .description("Apache Druid datasource name")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .properties(Property.Required)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> START_DATE = new PropertyDescriptor.Builder<String>()
            .name("start_date")
            .description("The start date of DataSource, for example, '2016-06-27', '2016-06-27 00:00:00', etc.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> END_DATE = new PropertyDescriptor.Builder<String>()
            .name("end_date")
            .description("The end date of DataSource, for example, '2016-06-28', '2016-06-28 00:00:00', etc.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
            .addValidator(Validators.NON_BLANK_VALIDATOR)
            .validateAndBuild();

    public static final PropertyDescriptor<String> COLUMNS = new PropertyDescriptor.Builder<String>()
            .name("columns")
            .description("columns to be queried from DataSource.")
            .type(PropertyType.STRING)
            .parser(Parsers.STRING_PARSER)
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
