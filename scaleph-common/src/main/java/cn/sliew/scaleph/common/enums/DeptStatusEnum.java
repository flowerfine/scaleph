package cn.sliew.scaleph.common.enums;

/**
 * @author gleiyu
 */
public enum DeptStatusEnum {

    /**
     * 正常
     */
    NORMAL("1", "normal"),
    /**
     * 禁用
     */
    DISABLE("2", "disable"),
    ;

    private String value;
    private String label;

    DeptStatusEnum(String value, String label) {
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
