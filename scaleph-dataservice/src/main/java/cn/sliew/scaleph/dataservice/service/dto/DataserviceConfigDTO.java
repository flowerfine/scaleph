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

package cn.sliew.scaleph.dataservice.service.dto;

import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 数据服务 配置
 * </p>
 */
@Data
@Schema(name = "DataserviceConfig对象", description = "数据服务 配置")
public class DataserviceConfigDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "project id")
    private Long projectId;

    @Schema(description = "name")
    private String name;

    @Schema(description = "uri path")
    private String path;

    @Schema(description = "http method")
    private String method;

    @Schema(description = "http content type")
    private String contentType;

    @Schema(description = "status, disabled or enabled")
    private String status;

    @Schema(description = "parameter map")
    private DataserviceParameterMapDTO parameterMap;

    @Schema(description = "result map")
    private DataserviceResultMapDTO resultMap;

    @Schema(description = "type")
    private String type;

    @Schema(description = "query")
    private String query;

    @Schema(description = "备注")
    private String remark;

}
