package cn.sliew.scaleph.system.dict.common;

import cn.sliew.scaleph.system.dict.DictDefinition;
import cn.sliew.scaleph.system.dict.DictInstance;
import cn.sliew.scaleph.system.dict.DictType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RegisterChannel implements DictInstance {

    REGISTER("01", "用户注册"),
    IMPORT("02", "后台导入"),
    ;

    @JsonValue
    @EnumValue
    private String code;
    private String value;

    RegisterChannel(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public DictDefinition getDefinition() {
        return DictType.REGISTER_CHANNEL;
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
