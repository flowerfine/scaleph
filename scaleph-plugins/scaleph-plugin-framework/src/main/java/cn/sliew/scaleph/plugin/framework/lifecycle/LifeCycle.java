package cn.sliew.scaleph.plugin.framework.lifecycle;

import java.util.Properties;

public interface LifeCycle {

    void initialize(Properties properties);

    void start();

    void shutdown();
}
