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

@Deprecated
public enum PageResource {
    ;

    public static Route STUDIO_DATA_BOARD = new Route(100000, "数据看板", PageCode.STUDIO_DATA_BOARD, "/studio/databoard", MenuResource.STUDIO);

    public static Route PROJECT_LIST = new Route(200000, "项目列表", PageCode.PROJECT_LIST, "/project", MenuResource.PROJECT);
    public static Route WORKSPACE_JOB = new Route(200001, "作业列表", PageCode.WORKSPACE_JOB_LIST, "/workspace/job/list", MenuResource.PROJECT);
    public static Route WORKSPACE_JOB_DETAIL = new Route(200002, "作业详情", PageCode.WORKSPACE_JOB_DETAIL, "/workspace/job/detail", MenuResource.PROJECT);
    public static Route WORKSPACE_JOB_ARTIFACT = new Route(200003, "Artifact", PageCode.WORKSPACE_JOB_ARTIFACT, "/workspace/job/artifact", MenuResource.PROJECT);
    public static Route WORKSPACE_JOB_ARTIFACT_JAR = new Route(200004, "Artifact Jar", PageCode.WORKSPACE_JOB_ARTIFACT_JAR, "/workspace/job/artifact/jar", MenuResource.PROJECT);
    public static Route WORKSPACE_JOB_SQL = new Route(200005, "SQL", PageCode.WORKSPACE_JOB_SQL, "/workspace/job/sql", MenuResource.PROJECT);
    public static Route WORKSPACE_JOB_SEATUNNEL = new Route(200006, "SeaTunnel", PageCode.WORKSPACE_JOB_SEATUNNEL, "/workspace/job/seatunnel", MenuResource.PROJECT);
    public static Route WORKSPACE_CLUSTER_CONFIG = new Route(200007, "集群配置", PageCode.WORKSPACE_CLUSTER_CONFIG, "/workspace/cluster/config", MenuResource.PROJECT);
    public static Route WORKSPACE_CLUSTER_CONFIG_OPTIONS = new Route(200008, "集群配置详情", PageCode.WORKSPACE_CLUSTER_CONFIG_OPTIONS, "/workspace/cluster/config/options", MenuResource.PROJECT);
    public static Route WORKSPACE_CLUSTER_INSTANCE = new Route(200009, "集群实例", PageCode.WORKSPACE_CLUSTER_INSTANCE, "/workspace/cluster/instance", MenuResource.PROJECT);

    public static Route RESOURCE_JAR = new Route(300000, "公共 Jar", PageCode.RESOURCE_JAR, "/resource/jar", MenuResource.RESOURCE);
    public static Route RESOURCE_FLINK_RELEASE = new Route(300001, "Flink Release", PageCode.RESOURCE_FLINK_RELEASE, "/resource/flink-release", MenuResource.RESOURCE);
    public static Route RESOURCE_SEATUNNEL_RELEASE = new Route(300002, "SeaTunnel Release", PageCode.RESOURCE_SEATUNNEL_RELEASE, "/resource/seatunnel-release", MenuResource.RESOURCE);
    public static Route RESOURCE_KERBEROS = new Route(300003, "Kerberos", PageCode.RESOURCE_KERBEROS, "/resource/kerberos", MenuResource.RESOURCE);
    public static Route RESOURCE_CLUSTER_CREDENTIAL = new Route(300004, "Cluster Credential", PageCode.RESOURCE_CLUSTER_CREDENTIAL, "/resource/cluster-credential", MenuResource.RESOURCE);
    public static Route RESOURCE_CLUSTER_CREDENTIAL_FILE = new Route(300005, "Cluster Credential File", PageCode.RESOURCE_CLUSTER_CREDENTIAL_FILE, "/resource/cluster-credential/file", MenuResource.RESOURCE);

    public static Route DATA_SOURCE = new Route(400000, "数据源", PageCode.DATA_SOURCE_LIST, "/dataSource", MenuResource.DATA_SOURCE);

    public static Route STDATA_SYSTEM = new Route(500000, "业务系统", PageCode.STDATA_SYSTEM, "/stdata/system", MenuResource.DATA_STANDARD);
    public static Route STDATA_DATA_ELEMENT = new Route(500001, "数据元", PageCode.STDATA_DATA_ELEMENT, "/stdata/dataElement", MenuResource.DATA_STANDARD);
    public static Route STDATA_REF_DATA_TYPE = new Route(500002, "参考数据", PageCode.STDATA_REF_DATA_TYPE, "/stdata/refdata", MenuResource.DATA_STANDARD);
    public static Route STDATA_REF_DATA_VALUE = new Route(500003, "参考数据值", PageCode.STDATA_REF_DATA_VALUE, "/stdata/refdata/value", MenuResource.DATA_STANDARD);
    public static Route STDATA_REF_DATA_MAP = new Route(500004, "数据映射", PageCode.STDATA_REF_DATA_MAP, "/stdata/refdataMap", MenuResource.DATA_STANDARD);

    public static Route ADMIN_DEPT = new Route(600000, "部门管理", PageCode.ADMIN_DEPT, "/admin/dept", MenuResource.ADMIN);
    public static Route ADMIN_ROLE = new Route(600001, "角色管理", PageCode.ADMIN_ROLE, "/admin/role", MenuResource.ADMIN);
    public static Route ADMIN_USER = new Route(600002, "用户管理", PageCode.ADMIN_USER, "/admin/user", MenuResource.ADMIN);
    public static Route ADMIN_RESOURCE_WEB = new Route(600003, "Web 资源", PageCode.ADMIN_RESOURCE_WEB, "/admin/resource/web", MenuResource.ADMIN);
    public static Route ADMIN_PRIVILEGE = new Route(600004, "权限管理", PageCode.ADMIN_PRIVILEGE, "/admin/privilege", MenuResource.ADMIN);
    public static Route ADMIN_WORKFLOW_QUARTZ = new Route(600005, "系统任务", PageCode.ADMIN_WORKFLOW_QUARTZ, "/admin/workflow/quartz", MenuResource.ADMIN);
    public static Route ADMIN_DICT = new Route(600006, "数据字典", PageCode.ADMIN_DICT, "/admin/dict", MenuResource.ADMIN);
    public static Route ADMIN_SETTING = new Route(600007, "系统设置", PageCode.ADMIN_SETTING, "/admin/setting", MenuResource.ADMIN);

}
