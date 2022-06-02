package cn.sliew.scaleph.common.enums;

import lombok.Getter;

/**
 * @author gleiyu
 */
@Getter
public enum MessageTypeEnum {

    /**
     * 系统消息
     */
    SYSTEM("1", "system"),
    ;

    private String value;
    private String label;

    MessageTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
