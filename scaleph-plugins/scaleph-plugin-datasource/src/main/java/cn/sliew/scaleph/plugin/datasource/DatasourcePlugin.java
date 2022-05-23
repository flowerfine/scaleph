package cn.sliew.scaleph.plugin.datasource;

import cn.sliew.scaleph.plugin.framework.core.Plugin;

import java.util.Properties;

/**
 * datasource plugin
 */
public interface DatasourcePlugin<T> extends Plugin {

    T getDatasource();

    void setAdditionalProperties(Properties properties);
}
