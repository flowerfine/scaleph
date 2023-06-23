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

package cn.sliew.scaleph.security.service.dto;

import cn.sliew.scaleph.common.dict.security.ResourceType;
import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author liyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "权限信息", description = "权限表")
public class SecPrivilegeDTO extends BaseDTO {

    private static final long serialVersionUID = 9063779257828908860L;

    @Schema(description = "权限标识")
    private String privilegeCode;

    @Schema(description = "权限名称")
    private String privilegeName;

    @Schema(description = "资源类型")
    private ResourceType resourceType;

    @Schema(description = "资源路径")
    private String resourcePath;

    @Schema(description = "上级权限id")
    private Long pid;

    @Schema(description = "下级资源")
    private List<SecPrivilegeDTO> children;

}
