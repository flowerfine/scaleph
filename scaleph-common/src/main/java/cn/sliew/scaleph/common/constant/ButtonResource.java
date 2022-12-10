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

public enum ButtonResource {
    ;

    public static Route PROJECT_ADD = new Route(200000, "创建项目", PageCode.PROJECT_LIST, "/project", PageResource.PROJECT_LIST);
    public static Route PROJECT_EDIT = new Route(200000, "修改项目", PageCode.PROJECT_LIST, "/project", PageResource.PROJECT_LIST);
    public static Route PROJECT_DELETE = new Route(200000, "删除项目", PageCode.PROJECT_LIST, "/project", PageResource.PROJECT_LIST);
    public static Route PROJECT_VIEW = new Route(200000, "进入项目", PageCode.PROJECT_LIST, "/project", PageResource.PROJECT_LIST);


}
