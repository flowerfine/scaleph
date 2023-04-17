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

package cn.sliew.scaleph.catalog.service.dto;

import cn.sliew.scaleph.common.dict.catalog.CatalogTableKind;
import cn.sliew.scaleph.common.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Map;

@Data
@ApiModel(value = "CatalogTable对象", description = "table")
public class CatalogTableDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    private Long databaseId;

    private CatalogTableKind kind;

    private String name;

    private Map<String, String> properties;

    private SchemaDTO schema;

    private String originalQuery;

    private String expandedQuery;

    private String remark;

}