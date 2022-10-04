create
    database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
use scaleph;
/* 数据字典类型表 */
drop table if exists sys_dict_type;
create table sys_dict_type
(
    id             bigint       not null auto_increment comment '自增主键',
    dict_type_code varchar(32)  not null comment '字典类型编码',
    dict_type_name varchar(128) not null comment '字典类型名称',
    remark         varchar(256) comment '备注',
    creator        varchar(32) comment '创建人',
    create_time    timestamp default current_timestamp comment '创建时间',
    editor         varchar(32) comment '修改人',
    update_time    timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (dict_type_code),
    key (dict_type_name)
) engine = innodb comment '数据字典类型';
-- init data
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('gender', '性别', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('yes_or_no', '是否', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('is_delete', '是否删除', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('id_card_type', '证件类型', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('nation', '国家', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('user_status', '用户状态', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('register_channel', '注册渠道', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('role_type', '角色类型', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('role_status', '角色状态', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('dept_status', '部门状态', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('resource_type', '权限资源类型', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('login_type', '登录类型', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('message_type', '消息类型', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('task_result', '任务运行结果', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('datasource_type', '数据源类型', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('connection_type', '连接类型', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('job_type', '作业类型', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('job_status', '作业状态', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('runtime_state', '运行状态', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('job_attr_type', '作业属性类型', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('job_step_type', '步骤类型', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('cluster_type', '集群类型', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('data_type', '数据类型', 'sys', 'sys');
insert into sys_dict_type(dict_type_code, dict_type_name, creator, editor)
values ('job_instance_state', '作业实例状态', 'sys', 'sys');
INSERT INTO `sys_dict_type` (`dict_type_code`, `dict_type_name`, `creator`, `editor`)
VALUES ('flink_resource_provider', 'Flink 资源类型', 'sys', 'sys');
INSERT INTO `sys_dict_type` (`dict_type_code`, `dict_type_name`, `creator`, `editor`)
VALUES ('flink_deployment_mode', 'Flink 部署模式', 'sys', 'sys');
INSERT INTO `sys_dict_type` (`dict_type_code`, `dict_type_name`, `creator`, `editor`)
VALUES ('flink_version', 'Flink 版本', 'sys', 'sys');
INSERT INTO `sys_dict_type`(`dict_type_code`, `dict_type_name`, `creator`, `editor`)
VALUES ('flink_state_backend', 'Flink State Backend', 'sys', 'sys');
INSERT INTO `sys_dict_type`(`dict_type_code`, `dict_type_name`, `creator`, `editor`)
VALUES ('flink_cluster_status', 'Flink 集群状态', 'sys', 'sys');
INSERT INTO `sys_dict_type`(`dict_type_code`, `dict_type_name`, `creator`, `editor`)
VALUES ('flink_semantic', 'Flink 时间语义', 'sys', 'sys');
INSERT INTO `sys_dict_type`(`dict_type_code`, `dict_type_name`, `creator`, `editor`)
VALUES ('flink_job_type', 'Flink 任务类型', 'sys', 'sys');
INSERT INTO `sys_dict_type`(`dict_type_code`, `dict_type_name`, `creator`, `editor`)
VALUES ('flink_job_status', 'Flink 任务状态', 'sys', 'sys');
INSERT INTO `sys_dict_type`(`dict_type_code`, `dict_type_name`, `creator`, `editor`)
VALUES ('flink_checkpoint_retain', 'Flink 外部 checkpoint 保留模式', 'sys', 'sys');
INSERT INTO `sys_dict_type`(`dict_type_code`, `dict_type_name`, `creator`, `editor`)
VALUES ('flink_restart_strategy', 'Flink 重启策略', 'sys', 'sys');
INSERT INTO `sys_dict_type` (`dict_type_code`, `dict_type_name`, `creator`, `editor`)
VALUES ('flink_high_availability', 'Flink HA', 'sys', 'sys');
INSERT INTO `sys_dict_type` (`dict_type_code`, `dict_type_name`, `creator`, `editor`)
VALUES ('flink_artifact_type', 'Flink Artifact 类型', 'sys', 'sys');
INSERT INTO `sys_dict_type`(`dict_type_code`, `dict_type_name`, `creator`, `editor`)
VALUES ('seatunnel_version', 'SeaTunnel 版本', 'sys', 'sys');


/* 数据字典表 */
drop table if exists sys_dict;
create table sys_dict
(
    id             bigint       not null auto_increment comment '自增主键',
    dict_type_code varchar(32)  not null comment '字典类型编码',
    dict_code      varchar(128) not null comment '字典编码',
    dict_value     varchar(128) not null comment '字典值',
    remark         varchar(256) comment '备注',
    is_valid       varchar(1) default '1' comment '是否有效',
    creator        varchar(32) comment '创建人',
    create_time    timestamp  default current_timestamp comment '创建时间',
    editor         varchar(32) comment '修改人',
    update_time    timestamp  default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (dict_type_code, dict_code),
    key (update_time)
) engine = innodb comment = '数据字典表';
-- init data
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('gender', '0', '未知', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('gender', '1', '男', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('gender', '2', '女', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('yes_or_no', '1', '是', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('yes_or_no', '0', '否', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('is_delete', '1', '是', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('is_delete', '0', '否', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('id_card_type', '111', '居民身份证', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('id_card_type', '113', '户口簿', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('id_card_type', '414', '普通护照', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('nation', 'cn', '中国', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('nation', 'us', '美国', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('nation', 'gb', '英国', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('nation', 'de', '德国', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('user_status', '10', '未绑定邮箱', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('user_status', '11', '已绑定邮箱', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('user_status', '90', '禁用', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('user_status', '91', '注销', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('register_channel', '01', '用户注册', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('register_channel', '02', '后台导入', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('role_type', '01', '系统角色', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('role_type', '02', '用户定义', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('role_status', '1', '正常', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('role_status', '0', '禁用', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('dept_status', '1', '正常', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('dept_status', '0', '禁用', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('resource_type', '0', '菜单权限', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('resource_type', '1', '操作权限', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('resource_type', '2', '数据权限', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('login_type', '0', '未知', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('login_type', '1', '登录', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('login_type', '2', '登出', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('message_type', '1', '系统消息', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('task_result', 'success', '成功', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('task_result', 'failure', '失败', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('datasource_type', 'JDBC', 'JDBC', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('datasource_type', 'Mysql', 'Mysql', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('datasource_type', 'Oracle', 'Oracle', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('datasource_type', 'PostGreSQL', 'PostGreSQL', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('datasource_type', 'Greenplum', 'Greenplum', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('datasource_type', 'Kafka', 'Kafka', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('datasource_type', 'Doris', 'Doris', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('datasource_type', 'ClickHouse', 'ClickHouse', 'sys', 'sys');
insert into `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
values ('datasource_type', 'Elasticsearch', 'Elasticsearch', 'sys', 'sys');
insert into `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
values ('datasource_type', 'Druid', 'Druid', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_type', 'b', '周期作业', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_type', 'r', '实时作业', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_status', '1', '草稿', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_status', '2', '发布', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_status', '3', '归档', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('runtime_state', '1', '停止', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('runtime_state', '2', '运行中', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('runtime_state', '3', '等待', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_attr_type', '1', '作业变量', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_attr_type', '2', '作业属性', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_attr_type', '3', '集群属性', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_step_type', 'source', '输入', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_step_type', 'trans', '转换', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_step_type', 'sink', '输出', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('cluster_type', 'flink', 'FLINK', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('cluster_type', 'spark', 'SPARK', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('data_type', 'int', 'INT', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('data_type', 'bigint', 'BIGINT', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('data_type', 'float', 'FLOAT', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('data_type', 'double', 'DOUBLE', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('data_type', 'string', 'STRING', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('data_type', 'date', 'DATE', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('data_type', 'timestamp', 'TIMESTAMP', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_instance_state', 'INITIALIZING', '初始化', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_instance_state', 'CREATED', '已创建', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_instance_state', 'RUNNING', '运行中', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_instance_state', 'FAILING', '失败中', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_instance_state', 'FAILED', '已失败', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_instance_state', 'CANCELLING', '取消中', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_instance_state', 'CANCELED', '已取消', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_instance_state', 'FINISHED', '已完成', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_instance_state', 'RESTARTING', '重启中', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_instance_state', 'SUSPENDED', '已暂停', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('job_instance_state', 'RECONCILING', '调节中', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_resource_provider', '0', 'Standalone', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_resource_provider', '1', 'Native Kubernetes', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_resource_provider', '2', 'YARN', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_deployment_mode', '0', 'Application', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_deployment_mode', '1', 'Per-Job', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_deployment_mode', '2', 'Session', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_version', '1.13.0', '1.13.0', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_version', '1.13.1', '1.13.1', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_version', '1.13.2', '1.13.2', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_version', '1.13.3', '1.13.3', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_version', '1.13.5', '1.13.5', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_version', '1.13.6', '1.13.6', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_version', '1.14.0', '1.14.0', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_version', '1.14.2', '1.14.2', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_version', '1.14.3', '1.14.3', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_version', '1.14.4', '1.14.4', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_version', '1.14.5', '1.14.5', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_version', '1.15.0', '1.15.0', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_state_backend', 'HashMapStateBackend', 'HashMap', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_state_backend', 'EmbeddedRocksDBStateBackend', 'RocksDB', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_cluster_status', '0', '已创建', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_cluster_status', '1', '运行中', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_cluster_status', '2', '停止', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_semantic', 'exactly_once', '精确一次', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_semantic', 'at_least_once', '至少一次', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_semantic', 'none', '无', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_job_type', '0', 'artifact', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_job_type', '1', 'sql+udf', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_job_status', '0', 'SUBMITED', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_job_status', '1', 'SUBMIT FAILED', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_job_status', '2', 'INITIALIZING', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_job_status', '3', 'CREATED', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_job_status', '4', 'RUNNING', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_job_status', '5', 'FAILING', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_job_status', '6', 'FAILED', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_job_status', '7', 'CANCELLING', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_job_status', '8', 'CANCELED', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_job_status', '9', 'FINISHED', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_job_status', '10', 'RESTARTING', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_job_status', '11', 'SUSPENDED', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_job_status', '12', 'RECONCILING', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_checkpoint_retain', 'DELETE_ON_CANCELLATION', 'DELETE_ON_CANCELLATION', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_checkpoint_retain', 'RETAIN_ON_CANCELLATION', 'RETAIN_ON_CANCELLATION', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_checkpoint_retain', 'NO_EXTERNALIZED_CHECKPOINTS', 'NO_EXTERNALIZED_CHECKPOINTS', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_restart_strategy', 'none', 'none', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_restart_strategy', 'fixeddelay', 'fixed-delay', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_restart_strategy', 'failurerate', 'failure-rate', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_restart_strategy', 'exponentialdelay', 'exponential-delay', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_high_availability', 'org.apache.flink.kubernetes.highavailability.KubernetesHaServicesFactory',
        'Kubernetes', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_high_availability', 'zookeeper', 'ZooKeeper', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_artifact_type', '0', 'Jar', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_artifact_type', '1', 'UDF', 'sys', 'sys');
INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('flink_artifact_type', '2', 'SQL', 'sys', 'sys');

INSERT INTO `sys_dict`(`dict_type_code`, `dict_code`, `dict_value`, `creator`, `editor`)
VALUES ('seatunnel_version', '2.1.2', '2.1.2', 'sys', 'sys');

/*系统配置信息表 */
drop table if exists sys_config;
create table sys_config
(
    id          bigint      not null auto_increment comment '自增主键',
    cfg_code    varchar(60) not null comment '配置编码',
    cfg_value   text comment '配置信息',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (cfg_code)
) engine = innodb comment = '系统配置信息表';

insert into sys_config(cfg_code, cfg_value, creator, editor)
values ('basic', '{\"seatunnelHome\":\"/opt/seatunnel\"}', 'sys_admin', 'sys_admin');

/*用户基本信息表 */
drop table if exists sec_user;
create table sec_user
(
    id               bigint       not null auto_increment comment '自增主键',
    user_name        varchar(32)  not null comment '用户名',
    nick_name        varchar(50) comment '昵称',
    email            varchar(128) not null comment '邮箱',
    password         varchar(64)  not null comment '密码',
    real_name        varchar(64) comment '真实姓名',
    id_card_type     varchar(4) comment '证件类型',
    id_card_no       varchar(18) comment '证件号码',
    gender           varchar(4) default '0' comment '性别',
    nation           varchar(4) comment '民族',
    birthday         date comment '出生日期',
    qq               varchar(18) comment 'qq号码',
    wechat           varchar(64) comment '微信号码',
    mobile_phone     varchar(16) comment '手机号码',
    user_status      varchar(4)   not null comment '用户状态',
    summary          varchar(512) comment '用户简介',
    register_channel varchar(4)   not null comment '注册渠道',
    register_time    timestamp  default current_timestamp comment '注册时间',
    register_ip      varchar(16) comment '注册ip',
    creator          varchar(32) comment '创建人',
    create_time      timestamp  default current_timestamp comment '创建时间',
    editor           varchar(32) comment '修改人',
    update_time      timestamp  default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id),
    unique key (user_name),
    unique key (email),
    unique key (mobile_phone),
    key (update_time)
) engine = innodb comment = '用户基本信息表';
-- init data
insert into sec_user (id, user_name, nick_name, email, password, real_name, id_card_type, id_card_no, gender, nation,
                      birthday, qq, wechat, mobile_phone, user_status, summary, register_channel, register_time,
                      register_ip, creator, editor)
values (1, 'sys_admin', '超级管理员', 'test@admin.com', '$2a$10$QX2DBrOBGLuhEmboliW66ulvQ5Hiy9GCdhsqqs1HgJVgslYhZEC6q',
        null,
        null, null, '0', null, null, null, null, null, '10', null, '01', '2021-12-25 21:51:17', '127.0.0.1', 'sys',
        'sys');

/* 角色表 */
drop table if exists sec_role;
create table sec_role
(
    id          bigint      not null auto_increment comment '角色id',
    role_code   varchar(32) not null comment '角色编码',
    role_name   varchar(64) not null comment '角色名称',
    role_type   varchar(4)  not null comment '角色类型',
    role_status varchar(4)  not null comment '角色状态',
    role_desc   varchar(128) comment '角色备注',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id),
    unique key (role_code),
    key (update_time)
) engine = innodb comment = '角色表';

insert into sec_role (role_code, role_name, role_type, role_status)
values ('sys_super_admin', '超级系统管理员', '01', '1');
insert into sec_role (role_code, role_name, role_type, role_status)
values ('sys_admin', '系统管理员', '01', '1');
insert into sec_role (role_code, role_name, role_type, role_status)
values ('sys_normal', '普通用户', '01', '1');


/* 权限表 */
drop table if exists sec_privilege;
create table sec_privilege
(
    id             bigint       not null auto_increment comment '自增主键',
    privilege_code varchar(64)  not null comment '权限标识',
    privilege_name varchar(128) not null comment '权限名称',
    resource_type  varchar(4)            default '1' comment '资源类型',
    resource_path  varchar(64) comment '资源路径',
    pid            bigint       not null default '0' comment '上级权限id',
    creator        varchar(32) comment '创建人',
    create_time    timestamp             default current_timestamp comment '创建时间',
    editor         varchar(32) comment '修改人',
    update_time    timestamp             default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (privilege_code),
    key (update_time)
) engine = innodb comment = '权限表';
/* init privilege */
-- truncate table sec_privilege;
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (10, 'psdo0', '工作台', '0', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (1001, 'psdb0', '数据看板', '0', '', 10, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20, 'pdev0', '数据开发', '0', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (2001, 'pdts0', '数据源', '0', '', 20, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (2002, 'pddp0', '项目管理', '0', '', 20, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (2003, 'pdde0', '资源管理', '0', '', 20, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (2004, 'pddj0', '作业管理', '0', '', 20, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (2005, 'pddc0', '集群管理', '0', '', 20, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (30, 'popc0', '运维中心', '0', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (3001, 'pobt0', '周期任务', '0', '', 30, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (3002, 'port0', '实时任务', '0', '', 30, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40, 'pstd0', '数据标准', '0', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (4001, 'pstr0', '参考数据', '0', '', 40, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (4002, 'pstm0', '数据映射', '0', '', 40, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (4003, 'pste0', '数据元', '0', '', 40, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (4004, 'psts0', '业务系统', '0', '', 40, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50, 'padm0', '系统管理', '0', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (5001, 'pusr0', '用户管理', '0', '', 50, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (5002, 'ppvg0', '权限管理', '0', '', 50, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (5003, 'pdic0', '数据字典', '0', '', 50, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (5004, 'pset0', '系统设置', '0', '', 50, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (200101, 'pdts4', '数据源', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20010101, 'pdts1', '新增数据源', '1', '', 200101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20010102, 'pdts3', '删除数据源', '1', '', 200101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20010103, 'pdts2', '修改数据源', '1', '', 200101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20010104, 'pdts6', '查看密码', '1', '', 200101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (200201, 'pddp4', '项目管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20020101, 'pddp1', '新增项目', '1', '', 200201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20020102, 'pddp3', '删除项目', '1', '', 200201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20020103, 'pddp2', '修改项目', '1', '', 200201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (200301, 'pdde4', '资源管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20030101, 'pdde1', '新增资源', '1', '', 200301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20030102, 'pdde3', '删除资源', '1', '', 200301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20030103, 'pdde2', '修改资源', '1', '', 200301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20030104, 'pdde7', '下载资源', '1', '', 200301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (200401, 'pddj4', '作业管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20040101, 'pddj1', '新增作业', '1', '', 200401, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20040102, 'pddj3', '删除作业', '1', '', 200401, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20040103, 'pddj2', '修改作业', '1', '', 200401, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (200402, 'pddr4', '目录管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20040201, 'pddr1', '新增目录', '1', '', 200402, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20040202, 'pddr3', '删除目录', '1', '', 200402, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20040203, 'pddr2', '修改目录', '1', '', 200402, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (200501, 'pddc4', '集群管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20050101, 'pddc1', '新增集群', '1', '', 200501, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20050102, 'pddc3', '删除集群', '1', '', 200501, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20050103, 'pddc2', '修改集群', '1', '', 200501, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (300101, 'pobt4', '周期任务', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (300201, 'port4', '实时任务', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (400101, 'pstt4', '参考数据类型管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40010101, 'pstt1', '新增参考数据类型', '1', '', 400101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40010102, 'pstt3', '删除参考数据类型', '1', '', 400101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40010103, 'pstt2', '修改参考数据类型', '1', '', 400101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (400102, 'pstr4', '参考数据管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40010201, 'pstr1', '新增参考数据', '1', '', 400102, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40010202, 'pstr3', '删除参考数据', '1', '', 400102, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40010203, 'pstr2', '修改参考数据', '1', '', 400102, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (400201, 'pstm4', '参考数据映射管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40020101, 'pstm1', '新增参考数据映射', '1', '', 400201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40020102, 'pstm3', '删除参考数据映射', '1', '', 400201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40020103, 'pstm2', '修改参考数据映射', '1', '', 400201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (400301, 'pste4', '数据元管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40030101, 'pste1', '新增数据元', '1', '', 400301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40030102, 'pste3', '删除数据元', '1', '', 400301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40030103, 'pste2', '修改数据元', '1', '', 400301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (400401, 'psts4', '业务系统', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40040101, 'psts1', '新增业务系统', '1', '', 400401, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40040102, 'psts3', '删除业务系统', '1', '', 400401, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40040103, 'psts2', '修改业务系统', '1', '', 400401, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (500101, 'pusr4', '用户管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50010101, 'pusr1', '新增用户', '1', '', 500101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50010102, 'pusr3', '删除用户', '1', '', 500101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50010103, 'pusr2', '修改用户', '1', '', 500101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (500201, 'prol4', '角色管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50020101, 'prol1', '新增角色', '1', '', 500201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50020102, 'prol3', '删除角色', '1', '', 500201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50020103, 'prol2', '修改角色', '1', '', 500201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50020104, 'prol5', '角色授权', '1', '', 500201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (500202, 'pdep4', '部门管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50020201, 'pdep1', '新增部门', '1', '', 500202, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50020202, 'pdep3', '删除部门', '1', '', 500202, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50020203, 'pdep2', '修改部门', '1', '', 500202, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50020204, 'pdep5', '部门授权', '1', '', 500202, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (500301, 'pdct4', '字典类型', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50030101, 'pdct1', '新增字典类型', '1', '', 500301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50030102, 'pdct3', '删除字典类型', '1', '', 500301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50030103, 'pdct2', '修改字典类型', '1', '', 500301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (500302, 'pdcd4', '数据字典', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50030201, 'pdcd1', '新增数据字典', '1', '', 500302, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50030202, 'pdcd3', '删除数据字典', '1', '', 500302, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50030203, 'pdcd2', '修改数据字典', '1', '', 500302, 'sys', 'sys');

/* 角色权限关联表 */
drop table if exists sec_role_privilege;
create table sec_role_privilege
(
    id           bigint not null auto_increment comment '自增主键',
    role_id      bigint not null comment '角色id',
    privilege_id bigint not null comment '权限id',
    creator      varchar(32) comment '创建人',
    create_time  timestamp default current_timestamp comment '创建时间',
    editor       varchar(32) comment '修改人',
    update_time  timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (role_id, privilege_id),
    key (update_time)
) engine = innodb comment = '角色权限关联表';
-- init normal role
-- truncate table sec_role_privilege;
insert into sec_role_privilege (role_id, privilege_id, creator, editor)
select r.id  as role_id,
       p.id  as privilege_id,
       'sys' as creator,
       'sys' as editor
from sec_privilege p,
     sec_role r
where r.role_code in ('sys_normal', 'sys_admin', 'sys_super_admin');
/* 部门表 */
drop table if exists sec_dept;
create table sec_dept
(
    id          bigint      not null auto_increment comment '部门id',
    dept_code   varchar(32) not null comment '部门编号',
    dept_name   varchar(64) not null comment '部门名称',
    pid         bigint      not null default '0' comment '上级部门',
    dept_status varchar(1)  not null default '1' comment '部门状态',
    creator     varchar(32) comment '创建人',
    create_time timestamp            default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp            default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique (dept_code),
    unique (dept_name),
    key (pid)
) engine = innodb comment = '部门表';

/*用户和部门关联表 */
drop table if exists sec_user_dept;
create table sec_user_dept
(
    id          bigint not null auto_increment comment '自增主键',
    user_id     bigint not null comment '用户id',
    dept_id     bigint not null comment '部门id',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (dept_id, user_id),
    key (update_time)
) engine = innodb comment = '用户和部门关联表';


/* 用户角色关联表 */
drop table if exists sec_user_role;
create table sec_user_role
(
    id          bigint not null auto_increment comment '自增主键',
    user_id     bigint not null comment '用户id',
    role_id     bigint not null comment '角色id',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id),
    unique key (user_id, role_id),
    key (update_time)
) engine = innodb comment = '用户角色关联表';

-- init data
insert into sec_user_role (id, user_id, role_id, creator, editor)
values (1, 1, 1, 'sys', 'sys');
/* 部门角色关联表 */
drop table if exists sec_dept_role;
create table sec_dept_role
(
    id          bigint not null auto_increment comment '自增主键',
    dept_id     bigint not null comment '部门id',
    role_id     bigint not null comment '角色id',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (dept_id, role_id),
    key (update_time)
) engine = innodb comment = '部门角色关联表';

/*用户邮箱激活日志表*/
drop table if exists sec_user_active;
create table sec_user_active
(
    id          bigint      not null auto_increment comment '自增主键',
    user_name   varchar(60) not null comment '用户名',
    active_code varchar(36) not null comment '激活码',
    expiry_time bigint      not null comment '激活码过期时间戳',
    active_time timestamp comment '激活时间',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (active_code),
    key (user_name),
    key (update_time)
) engine = innodb comment = '用户邮箱激活日志表';

/* 数据集成-项目信息 */
drop table if exists di_project;
create table di_project
(
    id           bigint      not null auto_increment comment '自增主键',
    project_code varchar(32) not null comment '项目编码',
    project_name varchar(64) comment '项目名称',
    remark       varchar(256) comment '备注',
    creator      varchar(32) comment '创建人',
    create_time  timestamp default current_timestamp comment '创建时间',
    editor       varchar(32) comment '修改人',
    update_time  timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique (project_code)
) engine = innodb comment '数据集成-项目信息';

insert into di_project(project_code, project_name, remark, creator, editor)
VALUES ('seatunnel', 'seatunnel-examples', NULL, 'sys_admin', 'sys_admin');

/* 资源信息表 */
drop table if exists di_resource_file;
create table di_resource_file
(
    id          bigint       not null auto_increment comment '自增主键',
    project_id  bigint       not null comment '项目id',
    file_name   varchar(128) not null comment '资源名称',
    file_type   varchar(4)   not null comment '资源类型',
    file_path   varchar(512) not null comment '资源路径',
    file_size   bigint       not null default 0 comment '资源路径',
    creator     varchar(32) comment '创建人',
    create_time timestamp             default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp             default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique (project_id, file_name)
) engine = innodb comment '数据集成-资源';

/* 数据同步-集群配置 */
drop table if exists di_cluster_config;
create table di_cluster_config
(
    id              bigint       not null auto_increment comment '自增主键',
    cluster_name    varchar(128) not null comment '集群名称',
    cluster_type    varchar(16)  not null comment '集群类型',
    cluster_home    varchar(256) comment '集群home文件目录地址',
    cluster_version varchar(64) comment '集群版本',
    cluster_conf    text         not null comment '配置信息json格式',
    remark          varchar(256) comment '备注',
    creator         varchar(32) comment '创建人',
    create_time     timestamp default current_timestamp comment '创建时间',
    editor          varchar(32) comment '修改人',
    update_time     timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique (cluster_name)
) engine = innodb comment '数据集成-集群配置';

insert into di_cluster_config(cluster_name, cluster_type, cluster_home, cluster_version, cluster_conf,
                              remark, creator, editor)
VALUES ('docker_standalone', 'flink', '/opt/flink', '1.13.6',
        'rest.port=8081\njobmanager.rpc.address=jobmanager\njobmanager.rpc.port=6123\n', 'docker environment',
        'sys', 'sys');
INSERT INTO di_cluster_config(cluster_name, cluster_type, cluster_home, cluster_version, cluster_conf, remark, creator,
                              editor)
VALUES ('local_standalone', 'flink', '/opt/flink', '1.13.6',
        'rest.port=8081\njobmanager.rpc.address=localhost\njobmanager.rpc.port=6123\n', 'local environment',
        'sys', 'sys');

/* 数据集成-项目目录*/
drop table if exists di_directory;
create table di_directory
(
    id             bigint not null auto_increment comment '自增主键',
    project_id     bigint not null comment '项目id',
    directory_name varchar(32) comment '目录名称',
    pid            bigint not null default 0 comment '上级目录id',
    creator        varchar(32) comment '创建人',
    create_time    timestamp       default current_timestamp comment '创建时间',
    editor         varchar(32) comment '修改人',
    update_time    timestamp       default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id, project_id),
    key (pid)
) engine = innodb comment '数据集成-项目目录';

insert into di_directory(project_id, directory_name, pid, creator, editor)
VALUES (1, 'seatunnel', 0, 'sys', 'sys');
insert into di_directory(project_id, directory_name, pid, creator, editor)
VALUES (1, 'example', 1, 'sys', 'sys');

/* 数据集成-作业信息*/
drop table if exists di_job;
create table di_job
(
    id            bigint       not null auto_increment comment '自增主键',
    project_id    bigint       not null comment '项目id',
    job_code      varchar(128) not null comment '作业编码',
    job_name      varchar(256) not null comment '作业名称',
    directory_id  bigint       not null comment '作业目录',
    job_type      varchar(4) comment '作业类型',
    job_owner     varchar(32) comment '负责人',
    job_status    varchar(4) default '1' comment '作业状态 草稿、发布、归档',
    runtime_state varchar(4) default '1' comment '运行状态',
    job_version   int        default 1 comment '作业版本号',
    cluster_id    int comment '集群id',
    job_crontab   varchar(32) comment '作业调度crontab表达式',
    remark        varchar(256) comment '备注',
    creator       varchar(32) comment '创建人',
    create_time   timestamp  default current_timestamp comment '创建时间',
    editor        varchar(32) comment '修改人',
    update_time   timestamp  default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (project_id, job_code, job_version)
) engine = innodb comment '数据集成-作业信息';

# insert into di_job(id, project_id, job_code, job_name, directory_id, job_type, job_owner, job_status,
#                    runtime_state, job_version, cluster_id, job_crontab, remark, creator, editor)
# VALUES (1, 1, 'docker_jdbc_to_jdbc', 'docker_jdbc_to_jdbc', 2, 'b', 'sys', '2', '1', 1, NULL, NULL, NULL,
#         'sys', 'sys');
# INSERT INTO di_job(id, project_id, job_code, job_name, directory_id, job_type, job_owner, job_status, runtime_state,
#                    job_version, cluster_id, job_crontab, remark, creator, editor)
# VALUES (2, 1, 'local_jdbc_to_jdbc', 'local_jdbc_to_jdbc', 2, 'b', 'sys', '2', '1', 1, NULL, NULL, NULL,
#         'sys', 'sys');
# INSERT INTO `di_job`(id, `project_id`, `job_code`, `job_name`, `directory_id`, `job_type`, `job_owner`, `job_status`,
#                      `runtime_state`, `job_version`, `cluster_id`, `job_crontab`, `remark`, `creator`, `editor`)
# VALUES (3, 1, 'local_jdbc_to_elasticsearch', 'local_jdbc_to_elasticsearch', 2, 'b', 'sys', '2', '1', 1, NULL,
#         NULL, NULL, 'sys', 'sys');
# INSERT INTO `di_job` (`id`, `project_id`, `job_code`, `job_name`, `directory_id`, `job_type`, `job_owner`, `job_status`,
#                       `runtime_state`, `job_version`, `cluster_id`, `job_crontab`, `remark`, `creator`, `editor`)
# VALUES (4, 1, 'local_druid_to_console', 'local_druid_to_console', 2, 'b', 'sys', '2', '1', 1, NULL, NULL, NULL,
#         'sys', 'sys');
# INSERT INTO `di_job`(`id`, `project_id`, `job_code`, `job_name`, `directory_id`, `job_type`, `job_owner`, `job_status`,
#                      `runtime_state`, `job_version`, `cluster_id`, `job_crontab`, `remark`, `creator`, `editor`)
# VALUES (5, 1, 'local_jdbc_to_druid', 'local_jdbc_to_druid', 2, 'b', 'sys', '2', '1', 1, NULL, NULL, NULL,
#         'sys', 'sys');

drop table if exists di_job_resource_file;
create table di_job_resource_file
(
    id               bigint not null auto_increment comment '自增主键',
    job_id           bigint not null comment '作业id',
    resource_file_id bigint not null comment '资源id',
    creator          varchar(32) comment '创建人',
    create_time      timestamp default current_timestamp comment '创建时间',
    editor           varchar(32) comment '修改人',
    update_time      timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (job_id, resource_file_id)
) engine = innodb comment '数据集成-作业资源';


/* 作业参数信息 包含变量和config配置信息*/
drop table if exists di_job_attr;
create table di_job_attr
(
    id             bigint       not null auto_increment comment '自增主键',
    job_id         bigint       not null comment '作业id',
    job_attr_type  varchar(4)   not null comment '作业参数类型',
    job_attr_key   varchar(128) not null comment '作业参数key',
    job_attr_value varchar(512) comment '作业参数value',
    creator        varchar(32) comment '创建人',
    create_time    timestamp default current_timestamp comment '创建时间',
    editor         varchar(32) comment '修改人',
    update_time    timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (job_id, job_attr_type, job_attr_key)
) engine = innodb comment '数据集成-作业参数';

/* 作业步骤信息 包含source，transform，sink,note 等*/
drop table if exists di_job_step;
create table di_job_step
(
    id          bigint       not null auto_increment comment '自增主键',
    job_id      bigint       not null comment '作业id',
    step_code   varchar(36)  not null comment '步骤编码',
    step_title  varchar(128) not null comment '步骤标题',
    step_type   varchar(12)  not null comment '步骤类型',
    step_name   varchar(128) not null comment '步骤名称',
    position_x  int          not null comment 'x坐标',
    position_y  int          not null comment 'y坐标',
    step_attrs  mediumtext comment '作业步骤属性',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (job_id, step_code)
) engine = innodb comment '数据集成-作业步骤信息';

/* 作业连线信息 */
drop table if exists di_job_link;
create table di_job_link
(
    id             bigint      not null auto_increment comment '自增主键',
    job_id         bigint      not null comment '作业id',
    link_code      varchar(36) not null comment '作业连线编码',
    from_step_code varchar(36) not null comment '源步骤编码',
    to_step_code   varchar(36) not null comment '目标步骤编码',
    creator        varchar(32) comment '创建人',
    create_time    timestamp default current_timestamp comment '创建时间',
    editor         varchar(32) comment '修改人',
    update_time    timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    key (job_id)
) engine = innodb comment '数据集成-作业连线';

/* 数据同步-运行日志 */
drop table if exists di_job_log;
create table di_job_log
(
    id                 bigint       not null auto_increment comment '自增主键',
    project_id         bigint       not null comment '项目id',
    job_id             bigint       not null comment '作业id',
    job_code           varchar(128) not null comment '作业编码',
    cluster_id         bigint       not null comment '执行集群id',
    job_instance_id    varchar(128) not null comment '作业实例id',
    job_log_url        varchar(512) comment '作业日志URL',
    start_time         datetime comment '开始时间',
    end_time           datetime comment '结束时间',
    duration           bigint comment '消耗时长-毫秒',
    job_instance_state varchar(12) comment '作业实例状态',
    creator            varchar(32) comment '创建人',
    create_time        timestamp default current_timestamp comment '创建时间',
    editor             varchar(32) comment '修改人',
    update_time        timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    key (project_id),
    key (job_id),
    unique key (job_instance_id)
) engine = innodb comment '数据集成-作业运行日志';


/* seatunnel release */
DROP TABLE IF EXISTS resource_seatunnel_release;
CREATE TABLE `resource_seatunnel_release`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `version`     VARCHAR(255) NOT NULL COMMENT '版本',
    `file_name`   VARCHAR(255) NOT NULL COMMENT '文件名称',
    `path`        VARCHAR(255) NOT NULL COMMENT '存储路径',
    `remark`      VARCHAR(255) COMMENT '备注',
    `creator`     VARCHAR(32) COMMENT '创建人',
    `create_time` TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `editor`      VARCHAR(32) COMMENT '修改人',
    `update_time` TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB COMMENT = 'seatunnel release';

/* flink release */
DROP TABLE IF EXISTS resource_flink_release;
CREATE TABLE `resource_flink_release`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `version`     VARCHAR(255) NOT NULL COMMENT '版本',
    `file_name`   VARCHAR(255) NOT NULL COMMENT '文件名称',
    `path`        VARCHAR(255) NOT NULL COMMENT '存储路径',
    `remark`      VARCHAR(255) COMMENT '备注',
    `creator`     VARCHAR(32) COMMENT '创建人',
    `create_time` TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `editor`      VARCHAR(32) COMMENT '修改人',
    `update_time` TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB COMMENT = 'flink release';

/* hadoop 或 kubernetes 部署配置文件 */
DROP TABLE IF EXISTS resource_cluster_credential;
CREATE TABLE resource_cluster_credential
(
    id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    config_type VARCHAR(4)  NOT NULL COMMENT '配置文件类型。0: Hadoop, 1: Kubernetes',
    `name`      VARCHAR(64) NOT NULL COMMENT '配置名称',
    remark      VARCHAR(256) COMMENT '备注',
    creator     VARCHAR(32) COMMENT '创建人',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    editor      VARCHAR(32) COMMENT '修改人',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id)
) ENGINE = INNODB COMMENT = 'cluster credential';

/* 公共 java jar */
DROP TABLE IF EXISTS resource_jar;
CREATE TABLE `resource_jar`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `group`       varchar(255) NOT NULL COMMENT 'jar group',
    `file_name`   varchar(255) NOT NULL COMMENT '文件名称',
    `path`        varchar(255) NOT NULL COMMENT '存储路径',
    `remark`      varchar(255)      DEFAULT NULL COMMENT '备注',
    `creator`     varchar(32)       DEFAULT NULL COMMENT '创建人',
    `create_time` timestamp    NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `editor`      varchar(32)       DEFAULT NULL COMMENT '修改人',
    `update_time` timestamp    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='java jar';

DROP TABLE IF EXISTS resource_kerberos;
CREATE TABLE `resource_kerberos`
(
    `id`          bigint       NOT NULL AUTO_INCREMENT,
    `name`        varchar(255) NOT NULL,
    `principal`   varchar(64)  NOT NULL,
    `file_name`   varchar(255) NOT NULL,
    `path`        varchar(255) NOT NULL,
    `remark`      varchar(255),
    `creator`     varchar(32),
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `editor`      varchar(32),
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY idx_name (`name`)
) ENGINE = InnoDB COMMENT ='kerberos';

DROP TABLE IF EXISTS snowflake_worker_node;
CREATE TABLE snowflake_worker_node
(
    `id`          bigint      NOT NULL AUTO_INCREMENT,
    `host_name`   varchar(64) NOT NULL COMMENT 'host name',
    `port`        varchar(64) NOT NULL COMMENT 'port',
    `type`        int         NOT NULL COMMENT 'node type: CONTAINER(1), ACTUAL(2), FAKE(3)',
    `launch_date` DATE        NOT NULL COMMENT 'launch date',
    `creator`     varchar(32),
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `editor`      varchar(32),
    `update_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (ID)
) ENGINE = INNODB COMMENT ='DB WorkerID Assigner for UID Generator';