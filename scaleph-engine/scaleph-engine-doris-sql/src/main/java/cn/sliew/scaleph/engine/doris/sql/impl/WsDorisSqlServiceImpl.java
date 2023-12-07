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

package cn.sliew.scaleph.engine.doris.sql.impl;

import cn.sliew.scaleph.engine.doris.sql.WsDorisSqlService;
import cn.sliew.scaleph.engine.doris.sql.dto.QueryResult;
import cn.sliew.scaleph.engine.doris.sql.dto.TableInfo;
import cn.sliew.scaleph.engine.doris.sql.dto.TableType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WsDorisSqlServiceImpl implements WsDorisSqlService {
    @Override
    public List<String> listCatalogs(Long clusterCredentialId) {
        return null;
    }

    @Override
    public List<String> listSchemas(Long clusterCredentialId, String catalogName) {
        return null;
    }

    @Override
    public List<String> listTables(Long clusterCredentialId, String catalogName, String schemaName, TableType[] tableTypes) {
        return null;
    }

    @Override
    public TableInfo getTableInfo(Long clusterCredentialId, String catalogName, String schemaName, @NotNull String tableName) {
        return null;
    }

    @Override
    public QueryResult executeSql(Long clusterCredentialId, String sql) {
        return null;
    }
}
