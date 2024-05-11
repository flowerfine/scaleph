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

import cn.sliew.scaleph.common.dict.common.Gender;
import cn.sliew.scaleph.common.dict.security.UserStatus;
import cn.sliew.scaleph.common.dict.security.UserType;
import cn.sliew.scaleph.system.model.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * <p>
 * 用户基本信息表
 * </p>
 *
 * @author liyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "用户信息", description = "用户基本信息表")
public class SecUserDTO extends BaseDTO {

    private static final long serialVersionUID = -1821402534416659344L;

    @Schema(description = "类型")
    private UserType type;

    @NotBlank
    @Length(min = 5, max = 30)
    @Pattern(regexp = "\\w+$")
    @Schema(description = "用户名")
    private String userName;

    @Length(max = 50)
    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "头像")
    private String avatar;

    @Email
    @NotNull
    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机")
    private String phone;

    @JsonIgnore
    @Schema(description = "密码")
    private String password;

    @Schema(description = "性别")
    private Gender gender;

    @Schema(description = "住址")
    private String address;

    @Schema(description = "自我介绍")
    private String summary;

    @Schema(description = "排序")
    private Integer order;

    @Schema(description = "status")
    private UserStatus status;

    @Schema(description = "备注")
    private String remark;

    private List<SecRoleDTO> roles;
}
