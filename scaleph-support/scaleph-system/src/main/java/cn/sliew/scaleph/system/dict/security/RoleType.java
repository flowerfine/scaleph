package cn.sliew.scaleph.system.dict.security;

import cn.sliew.scaleph.system.dict.DictDefinition;
import cn.sliew.scaleph.system.dict.DictInstance;
import cn.sliew.scaleph.system.dict.DictType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleType implements DictInstance {

    SYSTEM("01", "系统角色"),
    CUSTOM("02", "用户定义"),
    ;

    @JsonValue
    @EnumValue
    private String code;
    private String value;

    RoleType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public DictDefinition getDefinition() {
        return DictType.ROLE_TYPE;
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
