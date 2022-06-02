package cn.sliew.scaleph.common.enums;

import lombok.Getter;

/**
 * @author gleiyu
 */
@Getter
public enum TaskResultEnum {

    SUCCESS("SUCCESS", "成功"),
    FAILURE("FAILURE", "失败");

    private String code;
    private String value;

    TaskResultEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
