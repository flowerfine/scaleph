package cn.sliew.scaleph.common.enums;

/**
 * @author gleiyu
 */
public enum MessageTypeEnum {

    /**
     * 系统消息
     */
    SYSTEM("1", "system"),
    ;

    private String value;
    private String label;

    MessageTypeEnum(String value, String label) {
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
