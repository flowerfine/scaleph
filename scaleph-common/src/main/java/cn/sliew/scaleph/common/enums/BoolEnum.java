package cn.sliew.scaleph.common.enums;

import lombok.Getter;

/**
 * @author gleiyu
 */
@Getter
public enum BoolEnum {

    YES("1", "是"),
    NO("0", "否");

    String value;
    String label;

    BoolEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
