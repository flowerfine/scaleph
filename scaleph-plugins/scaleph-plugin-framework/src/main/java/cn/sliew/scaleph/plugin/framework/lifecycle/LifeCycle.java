package cn.sliew.scaleph.plugin.framework.lifecycle;

import java.util.Properties;

public interface LifeCycle {

    void initialize(Properties properties);

    default void start() {

    }

    default void shutdown() {

    }
}
