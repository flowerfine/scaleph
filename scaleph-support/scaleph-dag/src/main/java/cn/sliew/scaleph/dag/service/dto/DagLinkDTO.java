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

package cn.sliew.scaleph.dag.service.dto;

import cn.sliew.scaleph.dao.entity.master.dag.DagConfigLink;
import cn.sliew.scaleph.system.model.BaseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * DAG 连线
 */
@Data
@Schema(name = "DagLink", description = "DAG 连线")
public class DagLinkDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "DAG id")
    private Long dagInstanceId;

    @Schema(description = "连线id")
    private DagConfigLink dagConfigLink;

    @Schema(description = "instance id")
    private String instanceId;

    @Schema(description = "输入参数")
    private JsonNode inputs;

    @Schema(description = "输出参数")
    private JsonNode outputs;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "启动时间")
    private Date startTime;

    @Schema(description = "结束时间")
    private Date endTime;
}
