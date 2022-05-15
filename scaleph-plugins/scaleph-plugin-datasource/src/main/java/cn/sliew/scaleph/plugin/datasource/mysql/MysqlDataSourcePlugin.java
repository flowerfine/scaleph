package cn.sliew.scaleph.plugin.datasource.mysql;

import cn.sliew.scaleph.plugin.datasource.DataSourcePlugin;
import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;
import org.apache.flink.configuration.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MysqlDataSourcePlugin implements DataSourcePlugin {

    public static final String PLUGIN_NAME = "MySQL";

    private final ConfigOption<String> DRIVER_CLASS_NAME = ConfigOptions.key("driverClassName")
            .stringType()
            .noDefaultValue()
            .withDescription("driver class name");
    private final ConfigOption<String> URL = ConfigOptions.key("url")
            .stringType()
            .noDefaultValue()
            .withDescription("url");

    private final ConfigOption<String> USERNAME = ConfigOptions.key("username")
            .stringType()
            .noDefaultValue()
            .withDescription("username");

    private final ConfigOption<String> PASSWORD = ConfigOptions.key("password")
            .stringType()
            .noDefaultValue()
            .withDescription("password");


    private final List<ConfigOption> requiredProperties = Arrays.asList(DRIVER_CLASS_NAME,
            URL, USERNAME, PASSWORD);

    @Override
    public String getPluginName() {
        return PLUGIN_NAME;
    }

    @Override
    public List<ConfigOption> getRequiredProperties() {
        return Collections.unmodifiableList(requiredProperties);
    }

    @Override
    public List<ConfigOption> getOptionalProperties() {
        return Collections.emptyList();
    }

    @Override
    public boolean validate(Configuration configuration) {
        return getRequiredProperties().stream().filter(property -> !configuration.contains(property))
                .findAny().isPresent();
    }
}
