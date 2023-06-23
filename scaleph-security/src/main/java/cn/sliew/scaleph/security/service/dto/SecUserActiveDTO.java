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

import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 用户邮箱激活日志表
 * </p>
 *
 * @author liyu
 * @since 2021-09-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "用户激活码信息", description = "用户邮箱激活日志表")
public class SecUserActiveDTO extends BaseDTO {

    private static final long serialVersionUID = 8583076330823769080L;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "激活码")
    private String activeCode;

    @Schema(description = "激活码过期时间戳")
    private Long expiryTime;

    @Schema(description = "激活时间")
    private Date activeTime;

}
