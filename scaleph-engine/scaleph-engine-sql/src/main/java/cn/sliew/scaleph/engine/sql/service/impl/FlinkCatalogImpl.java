package cn.sliew.scaleph.engine.sql.service.impl;

import cn.sliew.scaleph.engine.sql.service.FlinkCatalogService;

import org.apache.flink.connector.jdbc.catalog.JdbcCatalog;
import org.apache.flink.table.catalog.hive.HiveCatalog;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.catalog.exceptions.TableNotExistException;

import org.apache.flink.table.api.*;
import org.apache.flink.table.catalog.*;
import org.apache.flink.table.catalog.exceptions.*;

import java.sql.*;
import java.util.*;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

public class FlinkCatalogImpl implements FlinkCatalogService {

    String catalogName = "my_catalog";

    Catalog currentCatalog;

    String catalogType = "";

    protected static class ObjectType {
        /**
         * 数据库
         */
        public static final String DATABASE = "database";
        /**
         * 数据表
         */
        public static final String TABLE = "TABLE";

        /**
         * 视图
         */
        public static final String VIEW = "VIEW";
    }

    protected static class CatalogType {

        public static final String INMEMORY = "INMEMORY";

        public static final String HIVE = "HIVE";

        public static final String JDBC = "JDBC";
    }

    protected String defaultDatabase = "scaleph";
    protected String username = "root";
    protected String password = "password";
    protected String baseUrl = "jdbc:mysql://localhost:3306";

    protected String hiveConfDir = "";

    protected ClassLoader userClassLoader;

    TableEnvironment tableEnv = TableEnvironment.create(EnvironmentSettings.inStreamingMode());

    public FlinkCatalogImpl(String catalogName, String catalogType, HashMap<String,String> properties) {
        this.catalogName = catalogName;
        this.catalogType = catalogType;
        if(catalogType.equalsIgnoreCase(CatalogType.HIVE)){
            this.hiveConfDir = properties.get("hiveConfDir");
            currentCatalog = new HiveCatalog(catalogName,defaultDatabase," ");
        }else if(catalogType.equalsIgnoreCase(CatalogType.INMEMORY)){
            currentCatalog = tableEnv.getCatalog("default_catalog").get();
        }else if(catalogType.equalsIgnoreCase(CatalogType.JDBC)){
            this.baseUrl = properties.get("baseUrl").endsWith("/") ? properties.get("baseUrl") : properties.get("baseUrl") + "/";
            this.username = properties.get("username");
            this.password = properties.get("password");
            this.defaultDatabase = properties.get("defaultDatabase");
            currentCatalog = new JdbcCatalog(Thread.currentThread().getContextClassLoader(),catalogName,defaultDatabase,username,password,baseUrl);
        }else{
            throw new CatalogException("No such catalog type, only allow INMEMORY, HIVE, JDBC");
        }
        tableEnv.registerCatalog(catalogName, currentCatalog);
    }

