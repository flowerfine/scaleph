package cn.sliew.scaleph.plugin.seatunnel.flink;

import cn.sliew.milky.common.util.JacksonUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class SeatunnelNativeFlinkConfBuilder {

    public ObjectNode buildConf() {
        return JacksonUtil.createObjectNode();
    }

    public ObjectNode appendEnvOptions(ObjectNode conf) {
        ObjectNode envOptions = JacksonUtil.createObjectNode();
        conf.set("env", envOptions);

        return conf;
    }

    public ObjectNode appendSourceOptions(ObjectNode conf) {
        ObjectNode sourceOptions = JacksonUtil.createObjectNode();
        conf.set("source", sourceOptions);

        return conf;
    }

    public ObjectNode appendTransformOptions(ObjectNode conf) {
        ObjectNode transformOptions = JacksonUtil.createObjectNode();
        conf.set("transform", transformOptions);

        return conf;
    }

    public ObjectNode appendSinkOptions(ObjectNode conf) {
        ObjectNode sinkOptions = JacksonUtil.createObjectNode();
        conf.set("sink", sinkOptions);

        return conf;
    }

    public String buildConfigFile(ObjectNode conf) {
        return conf.toString();
    }

}
