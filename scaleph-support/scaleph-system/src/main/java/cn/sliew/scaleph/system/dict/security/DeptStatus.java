package cn.sliew.scaleph.system.dict.security;

import cn.sliew.scaleph.system.dict.DictDefinition;
import cn.sliew.scaleph.system.dict.DictInstance;
import cn.sliew.scaleph.system.dict.DictType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DeptStatus implements DictInstance {

    DISABLED("0", "禁用"),
    ENABLED("1", "正常"),
    ;

    @JsonValue
    @EnumValue
    private String code;
    private String value;

    DeptStatus(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public DictDefinition getDefinition() {
        return DictType.DEPT_STATUS;
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
