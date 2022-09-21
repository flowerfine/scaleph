package cn.sliew.scaleph.system.dict.common;

import cn.sliew.scaleph.system.dict.DictDefinition;
import cn.sliew.scaleph.system.dict.DictInstance;
import cn.sliew.scaleph.system.dict.DictType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum IdCardType implements DictInstance {

    ID_CARD("111", "居民身份证"),
    HOUSEHOLD_REGISTRATION_BOOKLET("113", "户口簿"),
    PASSPORT("414", "普通护照"),
    ;

    @JsonValue
    @EnumValue
    private String code;
    private String value;

    IdCardType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public DictDefinition getDefinition() {
        return DictType.ID_CARD_TYPE;
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