    public void setTableEnv(TableEnvironment tableEnv) {
        this.tableEnv = tableEnv;
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

    public TableEnvironment getTableEnv() {
        return tableEnv;
    }

    @Override
    public List<String> listDatabases() throws CatalogException {
//        return currentCatalog.listDatabases();
        return List.of(tableEnv.listDatabases());
    }

    @Override
    public String getCurrentDatabase() throws CatalogException {
        return tableEnv.getCurrentDatabase();
    }

    @Override
    public CatalogDatabase getDatabase(String dbName) throws DatabaseNotExistException, CatalogException {
        return currentCatalog.getDatabase(dbName);
    }

    @Override
    public void useDatabase(String dbName){
        tableEnv.useDatabase(dbName);
    }

    @Override
    public boolean databaseExists(String dbName) throws CatalogException {
        return currentCatalog.databaseExists(dbName);
    }

    @Override
    public void createDatabase(String dbName, HashMap<String,String> properties, @Nullable String comment, boolean ignoreIfExists) throws DatabaseAlreadyExistException, CatalogException {
        CatalogDatabaseImpl catalogDatabase = new CatalogDatabaseImpl(properties,comment);
        currentCatalog.createDatabase(dbName,catalogDatabase,ignoreIfExists);
    }

    @Override
    public void dropDatabase(String dbName, boolean ignoreIfNotExists, boolean cascade) throws DatabaseNotExistException, DatabaseNotEmptyException, CatalogException {
        currentCatalog.dropDatabase(dbName,ignoreIfNotExists,cascade);
    }

    @Override
    public void alterDatabase(String dbName, CatalogDatabase newDatabase, boolean ignoreIfNotExists) throws DatabaseNotExistException, CatalogException {
        currentCatalog.alterDatabase(dbName,newDatabase,ignoreIfNotExists);
    }

    @Override
    public List<String> listCurrentDbTables() throws CatalogException {
        return List.of(tableEnv.listTables());
    }

    @Override
    public List<String> listTables(String dbName) throws DatabaseNotExistException, CatalogException {
        return currentCatalog.listTables(dbName);
    }

    @Override
    public List<String> listCurrentDbViews() throws DatabaseNotExistException, CatalogException {
        return List.of(tableEnv.listViews());
    }

    @Override
    public List<String> listViews(String dbName) throws DatabaseNotExistException, CatalogException {
        return currentCatalog.listViews(dbName);
    }

    @Override
    public CatalogBaseTable getTable(String dbName, String tableName) throws TableNotExistException, CatalogException {
        ObjectPath objectPath  = new ObjectPath(dbName,tableName);
        return currentCatalog.getTable(objectPath);
    }

    @Override
    public boolean tableExists(String dbName, String tableName) throws CatalogException {
        ObjectPath objectPath  = new ObjectPath(dbName,tableName);
        return currentCatalog.tableExists(objectPath);
    }

    @Override
    public void dropTable(String dbName, String tableName, boolean ignoreIfNotExists) throws TableNotExistException, CatalogException {
        ObjectPath objectPath  = new ObjectPath(dbName,tableName);
//        System.out.println(objectPath.getFullName());
        tableEnv.dropTemporaryTable(objectPath.getFullName());
//        currentCatalog.dropTable(objectPath,ignoreIfNotExists);
    }

    @Override
    public void renameTable(String dbName, String tableName, String newTableName, boolean ignoreIfNotExists) throws TableNotExistException, TableAlreadyExistException, CatalogException {
        ObjectPath objectPath  = new ObjectPath(dbName,tableName);
        currentCatalog.renameTable(objectPath, newTableName,ignoreIfNotExists);
    }

    @Override
    public void createTable(String dbName, String tableName, HashMap<String,String> fieldKeyValue, boolean ignoreIfExists) throws TableAlreadyExistException, DatabaseNotExistException, CatalogException {
//        List<String> fields = new ArrayList<>(fieldKeyValue.keySet()) ;
//        List<String> fieldsType_S = new ArrayList<>(fieldKeyValue.values());
//        List<DataType> fieldsType= new ArrayList<>();
//        fieldsType_S.forEach((type)->fieldsType.add(dataTypeTranslator(type)));
//        Schema schema = Schema.newBuilder().fromFields(fields,fieldsType).build();
////        if(catalogType )
//        if(catalogType.equalsIgnoreCase("INMEMORY")){
//            TableDescriptor tableDescriptor = TableDescriptor.forManaged().schema(schema).build();
//            ObjectPath objectPath  = new ObjectPath(dbName,tableName);
//            tableEnv.createTable(objectPath.getFullName(),tableDescriptor);
//        }else{
//            ObjectPath objectPath  = new ObjectPath(dbName,tableName);
//            CatalogBaseTable catalogBaseTable = CatalogTable.of(schema,"comment",new ArrayList<>(),new HashMap<>());
//            TableDescriptor tableDescriptor = TableDescriptor.forManaged().schema(schema).build();
//
//            currentCatalog.createTable(objectPath,tableDescriptor.toCatalogTable(),ignoreIfExists);
//        }
    }

    @Override
    public void alterTable(String dbName, String tableName, CatalogBaseTable catalogBaseTable, boolean ignoreIfNotExists) throws TableNotExistException, CatalogException {
        ObjectPath objectPath  = new ObjectPath(dbName,tableName);
        currentCatalog.alterTable(objectPath,catalogBaseTable,ignoreIfNotExists);

    }

    @Override
    public List<CatalogPartitionSpec> listPartitions(String dbName, String tableName) throws TableNotExistException, TableNotPartitionedException, CatalogException {
        ObjectPath objectPath  = new ObjectPath(dbName,tableName);
        return currentCatalog.listPartitions(objectPath);
    }

    @Override
    public List<CatalogPartitionSpec> listPartitions(String dbName, String tableName, CatalogPartitionSpec catalogPartitionSpec) throws TableNotExistException, TableNotPartitionedException, PartitionSpecInvalidException, CatalogException {
        ObjectPath objectPath  = new ObjectPath(dbName,tableName);
        return currentCatalog.listPartitions(objectPath,catalogPartitionSpec);
    }

    @Override
    public CatalogPartition getPartition(String dbName, String tableName, CatalogPartitionSpec catalogPartitionSpec) throws PartitionNotExistException, CatalogException {
        ObjectPath objectPath  = new ObjectPath(dbName,tableName);
        return currentCatalog.getPartition(objectPath,catalogPartitionSpec);
    }

    @Override
    public boolean partitionExists(String dbName, String tableName, CatalogPartitionSpec catalogPartitionSpec) throws CatalogException {
        ObjectPath objectPath  = new ObjectPath(dbName,tableName);
        return currentCatalog.partitionExists(objectPath,catalogPartitionSpec);
    }

    @Override
    public void createPartition(String dbName, String tableName, CatalogPartitionSpec catalogPartitionSpec, CatalogPartition catalogPartition, boolean ignoreIfExists) throws TableNotExistException, TableNotPartitionedException, PartitionSpecInvalidException, PartitionAlreadyExistsException, CatalogException {
        ObjectPath objectPath  = new ObjectPath(dbName,tableName);
        currentCatalog.createPartition(objectPath,catalogPartitionSpec,catalogPartition,ignoreIfExists);
    }

    @Override
    public void dropPartition(String dbName, String tableName, CatalogPartitionSpec catalogPartitionSpec, boolean ignoreIfNotExists) throws PartitionNotExistException, CatalogException {
        ObjectPath objectPath  = new ObjectPath(dbName,tableName);
        currentCatalog.dropPartition(objectPath,catalogPartitionSpec,ignoreIfNotExists);
    }

    @Override
    public void alterPartition(String dbName, String tableName, CatalogPartitionSpec catalogPartitionSpec, CatalogPartition catalogPartition, boolean ignoreIfNotExists) throws PartitionNotExistException, CatalogException {
        ObjectPath objectPath  = new ObjectPath(dbName,tableName);
        currentCatalog.alterPartition(objectPath,catalogPartitionSpec,catalogPartition,ignoreIfNotExists);
    }

    @Override
    public List<String> listFunctions(String dbName) throws DatabaseNotExistException, CatalogException {
        return currentCatalog.listFunctions(dbName);
    }

    @Override
    public CatalogFunction getFunction(ObjectPath objectPath) throws FunctionNotExistException, CatalogException {
        return currentCatalog.getFunction(objectPath);
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

//    private DataType dataTypeTranslator(String dataType){
//
//        dataType = dataType.toUpperCase();
//        if(dataType.equals("BIGINT")){
//            return DataTypes.BIGINT();
//        }
//        return DataTypes.VARCHAR(42);
//    }
}