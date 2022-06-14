package cn.sliew.scaleph.engine.flink.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ConfigType {

    HADOOP_CONF(0, "hadoop conf"),
    KUBECONFIG(1, "kubeconfig"),
    FLINK_CONF(2, "flink-conf.yaml"),
    ;

    @EnumValue
    @JsonValue
    private int code;
    private String desc;

    ConfigType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
