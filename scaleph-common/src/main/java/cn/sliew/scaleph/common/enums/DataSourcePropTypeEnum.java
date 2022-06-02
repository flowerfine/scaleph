package cn.sliew.scaleph.common.enums;

import lombok.Getter;

/**
 * @author gleiyu
 */
@Getter
public enum DataSourcePropTypeEnum {

    GENERAL("general", "General Properties"),
    JDBC("jdbc", "JDBC Connection Properties"),
    POOL("pool", "Connection Pool Properties");

    private String code;
    private String value;

    DataSourcePropTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

}
