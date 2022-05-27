package cn.sliew.scaleph.plugin.seatunnel.flink.check;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.JobManagerOptions;

import java.util.ArrayList;
import java.util.List;

public class StandaloneChecker implements ConfigurationChecker {

    public static List<ConfigOption> requiredConfigOptions;

    static {
        requiredConfigOptions = new ArrayList<>();
        requiredConfigOptions.add(JobManagerOptions.ADDRESS);
    }

    @Override
    public List<CheckResult> check(Configuration configuration) {
        List<CheckResult> results = new ArrayList<>();
        for (ConfigOption configOption : requiredConfigOptions) {
            final CheckResult.Builder builder = new CheckResult.Builder();
            builder.configOption(configOption);
            if (configuration.contains(configOption) == false) {
                builder.valid(false).explanation("require " + configOption.key());
            } else {
                builder.valid(true);
            }
            results.add(builder.build());
        }
        return results;
    }
}
