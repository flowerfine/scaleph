package cn.sliew.scaleph.common.enums;

import lombok.Getter;

/**
 * @author gleiyu
 */
@Getter
public enum LoginTypeEnum {

    UNKNOWN("0", "未知"),
    LOGIN("1", "登录"),
    LOGOUT("2", "登出"),
    ;

    private String value;
    private String label;

    LoginTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
