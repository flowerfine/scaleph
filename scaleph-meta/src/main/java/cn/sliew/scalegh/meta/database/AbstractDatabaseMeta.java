package cn.sliew.scalegh.meta.database;

import cn.hutool.core.util.StrUtil;
import cn.sliew.scalegh.common.constant.DictConstants;
import cn.sliew.scalegh.common.enums.ConnectionTypeEnum;
import cn.sliew.scalegh.common.enums.DataSourceTypeEnum;
import cn.sliew.scalegh.meta.util.JdbcUtil;
import cn.sliew.scalegh.service.dto.meta.DataSourceMetaDTO;
import cn.sliew.scalegh.service.dto.meta.TableColumnMetaDTO;
import cn.sliew.scalegh.service.dto.meta.TableMetaDTO;
import cn.sliew.scalegh.service.vo.DictVO;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 数据库连接元数据
 *
 * @author gleiyu
 */
public abstract class AbstractDatabaseMeta {

    private String dataSourceName;

    private DataSourceTypeEnum dataSourceType;

    private ConnectionTypeEnum connectionType;

    private String hostName;

    private String databaseName;

    private Integer port;

    private String userName;

    private String password;

    private Properties jdbcProps;

    private Properties poolProps;

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public DataSourceTypeEnum getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(DataSourceTypeEnum dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public ConnectionTypeEnum getConnectionType() {
        return connectionType == null ? ConnectionTypeEnum.JDBC : this.connectionType;
    }

    public void setConnectionType(ConnectionTypeEnum connectionType) {
        this.connectionType = connectionType;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Integer getPort() {
        return port == null ? this.getDefaultDatabasePort() : port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Properties getJdbcProps() {
        return jdbcProps;
    }

    public void setJdbcProps(Properties jdbcProps) {
        this.jdbcProps = jdbcProps;
    }

    public void setJdbcProps(String jdbcProps) {
        if (StrUtil.isEmpty(jdbcProps)) {
            this.jdbcProps = null;
        } else {
            Properties properties = new Properties();
            String[] prop = jdbcProps.split("\n");
            for (String s : prop) {
                String[] kv = s.split("=");
                if (kv.length == 2) {
                    properties.put(kv[0], kv[1]);
                }
            }
            this.jdbcProps = properties;
        }
    }

    public Properties getPoolProps() {
        return poolProps;
    }

    public void setPoolProps(Properties poolProps) {
        this.poolProps = poolProps;
    }

    public void setPoolProps(String poolProps) {
        if (StrUtil.isEmpty(poolProps)) {
            this.poolProps = null;
        } else {
            Properties properties = new Properties();
            String[] prop = poolProps.split("\n");
            for (String s : prop) {
                String[] kv = s.split("=");
                if (kv.length == 2) {
                    properties.put(kv[0], kv[1]);
                }
            }
            this.poolProps = properties;
        }
    }

    public AbstractDatabaseMeta(DataSourceMetaDTO meta) {
        this.setDataSourceName(meta.getDataSourceName());
        this.setConnectionType(ConnectionTypeEnum.valueOfName(meta.getConnectionType().getValue()));
        this.setHostName(meta.getHostName());
        this.setDatabaseName(meta.getDatabaseName());
        this.setPort(meta.getPort());
        this.setUserName(meta.getUserName());
        this.setPassword(meta.getPassword());
        this.setJdbcProps(meta.getJdbcProps());
        this.setPoolProps(meta.getPoolProps());
    }

    /**
     * 获取数据库默认端口号
     *
     * @return int
     */
    public abstract int getDefaultDatabasePort();

    /**
     * 是否支持事务
     *
     * @return true/false
     */
    public abstract boolean isSupportTransactions();

    /**
     * 获取驱动类名称
     *
     * @return string
     */
    public abstract String getDriverClass();

    /**
     * 获取拼接后的jdbc url
     *
     * @return url
     */
    public abstract String getUrl();

    /**
     * 通过自定义sql获取表meta信息
     *
     * @return table list
     * @throws SQLException exception
     */
    public abstract List<TableMetaDTO> getTables() throws SQLException;

    /**
     * 自定义sql获取字段信息
     *
     * @return table column list
     * @throws SQLException exception
     */
    public abstract Map<String, List<TableColumnMetaDTO>> getColumns() throws SQLException;

    /**
     * 从jdbc连接信息中获取meta信息
     *
     * @param meta             database meta 信息
     * @param schemaPattern    schema pattern 可以使用类似sql语法中的%查询
     * @param tableNamePattern tableName pattern 可以使用类似sql语法中的%查询
     * @return table list
     * @throws SQLException SQLException
     */
    public List<TableMetaDTO> getTables(AbstractDatabaseMeta meta, String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        List<TableMetaDTO> list = new ArrayList<>();
        Connection conn = JdbcUtil.getConnectionSilently(meta, ConnectionTypeEnum.JDBC);
        ResultSet rs;
        DatabaseMetaData dbMeta = conn.getMetaData();
        rs = dbMeta.getTables(catalog, schemaPattern, tableNamePattern, new String[]{"TABLE"});
        while (rs.next()) {
            TableMetaDTO table = new TableMetaDTO();
            table.setTableCatalog(rs.getString("TABLE_CAT") != null ? rs.getString("TABLE_CAT") : rs.getString("TABLE_SCHEM"));
            table.setTableSchema(rs.getString("TABLE_SCHEM") != null ? rs.getString("TABLE_SCHEM") : rs.getString("TABLE_CAT"));
            table.setTableName(rs.getString("TABLE_NAME"));
            table.setTableType(DictVO.toVO(DictConstants.TABLE_TYPE, rs.getString("table_type")));
            table.setTableComment(rs.getString("REMARKS"));
            list.add(table);
        }
        JdbcUtil.closeSilently(conn, null, rs);
        return list;
    }

    /**
     * 拼接参数字符串
     *
     * @param attrs 参数
     * @return prop string
     */
    protected String buildPropStr(Properties attrs) {
        StringBuilder builder = new StringBuilder("?");
        if (attrs != null) {
            attrs.forEach((k, v) -> {
                builder.append(k).append("=").append(v).append("&");
            });
            return builder.substring(0, builder.length() - 1);
        } else {
            return "";
        }
    }

}
