package cn.sliew.scaleph.common.enums;

/**
 * @author gleiyu
 */
public enum ConnectionTypeEnum {

    JDBC("jdbc", "SIMPLE JDBC"),
    POOLED("pooled", "CONNECTION POOL"),
    JNDI("jndi", "JNDI");

    private String code;
    private String value;

    ConnectionTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static ConnectionTypeEnum valueOfName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("connection type must not be null");
        }
        for (ConnectionTypeEnum connectionTypeEnum : values()) {
            if (connectionTypeEnum.getCode().equals(name)) {
                return connectionTypeEnum;
            }
        }
        throw new IllegalArgumentException("unknown connection type for " + name);
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

}
