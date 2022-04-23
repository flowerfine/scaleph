package cn.sliew.breeze.meta.util;


import cn.sliew.breeze.common.enums.ConnectionTypeEnum;
import cn.sliew.breeze.common.enums.DataSourceTypeEnum;
import cn.sliew.breeze.common.enums.ResponseCodeEnum;
import cn.sliew.breeze.common.exception.CustomException;
import cn.sliew.breeze.meta.database.AbstractDatabaseMeta;
import cn.sliew.breeze.meta.database.MysqlDatabaseMeta;
import cn.sliew.breeze.meta.database.OracleDatabaseMeta;
import cn.sliew.breeze.service.dto.meta.DataSourceMetaDTO;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * JDBC工具类
 *
 * @author gleiyu
 */
public class JdbcUtil {

    private static final Logger log = LoggerFactory.getLogger(JdbcUtil.class);
    /**
     * datasource map
     */
    private static final ConcurrentMap<String, DruidDataSource> DATA_SOURCES = new ConcurrentHashMap<>();

    /**
     * 测试元数据信息是否可以连接到数据库
     *
     * @param meta 数据源信息
     * @return boolean
     * @throws Exception Exception
     */
    public static boolean testConnection(AbstractDatabaseMeta meta) throws Exception {
        Connection conn = null;
        if (meta != null) {
            //jdbc rdbms
            if (ConnectionTypeEnum.JDBC.getCode().equals(meta.getConnectionType().getCode())) {
                conn = getConnection(meta, ConnectionTypeEnum.JDBC);
            } else if (ConnectionTypeEnum.POOLED.getCode().equals(meta.getConnectionType().getCode())) {
                conn = getConnection(meta, ConnectionTypeEnum.POOLED);
            }
            return conn != null;
        } else {
            return false;
        }
    }

    /**
     * 测试元数据信息是否可以连接到数据库
     *
     * @param dataSourceMeta 数据源信息
     * @return boolean
     * @throws Exception Exception
     */
    public static boolean testConnection(DataSourceMetaDTO dataSourceMeta) throws Exception {
        AbstractDatabaseMeta adm = JdbcUtil.getDataBaseMetaByType(dataSourceMeta);
        return JdbcUtil.testConnection(adm);
    }

    private static AbstractDatabaseMeta getDataBaseMetaByType(DataSourceMetaDTO dsMeta) throws CustomException {
        if (dsMeta == null || dsMeta.getDataSourceType() == null) {
            throw new CustomException(ResponseCodeEnum.ERROR_UNSUPPORTED_CONNECTION.getValue());
        }
        String dataSourceTypeCode = dsMeta.getDataSourceType().getValue();
        if (DataSourceTypeEnum.MYSQL.getCode().equals(dataSourceTypeCode)) {
            return new MysqlDatabaseMeta(dsMeta);
        } else if (DataSourceTypeEnum.ORACLE.getCode().equals(dataSourceTypeCode)) {
            return new OracleDatabaseMeta(dsMeta);
        } else {
            throw new CustomException(ResponseCodeEnum.ERROR_UNSUPPORTED_CONNECTION.getValue());
        }
    }

    public static String getUrl(DataSourceMetaDTO dsMeta) throws CustomException {
        AbstractDatabaseMeta adm = JdbcUtil.getDataBaseMetaByType(dsMeta);
        return adm.getUrl();
    }

    public static String getDriver(DataSourceMetaDTO dsMeta) throws CustomException {
        AbstractDatabaseMeta adm = JdbcUtil.getDataBaseMetaByType(dsMeta);
        return adm.getDriverClass();
    }

