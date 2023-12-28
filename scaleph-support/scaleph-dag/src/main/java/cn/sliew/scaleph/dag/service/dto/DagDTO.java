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

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * DAG 实例
 */
@Data
@Schema(name = "Dag", description = "DAG")
public class DagDTO extends DagInstanceDTO {

    @Schema(description = "元数据")
    private JsonNode dagMeta;

    @Schema(description = "属性")
    private JsonNode dagAttrs;

    @Schema(description = "连线")
    private List<DagLinkDTO> links;

    @Schema(description = "步骤")
    private List<DagStepDTO> steps;

}
