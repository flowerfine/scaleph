package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.ftp.source;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.local.source.LocalFileSourcePlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.file.local.source.LocalFileSourceProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class FtpFileSourcePlugin extends SeaTunnelConnectorPlugin {

    public FtpFileSourcePlugin() {
        this.pluginInfo = new PluginInfo(getPluginName().getLabel(),
                "Read data from ftp file server",
                FtpFileSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(PATH);
        props.add(TYPE);
        props.add(SCHEMA);
        props.add(CommonProperties.FIELD_NAME);
        props.add(CommonProperties.RESULT_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SOURCE_FTP_FILE;
    }
}
