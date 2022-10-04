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

package cn.sliew.scaleph.security.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 在线用户信息
 *
 * @author gleiyu
 */
@Data
@ApiModel(value = "在线用户信息", description = "在线用户信息")
public class OnlineUserVO {
    private String userName;
    private String email;
    private String ipAddress;
    private Date loginTime;
    private String token;
    private List<String> privileges;
    private List<String> roles;
    private Boolean remember;
    private Long expireTime;

    // fixme private boolean remember may be better?
    public Boolean getRemember() {
        return remember != null && remember;
    }
}
