package cn.sliew.scaleph.common.enums;

import lombok.Getter;

/**
 * @author gleiyu
 */
@Getter
public enum TransferDirectionEnum {

    TARGET("1", "TARGET"),
    SOURCE("0", "SOURCE");

    private String value;
    private String label;

    TransferDirectionEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
