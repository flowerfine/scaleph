package cn.sliew.scaleph.plugin.datasource;

import cn.sliew.scaleph.plugin.framework.core.Plugin;

import javax.sql.DataSource;
import java.util.Properties;
import java.util.Set;

/**
 * datasource plugin
 */
public interface DataSourcePlugin<T> extends Plugin {

    T getDataSource();

    void setAdditionalProperties(Properties properties);
}
