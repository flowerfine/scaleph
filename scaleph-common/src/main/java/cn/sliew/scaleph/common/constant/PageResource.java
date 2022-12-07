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
public enum PageResource {

//    DATA_BOARD("/studio/databoard", "数据看板"),
//
//    JAR("/resource/jar", "公共 Jar"),
//    FLINK_RELEASE("/resource/flink-release", "Flink Release"),
//    SEATUNNEL_RELEASE("/resource/seatunnel-release", "SeaTunnel Release"),
//    KERBEROS("/resource/kerberos", "Kerberos"),
//    CLUSTER_CREDENTIAL("/resource/cluster-credential", "Cluster Credential"),
//
//    SYSTEM("/stdata/system", "业务系统"),
//    DATA_ELEMENT("/stdata/dataElement", "数据元"),
//    REF_DATA("/stdata/refdata", "参考数据"),
//    REF_DATA_MAP("/stdata/refdataMap", "数据映射"),
//
//    USER("/admin/user", "用户管理"),
//    PRIVILEGE("/admin/privilege", "权限管理"),
//    WORKFLOW_QUARTZ("/admin/workflow/quartz", "系统任务"),
//    DICT("/admin/dict", "数据字典"),
//    SETTING("/admin/setting", "系统设置"),
    ;

    public static Route STUDIO_DATA_BOARD = new Route(100000, "数据看板", PrivilegeConstants.ModuleCode.STUDIO_DATA_BOARD, "/studio/databoard", MenuResource.STUDIO);

    public static Route PROJECT_DATA_BOARD = new Route(100000, "数据看板", PrivilegeConstants.ModuleCode.STUDIO_DATA_BOARD, "/studio/databoard", MenuResource.STUDIO);
}
