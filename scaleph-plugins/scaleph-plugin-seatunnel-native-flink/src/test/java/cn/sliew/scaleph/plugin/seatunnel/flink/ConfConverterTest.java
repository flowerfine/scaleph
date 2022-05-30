package cn.sliew.scaleph.plugin.seatunnel.flink;

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
//        ConfigFileConverter converter = new ConfigFileConverter();
//        converter.addEnvConverter(new CheckpointEnvConverter(properties));
//        converter.addSourceConverter(new JdbcSourceConnector(properties));
//        converter.addSinkConverter(new JdbcSinkConnector(properties));
//        ObjectNode conf = converter.create();
//        System.out.println(conf.toPrettyString());
    }
}
