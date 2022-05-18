package cn.sliew.scaleph.plugin.datasource.jdbc;

import cn.sliew.scaleph.plugin.framework.property.Property;
import cn.sliew.scaleph.plugin.framework.property.PropertyDescriptor;

public enum JdbcPoolProperties {
    ;

    public static final PropertyDescriptor JDBC_URL = new PropertyDescriptor.Builder()
            .name("jdbcUrl")
            .description("database connection url")
            .defaultValue(null)
            .properties(Property.Required)
            .build();

    public static final PropertyDescriptor DRIVER_CLASS_NAME = new PropertyDescriptor.Builder()
            .name("driverClassName")
            .description("fully-qualified class name of the JDBC driver. Example: com.mysql.cj.jdbc.Driver")
            .defaultValue(null)
            .properties(Property.Required)
            .build();

    public static final PropertyDescriptor USERNAME = new PropertyDescriptor.Builder()
            .name("username")
            .description("database username")
            .defaultValue(null)
            .properties(Property.Required)
            .build();

    public static final PropertyDescriptor PASSWORD = new PropertyDescriptor.Builder()
            .name("password")
            .description("password for the database user")
            .defaultValue(null)
            .properties(Property.Required, Property.Sensitive)
            .build();

    public static final PropertyDescriptor MININUM_IDLE = new PropertyDescriptor.Builder()
            .name("minimumIdle")
            .description("This property controls the minimum number of idle connections that HikariCP tries to maintain in the pool. If the idle connections dip below this value and total "
                    + "connections in the pool are less than 'Max Total Connections', HikariCP will make a best effort to add additional connections quickly and efficiently. It is recommended "
                    + "that this property to be set equal to 'Max Total Connections'.")
            .defaultValue("10")
            .properties(Property.Required)
            .build();

    public static final PropertyDescriptor MAXIMUM_POOL_SIZE = new PropertyDescriptor.Builder()
            .name("maximumPoolSize")
            .description("This property controls the maximum size that the pool is allowed to reach, including both idle and in-use connections. Basically this value will determine the "
                    + "maximum number of actual connections to the database backend. A reasonable value for this is best determined by your execution environment. When the pool reaches "
                    + "this size, and no idle connections are available, the service will block for up to connectionTimeout milliseconds before timing out.")
            .defaultValue("10")
            .properties(Property.Required)
            .build();

    public static final PropertyDescriptor IDLE_TIMEOUT = new PropertyDescriptor.Builder()
            .name("idleTimeout")
            .description("This property controls the maximum amount of time that a connection is allowed to sit idle in the pool")
            .defaultValue("600000")
            .properties(Property.Required)
            .build();

    public static final PropertyDescriptor VALIDATION_QUERY = new PropertyDescriptor.Builder()
            .name("connectionInitSql")
            .description("Validation Query used to validate connections before returning them. "
                    + "When connection is invalid, it gets dropped and new valid connection will be returned. "
                    + "NOTE: Using validation might have some performance penalty.")
            .defaultValue("SELECT 1")
            .properties(Property.Required)
            .build();
}
