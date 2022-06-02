package cn.sliew.scaleph.common.enums;

import lombok.Getter;

/**
 * @author gleiyu
 */
@Getter
public enum JobRuntimeStateEnum {

    STOP("1", "停止"),
    RUNNING("2", "运行中"),
    WAIT("3", "等待");

    private String value;
    private String label;

    JobRuntimeStateEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
