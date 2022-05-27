package cn.sliew.scaleph.plugin.seatunnel.flink.check;

import org.apache.flink.configuration.Configuration;

import java.util.List;

public interface ConfigurationChecker {

    List<CheckResult> check(Configuration configuration);
}
