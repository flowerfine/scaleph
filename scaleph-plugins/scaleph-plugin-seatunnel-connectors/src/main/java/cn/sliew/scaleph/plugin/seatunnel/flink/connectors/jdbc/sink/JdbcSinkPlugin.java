package cn.sliew.scaleph.plugin.seatunnel.flink.connectors.jdbc.sink;

import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelConnectorPlugin;
import cn.sliew.scaleph.plugin.seatunnel.flink.SeaTunnelPluginMapping;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CommonProperties;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.jdbc.JdbcProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.connectors.jdbc.sink.JdbcSinkProperties.*;

@AutoService(SeaTunnelConnectorPlugin.class)
public class JdbcSinkPlugin extends SeaTunnelConnectorPlugin {

    public JdbcSinkPlugin() {
        this.pluginInfo = new PluginInfo(getPluginName().getLabel(),
                "Jdbc Source Plugin , input records from jdbc connection.",
                JdbcSinkPlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(URL);
        props.add(DRIVER);
        props.add(USER);
        props.add(PASSWORD);
        props.add(QUERY);
        props.add(CONNECTION_CHECK_TIMEOUT_SEC);
        props.add(MAX_RETRIES);
        props.add(BATCH_SIZE);
        props.add(BATCH_INTERVAL_MS);
        props.add(IS_EXACTLY_ONCE);
        props.add(XA_DATA_SOURCE_CLASS_NAME);
        props.add(MAX_COMMIT_ATTEMPTS);
        props.add(TRANSACTION_TIMEOUT_SEC);
        props.add(CommonProperties.SOURCE_TABLE_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected SeaTunnelPluginMapping getPluginMapping() {
        return SeaTunnelPluginMapping.SINK_JDBC;
    }

}
