package cn.sliew.breeze.common.enums;

/**
 * @author gleiyu
 */
public enum JobStepTypeEnum {

    SOURCE("source", "输入"),
    TRANSFORM("trans", "转换"),
    SINK("sink", "输出"),
    ;

    private String value;
    private String label;

    JobStepTypeEnum(String value, String label) {
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
