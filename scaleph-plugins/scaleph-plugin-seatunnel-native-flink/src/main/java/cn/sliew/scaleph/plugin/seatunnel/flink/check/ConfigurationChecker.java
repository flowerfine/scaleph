package cn.sliew.scaleph.plugin.seatunnel.flink.check;

import java.util.List;

import org.apache.flink.configuration.Configuration;

public interface ConfigurationChecker {

    List<CheckResult> check(Configuration configuration);
}
