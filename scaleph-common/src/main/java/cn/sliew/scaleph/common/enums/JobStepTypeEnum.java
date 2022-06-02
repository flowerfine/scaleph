package cn.sliew.scaleph.common.enums;

import lombok.Getter;

/**
 * @author gleiyu
 */
@Getter
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
}
