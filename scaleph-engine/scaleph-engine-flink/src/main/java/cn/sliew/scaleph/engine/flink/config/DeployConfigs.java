package cn.sliew.scaleph.engine.flink.config;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.CoreOptions;

public enum DeployConfigs {
    ;

    public static final ConfigOption<String> HADOOP_CONF_DIR = CoreOptions.FLINK_HADOOP_CONF_DIR;

}
