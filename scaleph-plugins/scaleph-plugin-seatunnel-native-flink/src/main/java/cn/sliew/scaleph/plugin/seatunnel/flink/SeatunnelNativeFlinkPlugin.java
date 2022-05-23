package cn.sliew.scaleph.plugin.seatunnel.flink;

import cn.sliew.scaleph.plugin.framework.core.Plugin;

import java.net.URL;

public interface SeatunnelNativeFlinkPlugin extends Plugin {

    void buildConfiguration();

    URL buildConfigFile();

}
