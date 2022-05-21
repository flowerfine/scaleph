package cn.sliew.scaleph.plugin.datasource.kafka;

import cn.sliew.scaleph.plugin.framework.property.Property;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

public enum KafkaProperties {
    ;

    public static final PropertyDescriptor TOPICS = new PropertyDescriptor.Builder()
            .name("topics")
            .description("The name of the Kafka Topic(s) to pull from. More than one can be supplied if comma separated.")
            .defaultValue(null)
            .properties(Property.Required)
            .build();

    public static final PropertyDescriptor GROUP_ID = new PropertyDescriptor.Builder()
            .name(ConsumerConfig.GROUP_ID_CONFIG)
            .description("A unique string that identifies the consumer group this consumer belongs to. This property is required if the consumer uses either the group management functionality by using <code>subscribe(topic)</code> or the Kafka-based offset management strategy.")
            .defaultValue(null)
            .properties(Property.Required)
            .build();

    public static final PropertyDescriptor BOOTSTRAP_SERVERS = new PropertyDescriptor.Builder()
            .name(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG)
            .description("A comma-separated list of known Kafka Brokers in the format <host>:<port>")
            .defaultValue("localhost:9092")
            .properties(Property.Required)
            .build();
}
