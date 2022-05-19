package cn.sliew.scaleph.plugin.datasource;

import cn.sliew.scaleph.plugin.framework.core.PluginSPILoader;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DataSourcePluginTest {

    @Test
    void testJdbcDataSourcePlugin() throws Exception {
        final PluginSPILoader<DataSourcePlugin> pluginSPILoader = new PluginSPILoader<>(DataSourcePlugin.class, Thread.currentThread().getContextClassLoader());
        final Set<String> plugins = pluginSPILoader.availableServices();
        assertThat(plugins).isNotEmpty();
    }
}
