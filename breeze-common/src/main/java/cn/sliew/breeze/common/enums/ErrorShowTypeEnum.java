package cn.sliew.breeze.common.enums;

/**
 * SILENT = 0,
 * ERROR_MESSAGE = 2,
 * NOTIFICATION = 4,
 * REDIRECT = 9,
 *
 * @author gleiyu
 */
public enum ErrorShowTypeEnum {

    SILENT(0, "SILENT"),
    ERROR_MESSAGE(2, "ERROR_MESSAGE"),
    NOTIFICATION(4, "NOTIFICATION"),
    REDIRECT(9, "REDIRECT");

    private Integer code;
    private String value;

    ErrorShowTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
