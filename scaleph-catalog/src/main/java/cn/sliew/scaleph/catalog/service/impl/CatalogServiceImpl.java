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
import cn.sliew.scaleph.catalog.service.dto.CatalogDatabaseDTO;
import cn.sliew.scaleph.catalog.service.dto.CatalogFunctionDTO;
import cn.sliew.scaleph.catalog.service.dto.CatalogTableDTO;
import cn.sliew.scaleph.dao.mapper.sakura.CatalogDatabaseMapper;
import cn.sliew.scaleph.dao.mapper.sakura.CatalogFunctionMapper;
import cn.sliew.scaleph.dao.mapper.sakura.CatalogTableMapper;
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
        return null;
    }

    @Override
    public Optional<CatalogDatabaseDTO> getDatabase(String catalog, String database) {
        return Optional.empty();
    }

    @Override
    public void insertDatabase(CatalogDatabaseDTO database) throws DatabaseAlreadyExistException {

    }

    @Override
    public void updateDatabase(CatalogDatabaseDTO database) throws DatabaseNotExistException {

    }

    @Override
    public void deleteDatabase(String catalog, String database) throws DatabaseNotExistException {

    }

    @Override
    public boolean isDatabaseEmpty(String catalog, String database) {
        return false;
    }

    @Override
    public List<CatalogTableDTO> listTables(String catalog, String database) {
        return null;
    }

    @Override
    public Optional<CatalogTableDTO> getTable(String catalog, String database, String table) {
        return Optional.empty();
    }

    @Override
    public void insertTable(String catalog, String database, CatalogTableDTO table) throws DatabaseNotExistException, TableAlreadyExistException {

    }

    @Override
    public void updateTable(String catalog, String database, CatalogTableDTO table) throws TableNotExistException {

    }

    @Override
    public void renameTable(String catalog, String database, String currentName, String newName) throws TableAlreadyExistException, TableNotExistException {

    }

    @Override
    public void deleteTable(String catalog, String database, String table) throws TableNotExistException {

    }

    @Override
    public List<CatalogTableDTO> listViews(String catalog, String database) {
        return null;
    }

    @Override
    public Optional<CatalogTableDTO> getView(String catalog, String database, String view) {
        return Optional.empty();
    }

    @Override
    public void insertView(String catalog, String database, CatalogTableDTO view) throws DatabaseNotExistException, TableAlreadyExistException {

    }

    @Override
    public void updateView(String catalog, String database, CatalogTableDTO view) throws TableNotExistException {

    }

    @Override
    public void renameView(String catalog, String database, String currentName, String newName) throws TableNotExistException, TableAlreadyExistException {

    }

    @Override
    public void deleteView(String catalog, String database, String viewName) throws TableNotExistException {

    }

    @Override
    public List<CatalogFunctionDTO> listFunctions(String catalog, String database) {
        return null;
    }

    @Override
    public Optional<CatalogFunctionDTO> getFunction(String catalog, String database, String function) {
        return Optional.empty();
    }

    @Override
    public void insertFunction(String catalog, String database, CatalogFunctionDTO function) throws DatabaseNotExistException, FunctionAlreadyExistException {

    }

    @Override
    public void updateFunction(String catalog, String database, CatalogFunctionDTO function) throws FunctionNotExistException {

    }

    @Override
    public void deleteFunction(String catalog, String database, String functionName) throws FunctionNotExistException {

    }
}
