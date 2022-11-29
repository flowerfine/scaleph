package cn.sliew.scaleph.engine.sql.service.factory;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;

public class FlinkMysqlCatalogFactoryOption {
    public static final String IDENTIFIER = "flink_mysql";

    public static final ConfigOption<String> USERNAME = ConfigOptions.key("username").stringType().noDefaultValue();

    public static final ConfigOption<String> PASSWORD = ConfigOptions.key("password").stringType().noDefaultValue();

    public static final ConfigOption<String> URL = ConfigOptions.key("url").stringType().noDefaultValue();

    private FlinkMysqlCatalogFactoryOption(){}
}
