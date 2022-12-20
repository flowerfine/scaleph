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

import cn.sliew.scaleph.common.annotation.Desc;

/**
 * 权限编码
 * 1位前缀+3位模块code
 *
 * @author gleiyu
 */
@Deprecated
public enum PrivilegeConstants {
    ;

    /**
     * 权限编码前缀
     */
    public static final String PRIVILEGE_PREFIX = "p";
    /**
     * 角色编码前缀
     */
    public static final String ROLE_PREFIX = "r";
    /**
     * 0-菜单权限
     */
    @Desc("{\"id\":10,\"privilegeName\":\"工作台\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":0}")
    public static final String STUDIO_SHOW = PRIVILEGE_PREFIX + ModuleCode.STUDIO + ActionCode.SHOW;
    @Desc("{\"id\":1001,\"privilegeName\":\"数据看板\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":10}")
    public static final String STUDIO_DATA_BOARD_SHOW =
            PRIVILEGE_PREFIX + ModuleCode.STUDIO_DATA_BOARD + ActionCode.SHOW;
    @Desc("{\"id\":1002,\"privilegeName\":\"项目管理\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":10}")
    public static final String DATADEV_PROJECT_SHOW = PRIVILEGE_PREFIX + ModuleCode.DATADEV_PROJECT + ActionCode.SHOW;
    @Desc("{\"id\":20,\"privilegeName\":\"数据开发\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":0}")
    public static final String DATADEV_SHOW = PRIVILEGE_PREFIX + ModuleCode.DATADEV + ActionCode.SHOW;
    @Desc("{\"id\":2001,\"privilegeName\":\"数据源\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":20}")
    public static final String DATADEV_DATASOURCE_SHOW =
            PRIVILEGE_PREFIX + ModuleCode.DATADEV_DATASOURCE + ActionCode.SHOW;

    @Desc("{\"id\":2003,\"privilegeName\":\"资源管理\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":20}")
    public static final String DATADEV_RESOURCE_SHOW = PRIVILEGE_PREFIX + ModuleCode.DATADEV_RESOURCE + ActionCode.SHOW;
    @Desc("{\"id\":2004,\"privilegeName\":\"作业管理\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":20}")
    public static final String DATADEV_JOB_SHOW = PRIVILEGE_PREFIX + ModuleCode.DATADEV_JOB + ActionCode.SHOW;
    @Desc("{\"id\":2005,\"privilegeName\":\"集群管理\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":20}")
    public static final String DATADEV_CLUSTER_SHOW = PRIVILEGE_PREFIX + ModuleCode.DATADEV_CLUSTER + ActionCode.SHOW;
    @Desc("{\"id\":30,\"privilegeName\":\"运维中心\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":0}")
    public static final String OPSCENTER_SHOW = PRIVILEGE_PREFIX + ModuleCode.OPSCENTER + ActionCode.SHOW;
    @Desc("{\"id\":3001,\"privilegeName\":\"周期任务\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":30}")
    public static final String OPSCENTER_BATCH_SHOW = PRIVILEGE_PREFIX + ModuleCode.OPSCENTER_BATCH + ActionCode.SHOW;
    @Desc("{\"id\":3002,\"privilegeName\":\"实时任务\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":30}")
    public static final String OPSCENTER_REALTIME_SHOW =
            PRIVILEGE_PREFIX + ModuleCode.OPSCENTER_REALTIME + ActionCode.SHOW;
    @Desc("{\"id\":40,\"privilegeName\":\"数据标准\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":0}")
    public static final String STDATA_SHOW = PRIVILEGE_PREFIX + ModuleCode.STDATA + ActionCode.SHOW;
    @Desc("{\"id\":4001,\"privilegeName\":\"参考数据\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":40}")
    public static final String STDATA_REF_DATA_SHOW = PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA + ActionCode.SHOW;
    @Desc("{\"id\":4002,\"privilegeName\":\"数据映射\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":40}")
    public static final String STDATA_REF_DATA_MAP_SHOW =
            PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA_MAP + ActionCode.SHOW;
    @Desc("{\"id\":4003,\"privilegeName\":\"数据元\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":40}")
    public static final String STDATA_DATA_ELEMENT_SHOW =
            PRIVILEGE_PREFIX + ModuleCode.STDATA_DATA_ELEMENT + ActionCode.SHOW;
    @Desc("{\"id\":4004,\"privilegeName\":\"业务系统\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":40}")
    public static final String STDATA_SYSTEM_SHOW = PRIVILEGE_PREFIX + ModuleCode.STDATA_SYSTEM + ActionCode.SHOW;
    @Desc("{\"id\":50,\"privilegeName\":\"系统管理\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":0}")
    public static final String ADMIN_SHOW = PRIVILEGE_PREFIX + ModuleCode.ADMIN + ActionCode.SHOW;
    @Desc("{\"id\":5001,\"privilegeName\":\"用户管理\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":50}")
    public static final String USER_SHOW = PRIVILEGE_PREFIX + ModuleCode.USER + ActionCode.SHOW;
    @Desc("{\"id\":5002,\"privilegeName\":\"权限管理\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":50}")
    public static final String PRIVILEGE_SHOW = PRIVILEGE_PREFIX + ModuleCode.PRIVILEGE + ActionCode.SHOW;
    @Desc("{\"id\":5003,\"privilegeName\":\"数据字典\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":50}")
    public static final String DICT_SHOW = PRIVILEGE_PREFIX + ModuleCode.DICT + ActionCode.SHOW;
    @Desc("{\"id\":5004,\"privilegeName\":\"系统设置\",\"resourceType\":0, \"resourcePath\":\"\",\"pid\":50}")
    public static final String SETTING_SHOW = PRIVILEGE_PREFIX + ModuleCode.SETTING + ActionCode.SHOW;
    /**
     * 1-操作权限
     */

