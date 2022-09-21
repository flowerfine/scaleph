package cn.sliew.scaleph.system.dict.job;

import cn.sliew.scaleph.system.dict.DictDefinition;
import cn.sliew.scaleph.system.dict.DictInstance;
import cn.sliew.scaleph.system.dict.DictType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum JobAttrType implements DictInstance {

    VARIABLE("1", "作业变量"),
    ENV("2", "作业属性"),
    PROPERTIES("3", "集群属性"),
    ;

    @JsonValue
    @EnumValue
    private String code;
    private String value;

    JobAttrType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public DictDefinition getDefinition() {
        return DictType.JOB_ATTR_TYPE;
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
