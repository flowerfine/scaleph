package cn.sliew.scaleph.common.enums;

import lombok.Getter;

/**
 * @author gleiyu
 */
@Getter
public enum RegisterChannelEnum {

    REGISTER("01", "注册"),
    BACKGROUND_IMPORT("02", "后台导入");

    private String value;
    private String label;

    RegisterChannelEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
