package cn.sliew.scaleph.engine.sql.service.impl;

import cn.sliew.scaleph.engine.sql.service.factory.FlinkMysqlCatalogFactoryOption;

import org.apache.commons.compress.utils.Lists;
import org.apache.flink.util.Preconditions;
import org.apache.flink.util.StringUtils;
import org.apache.flink.util.TemporaryClassLoaderContext;
import org.apache.flink.table.catalog.exceptions.TableNotExistException;
import org.apache.flink.table.catalog.stats.CatalogColumnStatistics;
import org.apache.flink.table.catalog.stats.CatalogTableStatistics;
import org.apache.flink.table.expressions.Expression;

import org.apache.flink.table.api.*;
import org.apache.flink.table.catalog.*;
import org.apache.flink.table.catalog.exceptions.*;

import java.sql.*;
import java.util.*;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlinkCatalogImpl extends AbstractCatalog {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    String catalogName = "my_catalog";

    protected static final String defaultDatabase = "default_database";
    protected String username = "root";
    protected String password = "password";
    protected String baseUrl = "jdbc:mysql://localhost:3306";

    protected ClassLoader userClassLoader;

    private static final Set<String> builtinDatabases = new HashSet<String>() {
        {
            this.add("information_schema");
            this.add("mysql");
            this.add("performance_schema");
            this.add("sys");
        }
    };

    TableEnvironment tableEnv;

    Catalog currentCatalog;

    public FlinkCatalogImpl(String name) {
        super(name, defaultDatabase);
        this.baseUrl = FlinkMysqlCatalogFactoryOption.URL.defaultValue();
        this.username = FlinkMysqlCatalogFactoryOption.USERNAME.defaultValue();
        this.password = FlinkMysqlCatalogFactoryOption.PASSWORD.defaultValue();
    }

    public FlinkCatalogImpl(String name, String baseUrl, String username, String password) {
        super(name, defaultDatabase);
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        this.username = username;
        this.password = password;

    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    @Override
    public void open() throws CatalogException {
        TemporaryClassLoaderContext ignored = TemporaryClassLoaderContext.of(this.userClassLoader);
        Throwable var2 = null;

        try {
            try {
                Connection conn = DriverManager.getConnection(this.baseUrl, this.username, this.password);
                Object var4 = null;
                if (conn != null) {
                    if (var4 != null) {
                        try {
                            conn.close();
                        } catch (Throwable var15) {
                            ((Throwable)var4).addSuppressed(var15);
                        }
                    } else {
                        conn.close();
                    }
                }
            } catch (SQLException var16) {
                throw new ValidationException(String.format("Failed connecting to %s via JDBC.", this.baseUrl), var16);
            }

            logger.info("Catalog {} established connection to {}", this.getName(), this.baseUrl);
        } catch (Throwable var17) {
            var2 = var17;
            throw var17;
        } finally {
            if (ignored != null) {
                if (var2 != null) {
                    try {
                        ignored.close();
                    } catch (Throwable var14) {
                        var2.addSuppressed(var14);
                    }
                } else {
                    ignored.close();
                }
            }

        }
    }

    @Override
    public void close() throws CatalogException {
        logger.info("Catalog {} closing", this.getName());
    }

    @Override
    public List<String> listDatabases() throws CatalogException {
        return this.extractColumnValuesBySQL(this.baseUrl, "SELECT `SCHEMA_NAME` FROM `INFORMATION_SCHEMA`.`SCHEMATA`;", 1, (dbName) -> {
            return !builtinDatabases.contains(dbName);
        }, new Object[0]);
    }

    @Override
    public CatalogDatabase getDatabase(String dbName) throws DatabaseNotExistException, CatalogException {
        Preconditions.checkState(!StringUtils.isNullOrWhitespaceOnly(dbName), "Database name must not be blank.");
        if (this.listDatabases().contains(dbName)) {
            return new CatalogDatabaseImpl(Collections.emptyMap(), (String)null);
        } else {
            throw new DatabaseNotExistException(this.getName(), dbName);
        }
    }

    @Override
    public boolean databaseExists(String dbName) throws CatalogException {
        Preconditions.checkArgument(!StringUtils.isNullOrWhitespaceOnly(dbName));
        return this.listDatabases().contains(dbName);
    }

    @Override
    public void createDatabase(String dbName, CatalogDatabase catalogDatabase, boolean ignoreIfExists) throws DatabaseAlreadyExistException, CatalogException {
        Preconditions.checkArgument(!StringUtils.isNullOrWhitespaceOnly(dbName));
        Preconditions.checkNotNull(catalogDatabase);
        if (databaseExists(dbName)) {
            if (!ignoreIfExists) {
                throw new DatabaseAlreadyExistException(getName(), dbName);
            }
        } else {
            // 在这里实现创建库的代码
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(this.baseUrl, this.username, this.password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            // 启动事务
            String insertSql = "insert into information_schema.SCHEMATA (catalog_name, schema_name) values(?, ?)";
            //卡在这
            try (PreparedStatement stat = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                conn.setAutoCommit(false);
                stat.setString(1, dbName);
                stat.setString(2, this.catalogName);
                stat.executeUpdate();
                ResultSet idRs = stat.getGeneratedKeys();
                if (idRs.next() && catalogDatabase.getProperties() != null && catalogDatabase.getProperties().size() > 0) {
                    int id = idRs.getInt(1);
                    String propInsertSql = "insert into metadata_database_property(database_id, "
                            + "`key`,`value`) values (?,?,?)";
                    PreparedStatement pstat = conn.prepareStatement(propInsertSql);
                    for (Map.Entry<String, String> entry : catalogDatabase.getProperties().entrySet()) {
                        pstat.setInt(1, id);
                        pstat.setString(2, entry.getKey());
                        pstat.setString(3, entry.getValue());
                        pstat.addBatch();
                    }
                    pstat.executeBatch();
                    pstat.close();
                }
                conn.commit();
            } catch (SQLException e) {
//                sqlExceptionHappened = true;
                throw new RuntimeException(e);
//                logger.error("创建 database 信息失败：", e);
            }
        }
    }

    @Override
    public void dropDatabase(String s, boolean b, boolean b1) throws DatabaseNotExistException, DatabaseNotEmptyException, CatalogException {

    }

    @Override
    public void alterDatabase(String s, CatalogDatabase catalogDatabase, boolean b) throws DatabaseNotExistException, CatalogException {

    }

    @Override
    public List<String> listTables(String databaseName) throws DatabaseNotExistException, CatalogException {
        Preconditions.checkState(org.apache.commons.lang3.StringUtils.isNotBlank(databaseName), "Database name must not be blank.");
        if (!this.databaseExists(databaseName)) {
            throw new DatabaseNotExistException(this.getName(), databaseName);
        } else {
            return this.extractColumnValuesBySQL(this.baseUrl + databaseName, "SELECT TABLE_NAME FROM information_schema.`TABLES` WHERE TABLE_SCHEMA = ?", 1, (Predicate)null, new Object[]{databaseName});
        }
    }

    @Override
    public List<String> listViews(String s) throws DatabaseNotExistException, CatalogException {
        return null;
    }

    @Override
    public CatalogBaseTable getTable(ObjectPath objectPath) throws TableNotExistException, CatalogException {
        return null;
    }

    @Override
    public boolean tableExists(ObjectPath objectPath) throws CatalogException {
        return false;
    }

    @Override
    public void dropTable(ObjectPath objectPath, boolean b) throws TableNotExistException, CatalogException {

    }

    @Override
    public void renameTable(ObjectPath objectPath, String s, boolean b) throws TableNotExistException, TableAlreadyExistException, CatalogException {

    }

    @Override
    public void createTable(ObjectPath objectPath, CatalogBaseTable catalogBaseTable, boolean b) throws TableAlreadyExistException, DatabaseNotExistException, CatalogException {

    }

    @Override
    public void alterTable(ObjectPath objectPath, CatalogBaseTable catalogBaseTable, boolean b) throws TableNotExistException, CatalogException {

    }

    @Override
    public List<CatalogPartitionSpec> listPartitions(ObjectPath objectPath) throws TableNotExistException, TableNotPartitionedException, CatalogException {
        return null;
    }

    @Override
    public List<CatalogPartitionSpec> listPartitions(ObjectPath objectPath, CatalogPartitionSpec catalogPartitionSpec) throws TableNotExistException, TableNotPartitionedException, PartitionSpecInvalidException, CatalogException {
        return null;
    }

    @Override
    public List<CatalogPartitionSpec> listPartitionsByFilter(ObjectPath objectPath, List<Expression> list) throws TableNotExistException, TableNotPartitionedException, CatalogException {
        return null;
    }

    @Override
    public CatalogPartition getPartition(ObjectPath objectPath, CatalogPartitionSpec catalogPartitionSpec) throws PartitionNotExistException, CatalogException {
        return null;
    }

    @Override
    public boolean partitionExists(ObjectPath objectPath, CatalogPartitionSpec catalogPartitionSpec) throws CatalogException {
        return false;
    }

    @Override
    public void createPartition(ObjectPath objectPath, CatalogPartitionSpec catalogPartitionSpec, CatalogPartition catalogPartition, boolean b) throws TableNotExistException, TableNotPartitionedException, PartitionSpecInvalidException, PartitionAlreadyExistsException, CatalogException {

    }

    @Override
    public void dropPartition(ObjectPath objectPath, CatalogPartitionSpec catalogPartitionSpec, boolean b) throws PartitionNotExistException, CatalogException {

    }

    @Override
    public void alterPartition(ObjectPath objectPath, CatalogPartitionSpec catalogPartitionSpec, CatalogPartition catalogPartition, boolean b) throws PartitionNotExistException, CatalogException {

    }

    @Override
    public List<String> listFunctions(String s) throws DatabaseNotExistException, CatalogException {
        return null;
    }

    @Override
    public CatalogFunction getFunction(ObjectPath objectPath) throws FunctionNotExistException, CatalogException {
        return null;
    }

    @Override
    public boolean functionExists(ObjectPath objectPath) throws CatalogException {
        return false;
    }

    @Override
    public void createFunction(ObjectPath objectPath, CatalogFunction catalogFunction, boolean b) throws FunctionAlreadyExistException, DatabaseNotExistException, CatalogException {

    }

    @Override
    public void alterFunction(ObjectPath objectPath, CatalogFunction catalogFunction, boolean b) throws FunctionNotExistException, CatalogException {

    }

    @Override
    public void dropFunction(ObjectPath objectPath, boolean b) throws FunctionNotExistException, CatalogException {

    }

    @Override
    public CatalogTableStatistics getTableStatistics(ObjectPath objectPath) throws TableNotExistException, CatalogException {
        return null;
    }

    @Override
    public CatalogColumnStatistics getTableColumnStatistics(ObjectPath objectPath) throws TableNotExistException, CatalogException {
        return null;
    }

    @Override
    public CatalogTableStatistics getPartitionStatistics(ObjectPath objectPath, CatalogPartitionSpec catalogPartitionSpec) throws PartitionNotExistException, CatalogException {
        return null;
    }

    @Override
    public CatalogColumnStatistics getPartitionColumnStatistics(ObjectPath objectPath, CatalogPartitionSpec catalogPartitionSpec) throws PartitionNotExistException, CatalogException {
        return null;
    }

    @Override
    public void alterTableStatistics(ObjectPath objectPath, CatalogTableStatistics catalogTableStatistics, boolean b) throws TableNotExistException, CatalogException {

    }

    @Override
    public void alterTableColumnStatistics(ObjectPath objectPath, CatalogColumnStatistics catalogColumnStatistics, boolean b) throws TableNotExistException, CatalogException, TablePartitionedException {

    }

    @Override
    public void alterPartitionStatistics(ObjectPath objectPath, CatalogPartitionSpec catalogPartitionSpec, CatalogTableStatistics catalogTableStatistics, boolean b) throws PartitionNotExistException, CatalogException {

    }

    @Override
    public void alterPartitionColumnStatistics(ObjectPath objectPath, CatalogPartitionSpec catalogPartitionSpec, CatalogColumnStatistics catalogColumnStatistics, boolean b) throws PartitionNotExistException, CatalogException {

    }

    protected List<String> extractColumnValuesBySQL(String connUrl, String sql, int columnIndex, Predicate<String> filterFunc, Object... params) {
        List<String> columnValues = Lists.newArrayList();

        try {
            Connection conn = DriverManager.getConnection(connUrl, this.username, this.password);
            Throwable var8 = null;

            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                Throwable var10 = null;

                try {
                    if (Objects.nonNull(params) && params.length > 0) {
                        for(int i = 0; i < params.length; ++i) {
                            ps.setObject(i + 1, params[i]);
                        }
                    }

                    ResultSet rs = ps.executeQuery();

                    while(rs.next()) {
                        String columnValue = rs.getString(columnIndex);
                        if (Objects.isNull(filterFunc) || filterFunc.test(columnValue)) {
                            columnValues.add(columnValue);
                        }
                    }

                    ArrayList var43 = (ArrayList) columnValues;
                    return var43;
                } catch (Throwable var37) {
                    var10 = var37;
                    throw var37;
                } finally {
                    if (ps != null) {
                        if (var10 != null) {
                            try {
                                ps.close();
                            } catch (Throwable var36) {
                                var10.addSuppressed(var36);
                            }
                        } else {
                            ps.close();
                        }
                    }

                }
            } catch (Throwable var39) {
                var8 = var39;
                throw var39;
            } finally {
                if (conn != null) {
                    if (var8 != null) {
                        try {
                            conn.close();
                        } catch (Throwable var35) {
                            var8.addSuppressed(var35);
                        }
                    } else {
                        conn.close();
                    }
                }

            }
        } catch (Exception var41) {
            throw new CatalogException(String.format("The following SQL query could not be executed (%s): %s", connUrl, sql), var41);
        }
    }
}