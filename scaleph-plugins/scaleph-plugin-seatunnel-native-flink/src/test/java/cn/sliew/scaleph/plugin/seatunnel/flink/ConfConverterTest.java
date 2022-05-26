package cn.sliew.scaleph.plugin.seatunnel.flink;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import java.util.Properties;

class ConfConverterTest {

    private Properties properties;

    @Test
    void testCreate() {
        ConfConverter converter = new ConfigFileConverter();
        ObjectNode conf = converter.create(properties);
        System.out.println(conf.toPrettyString());
    }
}
