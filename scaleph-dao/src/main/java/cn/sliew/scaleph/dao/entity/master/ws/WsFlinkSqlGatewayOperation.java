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

package cn.sliew.scaleph.dao.entity.master.ws;

import cn.sliew.scaleph.common.dict.common.YesOrNo;
import cn.sliew.scaleph.dao.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * flink sql gateway operation
 * </p>
 */
@Data
@TableName("ws_flink_sql_gateway_operation")
public class WsFlinkSqlGatewayOperation extends BaseDO {

    private static final long serialVersionUID = 1L;

    @TableField("session_handler")
    private String sessionHandler;

    @TableField("operation_handler")
    private String operationHandler;

    @TableField("operation_status")
    private String operationStatus;

    @TableField("operation_error")
    private String operationError;

    @TableField("job_id")
    private String jobId;

    @TableField("is_query")
    private YesOrNo isQuery;

}
