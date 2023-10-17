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

package cn.sliew.scaleph.engine.sql.gateway.services.impl;

import cn.sliew.scaleph.dao.entity.master.ws.WsFlinkSqlGatewayOperation;
import cn.sliew.scaleph.dao.mapper.master.ws.WsFlinkSqlGatewayOperationMapper;
import cn.sliew.scaleph.engine.sql.gateway.services.OperationService;
import cn.sliew.scaleph.engine.sql.gateway.services.SessionService;
import cn.sliew.scaleph.engine.sql.gateway.services.dto.FlinkSqlGatewayOperation;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.EnumUtils;
import org.apache.flink.api.common.JobID;
import org.apache.flink.table.catalog.ResolvedSchema;
import org.apache.flink.table.gateway.api.operation.OperationHandle;
import org.apache.flink.table.gateway.api.operation.OperationStatus;
import org.apache.flink.table.gateway.api.results.OperationInfo;
import org.apache.flink.table.gateway.api.results.ResultSet;
import org.apache.flink.table.gateway.api.session.SessionHandle;
import org.apache.flink.table.gateway.api.utils.SqlGatewayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.Callable;

@Service
public class OperationServiceImpl implements OperationService {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private WsFlinkSqlGatewayOperationMapper wsFlinkSqlGatewayOperationMapper;

    @Override
    public OperationHandle submitOperation(SessionHandle sessionHandle, Callable<ResultSet> executor) throws SqlGatewayException {
        return null;
    }

    @Override
    public void cancelOperation(SessionHandle sessionHandle, OperationHandle operationHandle) throws SqlGatewayException {

    }

    @Override
    public void closeOperation(SessionHandle sessionHandle, OperationHandle operationHandle) throws SqlGatewayException {

    }

    @Override
    public OperationInfo getOperationInfo(SessionHandle sessionHandle, OperationHandle operationHandle) throws SqlGatewayException {
        return null;
    }

    @Override
    public ResolvedSchema getOperationResultSchema(SessionHandle sessionHandle, OperationHandle operationHandle) throws SqlGatewayException {
        return null;
    }

    private FlinkSqlGatewayOperation getOperation(SessionHandle sessionHandle, OperationHandle operationHandle) {
        LambdaQueryWrapper<WsFlinkSqlGatewayOperation> queryWrapper = Wrappers.lambdaQuery(WsFlinkSqlGatewayOperation.class)
                .eq(WsFlinkSqlGatewayOperation::getSessionHandler, sessionHandle.toString())
                .eq(WsFlinkSqlGatewayOperation::getOperationHandler, operationHandle.toString());

        WsFlinkSqlGatewayOperation record = wsFlinkSqlGatewayOperationMapper.selectOne(queryWrapper);
        if (record == null) {
            throw new SqlGatewayException(
                    String.format("Can not find the submitted operation with the %s.", operationHandle));
        }
        return convertOperation(record);
    }

    private FlinkSqlGatewayOperation convertOperation(WsFlinkSqlGatewayOperation record) {
        FlinkSqlGatewayOperation operation = new FlinkSqlGatewayOperation();
        operation.setSessionHandle(new SessionHandle(UUID.fromString(record.getSessionHandler())));
        operation.setOperationHandle(new OperationHandle(UUID.fromString(record.getOperationHandler())));
        operation.setOperationStatus(EnumUtils.getEnum(OperationStatus.class, record.getOperationStatus()));
        operation.setOperationError(record.getOperationError());
        if (StringUtils.hasText(record.getJobId())) {
            operation.setJobID(JobID.fromHexString(record.getJobId()));
        }
        operation.setIsQuery(record.getIsQuery());
        return operation;
    }
}
