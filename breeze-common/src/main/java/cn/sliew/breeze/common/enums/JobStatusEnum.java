package cn.sliew.breeze.common.enums;

/**
 * @author gleiyu
 */
public enum JobStatusEnum {

    /**
     * 草稿  只有当前未发布版本为草稿状态，一个作业可能有0个或者1个草稿版本
     */
    DRAFT("1", "草稿"),
    /**
     * 发布  每个作业只有一个发布版本
     */
    RELEASE("2", "发布"),
    /**
     * 归档  当作业新版本发布后，上一次的发布版本变更为归档
     */
    ARCHIVE("3", "归档"),
    ;

    private String value;
    private String label;

    JobStatusEnum(String value, String label) {
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
