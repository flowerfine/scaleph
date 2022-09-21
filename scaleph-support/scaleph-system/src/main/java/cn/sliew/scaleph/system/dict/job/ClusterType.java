package cn.sliew.scaleph.system.dict.job;

import cn.sliew.scaleph.system.dict.DictDefinition;
import cn.sliew.scaleph.system.dict.DictInstance;
import cn.sliew.scaleph.system.dict.DictType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ClusterType implements DictInstance {

    FLINK("flink", "FLINK"),
    SPARK("spark", "SPARK"),
    ;

    @JsonValue
    @EnumValue
    private String code;
    private String value;

    ClusterType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public DictDefinition getDefinition() {
        return DictType.CLUSTER_TYPE;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getValue() {
        return value;
    }

}
