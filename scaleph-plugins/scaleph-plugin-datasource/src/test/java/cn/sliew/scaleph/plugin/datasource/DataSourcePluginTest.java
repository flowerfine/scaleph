package cn.sliew.scaleph.plugin.datasource;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.core.PluginSPILoader;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DataSourcePluginTest {

    @Test
    void testJdbcDataSourcePlugin() throws Exception {
        final PluginSPILoader<DatasourcePlugin> pluginSPILoader = new PluginSPILoader<>(DatasourcePlugin.class, DatasourcePlugin.class.getClassLoader());
        final Set<PluginInfo> plugins = pluginSPILoader.availableServices();
        assertThat(plugins).isNotEmpty();
    }
}
