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

package cn.sliew.scaleph.catalog.service;

import cn.sliew.scaleph.catalog.service.dto.CatalogDatabaseDTO;
import cn.sliew.scaleph.catalog.service.dto.CatalogFunctionDTO;
import cn.sliew.scaleph.catalog.service.dto.CatalogTableDTO;
import org.apache.flink.table.catalog.exceptions.*;

import java.util.List;
import java.util.Optional;

public interface CatalogService {

    List<CatalogDatabaseDTO> listDatabases(String catalog);

    Optional<CatalogDatabaseDTO> getDatabase(String catalog, String database);

    void insertDatabase(String catalog, String database) throws DatabaseAlreadyExistException;

    void updateDatabase(String catalog, String database) throws DatabaseNotExistException;

    void deleteDatabase(String catalog, String database) throws DatabaseNotExistException;

    boolean isDatabaseEmpty(String catalog, String database);

    List<CatalogTableDTO> listTables(String catalog, String database);

    Optional<CatalogTableDTO> getTable(String catalog, String database, String table);

    void insertTable(String catalog, String database, CatalogTableDTO table) throws DatabaseNotExistException, TableAlreadyExistException;

    void updateTable(String catalog, String database, CatalogTableDTO table) throws TableNotExistException;

    void renameTable(String catalog, String database, String currentName, String newName) throws TableAlreadyExistException, TableNotExistException;

    void deleteTable(String catalog, String database, String table) throws TableNotExistException;

    List<CatalogTableDTO> listViews(String catalog, String database);

    Optional<CatalogTableDTO> getView(String catalog, String database, String view);

    void insertView(String catalog, String database, CatalogTableDTO view) throws DatabaseNotExistException, TableAlreadyExistException;

    void updateView(String catalog, String database, CatalogTableDTO view) throws TableNotExistException;

    void renameView(String catalog, String database, String currentName, String newName) throws TableNotExistException, TableAlreadyExistException;

    void deleteView(String catalog, String database, String viewName) throws TableNotExistException;

    List<CatalogFunctionDTO> listFunctions(String catalog, String database);

    Optional<CatalogFunctionDTO> getFunction(String catalog, String database, String function);

    void insertFunction(String catalog, String database, CatalogFunctionDTO function) throws DatabaseNotExistException, FunctionAlreadyExistException;

    void updateFunction(String catalog, String database, CatalogFunctionDTO function) throws FunctionNotExistException;

    void deleteFunction(String catalog, String database, String functionName) throws FunctionNotExistException;
}
