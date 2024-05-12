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

package cn.sliew.scaleph.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author gleiyu
 */
@Data
@Schema(name = "用户注册信息", description = "用户注册信息")
public class RegisterInfoVO {
    private static final long serialVersionUID = -2183662104593082350L;

    @NotBlank
    @Length(min = 5, max = 30)
    @Pattern(regexp = "\\w+$")
    @Schema(description = "用户名")
    private String userName;

    @NotBlank
    @Email
    @Schema(description = "邮箱")
    private String email;

    @NotBlank
    @Length(min = 6, max = 32)
    @Schema(description = "密码")
    private String password;

    @NotBlank
    @Length(min = 6, max = 32)
    @Schema(description = "确认密码")
    private String confirmPassword;

    @NotBlank
    @Length(max = 5)
    @Schema(description = "验证码")
    private String authCode;

    @NotBlank
    @Schema(description = "验证码uuid")
    private String uuid;

}
