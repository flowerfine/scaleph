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
import cn.sliew.scaleph.engine.doris.sql.dto.Function;
import cn.sliew.scaleph.engine.doris.sql.dto.QueryResult;
import cn.sliew.scaleph.engine.doris.sql.dto.Table;
import cn.sliew.scaleph.engine.doris.sql.dto.enums.TableType;
import cn.sliew.scaleph.engine.doris.sql.manager.DataSourceManager;
import cn.sliew.scaleph.engine.doris.sql.params.WsDorisSqlExecutionParam;
import cn.sliew.scaleph.engine.doris.sql.util.JdbcUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.DatabaseMetaData;
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

    private <T> T metadataAction(Long dorisInstanceId, DatabaseMetaFunction<T> action) {
        return manager.actionWithConnection(dorisInstanceId, connection -> {
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
    public List<String> listCatalogs(Long dorisInstanceId) {
        return metadataAction(dorisInstanceId, JdbcUtil::listCatalogs);
    }

    @Override
    public List<String> listSchemas(Long dorisInstanceId, String catalogName) {
        return metadataAction(dorisInstanceId, metaData -> JdbcUtil.listSchemas(metaData, catalogName));
    }

    @Override
    public List<String> listTables(Long dorisInstanceId, String catalogName, String schemaName, TableType[] tableTypes) {
        return metadataAction(dorisInstanceId, metaData ->
                JdbcUtil
                        .listTables(metaData, catalogName, schemaName, tableTypes)
                        .stream()
                        .map(BaseTable::getTableName)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Table getTableInfo(Long dorisInstanceId, String catalogName, String schemaName, @NotNull String tableName) {
        return metadataAction(dorisInstanceId, metaData -> JdbcUtil.getTableInfo(metaData, catalogName, schemaName, tableName, new MysqlDialect()));
    }

    @Override
    public List<Function> listFunctions(Long dorisInstanceId, String catalogName, String schemaName) {
        return metadataAction(dorisInstanceId, metaData -> JdbcUtil.getFunctions(metaData, catalogName, schemaName));
    }

    @Override
    public QueryResult executeSql(WsDorisSqlExecutionParam wsDorisSqlExecutionParam) {
        return manager.actionWithConnection(wsDorisSqlExecutionParam.getInstanceId(), connection -> {
            SqlDialect sqlDialect = new MysqlDialect();
            String sql = sqlDialect.getLimitationQuery(wsDorisSqlExecutionParam.getSql(), wsDorisSqlExecutionParam.getLimitation());
            log.info("Execution sql: {}", sql);
            try (Statement statement = connection.createStatement()) {
                boolean hasResultSet = statement.execute(sql);
                ResultSet rs = null;
                if (hasResultSet) {
                    rs = statement.getResultSet();
                }
                return QueryResult.fromResultSet(rs, sqlDialect);
            }
        });
    }


    @FunctionalInterface
    private interface DatabaseMetaFunction<T> {
        T apply(DatabaseMetaData metaData) throws SQLException;
    }
}
