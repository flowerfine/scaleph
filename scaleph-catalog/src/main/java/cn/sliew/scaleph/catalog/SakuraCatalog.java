/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.catalog;

import cn.sliew.scaleph.catalog.factory.CatalogDatabaseFactory;
import cn.sliew.scaleph.catalog.factory.CatalogFunctionFactory;
import cn.sliew.scaleph.catalog.factory.CatalogTableFactory;
import cn.sliew.scaleph.catalog.factory.CatalogViewFactory;
import cn.sliew.scaleph.catalog.service.CatalogService;
import cn.sliew.scaleph.catalog.service.dto.CatalogDatabaseDTO;
import cn.sliew.scaleph.catalog.service.dto.CatalogFunctionDTO;
import cn.sliew.scaleph.catalog.service.dto.CatalogTableDTO;
import org.apache.flink.table.catalog.*;
import org.apache.flink.table.catalog.exceptions.*;
import org.apache.flink.table.catalog.stats.CatalogColumnStatistics;
import org.apache.flink.table.catalog.stats.CatalogTableStatistics;
import org.apache.flink.table.expressions.Expression;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SakuraCatalog extends AbstractCatalog {

    private final CatalogService catalogService;

    public SakuraCatalog(String name, String defaultDatabase, CatalogService catalogService) {
        super(name, defaultDatabase);
        this.catalogService = catalogService;
    }

    @Override
    public void open() throws CatalogException {

    }

    @Override
    public void close() throws CatalogException {

    }

    @Override
    public List<String> listDatabases() throws CatalogException {
        return catalogService.listDatabases(getName()).stream().map(CatalogDatabaseDTO::getName).collect(Collectors.toList());
    }

    @Override
    public CatalogDatabase getDatabase(String databaseName) throws DatabaseNotExistException, CatalogException {
        Optional<CatalogDatabaseDTO> optional = catalogService.getDatabase(getName(), databaseName);
        return optional.map(CatalogDatabaseFactory::toDatabase)
                .orElseThrow(() -> new DatabaseNotExistException(getName(), databaseName));
    }

    @Override
    public boolean databaseExists(String databaseName) throws CatalogException {
        return catalogService.databaseExists(getName(), databaseName);
    }

    @Override
    public void createDatabase(String name, CatalogDatabase database, boolean ignoreIfExists) throws DatabaseAlreadyExistException, CatalogException {
        CatalogDatabaseDTO databaseDTO = CatalogDatabaseFactory.fromDatabase(getName(), name, database);
        catalogService.insertDatabase(databaseDTO);
    }

    @Override
    public void dropDatabase(String name, boolean ignoreIfNotExists, boolean cascade) throws DatabaseNotExistException, DatabaseNotEmptyException, CatalogException {
        if (databaseExists(name) == false) {
            if (!ignoreIfNotExists) {
                throw new DatabaseNotExistException(getName(), name);
            }
        }

        if (!cascade && !catalogService.isDatabaseEmpty(getName(), name)) {
            throw new DatabaseNotEmptyException(getName(), name);
        }

        try {
            listTables(name).forEach((tableName) -> {
                try {
                    dropTable(new ObjectPath(getName(), tableName), true);
                } catch (TableNotExistException ignored) {
                }
            });
            catalogService.deleteDatabase(getName(), name);
        } catch (DatabaseNotExistException e) {
            if (!ignoreIfNotExists) {
                throw e;
            }
        }
    }

    @Override
    public void alterDatabase(String name, CatalogDatabase newDatabase, boolean ignoreIfNotExists) throws DatabaseNotExistException, CatalogException {
        CatalogDatabaseDTO catalogDatabaseDTO = CatalogDatabaseFactory.fromDatabase(getName(), name, newDatabase);
        catalogService.updateDatabase(catalogDatabaseDTO);
    }

    @Override
    public List<String> listTables(String databaseName) throws DatabaseNotExistException, CatalogException {
        if (databaseExists(databaseName) == false) {
            throw new DatabaseNotExistException(getName(), databaseName);
        }
        return catalogService.listTables(getName(), databaseName).stream().map(CatalogTableDTO::getName).collect(Collectors.toList());
    }

    @Override
    public List<String> listViews(String databaseName) throws DatabaseNotExistException, CatalogException {
        if (databaseExists(databaseName) == false) {
            throw new DatabaseNotExistException(getName(), databaseName);
        }
        return catalogService.listViews(getName(), databaseName).stream().map(CatalogTableDTO::getName).collect(Collectors.toList());
    }

    @Override
    public CatalogBaseTable getTable(ObjectPath tablePath) throws TableNotExistException, CatalogException {
        String database = tablePath.getDatabaseName();
        String table = tablePath.getObjectName();
        Optional<CatalogBaseTable> tableOptional = catalogService.getTable(getName(), database, table).map(CatalogTableFactory::toTable);
        Optional<CatalogBaseTable> viewOptional = catalogService.getView(getName(), database, table).map(CatalogViewFactory::toView);
        return tableOptional.or(() -> viewOptional).orElseThrow(() -> new TableNotExistException(getName(), tablePath));
    }

    @Override
    public boolean tableExists(ObjectPath tablePath) throws CatalogException {
        String database = tablePath.getDatabaseName();
        String table = tablePath.getObjectName();
        Optional<CatalogTableDTO> tableOptional = catalogService.getTable(getName(), database, table);
        Optional<CatalogTableDTO> viewOptional = catalogService.getView(getName(), database, table);
        return tableOptional.isPresent() || viewOptional.isPresent();
    }

    @Override
    public void dropTable(ObjectPath tablePath, boolean ignoreIfNotExists) throws TableNotExistException, CatalogException {
        String database = tablePath.getDatabaseName();
        String table = tablePath.getObjectName();

        try {
            CatalogBaseTable object = getTable(tablePath);
            switch (object.getTableKind()) {
                case TABLE:
                    catalogService.deleteTable(getName(), database, table);
                    break;
                case VIEW:
                    catalogService.deleteView(getName(), database, table);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown table type: " + object.getTableKind());
            }
        } catch (TableNotExistException e) {
            if (!ignoreIfNotExists) {
                throw e;
            }
        }
    }

    @Override
    public void renameTable(ObjectPath tablePath, String newTableName, boolean ignoreIfNotExists) throws TableNotExistException, TableAlreadyExistException, CatalogException {
        String database = tablePath.getDatabaseName();
        String table = tablePath.getObjectName();

        try {
            CatalogBaseTable object = getTable(tablePath);
            switch (object.getTableKind()) {
                case TABLE:
                    catalogService.renameTable(getName(), database, table, newTableName);
                    break;
                case VIEW:
                    catalogService.renameView(getName(), database, table, newTableName);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown table type: " + object.getTableKind());
            }
        } catch (TableNotExistException e) {
            if (!ignoreIfNotExists) {
                throw e;
            }
        }
    }

    @Override
    public void createTable(ObjectPath tablePath, CatalogBaseTable catalogBaseTable, boolean ignoreIfExists) throws TableAlreadyExistException, DatabaseNotExistException, CatalogException {
        String database = tablePath.getDatabaseName();
        String table = tablePath.getObjectName();
        switch (catalogBaseTable.getTableKind()) {
            case TABLE:
                CatalogTable catalogTable = (CatalogTable) catalogBaseTable;
                CatalogTableDTO catalogTableDTO = CatalogTableFactory.fromResolvedTable(table, catalogTable);
                try {
                    catalogService.insertTable(getName(), database, catalogTableDTO);
                    break;
                } catch (TableAlreadyExistException e) {
                    if (!ignoreIfExists) {
                        throw e;
                    }
                }
            case VIEW:
                ResolvedCatalogView catalogView = (ResolvedCatalogView) catalogBaseTable;
                CatalogTableDTO catalogViewDTO = CatalogViewFactory.fromResolvedView(table, catalogView);
                try {
                    catalogService.insertView(getName(), database, catalogViewDTO);
                } catch (TableAlreadyExistException e) {
                    if (!ignoreIfExists) {
                        throw e;
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown table type: " + catalogBaseTable.getTableKind());
        }
    }

    @Override
    public void alterTable(ObjectPath tablePath, CatalogBaseTable newTable, boolean ignoreIfNotExists) throws TableNotExistException, CatalogException {
        String database = tablePath.getDatabaseName();
        String table = tablePath.getObjectName();

        try {
            CatalogBaseTable currentTable = getTable(tablePath);
            if (currentTable.getTableKind() != newTable.getTableKind()) {
                throw new IllegalArgumentException("Cannot convert between TABLE and VIEW.");
            }

            switch (currentTable.getTableKind()) {
                case TABLE:
                    CatalogTable catalogTable = (CatalogTable) newTable;
                    CatalogTableDTO catalogTableDTO = CatalogTableFactory.fromResolvedTable(table, catalogTable);
                    catalogService.updateTable(getName(), database, catalogTableDTO);
                    break;
                case VIEW:
                    ResolvedCatalogView catalogView = (ResolvedCatalogView) newTable;
                    CatalogTableDTO catalogViewDTO = CatalogViewFactory.fromResolvedView(table, catalogView);
                    catalogService.updateView(getName(), database, catalogViewDTO);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown table type: " + currentTable.getTableKind());
            }
        } catch (TableNotExistException e) {
            if (!ignoreIfNotExists) {
                throw e;
            }
        }
    }

    @Override
    public List<CatalogPartitionSpec> listPartitions(ObjectPath tablePath) throws TableNotExistException, TableNotPartitionedException, CatalogException {
        if (tableExists(tablePath) == false) {
            throw new TableNotExistException(getName(), tablePath);
        }
        throw new TableNotPartitionedException(getName(), tablePath);
    }

    @Override
    public List<CatalogPartitionSpec> listPartitions(ObjectPath tablePath, CatalogPartitionSpec partitionSpec) throws TableNotExistException, TableNotPartitionedException, PartitionSpecInvalidException, CatalogException {
        if (tableExists(tablePath) == false) {
            throw new TableNotExistException(getName(), tablePath);
        }
        throw new TableNotPartitionedException(getName(), tablePath);
    }

    @Override
    public List<CatalogPartitionSpec> listPartitionsByFilter(ObjectPath tablePath, List<Expression> filters) throws TableNotExistException, TableNotPartitionedException, CatalogException {
        if (tableExists(tablePath) == false) {
            throw new TableNotExistException(getName(), tablePath);
        }
        throw new TableNotPartitionedException(getName(), tablePath);
    }

    @Override
    public CatalogPartition getPartition(ObjectPath tablePath, CatalogPartitionSpec partitionSpec) throws PartitionNotExistException, CatalogException {
        throw new PartitionNotExistException(getName(), tablePath, partitionSpec);
    }

    @Override
    public boolean partitionExists(ObjectPath tablePath, CatalogPartitionSpec partitionSpec) throws CatalogException {
        return false;
    }

    @Override
    public void createPartition(ObjectPath tablePath, CatalogPartitionSpec partitionSpec, CatalogPartition partition, boolean ignoreIfExists) throws TableNotExistException, TableNotPartitionedException, PartitionSpecInvalidException, PartitionAlreadyExistsException, CatalogException {
        if (tableExists(tablePath) == false) {
            throw new TableNotExistException(getName(), tablePath);
        }
        throw new TableNotPartitionedException(getName(), tablePath);
    }

    @Override
    public void dropPartition(ObjectPath tablePath, CatalogPartitionSpec partitionSpec, boolean ignoreIfNotExists) throws PartitionNotExistException, CatalogException {
        throw new PartitionNotExistException(getName(), tablePath, partitionSpec);
    }

    @Override
    public void alterPartition(ObjectPath tablePath, CatalogPartitionSpec partitionSpec, CatalogPartition newPartition, boolean ignoreIfNotExists) throws PartitionNotExistException, CatalogException {
        throw new PartitionNotExistException(getName(), tablePath, partitionSpec);
    }

    @Override
    public List<String> listFunctions(String dbName) throws DatabaseNotExistException, CatalogException {
        if (databaseExists(dbName) == false) {
            throw new DatabaseNotExistException(getName(), dbName);
        }
        return catalogService.listFunctions(getName(), dbName).stream().map(CatalogFunctionDTO::getName).collect(Collectors.toList());
    }

    @Override
    public CatalogFunction getFunction(ObjectPath functionPath) throws FunctionNotExistException, CatalogException {
        String database = functionPath.getDatabaseName();
        String function = functionPath.getObjectName();
        return catalogService.getFunction(getName(), database, function)
                .map(CatalogFunctionFactory::toCatalogFunction)
                .orElseThrow(() -> new FunctionNotExistException(getName(), functionPath));
    }

    @Override
    public boolean functionExists(ObjectPath functionPath) throws CatalogException {
        String database = functionPath.getDatabaseName();
        String function = functionPath.getObjectName();
        return catalogService.getFunction(getName(), database, function).isPresent();
    }

    @Override
    public void createFunction(ObjectPath functionPath, CatalogFunction function, boolean ignoreIfExists) throws FunctionAlreadyExistException, DatabaseNotExistException, CatalogException {

    }

    @Override
    public void alterFunction(ObjectPath functionPath, CatalogFunction newFunction, boolean ignoreIfNotExists) throws FunctionNotExistException, CatalogException {

    }

    @Override
    public void dropFunction(ObjectPath functionPath, boolean ignoreIfNotExists) throws FunctionNotExistException, CatalogException {
        String database = functionPath.getDatabaseName();
        String function = functionPath.getObjectName();

        try {
            catalogService.deleteFunction(getName(), database, function);
        } catch (FunctionNotExistException e) {
            if (!ignoreIfNotExists) {
                throw e;
            }
        }
    }

    @Override
    public CatalogTableStatistics getTableStatistics(ObjectPath tablePath) throws TableNotExistException, CatalogException {
        if (tableExists(tablePath) == false) {
            throw new TableNotExistException(getName(), tablePath);
        }
        return CatalogTableStatistics.UNKNOWN;
    }

    @Override
    public CatalogColumnStatistics getTableColumnStatistics(ObjectPath tablePath) throws TableNotExistException, CatalogException {
        if (tableExists(tablePath) == false) {
            throw new TableNotExistException(getName(), tablePath);
        }
        return CatalogColumnStatistics.UNKNOWN;
    }

    @Override
    public CatalogTableStatistics getPartitionStatistics(ObjectPath tablePath, CatalogPartitionSpec partitionSpec) throws PartitionNotExistException, CatalogException {
        if (partitionExists(tablePath, partitionSpec) == false) {
            throw new PartitionNotExistException(getName(), tablePath, partitionSpec);
        }
        return CatalogTableStatistics.UNKNOWN;
    }

    @Override
    public CatalogColumnStatistics getPartitionColumnStatistics(ObjectPath tablePath, CatalogPartitionSpec partitionSpec) throws PartitionNotExistException, CatalogException {
        if (partitionExists(tablePath, partitionSpec) == false) {
            throw new PartitionNotExistException(getName(), tablePath, partitionSpec);
        }
        return CatalogColumnStatistics.UNKNOWN;
    }

    @Override
    public void alterTableStatistics(ObjectPath tablePath, CatalogTableStatistics tableStatistics, boolean ignoreIfNotExists) throws TableNotExistException, CatalogException {
        if (tableExists(tablePath) == false) {
            if (!ignoreIfNotExists) {
                throw new TableNotExistException(getName(), tablePath);
            }
        }
        throw new CatalogException("SakuraCatalog not support table statistics yet.");
    }

    @Override
    public void alterTableColumnStatistics(ObjectPath tablePath, CatalogColumnStatistics columnStatistics, boolean ignoreIfNotExists) throws TableNotExistException, CatalogException, TablePartitionedException {
        if (tableExists(tablePath) == false) {
            if (!ignoreIfNotExists) {
                throw new TableNotExistException(getName(), tablePath);
            }
        }
        throw new CatalogException("SakuraCatalog not support table column statistics yet.");
    }

    @Override
    public void alterPartitionStatistics(ObjectPath tablePath, CatalogPartitionSpec partitionSpec, CatalogTableStatistics partitionStatistics, boolean ignoreIfNotExists) throws PartitionNotExistException, CatalogException {
        if (partitionExists(tablePath, partitionSpec) == false) {
            if (!ignoreIfNotExists) {
                throw new PartitionNotExistException(getName(), tablePath, partitionSpec);
            }
        }
        throw new CatalogException("SakuraCatalog not support partition statistics yet.");
    }

    @Override
    public void alterPartitionColumnStatistics(ObjectPath tablePath, CatalogPartitionSpec partitionSpec, CatalogColumnStatistics columnStatistics, boolean ignoreIfNotExists) throws PartitionNotExistException, CatalogException {
        if (partitionExists(tablePath, partitionSpec) == false) {
            if (!ignoreIfNotExists) {
                throw new PartitionNotExistException(getName(), tablePath, partitionSpec);
            }
        }
        throw new CatalogException("SakuraCatalog not support partition column statistics yet.");
    }
}
