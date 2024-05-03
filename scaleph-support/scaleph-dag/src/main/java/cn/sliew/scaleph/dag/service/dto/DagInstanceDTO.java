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

import cn.sliew.scaleph.system.model.BaseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * DAG 实例
 */
@Data
@Schema(name = "DagInstance", description = "DAG 实例")
public class DagInstanceDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "DAG配置")
    private DagConfigComplexDTO dagConfig;

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
