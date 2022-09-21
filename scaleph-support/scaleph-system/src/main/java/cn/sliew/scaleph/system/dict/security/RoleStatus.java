package cn.sliew.scaleph.system.dict.security;

import cn.sliew.scaleph.system.dict.DictDefinition;
import cn.sliew.scaleph.system.dict.DictInstance;
import cn.sliew.scaleph.system.dict.DictType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleStatus implements DictInstance {

    DISABLED("0", "禁用"),
    ENABLED("1", "正常"),
    ;

    @JsonValue
    @EnumValue
    private String code;
    private String value;

    RoleStatus(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public DictDefinition getDefinition() {
        return DictType.ROLE_STATUS;
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
