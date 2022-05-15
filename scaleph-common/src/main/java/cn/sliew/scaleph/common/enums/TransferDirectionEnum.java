package cn.sliew.scaleph.common.enums;

/**
 * @author gleiyu
 */
public enum TransferDirectionEnum {

    TARGET("1", "TARGET"),
    SOURCE("0", "SOURCE");

    private String value;
    private String label;

    TransferDirectionEnum(String value, String label) {
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
