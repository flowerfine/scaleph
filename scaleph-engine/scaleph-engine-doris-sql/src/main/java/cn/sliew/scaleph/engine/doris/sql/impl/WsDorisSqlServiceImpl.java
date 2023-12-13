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
import cn.sliew.scaleph.engine.doris.sql.dialect.MysqlDialect;
import cn.sliew.scaleph.engine.doris.sql.dialect.SqlDialect;
import cn.sliew.scaleph.engine.doris.sql.dto.BaseTable;
import cn.sliew.scaleph.engine.doris.sql.dto.QueryResult;
import cn.sliew.scaleph.engine.doris.sql.dto.Table;
import cn.sliew.scaleph.engine.doris.sql.dto.enums.TableType;
import cn.sliew.scaleph.engine.doris.sql.manager.DataSourceManager;
import cn.sliew.scaleph.engine.doris.sql.params.SqlExecutionParam;
import cn.sliew.scaleph.engine.doris.sql.util.JdbcUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WsDorisSqlServiceImpl implements WsDorisSqlService {

    private final DataSourceManager manager;

    @Autowired
    public WsDorisSqlServiceImpl(DataSourceManager manager) {
        this.manager = manager;
    }

    private <T> T metadataAction(Long clusterCredentialId, DatabaseMetaFunction<T> action) {
        return manager.actionWithConnection(clusterCredentialId, connection -> {
            try {
                DatabaseMetaData metaData = connection.getMetaData();
                return action.apply(metaData);
            } catch (SQLException e) {
                log.error(e.getLocalizedMessage(), e);
                return null;
            }
        });
    }

    @Override
    public List<String> listCatalogs(Long clusterCredentialId) {
        return metadataAction(clusterCredentialId, JdbcUtil::listCatalogs);
    }

    @Override
    public List<String> listSchemas(Long clusterCredentialId, String catalogName) {
        return metadataAction(clusterCredentialId, metaData -> JdbcUtil.listSchemas(metaData, catalogName));
    }

    @Override
    public List<String> listTables(Long clusterCredentialId, String catalogName, String schemaName, TableType[] tableTypes) {
        return metadataAction(clusterCredentialId, metaData ->
                JdbcUtil
                        .listTables(metaData, catalogName, schemaName, tableTypes)
                        .stream()
                        .map(BaseTable::getTableName)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Table getTableInfo(Long clusterCredentialId, String catalogName, String schemaName, @NotNull String tableName) {
        return metadataAction(clusterCredentialId, metaData -> JdbcUtil.getTableInfo(metaData, catalogName, schemaName, tableName, new MysqlDialect()));
    }

    @Override
    public QueryResult executeSql(Long clusterCredentialId, SqlExecutionParam sqlExecutionParam) {
       return manager.actionWithConnection(clusterCredentialId, connection -> {
           SqlDialect sqlDialect = new MysqlDialect();
           String sql = sqlDialect.getLimitationQuery(sqlExecutionParam.getSql(), sqlExecutionParam.getLimitation());
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
                int resultSetType = preparedStatement.getResultSetType();
                return QueryResult.fromResultSet(resultSet, sqlDialect);
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }
        });
    }


    @FunctionalInterface
    private interface DatabaseMetaFunction<T> {
        T apply(DatabaseMetaData metaData) throws SQLException;
    }
}
