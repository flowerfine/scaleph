package cn.sliew.scaleph.plugin.seatunnel.flink.jdbc.sink;

import cn.sliew.scaleph.plugin.framework.core.AbstractPlugin;
import cn.sliew.scaleph.plugin.framework.core.Plugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import cn.sliew.scaleph.plugin.seatunnel.flink.common.CommonOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.sliew.scaleph.plugin.seatunnel.flink.jdbc.JdbcProperties.*;
import static cn.sliew.scaleph.plugin.seatunnel.flink.jdbc.sink.JdbcSinkProperties.*;

public class JdbcSink extends AbstractPlugin implements Plugin {

    private static final List<PropertyDescriptor> supportedProperties;

    static {
        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(URL);
        props.add(DRIVER);
        props.add(USERNAME);
        props.add(PASSWORD);
        props.add(QUERY);
        props.add(BATCH_SIZE);
        props.add(PARALLELISM);

        props.add(CommonOptions.RESULT_TABLE_NAME);
        props.add(CommonOptions.FIELD_NAME);
        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public PluginInfo getPluginInfo() {
        return null;
    }

    @Override
    public List<PropertyDescriptor> getSupportedProperties() {
        return supportedProperties;
    }


}
