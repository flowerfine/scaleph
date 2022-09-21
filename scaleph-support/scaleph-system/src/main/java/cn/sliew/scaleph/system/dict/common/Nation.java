package cn.sliew.scaleph.system.dict.common;

import cn.sliew.scaleph.system.dict.DictDefinition;
import cn.sliew.scaleph.system.dict.DictInstance;
import cn.sliew.scaleph.system.dict.DictType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Nation implements DictInstance {

    CHINA("cn", "中国"),
    AMERICA("us", "美国"),
    ENGLAND("gb", "英国"),
    GERMANY("de", "德国"),
    ;

    @JsonValue
    @EnumValue
    private String code;
    private String value;

    Nation(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public DictDefinition getDefinition() {
        return DictType.NATION;
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
