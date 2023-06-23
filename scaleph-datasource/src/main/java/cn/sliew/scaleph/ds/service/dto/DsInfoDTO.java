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

package cn.sliew.scaleph.ds.service.dto;

import cn.sliew.scaleph.ds.modal.PropValuePair;
import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * data source info
 * </p>
 */
@Data
@EqualsAndHashCode
@Schema(name = "DsInfo对象", description = "data source info")
public class DsInfoDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "data source type id")
    private DsTypeDTO dsType;

    @Schema(description = "version")
    private String version;

    @Schema(description = "name")
    private String name;

    @Schema(description = "props")
    private Map<String, Object> props;

    @Schema(description = "additional props")
    private List<PropValuePair> additionalProps;

    @Schema(description = "remark")
    private String remark;

}
