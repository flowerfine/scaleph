package cn.sliew.scalegh.common.enums;

/**
 * @author gleiyu
 */
public enum JobAttrTypeEnum {

    JOB_ATTR("1", "作业变量"),
    JOB_PROP("2", "作业属性"),
    ENGINE_PROP("3", "引擎属性"),
    ;

    private String value;
    private String label;

    JobAttrTypeEnum(String value, String label) {
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
