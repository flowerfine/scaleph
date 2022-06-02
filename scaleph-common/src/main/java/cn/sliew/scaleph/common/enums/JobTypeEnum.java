package cn.sliew.scaleph.common.enums;

import lombok.Getter;

/**
 * @author gleiyu
 */
@Getter
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
}
