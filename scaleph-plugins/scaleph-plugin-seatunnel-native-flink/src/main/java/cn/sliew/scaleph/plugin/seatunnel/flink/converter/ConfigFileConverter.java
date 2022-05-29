package cn.sliew.scaleph.plugin.seatunnel.flink.converter;

import cn.sliew.milky.common.util.JacksonUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

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
    public ObjectNode create() {
        ObjectNode conf = JacksonUtil.createObjectNode();
        ObjectNode envConf = JacksonUtil.createObjectNode();
        conf.set("env", envConf);
        appendEnvOptions(envConf);

        ObjectNode sourceConf = JacksonUtil.createObjectNode();
        conf.set("source", sourceConf);
        appendSourceOptions(sourceConf);

        ObjectNode transformConf = JacksonUtil.createObjectNode();
        conf.set("transform", transformConf);
        appendTransformOptions(transformConf);

        ObjectNode sinkConf = JacksonUtil.createObjectNode();
        conf.set("sink", sinkConf);
        appendSinkOptions(sinkConf);
        return conf;
    }

    private void appendEnvOptions(ObjectNode envConf) {
        for (EnvConverter converter : envs) {
            ObjectNode objectNode = converter.create();
            envConf.setAll(objectNode);
        }
    }

    private void appendSourceOptions(ObjectNode sourceConf) {
        for (SourceConverter converter : sources) {
            ObjectNode objectNode = converter.create();
            sourceConf.set(converter.getPluginName(), objectNode);
        }
    }

    private void appendTransformOptions(ObjectNode transformConf) {
        for (TransformConverter converter : transforms) {
            ObjectNode objectNode = converter.create();
            transformConf.set(converter.getPluginName(), objectNode);
        }
    }

    private void appendSinkOptions(ObjectNode sinkConf) {
        for (SinkConverter converter : sinks) {
            ObjectNode objectNode = converter.create();
            sinkConf.set(converter.getPluginName(), objectNode);
        }
    }
}
