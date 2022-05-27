package cn.sliew.scaleph.plugin.datasource;

import cn.sliew.scaleph.plugin.framework.core.Plugin;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.Properties;

/**
 * datasource plugin
 */
public interface DatasourcePlugin<T> extends Plugin {

    T getDatasource();

    void setAdditionalProperties(Properties properties);

    void setMeterRegistry(MeterRegistry meterRegistry);
}
