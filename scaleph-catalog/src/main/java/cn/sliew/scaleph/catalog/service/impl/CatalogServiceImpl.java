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

package cn.sliew.scaleph.catalog.service.impl;

import cn.sliew.scaleph.catalog.service.CatalogService;
import cn.sliew.scaleph.catalog.service.convert.CatalogDatabaseConvert;
import cn.sliew.scaleph.catalog.service.convert.CatalogFunctionConvert;
import cn.sliew.scaleph.catalog.service.convert.CatalogTableConvert;
import cn.sliew.scaleph.catalog.service.dto.CatalogDatabaseDTO;
import cn.sliew.scaleph.catalog.service.dto.CatalogFunctionDTO;
import cn.sliew.scaleph.catalog.service.dto.CatalogTableDTO;
import cn.sliew.scaleph.common.dict.catalog.CatalogTableKind;
import cn.sliew.scaleph.dao.entity.sakura.CatalogDatabase;
import cn.sliew.scaleph.dao.entity.sakura.CatalogFunction;
import cn.sliew.scaleph.dao.entity.sakura.CatalogTable;
import cn.sliew.scaleph.dao.mapper.sakura.CatalogDatabaseMapper;
import cn.sliew.scaleph.dao.mapper.sakura.CatalogFunctionMapper;
import cn.sliew.scaleph.dao.mapper.sakura.CatalogTableMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.flink.table.catalog.ObjectPath;
import org.apache.flink.table.catalog.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CatalogDatabaseMapper catalogDatabaseMapper;
    @Autowired
    private CatalogTableMapper catalogTableMapper;
    @Autowired
    private CatalogFunctionMapper catalogFunctionMapper;

    @Override
    public List<CatalogDatabaseDTO> listDatabases(String catalog) {
        LambdaQueryWrapper<CatalogDatabase> queryWrapper = Wrappers.lambdaQuery(CatalogDatabase.class)
                .eq(CatalogDatabase::getCatalog, catalog)
                .orderByAsc(CatalogDatabase::getName);
        List<CatalogDatabase> databases = catalogDatabaseMapper.selectList(queryWrapper);
        return CatalogDatabaseConvert.INSTANCE.toDto(databases);
    }

    @Override
    public Optional<CatalogDatabaseDTO> getDatabase(String catalog, String database) {
        LambdaQueryWrapper<CatalogDatabase> queryWrapper = Wrappers.lambdaQuery(CatalogDatabase.class)
                .eq(CatalogDatabase::getCatalog, catalog)
                .eq(CatalogDatabase::getName, database);
        CatalogDatabase record = catalogDatabaseMapper.selectOne(queryWrapper);
        return Optional.ofNullable(CatalogDatabaseConvert.INSTANCE.toDto(record));
    }

    @Override
    public boolean databaseExists(String catalog, String database) {
        Optional<CatalogDatabaseDTO> optional = getDatabase(catalog, database);
        return optional.isPresent();
    }

    @Override
    public void insertDatabase(CatalogDatabaseDTO database) throws DatabaseAlreadyExistException {
        if (databaseExists(database.getCatalog(), database.getName())) {
            throw new DatabaseAlreadyExistException(database.getCatalog(), database.getName());
        }
        CatalogDatabase record = CatalogDatabaseConvert.INSTANCE.toDo(database);
        catalogDatabaseMapper.insert(record);
    }

    @Override
    public void updateDatabase(CatalogDatabaseDTO database) throws DatabaseNotExistException {
        if (databaseExists(database.getCatalog(), database.getName()) == false) {
            throw new DatabaseNotExistException(database.getCatalog(), database.getName());
        }
        CatalogDatabase record = CatalogDatabaseConvert.INSTANCE.toDo(database);
        catalogDatabaseMapper.updateById(record);
    }

    @Override
    public void deleteDatabase(String catalog, String database) throws DatabaseNotExistException {
        if (databaseExists(catalog, database) == false) {
            throw new DatabaseNotExistException(catalog, database);
        }
        LambdaQueryWrapper<CatalogDatabase> queryWrapper = Wrappers.lambdaQuery(CatalogDatabase.class)
                .eq(CatalogDatabase::getCatalog, catalog)
                .eq(CatalogDatabase::getName, database);
        catalogDatabaseMapper.delete(queryWrapper);
    }

    @Override
    public boolean isDatabaseEmpty(String catalog, String database) {
        int tableCount = catalogTableMapper.countByDatabase(catalog, database, CatalogTableKind.TABLE);
        int functionCount = catalogFunctionMapper.countByDatabase(catalog, database);
        return tableCount != 0 || functionCount != 0;
    }

    @Override
    public List<CatalogTableDTO> listTables(String catalog, String database) {
        List<CatalogTable> records = catalogTableMapper.selectByDatabase(catalog, database, CatalogTableKind.TABLE);
        return CatalogTableConvert.INSTANCE.toDto(records);
    }

    @Override
    public Optional<CatalogTableDTO> getTable(String catalog, String database, String table) {
        Optional<CatalogTable> optional = catalogTableMapper.selectByName(catalog, database, CatalogTableKind.TABLE, table);
        return optional.map(record -> CatalogTableConvert.INSTANCE.toDto(record));
    }

    @Override
    public boolean tableExists(String catalog, String database, String table) {
        Optional<CatalogTableDTO> optional = getTable(catalog, database, table);
        return optional.isPresent();
    }

    @Override
    public void insertTable(String catalog, String database, CatalogTableDTO table) throws DatabaseNotExistException, TableAlreadyExistException {
        CatalogDatabaseDTO catalogDatabaseDTO = getDatabase(catalog, database).orElseThrow(() -> new DatabaseNotExistException(catalog, database));
        if (tableExists(catalog, database, table.getName())) {
            throw new TableAlreadyExistException(catalog, new ObjectPath(database, table.getName()));
        }
        CatalogTable record = CatalogTableConvert.INSTANCE.toDo(table);
        record.setDatabaseId(catalogDatabaseDTO.getId());
        catalogTableMapper.insert(record);
    }

    @Override
    public void updateTable(String catalog, String database, CatalogTableDTO table) throws TableNotExistException {
        if (tableExists(catalog, database, table.getName()) == false) {
            throw new TableNotExistException(catalog, new ObjectPath(database, table.getName()));
        }
        CatalogTable record = CatalogTableConvert.INSTANCE.toDo(table);
        catalogTableMapper.updateById(record);
    }

    @Override
    public void renameTable(String catalog, String database, String currentName, String newName) throws TableAlreadyExistException, TableNotExistException {
        CatalogTableDTO catalogTableDTO = getTable(catalog, database, currentName).orElseThrow(() -> new TableNotExistException(catalog, new ObjectPath(database, currentName)));
        if (tableExists(catalog, database, newName)) {
            throw new TableAlreadyExistException(catalog, new ObjectPath(database, newName));
        }
        CatalogTable record = CatalogTableConvert.INSTANCE.toDo(catalogTableDTO);
        record.setName(newName);
        catalogTableMapper.updateById(record);
    }

    @Override
    public void deleteTable(String catalog, String database, String table) throws TableNotExistException {
        CatalogTableDTO catalogTableDTO = getTable(catalog, database, table).orElseThrow(() -> new TableNotExistException(catalog, new ObjectPath(database, table)));
        CatalogTable record = CatalogTableConvert.INSTANCE.toDo(catalogTableDTO);
        catalogTableMapper.deleteById(record.getId());
    }

    @Override
    public List<CatalogTableDTO> listViews(String catalog, String database) {
        List<CatalogTable> views = catalogTableMapper.selectByDatabase(catalog, database, CatalogTableKind.VIEW);
        return CatalogTableConvert.INSTANCE.toDto(views);
    }

    @Override
    public Optional<CatalogTableDTO> getView(String catalog, String database, String view) {
        Optional<CatalogTable> optional = catalogTableMapper.selectByName(catalog, database, CatalogTableKind.VIEW, view);
        return optional.map(record -> CatalogTableConvert.INSTANCE.toDto(record));
    }

    @Override
    public boolean viewExists(String catalog, String database, String view) {
        Optional<CatalogTableDTO> optional = getView(catalog, database, view);
        return optional.isPresent();
    }

    @Override
    public void insertView(String catalog, String database, CatalogTableDTO view) throws DatabaseNotExistException, TableAlreadyExistException {
        CatalogDatabaseDTO catalogDatabaseDTO = getDatabase(catalog, database).orElseThrow(() -> new DatabaseNotExistException(catalog, database));
        if (viewExists(catalog, database, view.getName())) {
            throw new TableAlreadyExistException(catalog, new ObjectPath(database, view.getName()));
        }
        CatalogTable record = CatalogTableConvert.INSTANCE.toDo(view);
        record.setDatabaseId(catalogDatabaseDTO.getId());
        catalogTableMapper.insert(record);
    }

    @Override
    public void updateView(String catalog, String database, CatalogTableDTO view) throws TableNotExistException {
        if (viewExists(catalog, database, view.getName()) == false) {
            throw new TableNotExistException(catalog, new ObjectPath(database, view.getName()));
        }
        CatalogTable record = CatalogTableConvert.INSTANCE.toDo(view);
        catalogTableMapper.updateById(record);
    }

    @Override
    public void renameView(String catalog, String database, String currentName, String newName) throws TableNotExistException, TableAlreadyExistException {
        CatalogTableDTO catalogViewDTO = getView(catalog, database, currentName).orElseThrow(() -> new TableNotExistException(catalog, new ObjectPath(database, currentName)));
        if (viewExists(catalog, database, newName)) {
            throw new TableAlreadyExistException(catalog, new ObjectPath(database, newName));
        }
        CatalogTable record = CatalogTableConvert.INSTANCE.toDo(catalogViewDTO);
        record.setName(newName);
        catalogTableMapper.updateById(record);
    }

    @Override
    public void deleteView(String catalog, String database, String viewName) throws TableNotExistException {
        CatalogTableDTO catalogViewDTO = getView(catalog, database, viewName).orElseThrow(() -> new TableNotExistException(catalog, new ObjectPath(database, viewName)));
        CatalogTable record = CatalogTableConvert.INSTANCE.toDo(catalogViewDTO);
        catalogTableMapper.deleteById(record.getId());
    }

    @Override
    public List<CatalogFunctionDTO> listFunctions(String catalog, String database) {
        List<CatalogFunction> catalogFunctions = catalogFunctionMapper.selectByDatabase(catalog, database);
        return CatalogFunctionConvert.INSTANCE.toDto(catalogFunctions);
    }

    @Override
    public Optional<CatalogFunctionDTO> getFunction(String catalog, String database, String function) {
        Optional<CatalogFunction> optional = catalogFunctionMapper.selectByName(catalog, database, function);
        return optional.map(record -> CatalogFunctionConvert.INSTANCE.toDto(record));
    }

    @Override
    public boolean functionExists(String catalog, String database, String function) {
        Optional<CatalogFunctionDTO> optional = getFunction(catalog, database, function);
        return optional.isPresent();
    }

    @Override
    public void insertFunction(String catalog, String database, CatalogFunctionDTO function) throws DatabaseNotExistException, FunctionAlreadyExistException {
        CatalogDatabaseDTO catalogDatabaseDTO = getDatabase(catalog, database).orElseThrow(() -> new DatabaseNotExistException(catalog, database));
        if (functionExists(catalog, database, function.getName())) {
            throw new FunctionAlreadyExistException(catalog, new ObjectPath(database, function.getName()));
        }
        CatalogFunction record = CatalogFunctionConvert.INSTANCE.toDo(function);
        record.setDatabaseId(catalogDatabaseDTO.getId());
        catalogFunctionMapper.insert(record);
    }

    @Override
    public void updateFunction(String catalog, String database, CatalogFunctionDTO function) throws FunctionNotExistException {
        if (functionExists(catalog, database, function.getName()) == false) {
            throw new FunctionNotExistException(catalog, new ObjectPath(database, function.getName()));
        }
        CatalogFunction record = CatalogFunctionConvert.INSTANCE.toDo(function);
        catalogFunctionMapper.updateById(record);
    }

    @Override
    public void deleteFunction(String catalog, String database, String functionName) throws FunctionNotExistException {
        CatalogFunctionDTO catalogFunctionDTO = getFunction(catalog, database, functionName).orElseThrow(() -> new FunctionNotExistException(catalog, new ObjectPath(database, functionName)));
        CatalogFunction record = CatalogFunctionConvert.INSTANCE.toDo(catalogFunctionDTO);
        catalogFunctionMapper.deleteById(record.getId());
    }
}
