package com.liyu.breeze.meta.database;

import com.liyu.breeze.common.constant.DictConstants;
import com.liyu.breeze.common.enums.DataSourceTypeEnum;
import com.liyu.breeze.meta.util.JdbcUtil;
import com.liyu.breeze.service.dto.meta.DataSourceMetaDTO;
import com.liyu.breeze.service.dto.meta.TableColumnMetaDTO;
import com.liyu.breeze.service.dto.meta.TableMetaDTO;
import com.liyu.breeze.service.vo.DictVO;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author gleiyu
 */
@Slf4j
public class MysqlDatabaseMeta extends AbstractDatabaseMeta {

    public MysqlDatabaseMeta(DataSourceMetaDTO meta) {
        super(meta);
        this.setDataSourceType(DataSourceTypeEnum.MYSQL);
    }

    @Override
    public int getDefaultDatabasePort() {
        return 3306;
    }

    @Override
    public boolean isSupportTransactions() {
        return true;
    }

    @Override
    public String getDriverClass() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    public String getUrl() {
        return "jdbc:mysql://" + this.getHostName() + ":" + this.getPort() + "/" + this.getDatabaseName() + buildPropStr(this.getJdbcProps());
    }

    @Override
    public List<TableMetaDTO> getTables(AbstractDatabaseMeta meta, String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        //设置获取remark信息
        if (meta.getJdbcProps() == null) {
            meta.setJdbcProps(new Properties());
        }
        meta.getJdbcProps().put("remarks", "true");
        meta.getJdbcProps().put("useInformationSchema", "true");
        return super.getTables(meta, catalog, schemaPattern, tableNamePattern);
    }

    @Override
    public List<TableMetaDTO> getTables() throws SQLException {
        List<TableMetaDTO> result = new ArrayList<>();
        String sql = "select '' as table_catalog,t.table_schema,t.table_name," +
                "case when t.table_type='BASE TABLE' then 'TABLE' when t.table_type='SYSTEM VIEW' then 'VIEW' " +
                "else t.table_type end as table_type,t.table_comment as comment,t.table_rows,t.data_length as data_bytes," +
                "t.index_length as index_bytes,t.create_time,t.create_time as last_ddl_time,t.update_time as last_access_time," +
                "case when t1.table_name is not null then '1' else '0' end as is_partitioned " +
                "from information_schema.tables t " +
                "left join (select p.table_catalog,p.table_schema,p.table_name " +
                "from information_schema.partitions p " +
                "where p.table_schema = ? and p.partition_name is not null " +
                "group by p.table_catalog,p.table_schema,p.table_name) t1 " +
                "on t.table_catalog = t1.table_catalog " +
                "and t.table_schema = t1.table_schema " +
                "and t.table_name = t1.table_name " +
                "where t.table_schema = ? ";
        Connection conn = JdbcUtil.getConnectionSilently(this);
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, this.getDatabaseName());
        pstm.setString(2, this.getDatabaseName());
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            TableMetaDTO table = new TableMetaDTO();
            table.setTableCatalog(rs.getString("table_catalog"));
            table.setTableSchema(rs.getString("table_schema"));
            table.setTableName(rs.getString("table_name"));
            table.setTableType(DictVO.toVO(DictConstants.TABLE_TYPE, rs.getString("table_type")));
            table.setTableComment(rs.getString("comment"));
            table.setTableRows(rs.getLong("table_rows"));
            table.setDataBytes(rs.getLong("data_bytes"));
            table.setIndexBytes(rs.getLong("index_bytes"));
            table.setTableCreateTime(rs.getTimestamp("create_time"));
            table.setLastDdlTime(rs.getTimestamp("last_ddl_time"));
            table.setLastAccessTime(rs.getTimestamp("last_access_time"));
            table.setIsPartitioned(DictVO.toVO(DictConstants.YES_NO, rs.getString("is_partitioned")));
            result.add(table);
        }
        JdbcUtil.closeSilently(conn, pstm, rs);
        return result;
    }

    @Override
    public Map<String, List<TableColumnMetaDTO>> getColumns() throws SQLException {
        Map<String, List<TableColumnMetaDTO>> result = new HashMap<>(16);
        String sql = "select table_name,column_name,data_type,character_maximum_length as data_length," +
                "numeric_precision as data_precision,numeric_scale as data_scale," +
                "case when is_nullable='YES' then 1 else '0' end as nullable,column_default as data_default," +
                "ordinal_position as column_ordinal,column_comment," +
                "case when column_key='PRI' then '1' else '0' end as is_primary_key " +
                "from information_schema.columns t " +
                "where t.table_schema = ? ";
        Connection conn = JdbcUtil.getConnectionSilently(this);
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, this.getDatabaseName());
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            TableColumnMetaDTO column = new TableColumnMetaDTO();
            String tableName = rs.getString("table_name");
            column.setColumnName(rs.getString("column_name"));
            column.setDataType(rs.getString("data_type"));
            column.setDataLength(rs.getLong("data_length"));
            column.setDataPrecision(rs.getInt("data_precision"));
            column.setDataScale(rs.getInt("data_scale"));
            column.setNullable(DictVO.toVO(DictConstants.YES_NO, rs.getString("nullable")));
            column.setDataDefault(rs.getString("data_default"));
            column.setColumnOrdinal(rs.getInt("column_ordinal"));
            column.setColumnComment(rs.getString("column_comment"));
            column.setIsPrimaryKey(DictVO.toVO(DictConstants.YES_NO, rs.getString("is_primary_key")));
            if (result.containsKey(tableName)) {
                List<TableColumnMetaDTO> list = result.get(tableName);
                list.add(column);
            } else {
                List<TableColumnMetaDTO> list = new ArrayList<>();
                list.add(column);
                result.put(tableName, list);
            }
        }
        JdbcUtil.closeSilently(conn, pstm, rs);
        return result;
    }
}
