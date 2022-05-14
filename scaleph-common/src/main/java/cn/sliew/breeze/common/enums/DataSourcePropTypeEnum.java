package cn.sliew.breeze.common.enums;

/**
 * @author gleiyu
 */
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

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

}
