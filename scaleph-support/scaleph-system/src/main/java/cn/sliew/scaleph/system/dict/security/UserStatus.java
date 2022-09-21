package cn.sliew.scaleph.system.dict.security;

import cn.sliew.scaleph.system.dict.DictDefinition;
import cn.sliew.scaleph.system.dict.DictInstance;
import cn.sliew.scaleph.system.dict.DictType;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatus implements DictInstance {

    EMAIL_UNBOUND("10", "未绑定邮箱"),
    EMAIL_BOUND("11", "已绑定邮箱"),
    DISABLED("90", "禁用"),
    CANCELLATION("91", "注销"),
    ;

    @JsonValue
    @EnumValue
    private String code;
    private String value;

    UserStatus(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public DictDefinition getDefinition() {
        return DictType.USER_STATUS;
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
