package cn.sliew.scaleph.plugin.datasource.kafka;

import cn.sliew.scaleph.plugin.framework.property.Property;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import org.apache.kafka.clients.consumer.ConsumerConfig;

public enum KafkaConsumerProperties {
    ;

    public static final PropertyDescriptor GROUP_ID = new PropertyDescriptor.Builder()
            .name(ConsumerConfig.GROUP_ID_CONFIG)
            .description("A unique string that identifies the consumer group this consumer belongs to. This property is required if the consumer uses either the group management functionality by using <code>subscribe(topic)</code> or the Kafka-based offset management strategy.")
            .defaultValue(null)
            .properties(Property.Required)
            .build();
}
