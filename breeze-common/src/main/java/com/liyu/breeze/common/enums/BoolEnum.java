package com.liyu.breeze.common.enums;

/**
 * @author gleiyu
 */
public enum BoolEnum {
    /**
     * yes
     */
    YES("1", "是"),
    /**
     * no
     */
    NO("0", "否");

    String value;
    String label;

    BoolEnum(String value, String label) {
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
