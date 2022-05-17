package cn.sliew.scaleph.plugin.datasource;

import cn.sliew.scaleph.plugin.framework.core.Plugin;

import javax.sql.DataSource;

/**
 * datasource plugin demo
 */
public interface DataSourcePlugin extends Plugin {

    DataSource getDataSource();
}
