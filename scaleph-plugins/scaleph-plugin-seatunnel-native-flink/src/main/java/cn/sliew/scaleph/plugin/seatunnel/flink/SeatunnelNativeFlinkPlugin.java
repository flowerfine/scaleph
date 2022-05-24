package cn.sliew.scaleph.plugin.seatunnel.flink;

import cn.sliew.scaleph.plugin.framework.core.Plugin;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URL;

public interface SeatunnelNativeFlinkPlugin extends Plugin {

    ObjectNode buildConfiguration();

    ObjectNode appendEnvOptions(ObjectNode conf);

    ObjectNode appendSourceOptions(ObjectNode conf);

    ObjectNode appendSinkOptions(ObjectNode conf);

    URL buildConfigFile();

}
