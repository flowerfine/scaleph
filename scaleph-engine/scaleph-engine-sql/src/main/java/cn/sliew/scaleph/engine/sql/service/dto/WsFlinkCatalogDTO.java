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

package cn.sliew.scaleph.engine.sql.service.dto;

import cn.sliew.scaleph.common.dict.catalog.CatalogType;
import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode
@ApiModel(value = "FlinkCatalog对象", description = "flink catalog")
public class WsFlinkCatalogDTO extends BaseDTO {

    @ApiModelProperty("`type`")
    private CatalogType type;

    @ApiModelProperty("`name`")
    private String name;

    @ApiModelProperty("properties")
    private Map<String, String> properties;

    @ApiModelProperty("remark")
    private String remark;

}
