package cn.sliew.breeze.common.enums;

/**
 * @author gleiyu
 */
public enum ClusterTypeEnum {

    SPARK("spark", "SPARK"),
    FLINK("flink", "FLINK"),
    ;

    private String value;
    private String label;

    ClusterTypeEnum(String value, String label) {
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
