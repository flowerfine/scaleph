package cn.sliew.scaleph.common.enums;

import lombok.Getter;

/**
 * @author gleiyu
 */
@Getter
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

}
