package cn.sliew.scaleph.system.dict.common;

import cn.sliew.scaleph.system.dict.DictDefinition;
import cn.sliew.scaleph.system.dict.DictInstance;
import cn.sliew.scaleph.system.dict.DictType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender implements DictInstance {

    UNKNOWN("0", "未知"),
    MALE("1", "男"),
    FEMALE("2", "女"),
    ;

    @JsonValue
    @EnumValue
    private String code;
    private String value;

    Gender(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public DictDefinition getDefinition() {
        return DictType.GENDER;
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
