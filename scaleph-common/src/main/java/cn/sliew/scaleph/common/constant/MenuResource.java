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

@Deprecated
@Getter
public enum MenuResource {
    ;

    public static Route ROOT = new Route(0, "Scaleph", PageCode.SCALEPH, "/", null);

    public static Route STUDIO = new Route(1, "工作台", PageCode.STUDIO, "/studio", ROOT);
    public static Route PROJECT = new Route(2, "项目", PageCode.PROJECT, "/project", ROOT);
    public static Route RESOURCE = new Route(3, "资源", PageCode.RESOURCE, "/resource", ROOT);
    public static Route DATA_SOURCE = new Route(4, "数据源", PageCode.DATA_SOURCE, "/dataSource", ROOT);
    public static Route DATA_STANDARD = new Route(5, "数据标准", PageCode.STDATA, "/stdata", ROOT);
    public static Route ADMIN = new Route(6, "系统管理", PageCode.ADMIN, "/admin", ROOT);

    public static Route WORKSPACE_JOB = new Route(7, "作业管理", PageCode.WORKSPACE_JOB, "/workspace/job", PROJECT);
    public static Route WORKSPACE_CLUSTER = new Route(8, "项目管理", PageCode.WORKSPACE_CLUSTER, "/workspace/cluster", PROJECT);

}