    /**
     * 获取数据库连接，如出现异常则捕获
     *
     * @param dataSource 数据源信息
     * @return Connection
     */
    public static Connection getConnectionSilently(DataSource dataSource) {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error("get database connection error:{}", e.getMessage());
            return null;
        }
    }

    /**
     * 使用默认jdbc获取连接
     *
     * @param meta 元数据信息
     * @return connection
     */
    public static Connection getConnectionSilently(AbstractDatabaseMeta meta) {
        return getConnectionSilently(meta, ConnectionTypeEnum.JDBC);
    }

    /**
     * 获取数据库连接，如出现异常则捕获
     *
     * @param meta 数据源信息
     * @param type 连接方式
     * @return Connection
     */
    public static Connection getConnectionSilently(AbstractDatabaseMeta meta, ConnectionTypeEnum type) {
        switch (type) {
            case POOLED:
                log.info("connection database {} using connection pool", meta.getDatabaseName());
                DataSource dataSource = getDataSource(meta);
                return getConnectionSilently(dataSource);
            case JDBC:
                log.info("connection database {} using simple jdbc", meta.getDatabaseName());
                try {
                    Class.forName(meta.getDriverClass());
                    Properties props = new Properties();
                    props.put("user", meta.getUserName());
                    props.put("password", meta.getPassword());
                    if (meta.getJdbcProps() != null) {
                        props.putAll(meta.getPoolProps());
                    }
                    return DriverManager.getConnection(meta.getUrl(), props);
                } catch (SQLException | ClassNotFoundException e) {
                    log.error("get database connection error:{}", e.getMessage());
                }
                break;
            default:
                log.error("unsupported connection type!");
                return null;
        }
        return null;
    }

    /**
     * 获取数据库连接,如出现异常则抛出
     *
     * @param dataSource 数据源信息
     * @return Connection
     * @throws SQLException sql exception
     */
    public static Connection getConnection(DataSource dataSource) throws SQLException {
        if (dataSource == null) {
            return null;
        }
        return dataSource.getConnection();
    }

    /**
     * 获取数据库连接
     * 1.连接池方式
     * 2.jdbc普通连接方式
     *
     * @param meta database meta
     * @return Connection
     */
    public static Connection getConnection(AbstractDatabaseMeta meta, ConnectionTypeEnum type) throws SQLException, ClassNotFoundException {
        switch (type) {
            case POOLED:
                log.info("connection database {} using connection pool", meta.getDatabaseName());
                DataSource dataSource = getDataSource(meta);
                return getConnection(dataSource);
            case JDBC:
                log.info("connection database {} using simple jdbc", meta.getDatabaseName());
                Class.forName(meta.getDriverClass());
                Properties props = new Properties();
                props.put("user", meta.getUserName());
                props.put("password", meta.getPassword());
                if (meta.getJdbcProps() != null) {
                    props.putAll(meta.getJdbcProps());
                }
                return DriverManager.getConnection(meta.getUrl(), props);
            default:
                log.error("unsupported connection type!");
                return null;
        }
    }

    /**
     * 判断data source 是否已注册
     *
     * @param meta data base meta
     * @return boolean
     */
    private static boolean isDataSourceRegistered(AbstractDatabaseMeta meta) {
        String datasourceId = getDatasourceName(meta);
        return DATA_SOURCES.containsKey(datasourceId);
    }

    /**
     * 获取datasource的name
     *
     * @return datasource name md5 code
     */
    private static String getDatasourceName(AbstractDatabaseMeta meta) {
        StringBuilder builder = new StringBuilder();
        builder.append(meta.getDataSourceType().getCode())
                .append(meta.getHostName())
                .append(meta.getDataSourceName())
                .append(meta.getDatabaseName())
                .append(meta.getUserName())
                .append(meta.getPort());
        return DigestUtils.md5DigestAsHex(builder.toString().getBytes());
    }

    /**
     * 获取datasource
     *
     * @param meta database meta
     * @return datasource
     */
    public static DataSource getDataSource(AbstractDatabaseMeta meta) {
        if (meta == null) {
            return null;
        } else if (isDataSourceRegistered(meta)) {
            return DATA_SOURCES.get(getDatasourceName(meta));
        } else {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setName(meta.getDataSourceName());
            dataSource.setUsername(meta.getUserName());
            dataSource.setPassword(meta.getPassword());
            dataSource.setDriverClassName(meta.getDriverClass());
            dataSource.setUrl(meta.getUrl());
            if (meta.getPoolProps() != null) {
                try {
                    DruidDataSourceFactory.config(dataSource, meta.getPoolProps());
                } catch (SQLException e) {
                    log.error("get dataSource error:{}", e.getMessage());
                }
            }
            DATA_SOURCES.put(getDatasourceName(meta), dataSource);
            return dataSource;
        }
    }

    /**
     * 关闭数据库连接
     *
     * @param conn Connection
     */
    public static void closeSilently(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("close database connection error:{}", e.getMessage());
            }
        }
    }

    /**
     * 关闭Statement
     *
     * @param st Statement
     */
    public static void closeSilently(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                log.error("close statement error:{}", e.getMessage());
            }
        }
    }

    /**
     * 关闭 PreparedStatement
     *
     * @param pst PreparedStatement
     */
    public static void closeSilently(PreparedStatement pst) {
        if (pst != null) {
            try {
                pst.close();
            } catch (SQLException e) {
                log.error("close prepared statement error:{}", e.getMessage());
            }
        }
    }

    /**
     * 关闭 result set
     *
     * @param rs ResultSet
     */
    public static void closeSilently(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("close resultSet error:{}", e.getMessage());
            }
        }
    }

    /**
     * 关闭数据库连接
     *
     * @param conn Connection
     * @param st   Statement
     * @param rs   ResultSet
     */
    public static void closeSilently(Connection conn, Statement st, ResultSet rs) {
        closeSilently(rs);
        closeSilently(st);
        closeSilently(conn);
    }

    /**
     * 关闭数据库连接
     *
     * @param conn Connection
     * @param pst  PreparedStatement
     * @param rs   ResultSet
     */
    public static void closeSilently(Connection conn, PreparedStatement pst, ResultSet rs) {
        closeSilently(rs);
        closeSilently(pst);
        closeSilently(conn);
    }

    /**
     * 关闭数据库连接
     *
     * @param conn Connection
     * @param pst  PreparedStatement
     */
    public static void close(Connection conn, PreparedStatement pst) {
        closeSilently(pst);
        closeSilently(conn);
    }

}
