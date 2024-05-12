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

package cn.sliew.scaleph.dataservice.service.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DataserviceConfigSaveParam {

    @Schema(description = "id")
    private Long id;

    @NotNull
    @Schema(description = "project id")
    private Long projectId;

    @NotBlank
    @Schema(description = "name")
    private String name;

    @NotBlank
    @Schema(description = "uri path")
    private String path;

    @NotBlank
    @Schema(description = "http method")
    private String method;

    @NotBlank
    @Schema(description = "http content type")
    private String contentType;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "参数映射")
    private List<DataserviceParameterMappingParam> parameterMappings;

    @Schema(description = "结果映射")
    private List<DataserviceResultMappingParam> resultMappings;

    @NotBlank
    @Schema(description = "类型")
    private String type;

    @NotBlank
    @Schema(description = "sql")
    private String query;
}
