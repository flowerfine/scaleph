package cn.sliew.scaleph.common.enums;

import lombok.Getter;

/**
 * @author gleiyu
 */
@Getter
public enum DeptStatusEnum {

    NORMAL("1", "normal"),
    DISABLE("2", "disable"),
    ;

    private String value;
    private String label;

    DeptStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

}
