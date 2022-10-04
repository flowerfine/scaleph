package cn.sliew.scaleph.plugin.datasource.greenplum;

import cn.sliew.scaleph.common.enums.DataSourceTypeEnum;
import cn.sliew.scaleph.plugin.datasource.DatasourcePlugin;
import cn.sliew.scaleph.plugin.datasource.postgre.PostgreDataSourcePlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyContext;
import com.google.auto.service.AutoService;

@AutoService(DatasourcePlugin.class)
public class GreenplumDataSourcePlugin extends PostgreDataSourcePlugin {
    public GreenplumDataSourcePlugin() {
        super();
        this.pluginInfo = new PluginInfo(DataSourceTypeEnum.GREENPLUM.getValue(), "Greenplum SQL Jdbc Datasource", GreenplumDataSourcePlugin.class.getName());

    }

    @Override
    public void configure(PropertyContext properties) {
        super.configure(properties);
    }
}
