package cn.sliew.scaleph.plugin.seatunnel.flink;

import cn.sliew.milky.common.util.JacksonUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigFileConverter implements ConfConverter {

    private List<EnvConverter> envs = new ArrayList<>();
    private List<SourceConverter> sources = new ArrayList<>();
    private List<TransformConverter> transforms = new ArrayList<>();
    private List<SinkConverter> sinks = new ArrayList<>();

    public void addEnvConverter(EnvConverter converter) {
        this.envs.add(converter);
    }

    public void addSourceConverter(SourceConverter converter) {
        this.sources.add(converter);
    }

    public void addTransformConverter(TransformConverter converter) {
        this.transforms.add(converter);
    }

    public void addSinkConverter(SinkConverter converter) {
        this.sinks.add(converter);
    }

    @Override
    public ObjectNode create(Properties properties) {
        ObjectNode conf = JacksonUtil.createObjectNode();
        ObjectNode envConf = JacksonUtil.createObjectNode();
        conf.set("env", envConf);
        appendEnvOptions(envConf, properties);

        ObjectNode sourceConf = JacksonUtil.createObjectNode();
        conf.set("source", sourceConf);
        appendSourceOptions(sourceConf, properties);

        ObjectNode transformConf = JacksonUtil.createObjectNode();
        conf.set("transform", transformConf);
        appendTransformOptions(transformConf, properties);

        ObjectNode sinkConf = JacksonUtil.createObjectNode();
        conf.set("sink", sinkConf);
        appendSinkOptions(sinkConf, properties);
        return conf;
    }

    private void appendEnvOptions(ObjectNode envConf, Properties properties) {
        for (EnvConverter converter : envs) {
            ObjectNode objectNode = converter.create(properties);
            envConf.setAll(objectNode);
        }
    }

    private void appendSourceOptions(ObjectNode sourceConf, Properties properties) {
        for (SourceConverter converter : sources) {
            ObjectNode objectNode = converter.create(properties);
            sourceConf.setAll(objectNode);
        }
    }

    private void appendTransformOptions(ObjectNode transformConf, Properties properties) {
        for (TransformConverter converter : transforms) {
            ObjectNode objectNode = converter.create(properties);
            transformConf.setAll(objectNode);
        }
    }

    private void appendSinkOptions(ObjectNode sinkConf, Properties properties) {
        for (SinkConverter converter : sinks) {
            ObjectNode objectNode = converter.create(properties);
            sinkConf.setAll(objectNode);
        }
    }
}
