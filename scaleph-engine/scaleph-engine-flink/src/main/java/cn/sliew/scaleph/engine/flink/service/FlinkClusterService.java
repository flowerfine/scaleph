package cn.sliew.scaleph.engine.flink.service;

import org.apache.flink.configuration.Configuration;

public interface FlinkClusterService {

    Configuration buildConfiguration(Long clusterId);
}
