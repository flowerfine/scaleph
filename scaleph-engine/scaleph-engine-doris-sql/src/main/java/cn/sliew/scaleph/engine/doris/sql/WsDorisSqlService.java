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
 *
 */

package cn.sliew.scaleph.engine.doris.sql;

import cn.sliew.scaleph.engine.doris.sql.dto.Function;
import cn.sliew.scaleph.engine.doris.sql.dto.QueryResult;
import cn.sliew.scaleph.engine.doris.sql.dto.Table;
import cn.sliew.scaleph.engine.doris.sql.dto.enums.TableType;
import cn.sliew.scaleph.engine.doris.sql.params.WsDorisSqlExecutionParam;

import javax.annotation.Nonnull;
import java.sql.DatabaseMetaData;
import java.util.List;

/**
 * Service to process doris notebook query request.
 */
public interface WsDorisSqlService {

    /**
     * List catalogs of a doris cluster
     *
     * @param dorisInstanceId Doris k8s instance id
     * @return A list of catalogs
     */
    List<String> listCatalogs(Long dorisInstanceId);


    /**
     * List schemas of a doris cluster
     *
     * @param dorisInstanceId Doris k8s instance id
     * @param catalogName     Catalog name. null for all catalogs
     * @return A list of schemas
     */
    List<String> listSchemas(Long dorisInstanceId, String catalogName);

    /**
     * List table names of a doris cluster
     *
     * @param dorisInstanceId Doris k8s cluster id
     * @param catalogName     Catalog name. null for all catalogs
     * @param schemaName      Schema name. null for all schemas
     * @param tableTypes      Table types {@link DatabaseMetaData#getTableTypes()}
     * @return A list of table names
     */
    List<String> listTables(Long dorisInstanceId, String catalogName, String schemaName, TableType[] tableTypes);

    /**
     * Get detail information of a table
     *
     * @param dorisInstanceId Doris k8s instance id
     * @param catalogName     Catalog name. null for all catalogs
     * @param schemaName      Schema name. null for all schemas
     * @param tableName       Table name, not null
     * @return Detail information of a table
     */
    Table getTableInfo(Long dorisInstanceId, String catalogName, String schemaName, @Nonnull String tableName);

    List<Function> listFunctions(Long dorisInstanceId, String catalogName, String schemaName);

    /**
     * Execute a sql in a doris cluster
     *
     * @param wsDorisSqlExecutionParam Parameters
     * @return Query result
     */
    QueryResult executeSql(WsDorisSqlExecutionParam wsDorisSqlExecutionParam);

}
