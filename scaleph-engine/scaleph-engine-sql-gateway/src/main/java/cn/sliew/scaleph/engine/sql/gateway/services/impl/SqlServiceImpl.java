/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.scaleph.engine.sql.gateway.services.impl;

import java.util.List;

import org.apache.calcite.rel.RelCollations;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.logical.LogicalSort;
import org.apache.calcite.rel.type.RelDataTypeSystem;
import org.apache.calcite.rex.RexBuilder;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.type.BasicSqlType;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.table.api.internal.TableEnvironmentInternal;
import org.apache.flink.table.api.internal.TableResultInternal;
import org.apache.flink.table.delegation.Parser;
import org.apache.flink.table.gateway.api.operation.OperationHandle;
import org.apache.flink.table.gateway.api.session.SessionHandle;
import org.apache.flink.table.gateway.api.utils.SqlGatewayException;
import org.apache.flink.table.gateway.service.context.SessionContext;
import org.apache.flink.table.gateway.service.operation.OperationExecutor;
import org.apache.flink.table.gateway.service.result.ResultFetcher;
import org.apache.flink.table.operations.Operation;
import org.apache.flink.table.operations.QueryOperation;
import org.apache.flink.table.operations.SinkModifyOperation;
import org.apache.flink.table.planner.calcite.FlinkTypeFactory;
import org.apache.flink.table.planner.operations.PlannerQueryOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.sliew.scaleph.engine.sql.gateway.services.SessionService;
import cn.sliew.scaleph.engine.sql.gateway.services.SqlService;
import cn.sliew.scaleph.engine.sql.gateway.services.dto.FlinkSqlGatewaySession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SqlServiceImpl implements SqlService {

    private final SessionService sessionService;

    @Autowired
    public SqlServiceImpl(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public Object validate(SessionHandle sessionHandle, String statement, Configuration executionConfig)
            throws Exception {
        TableEnvironmentInternal tableEnvironment = sessionService
                .getSession(sessionHandle)
                .getSessionContext()
                .createOperationExecutor(executionConfig)
                .getTableEnvironment();
        Parser parser = tableEnvironment.getParser();
        List<Operation> operations = parser.parse(statement);
        return !operations.isEmpty();
    }

    @Override
    public List<String> completeStatement(SessionHandle sessionHandle, String statement, int position)
            throws SqlGatewayException {
        FlinkSqlGatewaySession session = sessionService.getSession(sessionHandle);
        SessionContext sessionContext = session.getSessionContext();
        OperationExecutor operationExecutor = sessionContext.createOperationExecutor(sessionContext.getSessionConf());
        return List.of(operationExecutor.getTableEnvironment().getParser().getCompletionHints(statement, position));
    }

    @Override
    public OperationHandle executeStatement(
            SessionHandle sessionHandle, String statement, long executionTimeoutMs, Configuration executionConfig)
            throws SqlGatewayException {
        try {
            if (executionTimeoutMs > 0) {
                // TODO: support the feature in FLINK-27838
                throw new UnsupportedOperationException("SqlGatewayService doesn't support timeout mechanism now.");
            }

            return sessionService
                    .getSession(sessionHandle)
                    .getSessionContext()
                    .getOperationManager()
                    .submitOperation(handle -> sessionService
                            .getSession(sessionHandle)
                            .getSessionContext()
                            .createOperationExecutor(executionConfig)
                            .executeStatement(handle, statement));
        } catch (Throwable t) {
            log.error("Failed to execute statement.", t);
            throw new SqlGatewayException("Failed to execute statement.", t);
        }
    }

    @Override
    public OperationHandle previewStatement(
            SessionHandle sessionHandle, String statement, long executionTimeoutMs, Configuration executionConfig)
            throws SqlGatewayException {
        if (executionTimeoutMs > 0) {
            throw new UnsupportedOperationException("SqlGatewayService doesn't support timeout mechanism now.");
        }
        return previewStatement(sessionHandle, statement, executionConfig, -1);
    }

    @Override
    public OperationHandle previewStatement(
            SessionHandle sessionHandle, String statement, Configuration executionConfig, long limit)
            throws SqlGatewayException {
        FlinkSqlGatewaySession session = sessionService.getSession(sessionHandle);
        SessionContext sessionContext = session.getSessionContext();
        Configuration sessionConf = new Configuration(sessionContext.getSessionConf());
        sessionConf.addAll(sessionConf);
        TableEnvironmentInternal tableEnvironment =
                sessionContext.createOperationExecutor(sessionConf).getTableEnvironment();
        QueryOperation queryOperation = parseSqlToQueryOperation(tableEnvironment, statement, limit);
        return sessionContext.getOperationManager().submitOperation(operationHandle -> {
            TableResultInternal tableResultInternal = tableEnvironment.executeInternal(queryOperation);
            return ResultFetcher.fromTableResult(operationHandle, tableResultInternal, true);
        });
    }

    private QueryOperation parseSqlToQueryOperation(TableEnvironmentInternal tEnv, String sql, long limitation) {
        Parser parser = tEnv.getParser();
        List<Operation> operations = parser.parse(sql);
        if (operations.size() == 1) {
            Operation operation = operations.get(0);
            QueryOperation queryOperation;
            if (operation instanceof SinkModifyOperation) {
                queryOperation = ((SinkModifyOperation) operation).getChild();
            } else if (operation instanceof QueryOperation) {
                queryOperation = (QueryOperation) operation;
            } else {
                throw new IllegalArgumentException("Only `SELECT` and `INSERT` statement is supported!");
            }
            if (limitation > 0 && queryOperation instanceof PlannerQueryOperation) {
                PlannerQueryOperation plannerQueryOperation = (PlannerQueryOperation) queryOperation;
                RelNode calciteTree = plannerQueryOperation.getCalciteTree();
                RexNode fetch = new RexBuilder(
                                new FlinkTypeFactory(tEnv.getClass().getClassLoader(), RelDataTypeSystem.DEFAULT))
                        .makeLiteral(limitation, new BasicSqlType(RelDataTypeSystem.DEFAULT, SqlTypeName.DECIMAL));
                LogicalSort logicalSort = LogicalSort.create(calciteTree, RelCollations.EMPTY, null, fetch);
                return new PlannerQueryOperation(logicalSort);
            }
            return queryOperation;
        }
        throw new IllegalArgumentException("Only one statement should appear");
    }
}