    @Desc("{\"id\":200101,\"privilegeName\":\"数据源\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    public static final String DATADEV_DATASOURCE_SELECT =
            PRIVILEGE_PREFIX + ModuleCode.DATADEV_DATASOURCE + ActionCode.SELECT;
    @Desc("{\"id\":20010101,\"privilegeName\":\"新增数据源\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":200101}")
    public static final String DATADEV_DATASOURCE_ADD =
            PRIVILEGE_PREFIX + ModuleCode.DATADEV_DATASOURCE + ActionCode.ADD;
    @Desc("{\"id\":20010102,\"privilegeName\":\"删除数据源\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":200101}")
    public static final String DATADEV_DATASOURCE_DELETE =
            PRIVILEGE_PREFIX + ModuleCode.DATADEV_DATASOURCE + ActionCode.DELETE;
    @Desc("{\"id\":20010103,\"privilegeName\":\"修改数据源\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":200101}")
    public static final String DATADEV_DATASOURCE_EDIT =
            PRIVILEGE_PREFIX + ModuleCode.DATADEV_DATASOURCE + ActionCode.EDIT;
    @Desc("{\"id\":20010104,\"privilegeName\":\"查看密码\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":200101}")
    public static final String DATADEV_DATASOURCE_SECURITY =
            PRIVILEGE_PREFIX + ModuleCode.DATADEV_DATASOURCE + ActionCode.SECURITY;
    @Desc("{\"id\":100201,\"privilegeName\":\"项目管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    public static final String DATADEV_PROJECT_SELECT =
            PRIVILEGE_PREFIX + ModuleCode.DATADEV_PROJECT + ActionCode.SELECT;
    @Desc("{\"id\":10020101,\"privilegeName\":\"新增项目\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100201}")
    public static final String DATADEV_PROJECT_ADD = PRIVILEGE_PREFIX + ModuleCode.DATADEV_PROJECT + ActionCode.ADD;
    @Desc("{\"id\":10020102,\"privilegeName\":\"删除项目\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100201}")
    public static final String DATADEV_PROJECT_DELETE =
            PRIVILEGE_PREFIX + ModuleCode.DATADEV_PROJECT + ActionCode.DELETE;
    @Desc("{\"id\":10020103,\"privilegeName\":\"修改项目\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":100201}")
    public static final String DATADEV_PROJECT_EDIT = PRIVILEGE_PREFIX + ModuleCode.DATADEV_PROJECT + ActionCode.EDIT;
    @Desc("{\"id\":200301,\"privilegeName\":\"资源管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    public static final String DATADEV_RESOURCE_SELECT =
            PRIVILEGE_PREFIX + ModuleCode.DATADEV_RESOURCE + ActionCode.SELECT;
    @Desc("{\"id\":20030101,\"privilegeName\":\"新增资源\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":200301}")
    public static final String DATADEV_RESOURCE_ADD = PRIVILEGE_PREFIX + ModuleCode.DATADEV_RESOURCE + ActionCode.ADD;
    @Desc("{\"id\":20030102,\"privilegeName\":\"删除资源\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":200301}")
    public static final String DATADEV_RESOURCE_DELETE =
            PRIVILEGE_PREFIX + ModuleCode.DATADEV_RESOURCE + ActionCode.DELETE;
    @Desc("{\"id\":20030103,\"privilegeName\":\"修改资源\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":200301}")
    public static final String DATADEV_RESOURCE_EDIT = PRIVILEGE_PREFIX + ModuleCode.DATADEV_RESOURCE + ActionCode.EDIT;
    @Desc("{\"id\":20030104,\"privilegeName\":\"下载资源\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":200301}")
    public static final String DATADEV_RESOURCE_DOWNLOAD =
            PRIVILEGE_PREFIX + ModuleCode.DATADEV_RESOURCE + ActionCode.DOWNLOAD;
    @Desc("{\"id\":200401,\"privilegeName\":\"作业管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    public static final String DATADEV_JOB_SELECT = PRIVILEGE_PREFIX + ModuleCode.DATADEV_JOB + ActionCode.SELECT;
    @Desc("{\"id\":20040101,\"privilegeName\":\"新增作业\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":200401}")
    public static final String DATADEV_JOB_ADD = PRIVILEGE_PREFIX + ModuleCode.DATADEV_JOB + ActionCode.ADD;
    @Desc("{\"id\":20040102,\"privilegeName\":\"删除作业\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":200401}")
    public static final String DATADEV_JOB_DELETE = PRIVILEGE_PREFIX + ModuleCode.DATADEV_JOB + ActionCode.DELETE;
    @Desc("{\"id\":20040103,\"privilegeName\":\"修改作业\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":200401}")
    public static final String DATADEV_JOB_EDIT = PRIVILEGE_PREFIX + ModuleCode.DATADEV_JOB + ActionCode.EDIT;
    @Desc("{\"id\":200402,\"privilegeName\":\"目录管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    public static final String DATADEV_DIR_SELECT = PRIVILEGE_PREFIX + ModuleCode.DATADEV_DIR + ActionCode.SELECT;
    @Desc("{\"id\":20040201,\"privilegeName\":\"新增目录\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":200402}")
    public static final String DATADEV_DIR_ADD = PRIVILEGE_PREFIX + ModuleCode.DATADEV_DIR + ActionCode.ADD;
    @Desc("{\"id\":20040202,\"privilegeName\":\"删除目录\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":200402}")
    public static final String DATADEV_DIR_DELETE = PRIVILEGE_PREFIX + ModuleCode.DATADEV_DIR + ActionCode.DELETE;
    @Desc("{\"id\":20040203,\"privilegeName\":\"修改目录\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":200402}")
    public static final String DATADEV_DIR_EDIT = PRIVILEGE_PREFIX + ModuleCode.DATADEV_DIR + ActionCode.EDIT;
    @Desc("{\"id\":200501,\"privilegeName\":\"集群管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    public static final String DATADEV_CLUSTER_SELECT =
            PRIVILEGE_PREFIX + ModuleCode.DATADEV_CLUSTER + ActionCode.SELECT;
    @Desc("{\"id\":20050101,\"privilegeName\":\"新增集群\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":200501}")
    public static final String DATADEV_CLUSTER_ADD = PRIVILEGE_PREFIX + ModuleCode.DATADEV_CLUSTER + ActionCode.ADD;
    @Desc("{\"id\":20050102,\"privilegeName\":\"删除集群\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":200501}")
    public static final String DATADEV_CLUSTER_DELETE =
            PRIVILEGE_PREFIX + ModuleCode.DATADEV_CLUSTER + ActionCode.DELETE;
    @Desc("{\"id\":20050103,\"privilegeName\":\"修改集群\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":200501}")
    public static final String DATADEV_CLUSTER_EDIT = PRIVILEGE_PREFIX + ModuleCode.DATADEV_CLUSTER + ActionCode.EDIT;
    @Desc("{\"id\":300101,\"privilegeName\":\"周期任务\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    public static final String OPSCENTER_BATCH_SELECT =
            PRIVILEGE_PREFIX + ModuleCode.OPSCENTER_BATCH + ActionCode.SELECT;
    @Desc("{\"id\":300201,\"privilegeName\":\"实时任务\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    public static final String OPSCENTER_REALTIME_SELECT =
            PRIVILEGE_PREFIX + ModuleCode.OPSCENTER_REALTIME + ActionCode.SELECT;
    @Desc("{\"id\":400101,\"privilegeName\":\"参考数据类型管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    public static final String STDATA_REF_DATA_TYPE_SELECT =
            PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA_TYPE + ActionCode.SELECT;
    @Desc("{\"id\":40010101,\"privilegeName\":\"新增参考数据类型\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":400101}")
    public static final String STDATA_REF_DATA_TYPE_ADD =
            PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA_TYPE + ActionCode.ADD;
    @Desc("{\"id\":40010102,\"privilegeName\":\"删除参考数据类型\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":400101}")
    public static final String STDATA_REF_DATA_TYPE_DELETE =
            PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA_TYPE + ActionCode.DELETE;
    @Desc("{\"id\":40010103,\"privilegeName\":\"修改参考数据类型\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":400101}")
    public static final String STDATA_REF_DATA_TYPE_EDIT =
            PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA_TYPE + ActionCode.EDIT;
    @Desc("{\"id\":400102,\"privilegeName\":\"参考数据管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    public static final String STDATA_REF_DATA_SELECT =
            PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA + ActionCode.SELECT;
    @Desc("{\"id\":40010201,\"privilegeName\":\"新增参考数据\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":400102}")
    public static final String STDATA_REF_DATA_ADD = PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA + ActionCode.ADD;
    @Desc("{\"id\":40010202,\"privilegeName\":\"删除参考数据\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":400102}")
    public static final String STDATA_REF_DATA_DELETE =
            PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA + ActionCode.DELETE;
    @Desc("{\"id\":40010203,\"privilegeName\":\"修改参考数据\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":400102}")
    public static final String STDATA_REF_DATA_EDIT = PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA + ActionCode.EDIT;
    @Desc("{\"id\":400201,\"privilegeName\":\"参考数据映射管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    public static final String STDATA_REF_DATA_MAP_SELECT =
            PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA_MAP + ActionCode.SELECT;
    @Desc("{\"id\":40020101,\"privilegeName\":\"新增参考数据映射\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":400201}")
    public static final String STDATA_REF_DATA_MAP_ADD =
            PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA_MAP + ActionCode.ADD;
    @Desc("{\"id\":40020102,\"privilegeName\":\"删除参考数据映射\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":400201}")
    public static final String STDATA_REF_DATA_MAP_DELETE =
            PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA_MAP + ActionCode.DELETE;
    @Desc("{\"id\":40020103,\"privilegeName\":\"修改参考数据映射\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":400201}")
    public static final String STDATA_REF_DATA_MAP_EDIT =
            PRIVILEGE_PREFIX + ModuleCode.STDATA_REF_DATA_MAP + ActionCode.EDIT;
    @Desc("{\"id\":400301,\"privilegeName\":\"数据元管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    public static final String STDATA_DATA_ELEMENT_SELECT =
            PRIVILEGE_PREFIX + ModuleCode.STDATA_DATA_ELEMENT + ActionCode.SELECT;
    @Desc("{\"id\":40030101,\"privilegeName\":\"新增数据元\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":400301}")
    public static final String STDATA_DATA_ELEMENT_ADD =
            PRIVILEGE_PREFIX + ModuleCode.STDATA_DATA_ELEMENT + ActionCode.ADD;
    @Desc("{\"id\":40030102,\"privilegeName\":\"删除数据元\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":400301}")
    public static final String STDATA_DATA_ELEMENT_DELETE =
            PRIVILEGE_PREFIX + ModuleCode.STDATA_DATA_ELEMENT + ActionCode.DELETE;
    @Desc("{\"id\":40030103,\"privilegeName\":\"修改数据元\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":400301}")
    public static final String STDATA_DATA_ELEMENT_EDIT =
            PRIVILEGE_PREFIX + ModuleCode.STDATA_DATA_ELEMENT + ActionCode.EDIT;
    @Desc("{\"id\":400401,\"privilegeName\":\"业务系统\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    public static final String STDATA_SYSTEM_SELECT = PRIVILEGE_PREFIX + ModuleCode.STDATA_SYSTEM + ActionCode.SELECT;
    @Desc("{\"id\":40040101,\"privilegeName\":\"新增业务系统\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":400401}")
    public static final String STDATA_SYSTEM_ADD = PRIVILEGE_PREFIX + ModuleCode.STDATA_SYSTEM + ActionCode.ADD;
    @Desc("{\"id\":40040102,\"privilegeName\":\"删除业务系统\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":400401}")
    public static final String STDATA_SYSTEM_DELETE = PRIVILEGE_PREFIX + ModuleCode.STDATA_SYSTEM + ActionCode.DELETE;
    @Desc("{\"id\":40040103,\"privilegeName\":\"修改业务系统\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":400401}")
    public static final String STDATA_SYSTEM_EDIT = PRIVILEGE_PREFIX + ModuleCode.STDATA_SYSTEM + ActionCode.EDIT;
    @Desc("{\"id\":500101,\"privilegeName\":\"用户管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    public static final String USER_SELECT = PRIVILEGE_PREFIX + ModuleCode.USER + ActionCode.SELECT;
    @Desc("{\"id\":50010101,\"privilegeName\":\"新增用户\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":500101}")
    public static final String USER_ADD = PRIVILEGE_PREFIX + ModuleCode.USER + ActionCode.ADD;
    @Desc("{\"id\":50010102,\"privilegeName\":\"删除用户\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":500101}")
    public static final String USER_DELETE = PRIVILEGE_PREFIX + ModuleCode.USER + ActionCode.DELETE;
    @Desc("{\"id\":50010103,\"privilegeName\":\"修改用户\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":500101}")
    public static final String USER_EDIT = PRIVILEGE_PREFIX + ModuleCode.USER + ActionCode.EDIT;
    @Desc("{\"id\":500201,\"privilegeName\":\"角色管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    public static final String ROLE_SELECT = PRIVILEGE_PREFIX + ModuleCode.ROLE + ActionCode.SELECT;
    @Desc("{\"id\":50020101,\"privilegeName\":\"新增角色\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":500201}")
    public static final String ROLE_ADD = PRIVILEGE_PREFIX + ModuleCode.ROLE + ActionCode.ADD;
    @Desc("{\"id\":50020102,\"privilegeName\":\"删除角色\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":500201}")
    public static final String ROLE_DELETE = PRIVILEGE_PREFIX + ModuleCode.ROLE + ActionCode.DELETE;
    @Desc("{\"id\":50020103,\"privilegeName\":\"修改角色\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":500201}")
    public static final String ROLE_EDIT = PRIVILEGE_PREFIX + ModuleCode.ROLE + ActionCode.EDIT;
    @Desc("{\"id\":50020104,\"privilegeName\":\"角色授权\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":500201}")
    public static final String ROLE_GRANT = PRIVILEGE_PREFIX + ModuleCode.ROLE + ActionCode.GRANT;
    @Desc("{\"id\":500202,\"privilegeName\":\"部门管理\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    public static final String DEPT_SELECT = PRIVILEGE_PREFIX + ModuleCode.DEPT + ActionCode.SELECT;
    @Desc("{\"id\":50020201,\"privilegeName\":\"新增部门\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":500202}")
    public static final String DEPT_ADD = PRIVILEGE_PREFIX + ModuleCode.DEPT + ActionCode.ADD;
    @Desc("{\"id\":50020202,\"privilegeName\":\"删除部门\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":500202}")
    public static final String DEPT_DELETE = PRIVILEGE_PREFIX + ModuleCode.DEPT + ActionCode.DELETE;
    @Desc("{\"id\":50020203,\"privilegeName\":\"修改部门\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":500202}")
    public static final String DEPT_EDIT = PRIVILEGE_PREFIX + ModuleCode.DEPT + ActionCode.EDIT;
    @Desc("{\"id\":50020204,\"privilegeName\":\"部门授权\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":500202}")
    public static final String DEPT_GRANT = PRIVILEGE_PREFIX + ModuleCode.DEPT + ActionCode.GRANT;
    @Desc("{\"id\":500301,\"privilegeName\":\"字典类型\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    public static final String DICT_TYPE_SELECT = PRIVILEGE_PREFIX + ModuleCode.DICT_TYPE + ActionCode.SELECT;
    @Desc("{\"id\":50030101,\"privilegeName\":\"新增字典类型\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":500301}")
    public static final String DICT_TYPE_ADD = PRIVILEGE_PREFIX + ModuleCode.DICT_TYPE + ActionCode.ADD;
    @Desc("{\"id\":50030102,\"privilegeName\":\"删除字典类型\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":500301}")
    public static final String DICT_TYPE_DELETE = PRIVILEGE_PREFIX + ModuleCode.DICT_TYPE + ActionCode.DELETE;
    @Desc("{\"id\":50030103,\"privilegeName\":\"修改字典类型\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":500301}")
    public static final String DICT_TYPE_EDIT = PRIVILEGE_PREFIX + ModuleCode.DICT_TYPE + ActionCode.EDIT;
    @Desc("{\"id\":500302,\"privilegeName\":\"数据字典\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":0}")
    public static final String DICT_DATA_SELECT = PRIVILEGE_PREFIX + ModuleCode.DICT_DATA + ActionCode.SELECT;
    @Desc("{\"id\":50030201,\"privilegeName\":\"新增数据字典\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":500302}")
    public static final String DICT_DATA_ADD = PRIVILEGE_PREFIX + ModuleCode.DICT_DATA + ActionCode.ADD;
    @Desc("{\"id\":50030202,\"privilegeName\":\"删除数据字典\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":500302}")
    public static final String DICT_DATA_DELETE = PRIVILEGE_PREFIX + ModuleCode.DICT_DATA + ActionCode.DELETE;
    @Desc("{\"id\":50030203,\"privilegeName\":\"修改数据字典\",\"resourceType\":1, \"resourcePath\":\"\",\"pid\":500302}")
    public static final String DICT_DATA_EDIT = PRIVILEGE_PREFIX + ModuleCode.DICT_DATA + ActionCode.EDIT;

    /**
     * 系统菜单
     */
    public enum ModuleCode {
        ;

        /**
         * 工作台
         */
        public static final String STUDIO = "sdo";
        public static final String STUDIO_DATA_BOARD = "sdb";

        /**
         * 系统管理
         */
        public static final String ADMIN = "adm";
        /**
         * 数据字典
         */
        public static final String DICT = "dic";
        /**
         * 字典类型管理
         */
        public static final String DICT_TYPE = "dct";
        /**
         * 数据字典管理
         */
        public static final String DICT_DATA = "dcd";
        /**
         * 用户管理
         */
        public static final String USER = "usr";
        /**
         * 角色管理
         */
        public static final String ROLE = "rol";
        /**
         * 权限管理
         */
        public static final String PRIVILEGE = "pvg";
        /**
         * 部门管理
         */
        public static final String DEPT = "dep";


        /**
         * 参数设置
         */
        public static final String SETTING = "set";

        /**
         * 元数据管理
         */
        public static final String DATADEV = "dev";
        /**
         * 元数据-数据源管理
         */
        public static final String DATADEV_DATASOURCE = "dts";

        /**
         * 项目管理
         */
        public static final String DATADEV_PROJECT = "ddp";
        /**
         * 作业管理
         */
        public static final String DATADEV_JOB = "ddj";

        /**
         * 目录管理
         */
        public static final String DATADEV_DIR = "ddr";

        /**
         * 资源管理
         */
        public static final String DATADEV_RESOURCE = "dde";
        /**
         * 集群管理
         */
        public static final String DATADEV_CLUSTER = "ddc";

        /**
         * 数据标准
         */
        public static final String STDATA = "std";
        /**
         * 数据标准-业务系统管理
         */
        public static final String STDATA_SYSTEM = "sts";
        /**
         * 数据标准-参考数据类型管理
         */
        public static final String STDATA_REF_DATA_TYPE = "stt";
        /**
         * 数据标准-参考数据管理
         */
        public static final String STDATA_REF_DATA = "str";
        /**
         * 数据标准-参考数据映射管理
         */
        public static final String STDATA_REF_DATA_MAP = "stm";
        /**
         * 数据标准-数据元管理
         */
        public static final String STDATA_DATA_ELEMENT = "ste";


        /**
         * 运维中心
         */
        public static final String OPSCENTER = "opc";
        public static final String OPSCENTER_BATCH = "obt";
        public static final String OPSCENTER_REALTIME = "ort";
    }

    /**
     * 操作权限
     */
    public enum ActionCode {
        ;
        /**
         * 菜单显示
         */
        public static final String SHOW = "0";
        /**
         * 新增
         */
        public static final String ADD = "1";
        /**
         * 修改
         */
        public static final String EDIT = "2";
        /**
         * 删除
         */
        public static final String DELETE = "3";
        /**
         * 查询
         */
        public static final String SELECT = "4";
        /**
         * 授权
         */
        public static final String GRANT = "5";
        /**
         * 查看加密敏感信息
         */
        public static final String SECURITY = "6";
        /**
         * 下载
         */
        public static final String DOWNLOAD = "7";
    }

}
