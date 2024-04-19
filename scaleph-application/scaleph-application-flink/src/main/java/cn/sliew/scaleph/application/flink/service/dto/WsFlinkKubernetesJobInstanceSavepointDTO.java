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

package cn.sliew.scaleph.application.flink.service.dto;

import cn.sliew.scaleph.common.dict.flink.kubernetes.SavepointFormatType;
import cn.sliew.scaleph.common.dict.flink.kubernetes.SavepointTriggerType;
import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * flink kubernetes job instance
 * </p>
 */
@Data
@Schema(name = "WsFlinkKubernetesJobInstance对象", description = "flink kubernetes job instance")
public class WsFlinkKubernetesJobInstanceSavepointDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "flink kubernetes job instance id")
    private Long wsFlinkKubernetesJobInstanceId;

    @Schema(description = "savepoint timestamp")
    private Long timeStamp;

    @Schema(description = "savepoint location")
    private String location;

    @Schema(description = "savepoint trigger type")
    private SavepointTriggerType triggerType;

    @Schema(description = "savepoint format type")
    private SavepointFormatType formatType;
}
