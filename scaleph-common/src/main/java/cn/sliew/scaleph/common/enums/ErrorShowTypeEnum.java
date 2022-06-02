package cn.sliew.scaleph.common.enums;

import lombok.Getter;

/**
 * @author gleiyu
 */
@Getter
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
}
