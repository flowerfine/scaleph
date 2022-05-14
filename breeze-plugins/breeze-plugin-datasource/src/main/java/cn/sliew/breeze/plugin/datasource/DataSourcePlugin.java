package cn.sliew.breeze.plugin.datasource;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.Configuration;

import java.util.List;

/**
 * datasource plugin demo
 */
public interface DataSourcePlugin {

    String getPluginName();

    List<ConfigOption> getRequiredProperties();

    List<ConfigOption> getOptionalProperties();

    boolean validate(Configuration configuration);
}
