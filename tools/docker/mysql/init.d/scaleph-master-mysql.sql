create database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
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
INSERT INTO sys_dict_type (dict_type_code, dict_type_name, creator, editor)
VALUES ('flink_resource_provider', 'Flink 资源类型', 'sys', 'sys');
INSERT INTO sys_dict_type (dict_type_code, dict_type_name, creator, editor)
VALUES ('flink_deployment_mode', 'Flink 部署模式', 'sys', 'sys');
INSERT INTO sys_dict_type (dict_type_code, dict_type_name, creator, editor)
VALUES ('flink_version', 'Flink 版本', 'sys', 'sys');
INSERT INTO sys_dict_type(dict_type_code, dict_type_name, creator, editor)
VALUES ('flink_state_backend', 'Flink State Backend', 'sys', 'sys');
INSERT INTO sys_dict_type(dict_type_code, dict_type_name, creator, editor)
VALUES ('flink_cluster_status', 'Flink 集群状态', 'sys', 'sys');
INSERT INTO sys_dict_type(dict_type_code, dict_type_name, creator, editor)
VALUES ('flink_semantic', 'Flink 时间语义', 'sys', 'sys');
INSERT INTO sys_dict_type(dict_type_code, dict_type_name, creator, editor)
VALUES ('flink_job_type', 'Flink 任务类型', 'sys', 'sys');
INSERT INTO sys_dict_type(dict_type_code, dict_type_name, creator, editor)
VALUES ('flink_job_status', 'Flink 任务状态', 'sys', 'sys');
INSERT INTO sys_dict_type(dict_type_code, dict_type_name, creator, editor)
VALUES ('flink_checkpoint_retain', 'Flink 外部 checkpoint 保留模式', 'sys', 'sys');
INSERT INTO sys_dict_type(dict_type_code, dict_type_name, creator, editor)
VALUES ('flink_restart_strategy', 'Flink 重启策略', 'sys', 'sys');
INSERT INTO sys_dict_type (dict_type_code, dict_type_name, creator, editor)
VALUES ('flink_high_availability', 'Flink HA', 'sys', 'sys');
INSERT INTO sys_dict_type(dict_type_code, dict_type_name, creator, editor)
VALUES ('seatunnel_version', 'SeaTunnel 版本', 'sys', 'sys');
INSERT INTO sys_dict_type(dict_type_code, dict_type_name, creator, editor)
VALUES ('image_pull_policy', 'Image Pull Policy', 'sys', 'sys');


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
values ('datasource_type', 'MySQL', 'MySQL', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('datasource_type', 'Oracle', 'Oracle', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('datasource_type', 'PostgreSQL', 'PostgreSQL', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('datasource_type', 'Greenplum', 'Greenplum', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('datasource_type', 'Kafka', 'Kafka', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('datasource_type', 'Doris', 'Doris', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('datasource_type', 'ClickHouse', 'ClickHouse', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
values ('datasource_type', 'Elasticsearch', 'Elasticsearch', 'sys', 'sys');
insert into sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
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
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_resource_provider', '0', 'Standalone', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_resource_provider', '1', 'Native Kubernetes', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_resource_provider', '2', 'YARN', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_deployment_mode', '0', 'Application', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_deployment_mode', '1', 'Per-Job', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_deployment_mode', '2', 'Session', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_version', '1.13.6', '1.13.6', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_state_backend', 'HashMapStateBackend', 'HashMap', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_state_backend', 'EmbeddedRocksDBStateBackend', 'RocksDB', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_cluster_status', '0', '已创建', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_cluster_status', '1', '运行中', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_cluster_status', '2', '停止', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_semantic', 'exactly_once', '精确一次', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_semantic', 'at_least_once', '至少一次', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_semantic', 'none', '无', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_job_type', '0', 'Jar', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_job_type', '1', 'SQL', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_job_type', '2', 'SeaTunnel', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_job_status', '0', 'SUBMITED', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_job_status', '1', 'SUBMIT FAILED', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_job_status', '2', 'INITIALIZING', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_job_status', '3', 'CREATED', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_job_status', '4', 'RUNNING', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_job_status', '5', 'FAILING', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_job_status', '6', 'FAILED', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_job_status', '7', 'CANCELLING', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_job_status', '8', 'CANCELED', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_job_status', '9', 'FINISHED', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_job_status', '10', 'RESTARTING', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_job_status', '11', 'SUSPENDED', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_job_status', '12', 'RECONCILING', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_checkpoint_retain', 'DELETE_ON_CANCELLATION', 'DELETE_ON_CANCELLATION', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_checkpoint_retain', 'RETAIN_ON_CANCELLATION', 'RETAIN_ON_CANCELLATION', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_checkpoint_retain', 'NO_EXTERNALIZED_CHECKPOINTS', 'NO_EXTERNALIZED_CHECKPOINTS', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_restart_strategy', 'none', 'none', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_restart_strategy', 'fixeddelay', 'fixed-delay', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_restart_strategy', 'failurerate', 'failure-rate', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_restart_strategy', 'exponentialdelay', 'exponential-delay', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_high_availability', 'org.apache.flink.kubernetes.highavailability.KubernetesHaServicesFactory',
        'Kubernetes', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('flink_high_availability', 'zookeeper', 'ZooKeeper', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('seatunnel_version', '2.3.0-beta', '2.3.0-beta', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('image_pull_policy', 'IfNotPresent', 'IfNotPresent', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('image_pull_policy', 'Always', 'Always', 'sys', 'sys');
INSERT INTO sys_dict(dict_type_code, dict_code, dict_value, creator, editor)
VALUES ('image_pull_policy', 'Never', 'Never', 'sys', 'sys');

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

/* 数据集成-项目信息 */
drop table if exists ws_project;
create table ws_project
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

insert into ws_project(project_code, project_name, remark, creator, editor)
VALUES ('seatunnel', 'seatunnel-examples', NULL, 'sys_admin', 'sys_admin');

/* 数据集成-作业信息*/
drop table if exists ws_di_job;
create table ws_di_job
(
    id          bigint       not null auto_increment comment '自增主键',
    project_id  bigint       not null comment '项目id',
    job_code    bigint       not null comment '作业编码',
    job_name    varchar(256) not null comment '作业名称',
    job_type    varchar(4) comment '作业类型',
    job_status  varchar(4) default '1' comment '作业状态 草稿、发布、归档',
    job_version int        default 1 comment '作业版本号',
    remark      varchar(256) comment '备注',
    creator     varchar(32) comment '创建人',
    create_time timestamp  default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp  default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (project_id, job_code, job_version)
) engine = innodb comment '数据集成-作业信息';
INSERT INTO ws_di_job (id, project_id, job_code, job_name, job_type, job_status,
                    job_version, remark, creator, editor)
VALUES (1, 1, 1, 'e_commerce', 'b', '2', 1, NULL, 'sys', 'sys');
INSERT INTO ws_di_job(id, project_id, job_code, job_name, job_type, job_status,
                   job_version, remark, creator, editor)
VALUES (2, 1, 2, 'fake', 'b', '2', 1, NULL, 'sys', 'sys');


/* 作业参数信息 作业参数*/
drop table if exists ws_di_job_attr;
create table ws_di_job_attr
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
drop table if exists ws_di_job_step;
create table ws_di_job_step
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
INSERT INTO ws_di_job_step (job_id, step_code, step_title, step_type, step_name, position_x, position_y,
                         step_attrs, creator, editor)
VALUES (1, 'f3e02087-91fa-494d-86f4-694970a49ebd', 'Jdbc Source', 'source', 'Jdbc', -400, -320,
        '{\"stepTitle\":\"Jdbc Source\",\"dataSourceType\":\"MySQL\",\"dataSource\":2,\"query\":\"select * from sample_data_e_commerce\"}',
        'sys', 'sys');
INSERT INTO ws_di_job_step (job_id, step_code, step_title, step_type, step_name, position_x, position_y,
                         step_attrs, creator, editor)
VALUES (1, '68834928-2a32-427a-a864-83b6b5848e04', 'Jdbc Sink', 'sink', 'Jdbc', -310, -120,
        '{\"stepTitle\":\"Jdbc Sink\",\"dataSourceType\":\"MySQL\",\"dataSource\":2,\"batch_size\":300,\"batch_interval_ms\":1000,\"max_retries\":3,\"is_exactly_once\":false,\"query\":\"insert into sample_data_e_commerce_duplicate ( id, invoice_no, stock_code, description, quantity, invoice_date, unit_price, customer_id, country )\\nvalues (?,?,?,?,?,?,?,?,?)\"}',
        'sys', 'sys');
INSERT INTO ws_di_job_step(job_id, step_code, step_title, step_type, step_name, position_x, position_y,
                        step_attrs, creator, editor)
VALUES (2, '6223c6c3-b552-4c69-adab-5300b7514fad', 'Fake Source', 'source', 'FakeSource', -400, -320,
        '{\"stepTitle\":\"Fake Source\",\"fields\":[{\"field\":\"c_string\",\"type\":\"string\"},{\"field\":\"c_boolean\",\"type\":\"boolean\"},{\"field\":\"c_tinyint\",\"type\":\"tinyint\"},{\"field\":\"c_smallint\",\"type\":\"smallint\"},{\"field\":\"c_int\",\"type\":\"int\"},{\"field\":\"c_bigint\",\"type\":\"bigint\"},{\"field\":\"c_float\",\"type\":\"float\"},{\"field\":\"c_double\",\"type\":\"double\"},{\"field\":\"c_decimal\",\"type\":\"decimal(30, 8)\"},{\"field\":\"c_bytes\",\"type\":\"bytes\"},{\"field\":\"c_map\",\"type\":\"map<string, string>\"},{\"field\":\"c_date\",\"type\":\"date\"},{\"field\":\"c_time\",\"type\":\"time\"},{\"field\":\"c_timestamp\",\"type\":\"timestamp\"}],\"schema\":\"{\\\"fields\\\":{\\\"c_string\\\":\\\"string\\\",\\\"c_boolean\\\":\\\"boolean\\\",\\\"c_tinyint\\\":\\\"tinyint\\\",\\\"c_smallint\\\":\\\"smallint\\\",\\\"c_int\\\":\\\"int\\\",\\\"c_bigint\\\":\\\"bigint\\\",\\\"c_float\\\":\\\"float\\\",\\\"c_double\\\":\\\"double\\\",\\\"c_decimal\\\":\\\"decimal(30, 8)\\\",\\\"c_bytes\\\":\\\"bytes\\\",\\\"c_map\\\":\\\"map<string, string>\\\",\\\"c_date\\\":\\\"date\\\",\\\"c_time\\\":\\\"time\\\",\\\"c_timestamp\\\":\\\"timestamp\\\"}}\"}',
        'sys', 'sys');
INSERT INTO ws_di_job_step(job_id, step_code, step_title, step_type, step_name, position_x, position_y,
                        step_attrs, creator, editor)
VALUES (2, 'f08143b4-34dc-4190-8723-e8d8ce49738f', 'Console Sink', 'sink', 'Console', -320, -120,
        '{\"stepTitle\":\"Console Sink\"}', 'sys', 'sys');

/* 作业连线信息 */
drop table if exists ws_di_job_link;
create table ws_di_job_link
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
INSERT INTO ws_di_job_link (job_id, link_code, from_step_code, to_step_code, creator, editor)
VALUES (1, 'fabfda41-aacb-4a19-b5ef-9e84a75ed4e9', 'f3e02087-91fa-494d-86f4-694970a49ebd',
        '68834928-2a32-427a-a864-83b6b5848e04', 'sys', 'sys');
INSERT INTO ws_di_job_link(job_id, link_code, from_step_code, to_step_code, creator, editor)
VALUES (2, 'd57021a1-65c7-4dfe-ae89-3b73d00fcf72', '6223c6c3-b552-4c69-adab-5300b7514fad',
        'f08143b4-34dc-4190-8723-e8d8ce49738f', 'sys', 'sys');

/* seatunnel release */
DROP TABLE IF EXISTS resource_seatunnel_release;
CREATE TABLE resource_seatunnel_release
(
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    version     VARCHAR(255) NOT NULL COMMENT '版本',
    file_name   VARCHAR(255) NOT NULL COMMENT '文件名称',
    path        VARCHAR(255) NOT NULL COMMENT '存储路径',
    remark      VARCHAR(255) COMMENT '备注',
    creator     VARCHAR(32) COMMENT '创建人',
    create_time TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    editor      VARCHAR(32) COMMENT '修改人',
    update_time TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id)
) ENGINE = INNODB COMMENT = 'seatunnel release';

/* flink release */
DROP TABLE IF EXISTS resource_flink_release;
CREATE TABLE resource_flink_release
(
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    version     VARCHAR(255) NOT NULL COMMENT '版本',
    file_name   VARCHAR(255) NOT NULL COMMENT '文件名称',
    path        VARCHAR(255) NOT NULL COMMENT '存储路径',
    remark      VARCHAR(255) COMMENT '备注',
    creator     VARCHAR(32) COMMENT '创建人',
    create_time TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    editor      VARCHAR(32) COMMENT '修改人',
    update_time TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id)
) ENGINE = INNODB COMMENT = 'flink release';

/* hadoop 或 kubernetes 部署配置文件 */
DROP TABLE IF EXISTS resource_cluster_credential;
CREATE TABLE resource_cluster_credential
(
    id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    config_type VARCHAR(4)  NOT NULL COMMENT '配置文件类型。0: Hadoop, 1: Kubernetes',
    name        VARCHAR(64) NOT NULL COMMENT '配置名称',
    remark      VARCHAR(256) COMMENT '备注',
    creator     VARCHAR(32) COMMENT '创建人',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    editor      VARCHAR(32) COMMENT '修改人',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id)
) ENGINE = INNODB COMMENT = 'cluster credential';

/* 公共 java jar */
DROP TABLE IF EXISTS resource_jar;
CREATE TABLE resource_jar
(
    id          bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `group`     varchar(255) NOT NULL COMMENT 'jar group',
    file_name   varchar(255) NOT NULL COMMENT '文件名称',
    path        varchar(255) NOT NULL COMMENT '存储路径',
    remark      varchar(255)      DEFAULT NULL COMMENT '备注',
    creator     varchar(32)       DEFAULT NULL COMMENT '创建人',
    create_time timestamp    NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    editor      varchar(32)       DEFAULT NULL COMMENT '修改人',
    update_time timestamp    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id)
) ENGINE = InnoDB COMMENT ='java jar';

DROP TABLE IF EXISTS resource_kerberos;
CREATE TABLE resource_kerberos
(
    id          bigint       NOT NULL AUTO_INCREMENT,
    name        varchar(255) NOT NULL,
    principal   varchar(64)  NOT NULL,
    file_name   varchar(255) NOT NULL,
    path        varchar(255) NOT NULL,
    remark      varchar(255),
    creator     varchar(32),
    create_time datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    editor      varchar(32),
    update_time datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_name (name)
) ENGINE = InnoDB COMMENT ='kerberos';

DROP TABLE IF EXISTS snowflake_worker_node;
CREATE TABLE snowflake_worker_node
(
    id          bigint      NOT NULL AUTO_INCREMENT,
    host_name   varchar(64) NOT NULL COMMENT 'host name',
    port        varchar(64) NOT NULL COMMENT 'port',
    type        int         NOT NULL COMMENT 'node type: CONTAINER(1), ACTUAL(2), FAKE(3)',
    launch_date DATE        NOT NULL COMMENT 'launch date',
    creator     varchar(32),
    create_time datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    editor      varchar(32),
    update_time datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (ID)
) ENGINE = INNODB COMMENT ='DB WorkerID Assigner for UID Generator';