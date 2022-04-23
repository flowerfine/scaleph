package com.liyu.breeze.common.enums;

/**
 * @author gleiyu
 */
public enum UserStatusEnum {

    UNBIND_EMAIL("10", "未绑定邮箱"),
    BIND_EMAIL("11", "已绑定邮箱"),
    DISABLE("90", "禁用"),
    LOGOFF("91", "注销");

    private String value;
    private String label;

    UserStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
