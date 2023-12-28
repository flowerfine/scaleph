package cn.sliew.scaleph.plugin.flink.cdc.connectors.mysql.source;

import cn.sliew.scaleph.common.dict.flink.cdc.FlinkCDCPluginMapping;
import cn.sliew.scaleph.plugin.flink.cdc.FlinkCDCPipilineConnectorPlugin;
import cn.sliew.scaleph.plugin.flink.cdc.connectors.CommonProperties;
import cn.sliew.scaleph.plugin.flink.cdc.transform.RoutePlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.flink.cdc.connectors.mysql.source.MySQLSourceProperties.*;

@AutoService(FlinkCDCPipilineConnectorPlugin.class)
public class MySQLSourcePlugin extends FlinkCDCPipilineConnectorPlugin {

    public MySQLSourcePlugin() {
        this.pluginInfo = new PluginInfo(getIdentity(),
                "mysql",
                RoutePlugin.class.getName());
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(CommonProperties.NAME);
        props.add(CommonProperties.TYPE);
        props.add(CommonProperties.DESCRIPTION);

        props.add(HOSTNAME);
        props.add(PORT);
        props.add(USERNAME);
        props.add(PASSWORD);
        props.add(TABLES);
        props.add(SCHEMA_CHANGE_ENABLED);
        props.add(SERVER_ID);
        props.add(SCAN_INCREMENTAL_CLOSE_IDLE_READER_ENABLED);
        props.add(SCAN_INCREMENTAL_SNAPSHOT_CHUNK_SIZE);
        props.add(SCAN_SNAPSHOT_FETCH_SIZE);
        props.add(SCAN_STARTUP_MODE);
        props.add(SCAN_STARTUP_SPECIFIC_OFFSET_FILE);
        props.add(SCAN_STARTUP_SPECIFIC_OFFSET_POS);
        props.add(SCAN_STARTUP_SPECIFIC_OFFSET_GTID_SET);
        props.add(SCAN_STARTUP_SPECIFIC_OFFSET_SKIP_EVENTS);
        props.add(SCAN_STARTUP_SPECIFIC_OFFSET_SKIP_ROWS);
        props.add(CONNECT_TIMEOUT);
        props.add(CONNECT_MAX_RETRIES);
        props.add(CONNECT_POOL_SIZE);
        props.add(HEARTBEAT_INTERVAL);
        props.add(JDBC_PROPERTIES);
        props.add(DEBEZIUM);
        this.supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    protected FlinkCDCPluginMapping getPluginMapping() {
        return FlinkCDCPluginMapping.SOURCE_MYSQL;
    }
}
