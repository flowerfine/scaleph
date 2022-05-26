package cn.sliew.scaleph.plugin.seatunnel.flink;

import cn.sliew.scaleph.plugin.seatunnel.flink.converter.ConfigFileConverter;
import cn.sliew.scaleph.plugin.seatunnel.flink.env.CheckpointEnvConverter;
import cn.sliew.scaleph.plugin.seatunnel.flink.jdbc.sink.JdbcSinkConverter;
import cn.sliew.scaleph.plugin.seatunnel.flink.jdbc.source.JdbcSourceConverter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Properties;

class ConfConverterTest {

    private Properties properties;

    @BeforeEach
    private void beforeEach() throws Exception {
        final InputStream inputStream = ConfConverterTest.class.getClassLoader().getResourceAsStream("conf.properties");
        properties = new Properties();
        properties.load(inputStream);
    }

    @Test
    void testCreate() {
        ConfigFileConverter converter = new ConfigFileConverter();
        converter.addEnvConverter(new CheckpointEnvConverter());
        converter.addSourceConverter(new JdbcSourceConverter());
        converter.addSinkConverter(new JdbcSinkConverter());
        ObjectNode conf = converter.create(properties);
        System.out.println(conf.toPrettyString());
    }
}
