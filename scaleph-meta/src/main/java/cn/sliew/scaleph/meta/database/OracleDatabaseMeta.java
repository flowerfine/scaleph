package cn.sliew.scaleph.meta.database;

import cn.sliew.scaleph.common.constant.DictConstants;
import cn.sliew.scaleph.common.enums.DataSourceTypeEnum;
import cn.sliew.scaleph.meta.util.JdbcUtil;
import cn.sliew.scaleph.meta.service.dto.DataSourceMetaDTO;
import cn.sliew.scaleph.meta.service.dto.TableColumnMetaDTO;
import cn.sliew.scaleph.meta.service.dto.TableMetaDTO;
import cn.sliew.scaleph.system.service.vo.DictVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author gleiyu
 */
public class OracleDatabaseMeta extends AbstractDatabaseMeta {

    public OracleDatabaseMeta(DataSourceMetaDTO meta) {
        super(meta);
        this.setDataSourceType(DataSourceTypeEnum.ORACLE);
    }

    @Override
    public int getDefaultDatabasePort() {
        return 1521;
    }

    @Override
    public boolean isSupportTransactions() {
        return true;
    }

    @Override
    public String getDriverClass() {
        return "oracle.jdbc.driver.OracleDriver";
    }

    @Override
    public String getUrl() {
        return "jdbc:oracle:thin:@//" + this.getHostName() + ":" + this.getPort() + "/" + this.getDatabaseName();
    }

    @Override
    public List<TableMetaDTO> getTables(AbstractDatabaseMeta meta, String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        //设置获取remark信息
        if (meta.getJdbcProps() == null) {
            meta.setJdbcProps(new Properties());
        }
        meta.getJdbcProps().put("remarks", "true");
        return super.getTables(meta, null, schemaPattern.toUpperCase(), tableNamePattern.toUpperCase());
    }

    @Override
    public List<TableMetaDTO> getTables() throws SQLException {
        List<TableMetaDTO> result = new ArrayList<>();
        String sql = "select null as table_cat,o.owner as table_schema,o.object_name as table_name," +
                "o.object_type as table_type,c.comments as table_comment,t.tablespace_name as table_space," +
                "case when t.partitioned = 'NO' then t.num_rows else p.num_rows end as table_rows," +
                "nvl(d.total_data_bytes,0) as data_bytes,nvl(i.total_index_bytes,0) as index_bytes," +
                "o.created as create_time,o.last_ddl_time as last_ddl_time," +
                "decode(t.partitioned, 'NO', '0', 'YES', '1') as is_partitioned " +
                "from all_objects o,all_tab_comments c,all_tables t," +
                "(select t1.owner,t1.segment_name,sum(nvl(t1.bytes, 0) + nvl(t2.lob_data_bytes, 0)) as total_data_bytes " +
                "from dba_segments t1,(select b.owner, b.table_name, sum(t.bytes) as lob_data_bytes " +
                "from dba_segments t, dba_lobs b where t.owner = b.owner and t.segment_name = b.segment_name " +
                "group by b.owner, b.table_name) t2 where t1.owner = t2.owner(+) and t1.segment_name = t2.table_name(+) " +
                "and t1.segment_type in ('TABLE', 'TABLE PARTITION') group by t1.owner, t1.segment_name) d," +
                "(select table_owner, table_name, nvl(sum(num_rows), 0) as num_rows from all_tab_partitions o " +
                "group by table_owner, table_name) p,(select t1.table_owner,t1.table_name,sum(t2.bytes) as total_index_bytes " +
                "from (select t.table_owner, t.table_name, t.index_name from all_indexes t union " +
                "select t.owner, t.table_name, t.index_name from dba_lobs t) t1, dba_segments t2 " +
                "where t1.table_owner = t2.owner and t1.index_name = t2.segment_name group by t1.table_owner, t1.table_name) i " +
                "where o.owner = c.owner(+) and o.object_name = c.table_name(+) and o.owner = t.owner(+)" +
                "and o.object_name = t.table_name(+) and o.owner = d.owner(+) and o.object_name = d.segment_name(+)" +
                "and o.owner = p.table_owner(+) and o.object_name = p.table_name(+) and o.owner = i.table_owner(+)" +
                "and o.object_name = i.table_name(+) and o.owner in (?) and o.object_type in ('VIEW', 'TABLE')";
        Connection conn = JdbcUtil.getConnectionSilently(this);
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, this.getUserName().toUpperCase());
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            TableMetaDTO table = new TableMetaDTO();
            table.setTableCatalog(rs.getString("table_cat"));
            table.setTableSchema(rs.getString("table_schema"));
            table.setTableName(rs.getString("table_name"));
            table.setTableType(DictVO.toVO(DictConstants.TABLE_TYPE, rs.getString("table_type")));
            table.setTableComment(rs.getString("table_comment"));
            table.setTableSpace(rs.getString("table_space"));
            table.setTableRows(rs.getLong("table_rows"));
            table.setDataBytes(rs.getLong("data_bytes"));
            table.setIndexBytes(rs.getLong("index_bytes"));
            table.setTableCreateTime(rs.getTimestamp("create_time"));
            table.setLastDdlTime(rs.getTimestamp("last_ddl_time"));
            table.setIsPartitioned(DictVO.toVO(DictConstants.YES_NO, rs.getString("is_partitioned")));
            result.add(table);
        }
        JdbcUtil.closeSilently(conn, pstm, rs);
        return result;
    }

    @Override
    public Map<String, List<TableColumnMetaDTO>> getColumns() throws SQLException {
        Map<String, List<TableColumnMetaDTO>> result = new HashMap<>(16);
        String sql = "select tc.table_name,tc.column_name,tc.data_type,tc.data_length,tc.data_precision," +
                "tc.data_scale,decode(tc.nullable, 'y', '1', 'n', '0') as nullable,tc.data_default," +
                "tc.low_value,tc.high_value,tc.column_id as column_ordinal,cc.comments as column_comment," +
                "case when tic.column_name is not null then '1' else '0' end as is_primary_key " +
                "from all_tab_columns tc,all_col_comments cc," +
                "(select t.index_name, ic.column_name, ic.table_owner, ic.table_name " +
                "from all_constraints t, all_ind_columns ic where t.index_owner = ic.index_owner " +
                "and t.index_name = ic.index_name and t.constraint_type = 'p' ) tic " +
                "where tc.owner = cc.owner(+) and tc.table_name = cc.table_name(+) " +
                "and tc.column_name = cc.column_name(+) and tc.owner = tic.table_owner(+) " +
                "and tc.table_name = tic.table_name(+) and tc.column_name = tic.column_name(+) " +
                "and tc.owner = ?";
        Connection conn = JdbcUtil.getConnectionSilently(this);
        PreparedStatement pstm = conn.prepareStatement(sql);
        pstm.setString(1, this.getUserName().toUpperCase());
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
            column.setLowValue(rs.getString("low_value"));
            column.setHighValue(rs.getString("high_value"));
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
