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

import cn.sliew.scaleph.common.dict.common.*;
import cn.sliew.scaleph.common.dict.flink.*;
import cn.sliew.scaleph.common.dict.image.ImagePullPolicy;
import cn.sliew.scaleph.common.dict.job.*;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelEngineType;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginName;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelPluginType;
import cn.sliew.scaleph.common.dict.seatunnel.SeaTunnelVersion;
import cn.sliew.scaleph.common.dict.security.*;
import cn.sliew.scaleph.common.dict.workflow.*;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DictType implements DictDefinition {

    YES_OR_NO("yes_or_no", "是否", YesOrNo.class),
    IS_DELETED("is_delete", "是否删除", IsDeleted.class),

    GENDER("gender", "性别", Gender.class),
    ID_CARD_TYPE("id_card_type", "证件类型", IdCardType.class),
    NATION("nation", "国家", Nation.class),
    MESSAGE_TYPE("message_type", "消息类型", MessageType.class),
    REGISTER_CHANNEL("register_channel", "注册渠道", RegisterChannel.class),
    LOGIN_TYPE("login_type", "登录类型", LoginType.class),
    USER_STATUS("user_status", "用户状态", UserStatus.class),
    ROLE_TYPE("role_type", "角色类型", RoleType.class),
    ROLE_STATUS("role_status", "角色状态", RoleStatus.class),
    DEPT_STATUS("dept_status", "部门状态", DeptStatus.class),
    RESOURCE_TYPE("resource_type", "权限资源类型", ResourceType.class),

    TASK_RESULT("task_result", "任务运行结果", TaskResult.class),
    DATASOURCE_TYPE("datasource_type", "数据源类型", DataSourceType.class),

    JOB_TYPE("job_type", "作业类型", JobType.class),
    JOB_STATUS("job_status", "作业状态", JobStatus.class),
    JOB_INSTANCE_TYPE("job_instance_state", "作业实例状态", JobInstanceType.class),
    RUNTIME_STATE("runtime_state", "运行状态", RuntimeState.class),
    JOB_ATTR_TYPE("job_attr_type", "作业属性类型", JobAttrType.class),
    JOB_STEP_TYPE("job_step_type", "步骤类型", JobStepType.class),

    CLUSTER_TYPE("cluster_type", "集群类型", ClusterType.class),
    DATA_TYPE("data_type", "数据类型", DataType.class),

    FLINK_VERSION("flink_version", "Flink 版本", FlinkVersion.class),
    FLINK_RESOURCE_PROVIDER("flink_resource_provider", "Flink 资源类型", FlinkResourceProvider.class),
    FLINK_DEPLOYMENT_MODE("flink_deployment_mode", "Flink 部署模式", FlinkDeploymentMode.class),
    FLINK_HIGH_AVAILABILITY("flink_high_availability", "Flink HA", FlinkHighAvailability.class),
    FLINK_RESTART_STRATEGY("flink_restart_strategy", "Flink 重启策略", FlinkRestartStrategy.class),
    FLINK_STATE_BACKEND("flink_state_backend", "Flink State Backend", FlinkStateBackend.class),
    FLINK_CHECKPOINT_RETAIN("flink_checkpoint_retain", "Flink 外部 checkpoint 保留模式", FlinkCheckpointRetain.class),
    FLINK_SEMANTIC("flink_semantic", "Flink 时间语义", FlinkSemantic.class),
    FLINK_CLUSTER_STATUS("flink_cluster_status", "Flink 集群状态", FlinkClusterStatus.class),
    FLINK_JOB_STATUS("flink_job_status", "Flink 任务状态", FlinkJobState.class),
    FLINK_JOB_TYPE("flink_job_type", "Flink 任务类型", FlinkJobType.class),
    FLINK_ARTIFACT_TYPE("flink_artifact_type", "Flink Artifact 类型", FlinkArtifactType.class),

    SEATUNNEL_VERSION("seatunnel_version", "SeaTunnel 版本", SeaTunnelVersion.class),
    SEATUNNEL_ENGINE_TYPE("seatunnel_engine_type", "SeaTunnel 引擎类型", SeaTunnelEngineType.class),
    SEATUNNEL_PLUGIN_TYPE("seatunnel_plugin_type", "SeaTunnel 插件类型", SeaTunnelPluginType.class),
    SEATUNNEL_PLUGIN_NAME("seatunnel_plugin_name", "SeaTunnel 插件名称", SeaTunnelPluginName.class),

    IMAGE_PULL_POLICY("image_pull_policy", "Image Pull Policy", ImagePullPolicy.class),

    WORKFLOW_TYPE("workflow_type", "Workflow Type", WorkflowType.class),
    WORKFLOW_EXECUTE_TYPE("workflow_execute_type", "Workflow Execute Type", WorkflowExecuteType.class),
    WORKFLOW_INSTANCE_STATE("workflow_instance_state", "Workflow Instance State", WorkflowInstanceState.class),
    WORKFLOW_TASK_TYPE("workflow_task_type", "Workflow Task Type", WorkflowTaskType.class),
    WORKFLOW_TASK_INSTANCE_STAGE("workflow_task_instance_stage", "Workflow Task Instance Stage", WorkflowTaskInstanceStage.class),
    SCHEDULE_STATUS("schedule_status", "Schedule Status", ScheduleStatus.class),
    ;

    @JsonValue
    @EnumValue
    private String code;
    private String name;
    private Class instanceClass;

    DictType(String code, String name, Class instanceClass) {
        this.code = code;
        this.name = name;
        this.instanceClass = instanceClass;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getName() {
        return name;
    }

    public Class getInstanceClass() {
        return instanceClass;
    }
}
