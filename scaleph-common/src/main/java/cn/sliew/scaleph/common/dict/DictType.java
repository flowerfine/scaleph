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

package cn.sliew.scaleph.common.dict;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DictType implements DictDefinition {

    YES_OR_NO("yes_or_no", "是否"),
    IS_DELETED("is_delete", "是否删除"),

    GENDER("gender", "性别"),
    ID_CARD_TYPE("id_card_type", "证件类型"),
    NATION("nation", "国家"),
    MESSAGE_TYPE("message_type", "消息类型"),
    REGISTER_CHANNEL("register_channel", "注册渠道"),
    LOGIN_TYPE("login_type", "登录类型"),
    USER_STATUS("user_status", "用户状态"),
    ROLE_TYPE("role_type", "角色类型"),
    ROLE_STATUS("role_status", "角色状态"),
    DEPT_STATUS("dept_status", "部门状态"),
    RESOURCE_TYPE("resource_type", "权限资源类型"),

    TASK_RESULT("task_result", "任务运行结果"),
    DATASOURCE_TYPE("datasource_type", "数据源类型"),

    JOB_TYPE("job_type", "作业类型"),
    JOB_STATUS("job_status", "作业状态"),
    JOB_INSTANCE_TYPE("job_instance_state", "作业实例状态"),
    RUNTIME_STATE("runtime_state", "运行状态"),
    JOB_ATTR_TYPE("job_attr_type", "作业属性类型"),
    JOB_STEP_TYPE("job_step_type", "步骤类型"),

    CLUSTER_TYPE("cluster_type", "集群类型"),
    DATA_TYPE("data_type", "数据类型"),

    FLINK_VERSION("flink_version", "Flink 版本"),
    FLINK_RESOURCE_PROVIDER("flink_resource_provider", "Flink 资源类型"),
    FLINK_DEPLOYMENT_MODE("flink_deployment_mode", "Flink 部署模式"),
    FLINK_HIGH_AVAILABILITY("flink_high_availability", "Flink HA"),
    FLINK_RESTART_STRATEGY("flink_restart_strategy", "Flink 重启策略"),
    FLINK_STATE_BACKEND("flink_state_backend", "Flink State Backend"),
    FLINK_CHECKPOINT_RETAIN("flink_checkpoint_retain", "Flink 外部 checkpoint 保留模式"),
    FLINK_SEMANTIC("flink_semantic", "Flink 时间语义"),
    FLINK_CLUSTER_STATUS("flink_cluster_status", "Flink 集群状态"),
    FLINK_JOB_STATUS("flink_job_status", "Flink 任务状态"),
    FLINK_JOB_TYPE("flink_job_type", "Flink 任务类型"),
    FLINK_ARTIFACT_TYPE("flink_artifact_type", "Flink Artifact 类型"),

    SEATUNNEL_VERSION("seatunnel_version", "SeaTunnel 版本"),
    SEATUNNEL_ENGINE_TYPE("seatunnel_engine_type", "SeaTunnel 引擎类型"),
    SEATUNNEL_PLUGIN_TYPE("seatunnel_plugin_type", "SeaTunnel 插件类型"),
    SEATUNNEL_PLUGIN_NAME("seatunnel_plugin_name", "SeaTunnel 插件名称"),
    ;

    @EnumValue
    private String code;
    private String name;

    DictType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }
}
