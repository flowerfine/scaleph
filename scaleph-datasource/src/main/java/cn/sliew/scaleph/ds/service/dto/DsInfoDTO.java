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

import cn.sliew.scaleph.common.dto.BaseDTO;
import cn.sliew.scaleph.ds.modal.PropValuePair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "DsInfo对象", description = "data source info")
public class DsInfoDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("data source type id")
    private DsTypeDTO dsType;

    @ApiModelProperty("version")
    private String version;

    @ApiModelProperty("name")
    private String name;

    @ApiModelProperty("props")
    private Map<String, Object> props;

    @ApiModelProperty("additional props")
    private List<PropValuePair> additionalProps;

    @ApiModelProperty("remark")
    private String remark;

}
