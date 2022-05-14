package cn.sliew.scalegh.common.enums;

/**
 * @author gleiyu
 */
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

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
