package cn.sliew.scaleph.common.enums;

import lombok.Getter;

/**
 * @author gleiyu
 */
@Getter
public enum RoleTypeEnum {

    SYSTEM("01", "system"),
    USER_DEF("02", "user define"),
    ;

    private String value;
    private String label;

    RoleTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
