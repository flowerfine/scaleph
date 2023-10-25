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

package cn.sliew.scaleph.engine.sql.gateway.services;

import java.util.Set;

import org.apache.flink.table.catalog.CatalogBaseTable;
import org.apache.flink.table.catalog.ObjectIdentifier;
import org.apache.flink.table.catalog.ResolvedCatalogBaseTable;
import org.apache.flink.table.catalog.UnresolvedIdentifier;
import org.apache.flink.table.functions.FunctionDefinition;
import org.apache.flink.table.gateway.api.results.FunctionInfo;
import org.apache.flink.table.gateway.api.results.TableInfo;
import org.apache.flink.table.gateway.api.session.SessionHandle;
import org.apache.flink.table.gateway.api.utils.SqlGatewayException;

import cn.sliew.scaleph.engine.sql.gateway.services.dto.catalog.CatalogInfo;

/**
 * 使用用户 id 关联的 session 获取
 */
public interface CatalogService {

    /**
     * List catalogs.
     *
     * @return Set of catalog information.
     */
    Set<CatalogInfo> getCatalogs(SessionHandle sessionHandle) throws SqlGatewayException;

    /**
     * Return current catalog name.
     *
     * @return name of the current catalog.
     */
    String getCurrentCatalog(SessionHandle sessionHandle) throws SqlGatewayException;

    /**
     * Return all available catalogs in the current session.
     *
     * @return names of the registered catalogs.
     */
    Set<String> listCatalogs(SessionHandle sessionHandle) throws SqlGatewayException;

    /**
     * Return all available schemas in the given catalog.
     *
     * @param catalogName name string of the given catalog.
     * @return names of the registered schemas.
     */
    Set<String> listDatabases(SessionHandle sessionHandle, String catalogName) throws SqlGatewayException;

    /**
     * Return all available tables/views in the given catalog and database.
     *
     * @param catalogName  name of the given catalog.
     * @param databaseName name of the given database.
     * @param tableKinds   used to specify the type of return values.
     * @return table info of the registered tables/views.
     */
    Set<TableInfo> listTables(
            SessionHandle sessionHandle,
            String catalogName,
            String databaseName,
            Set<CatalogBaseTable.TableKind> tableKinds)
            throws SqlGatewayException;

    /**
     * Return table of the given fully qualified name.
     *
     * @param tableIdentifier fully qualified name of the table.
     * @return information of the table.
     */
    ResolvedCatalogBaseTable<?> getTable(SessionHandle sessionHandle, ObjectIdentifier tableIdentifier)
            throws SqlGatewayException;

    /**
     * List all user defined functions.
     *
     * @param catalogName  name string of the given catalog.
     * @param databaseName name string of the given database.
     * @return user defined functions info.
     */
    Set<FunctionInfo> listUserDefinedFunctions(SessionHandle sessionHandle, String catalogName, String databaseName)
            throws SqlGatewayException;

    /**
     * List all available system functions.
     *
     * @return system functions info.
     */
    Set<FunctionInfo> listSystemFunctions(SessionHandle sessionHandle) throws SqlGatewayException;

    /**
     * Get the specific definition of the function. If the input identifier only contains the
     * function name, it is resolved with the order of the temporary system function, system
     * function, temporary function and catalog function.
     *
     * @param functionIdentifier identifier of the function.
     * @return the definition of the function.
     */
    FunctionDefinition getFunctionDefinition(SessionHandle sessionHandle, UnresolvedIdentifier functionIdentifier)
            throws SqlGatewayException;
}
