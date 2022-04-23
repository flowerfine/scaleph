package cn.sliew.breeze.common.enums;

/**
 * @author gleiyu
 */
public enum TaskResultEnum {

    SUCCESS("SUCCESS", "成功"),
    FAILURE("FAILURE", "失败");

    private String code;
    private String value;

    TaskResultEnum(String code, String value) {
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
