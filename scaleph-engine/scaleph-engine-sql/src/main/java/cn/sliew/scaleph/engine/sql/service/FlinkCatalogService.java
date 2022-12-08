package cn.sliew.scaleph.engine.sql.service;

import org.apache.flink.table.catalog.*;
import org.apache.flink.table.catalog.exceptions.*;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public interface FlinkCatalogService {

    List<String> listDatabases() throws CatalogException;

    String getCurrentDatabase() throws DatabaseNotExistException, CatalogException;

    CatalogDatabase getDatabase(String dbName) throws DatabaseNotExistException, CatalogException;

    void useDatabase(String dbName);

    boolean databaseExists(String dbName) throws CatalogException;

    void createDatabase(String dbName, HashMap<String,String> properties, @Nullable String comment, boolean ignoreIfExists) throws DatabaseAlreadyExistException, CatalogException;


    void dropDatabase(String dbName, boolean ignoreIfNotExists, boolean cascade) throws DatabaseNotExistException, DatabaseNotEmptyException, CatalogException;

    void alterDatabase(String dbName, CatalogDatabase newDatabase, boolean ignoreIfNotExists) throws DatabaseNotExistException, CatalogException;

    List<String> listCurrentDbTables() throws DatabaseNotExistException, CatalogException;

    List<String> listTables(String dbName) throws DatabaseNotExistException, CatalogException;

    List<String> listCurrentDbViews() throws DatabaseNotExistException, CatalogException;

    List<String> listViews(String dbName) throws DatabaseNotExistException, CatalogException;

    CatalogBaseTable getTable(String dbName, String tableName) throws TableNotExistException, CatalogException;

    boolean tableExists(String dbName, String tableName) throws CatalogException;

    void dropTable(String dbName, String tableName, boolean ignoreIfNotExists) throws TableNotExistException, CatalogException;

    void renameTable(String dbName, String tableName, String newTableName, boolean ignoreIfNotExists) throws TableNotExistException, TableAlreadyExistException, CatalogException;

    void createTable(String dbName, String tableName, HashMap<String,String> fieldKeyValue, boolean ignoreIfExists) throws TableAlreadyExistException, DatabaseNotExistException, CatalogException;

    void alterTable(String dbName, String tableName, CatalogBaseTable catalogBaseTable, boolean ignoreIfNotExists) throws TableNotExistException, CatalogException;

    List<CatalogPartitionSpec> listPartitions(String dbName, String tableName) throws TableNotExistException, TableNotPartitionedException, CatalogException;

    List<CatalogPartitionSpec> listPartitions(String dbName, String tableName, CatalogPartitionSpec catalogPartitionSpec) throws TableNotExistException, TableNotPartitionedException, PartitionSpecInvalidException, CatalogException;

    CatalogPartition getPartition(String dbName, String tableName, CatalogPartitionSpec catalogPartitionSpec) throws PartitionNotExistException, CatalogException;

    boolean partitionExists(String dbName, String tableName, CatalogPartitionSpec catalogPartitionSpec) throws CatalogException;

    void createPartition(String dbName, String tableName, CatalogPartitionSpec catalogPartitionSpec, CatalogPartition catalogPartition, boolean ignoreIfExists) throws TableNotExistException, TableNotPartitionedException, PartitionSpecInvalidException, PartitionAlreadyExistsException, CatalogException;

    void dropPartition(String dbName, String tableName, CatalogPartitionSpec catalogPartitionSpec, boolean ignoreIfNotExists) throws PartitionNotExistException, CatalogException;

    void alterPartition(String dbName, String tableName, CatalogPartitionSpec catalogPartitionSpec, CatalogPartition catalogPartition, boolean ignoreIfNotExists) throws PartitionNotExistException, CatalogException;

    List<String> listFunctions(String dbName) throws DatabaseNotExistException, CatalogException;

    CatalogFunction getFunction(ObjectPath objectPath) throws FunctionNotExistException, CatalogException;

    boolean functionExists(ObjectPath objectPath) throws CatalogException;

    void createFunction(ObjectPath objectPath, CatalogFunction catalogFunction, boolean b) throws FunctionAlreadyExistException, DatabaseNotExistException, CatalogException;

    void alterFunction(ObjectPath objectPath, CatalogFunction catalogFunction, boolean b) throws FunctionNotExistException, CatalogException;

    void dropFunction(ObjectPath objectPath, boolean b) throws FunctionNotExistException, CatalogException;
}
