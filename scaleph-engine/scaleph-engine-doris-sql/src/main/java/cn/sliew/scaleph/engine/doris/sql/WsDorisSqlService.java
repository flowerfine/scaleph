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

import cn.sliew.scaleph.engine.doris.sql.dto.QueryResult;
import cn.sliew.scaleph.engine.doris.sql.dto.Table;
import cn.sliew.scaleph.engine.doris.sql.dto.enums.TableType;
import cn.sliew.scaleph.engine.doris.sql.params.SqlExecutionParam;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.sql.DatabaseMetaData;
import java.util.List;

/**
 * Service to process doris notebook query request.
 */
@Service
public interface WsDorisSqlService {

    /**
     * List catalogs of a doris cluster
     *
     * @param clusterCredentialId Doris k8s cluster id
     * @return A list of catalogs
     */
    List<String> listCatalogs(Long clusterCredentialId);


    /**
     * List schemas of a doris cluster
     *
     * @param clusterCredentialId Doris k8s cluster id
     * @param catalogName         Catalog name. null for all catalogs
     * @return A list of schemas
     */
    List<String> listSchemas(Long clusterCredentialId, String catalogName);

    /**
     * List table names of a doris cluster
     *
     * @param clusterCredentialId Doris k8s cluster id
     * @param catalogName         Catalog name. null for all catalogs
     * @param schemaName          Schema name. null for all schemas
     * @param tableTypes          Table types {@link DatabaseMetaData#getTableTypes()}
     * @return A list of table names
     */
    List<String> listTables(Long clusterCredentialId, String catalogName, String schemaName, TableType[] tableTypes);

    /**
     * Get detail information of a table
     *
     * @param clusterCredentialId Doris k8s cluster id
     * @param catalogName         Catalog name. null for all catalogs
     * @param schemaName          Schema name. null for all schemas
     * @param tableName           Table name, not null
     * @return Detail information of a table
     */
    Table getTableInfo(Long clusterCredentialId, String catalogName, String schemaName, @Nonnull String tableName);

    /**
     * Execute a sql in a doris cluster
     *
     * @param clusterCredentialId Doris k8s cluster id
     * @param sqlExecutionParam   Parameters
     * @return Query result
     */
    QueryResult executeSql(Long clusterCredentialId, SqlExecutionParam sqlExecutionParam);

}
