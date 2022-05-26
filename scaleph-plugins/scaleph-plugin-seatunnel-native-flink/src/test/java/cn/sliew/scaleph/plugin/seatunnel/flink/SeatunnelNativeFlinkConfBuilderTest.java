package cn.sliew.scaleph.plugin.seatunnel.flink;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

class SeatunnelNativeFlinkConfBuilderTest {

    @Test
    void testBuildConfigFile() {
        SeatunnelNativeFlinkConfBuilder builder = new SeatunnelNativeFlinkConfBuilder();
        ObjectNode conf = builder.buildConf();
        conf = builder.appendEnvOptions(conf);
        conf = builder.appendSourceOptions(conf);
        conf = builder.appendTransformOptions(conf);
        conf = builder.appendSinkOptions(conf);
        String configFile = builder.buildConfigFile(conf);
        System.out.println(configFile);
    }
}
