package cn.sliew.scaleph.plugin.datasource.kafka;

import cn.sliew.scaleph.common.enums.DataSourceTypeEnum;
import cn.sliew.scaleph.plugin.datasource.DatasourcePlugin;
import cn.sliew.scaleph.plugin.framework.core.PluginInfo;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.kafka.clients.KafkaClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static cn.sliew.scaleph.plugin.datasource.kafka.KafkaConsumerProperties.GROUP_ID;
import static cn.sliew.scaleph.plugin.datasource.kafka.KafkaProducerProperties.*;
import static cn.sliew.scaleph.plugin.datasource.kafka.KafkaProperties.BOOTSTRAP_SERVERS;
import static cn.sliew.scaleph.plugin.datasource.kafka.KafkaProperties.TOPICS;

public class KafkaDataSourcePlugin extends DatasourcePlugin<KafkaClient> {

    public KafkaDataSourcePlugin() {
        this.pluginInfo = new PluginInfo(DataSourceTypeEnum.KAFKA.getValue(), "kafka datasource", "2.8.1", KafkaDataSourcePlugin.class.getName());

        final List<PropertyDescriptor> props = new ArrayList<>();
        props.add(TOPICS);
        props.add(BOOTSTRAP_SERVERS);

        props.add(DELIVERY_GUARANTEE);
        props.add(METADATA_WAIT_TIME);
        props.add(ACK_WAIT_TIME);
        props.add(MAX_REQUEST_SIZE);
        props.add(COMPRESSION_CODEC);

        props.add(GROUP_ID);

        supportedProperties = Collections.unmodifiableList(props);
    }

    @Override
    public KafkaClient getDatasource() {
        return null;
    }

    @Override
    public void setAdditionalProperties(Properties properties) {

    }

    @Override
    public void setMeterRegistry(MeterRegistry meterRegistry) {

    }

    @Override
    public boolean testConnection() {
        //todo implement
        return false;
    }

}
