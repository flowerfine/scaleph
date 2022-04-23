package com.liyu.breeze.common.enums;

/**
 * @author gleiyu
 */
public enum ResponseCodeEnum {
    /**
     * 操作结果枚举类
     * 530 自定义异常
     */
    SUCCESS("204", "response.success"),
    ERROR_NO_PRIVILEGE("403", "response.error.no.privilege"),
    ERROR_UNAUTHORIZED("401", "response.error.unauthorized"),
    ERROR("500", "response.error"),
    ERROR_CUSTOM("530", ""),
    ERROR_EMAIL("531", "response.error.email"),
    ERROR_DUPLICATE_DATA("532", "response.error.duplicate.data"),
    ERROR_UNSUPPORTED_CONNECTION("533", "response.error.unsupported.connection"),
    ERROR_CONNECTION("534", "response.error.connection");

    private String code;
    private String value;

    ResponseCodeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }
}
