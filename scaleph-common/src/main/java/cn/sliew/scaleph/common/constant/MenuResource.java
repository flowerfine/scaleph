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

package cn.sliew.scaleph.common.constant;

import lombok.Getter;

@Getter
public enum MenuResource {

    STUDIO("/studio", "工作台"),
    PROJECT("/project", "项目"),
    RESOURCE("/resource", "资源"),
    DATA_SOURCE("/dataSource", "数据源"),
    DATA_STANDARD("/stdata", "数据标准"),
    ADMIN("/admin", "系统管理"),
    ;

    private String path;
    private String desc;

    MenuResource(String path, String desc) {
        this.path = path;
        this.desc = desc;
    }
}
