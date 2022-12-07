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
    ;

    public static Route ROOT = new Route(0, "Scaleph", PrivilegeConstants.ModuleCode.STUDIO, "/", null);

    public static Route STUDIO = new Route(1, "工作台", PrivilegeConstants.ModuleCode.STUDIO, "/studio", ROOT);
    public static Route PROJECT = new Route(2, "项目", PrivilegeConstants.ModuleCode.DATADEV_PROJECT, "/project", ROOT);
    public static Route RESOURCE = new Route(3, "资源", PrivilegeConstants.ModuleCode.DATADEV_RESOURCE, "/resource", ROOT);
    public static Route DATA_SOURCE = new Route(4, "数据源", PrivilegeConstants.ModuleCode.DATADEV_DATASOURCE, "/dataSource", ROOT);
    public static Route DATA_STANDARD = new Route(4, "数据标准", PrivilegeConstants.ModuleCode.STDATA, "/stdata", ROOT);
    public static Route ADMIN = new Route(4, "系统管理", PrivilegeConstants.ModuleCode.STDATA, "/admin", ROOT);

}
