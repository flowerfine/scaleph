package cn.sliew.scaleph.system.dict.security;

import cn.sliew.scaleph.system.dict.DictDefinition;
import cn.sliew.scaleph.system.dict.DictInstance;
import cn.sliew.scaleph.system.dict.DictType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ResourceType implements DictInstance {

    MENU("0", "菜单权限"),
    OPERATION("1", "操作权限"),
    DATA("2", "数据权限"),
    ;

    @JsonValue
    @EnumValue
    private String code;
    private String value;

    ResourceType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public DictDefinition getDefinition() {
        return DictType.RESOURCE_TYPE;
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
