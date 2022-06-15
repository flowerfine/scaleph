package cn.sliew.scaleph.engine.flink.config;

import org.apache.flink.configuration.Configuration;

/**
 * checkpoint 配置
 */
public class CheckpointConfigBuilder {

    private final Configuration configuration;

    public CheckpointConfigBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public CheckpointConfigBuilder set() {
        return this;
    }
}
