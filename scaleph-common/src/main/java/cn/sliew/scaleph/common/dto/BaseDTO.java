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

package cn.sliew.scaleph.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gleiyu
 */
@Data
public class BaseDTO implements Serializable {

    private static final long serialVersionUID = -3170630380110141492L;

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("creator")
    private String creator;

    @ApiModelProperty("create time")
    private Date createTime;

    @ApiModelProperty("editor")
    private String editor;

    @ApiModelProperty("update time")
    private Date updateTime;

}
