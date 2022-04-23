package com.liyu.breeze.common.enums;

/**
 * @author gleiyu
 */
public enum RoleTypeEnum {

    /**
     * 系统角色
     */
    SYSTEM("01", "system"),
    /**
     * 用户定义
     */
    USER_DEF("02", "user define"),
    ;

    private String value;
    private String label;

    RoleTypeEnum(String value, String label) {
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
