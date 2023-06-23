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

package cn.sliew.scaleph.log.service.dto;

import java.util.Date;

import cn.sliew.scaleph.system.model.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户操作日志
 * </p>
 *
 * @author liyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "操作日志信息", description = "用户操作日志")
public class LogActionDTO extends BaseDTO {

    private static final long serialVersionUID = 8003025002075099514L;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "操作时间")
    private Date actionTime;

    @Schema(description = "ip地址")
    private String ipAddress;

    @Schema(description = "操作接口地址")
    private String actionUrl;

    @Schema(description = "会话token字符串")
    private String token;

    @Schema(description = "客户端信息")
    private String clientInfo;

    @Schema(description = "操作系统信息")
    private String osInfo;

    @Schema(description = "浏览器信息")
    private String browserInfo;

    @Schema(description = "接口执行信息，包含请求结果")
    private String actionInfo;

}
