package cn.sliew.scalegh.common.enums;

/**
 * @author gleiyu
 */
public enum JobTypeEnum {

    BATCH("b", "周期作业"),
    REALTIME("r", "实时作业"),
    ;

    private String value;
    private String label;

    JobTypeEnum(String value, String label) {
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
