package cn.sliew.scaleph.common.enums;

import lombok.Getter;

/**
 * @author gleiyu
 */
@Getter
public enum DataSourceTypeEnum {

    MYSQL("mysql", "Mysql"),
    ORACLE("oracle", "Oracle");

    private String code;
    private String value;

    DataSourceTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static DataSourceTypeEnum valueOfName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("datasource type must not be null");
        }
        for (DataSourceTypeEnum dataSourceTypeEnum : values()) {
            if (dataSourceTypeEnum.getCode().equals(name)) {
                return dataSourceTypeEnum;
            }
        }
        throw new IllegalArgumentException("unknown datasource type for " + name);
    }

}
