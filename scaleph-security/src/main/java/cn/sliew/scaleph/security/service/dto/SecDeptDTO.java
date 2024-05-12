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

import cn.sliew.scaleph.common.dict.security.DeptStatus;
import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * @author gleiyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "部门信息", description = "部门表")
public class SecDeptDTO extends BaseDTO {
    private static final long serialVersionUID = 1457138850402052741L;

    @Schema(description = "部门编号")
    private String deptCode;

    @NotNull
    @Length(max = 60)
    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "上级部门id")
    private Long pid;

    @Schema(description = "部门状态")
    private DeptStatus deptStatus;

}
