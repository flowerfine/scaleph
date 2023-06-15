create database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
use scaleph;

drop table if exists ws_project;
create table ws_project
(
    id           bigint      not null auto_increment comment '自增主键',
    project_code varchar(32) not null comment '项目编码',
    project_name varchar(64) comment '项目名称',
    remark       varchar(256) comment '备注',
    creator      varchar(32) comment '创建人',
    create_time  datetime    not null default current_timestamp comment '创建时间',
    editor       varchar(32) comment '修改人',
    update_time  datetime    not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key uniq_project (project_code)
) engine = innodb comment '数据集成-项目信息';

insert into ws_project(id, project_code, project_name, remark, creator, editor)
VALUES (1, 'seatunnel', 'seatunnel-examples', NULL, 'sys', 'sys');

drop table if exists ws_flink_artifact;
create table ws_flink_artifact
(
    id          bigint       not null auto_increment comment '自增主键',
    project_id  bigint       not null comment '项目id',
    `type`      varchar(4)   not null comment '作业artifact类型',
    name        varchar(255) not null comment '作业artifact名称',
    remark      varchar(255) comment '备注',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique uniq_name (project_id, `type`, name)
) engine = innodb comment = 'flink artifact';

INSERT INTO `ws_flink_artifact` (`id`, `project_id`, `type`, `name`, `remark`, `creator`, `editor`)
VALUES (1, 1, '0', 'simple sql', NULL, 'sys', 'sys');
INSERT INTO `ws_flink_artifact` (`id`, `project_id`, `type`, `name`, `remark`, `creator`, `editor`)
VALUES (2, 1, '2', 'fake', NULL, 'sys', 'sys');
INSERT INTO `ws_flink_artifact` (`id`, `project_id`, `type`, `name`, `remark`, `creator`, `editor`)
VALUES (3, 1, '2', 'e_commerce', NULL, 'sys', 'sys');

drop table if exists ws_flink_artifact_jar;
create table ws_flink_artifact_jar
(
    id                bigint       not null auto_increment comment '自增主键',
    flink_artifact_id bigint       not null comment '作业artifact id',
    flink_version     varchar(32)  not null comment 'flink版本',
    entry_class       varchar(255) not null comment 'main class',
    file_name         varchar(255) not null comment '文件名称',
    path              varchar(255) not null comment '文件路径',
    jar_params        text comment 'jar 运行参数',
    current           varchar(16)  not null comment 'current artifact',
    creator           varchar(32) comment '创建人',
    create_time       timestamp default current_timestamp comment '创建时间',
    editor            varchar(32) comment '修改人',
    update_time       timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    key idx_flink_artifact (flink_artifact_id)
) engine = innodb comment = 'flink artifact jar';

DROP TABLE IF EXISTS ws_flink_artifact_sql;
CREATE TABLE ws_flink_artifact_sql
(
    id                bigint      not null auto_increment,
    flink_artifact_id bigint      not null comment '作业artifact id',
    flink_version     varchar(32) not null comment 'flink版本',
    script            mediumtext comment 'sql script',
    current           varchar(16) not null comment 'current artifact',
    creator           varchar(32),
    create_time       datetime    not null default current_timestamp,
    editor            varchar(32),
    update_time       datetime    not null default current_timestamp on update current_timestamp,
    PRIMARY KEY (id),
    key idx_flink_artifact (flink_artifact_id)
) ENGINE = INNODB COMMENT = 'flink artifact sql';

INSERT INTO `ws_flink_artifact_sql` (`id`, `flink_artifact_id`, `flink_version`, `script`, `current`, `creator`,
                                     `editor`)
VALUES (1, 1, '1.16.1',
        'CREATE TEMPORARY TABLE source_table (\n  `id` bigint,\n  `name` string,\n  `age` int,\n  `address` string,\n  `create_time`TIMESTAMP(3),\n  `update_time`TIMESTAMP(3),\n  WATERMARK FOR `update_time` AS update_time - INTERVAL \'1\' MINUTE\n)\nCOMMENT \'\'\nWITH (\n  \' connector\' = \'datagen\',\n  \'number-of-rows\' = \'100\'\n);\n\nCREATE TEMPORARY TABLE `sink_table` (\n  `id` BIGINT,\n  `name` VARCHAR(2147483647),\n  `age` INT,\n  `address` VARCHAR(2147483647),\n  `create_time` TIMESTAMP(3),\n  `update_time` TIMESTAMP(3)\n)\nCOMMENT \'\'\nWITH (\n  \'connector\' = \'print\'\n);\n\ninsert into sink_table\nselect id, name, age, address, create_time, update_time from source_table;',
        '1', 'sys', 'sys');

/* 数据集成-作业信息*/
drop table if exists ws_di_job;
create table ws_di_job
(
    id                bigint      not null auto_increment comment '自增主键',
    flink_artifact_id bigint      not null comment '作业artifact id',
    job_engine        varchar(16) not null comment '作业引擎',
    job_id            varchar(64) not null,
    current           varchar(16) not null comment 'current artifact',
    creator           varchar(32) comment '创建人',
    create_time       timestamp default current_timestamp comment '创建时间',
    editor            varchar(32) comment '修改人',
    update_time       timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    key idx_flink_artifact (flink_artifact_id)
) engine = innodb comment '数据集成-作业信息';
INSERT INTO ws_di_job (id, flink_artifact_id, job_engine, job_id, current, creator, editor)
VALUES (1, 2, 'seatunnel', 'b8e16c94-258c-4487-a88c-8aad40a38b35', 1, 'sys', 'sys');
INSERT INTO ws_di_job(id, flink_artifact_id, job_engine, job_id, current, creator, editor)
VALUES (2, 3, 'seatunnel', '0a6d475e-ed50-46ee-82af-3ef90b7d8509', 1, 'sys', 'sys');

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

DROP TABLE IF EXISTS ws_flink_cluster_config;
CREATE TABLE ws_flink_cluster_config
(
    id                    bigint       not null auto_increment comment '自增主键',
    project_id            bigint       not null comment '项目id',
    `name`                varchar(255) not null comment 'flink配置名称',
    flink_version         varchar(32)  not null comment 'flink版本',
    resource_provider     varchar(4)   not null comment '资源管理器：0: Standalone, 1: Native Kubernetes, 2: YARN',
    deploy_mode           varchar(4)   not null comment '部署模式：0: Application, 1: Per-Job, 2: Session',
    flink_release_id      bigint       not null comment 'flink release id',
    cluster_credential_id bigint       not null comment '集群凭证id',
    kubernetes_options    text comment 'k8s 配置',
    config_options        text comment 'flink集群配置',
    remark                varchar(255) comment '备注',
    creator               varchar(32) comment '创建人',
    create_time           timestamp default current_timestamp comment '创建时间',
    editor                varchar(32) comment '修改人',
    update_time           timestamp default current_timestamp on update current_timestamp comment '修改时间',
    PRIMARY KEY (id),
    unique key (project_id, name)
) ENGINE = INNODB COMMENT = 'flink cluster config';

DROP TABLE IF EXISTS ws_flink_cluster_instance;
CREATE TABLE ws_flink_cluster_instance
(
    id                      bigint       not null auto_increment comment '自增主键',
    project_id              bigint       not null comment '项目id',
    flink_cluster_config_id bigint       not null comment 'flink集群配置id',
    `name`                  varchar(255) not null comment 'flink实例名称',
    cluster_id              varchar(64) comment 'flink集群内部id',
    web_interface_url       varchar(255) comment 'WEB UI',
    status                  varchar(4) comment '集群状态',
    creator                 varchar(32) comment '创建人',
    create_time             timestamp default current_timestamp comment '创建时间',
    editor                  varchar(32) comment '修改人',
    update_time             timestamp default current_timestamp on update current_timestamp comment '修改时间',
    PRIMARY KEY (id),
    unique key (project_id, flink_cluster_config_id, name),
    KEY idx_name (name)
) ENGINE = INNODB COMMENT = 'flink cluster instance';

drop table if exists ws_flink_job;
create table ws_flink_job
(
    id                        bigint       not null auto_increment comment '自增主键',
    project_id                bigint       not null comment '项目id',
    type                      varchar(4)   not null comment '作业类型 0: jar, 1: sql, 2: seatunnel',
    code                      bigint       not null comment '作业编码',
    name                      varchar(255) not null comment '作业名称',
    flink_artifact_id         bigint       not null comment '作业artifact id',
    flink_cluster_config_id   bigint       not null comment '集群配置id',
    flink_cluster_instance_id bigint       not null comment '集群实例id',
    job_config                text comment '作业配置，对应作业变量',
    flink_config              text comment '作业级别集群配置',
    jars                      text comment '作业依赖资源',
    creator                   varchar(32) comment '创建人',
    create_time               timestamp default current_timestamp comment '创建时间',
    editor                    varchar(32) comment '修改人',
    update_time               timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key idx_code (code),
    key idx_name (type, name),
    key idx_flink_artifact (type, flink_artifact_id),
    key idx_flink_cluster_config (flink_cluster_config_id),
    key idx_flink_cluster_instance (flink_cluster_instance_id)
) engine = innodb comment ='flink作业信息';

drop table if exists ws_flink_job_instance;
create table ws_flink_job_instance
(
    id                bigint       not null auto_increment comment '自增主键',
    flink_job_code    bigint       not null comment 'flink作业编码',
    job_id            varchar(64)  not null comment 'flink作业id',
    job_name          varchar(64)  not null comment '作业名称',
    job_state         varchar(16)  not null comment '作业状态',
    cluster_id        varchar(64)  not null comment '集群id',
    cluster_status    varchar(16)  not null comment '集群状态',
    web_interface_url varchar(255) not null comment 'WEB UI',
    start_time        datetime comment '开始时间',
    end_time          datetime comment '结束时间',
    duration          bigint comment '耗时',
    creator           varchar(32) comment '创建人',
    create_time       timestamp default current_timestamp comment '创建时间',
    editor            varchar(32) comment '修改人',
    update_time       timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key uniq_job (flink_job_code, job_id)
) engine = innodb comment = 'flink作业实例';

DROP TABLE IF EXISTS ws_flink_job_log;
CREATE TABLE ws_flink_job_log
(
    id                bigint       not null auto_increment comment '自增主键',
    flink_job_code    bigint       not null comment 'flink作业编码',
    job_id            varchar(64)  not null comment 'flink作业id',
    job_name          varchar(64)  not null comment '作业名称',
    job_state         varchar(16)  not null comment '作业状态',
    cluster_id        varchar(64)  not null comment '集群id',
    cluster_status    varchar(16)  not null comment '集群状态',
    web_interface_url varchar(255) not null comment 'WEB UI',
    start_time        datetime comment '开始时间',
    end_time          datetime comment '结束时间',
    duration          bigint comment '耗时',
    creator           varchar(32) comment '创建人',
    create_time       timestamp default current_timestamp comment '创建时间',
    editor            varchar(32) comment '修改人',
    update_time       timestamp default current_timestamp on update current_timestamp comment '修改时间',
    PRIMARY KEY (id),
    UNIQUE KEY uniq_job (flink_job_code, job_id)
) ENGINE = INNODB COMMENT = 'flink作业日志';

DROP TABLE IF EXISTS ws_flink_checkpoint;
CREATE TABLE ws_flink_checkpoint
(
    id                        bigint      not null auto_increment comment 'id',
    flink_job_instance_id     bigint      not null comment 'flink job instance id',
    flink_checkpoint_id       bigint      not null comment 'flink checkpoint id',
    checkpoint_type           varchar(16) not null comment 'checkpoint type',
    `status`                  varchar(16) not null comment 'checkpoint status',
    `savepoint`               tinyint     not null comment 'is savepoint',
    trigger_timestamp         bigint      not null comment 'checkpoint trigger timestamp',
    duration                  bigint comment 'checkpoint duration',
    discarded                 tinyint     not null comment 'is discarded',
    external_path             text comment 'checkpoint path',
    state_size                bigint comment 'state size',
    processed_data            bigint comment 'processed data size',
    persisted_data            bigint comment 'persisted data size',
    alignment_buffered        bigint comment 'checkpoint alignment buffered size',
    num_subtasks              int comment 'subtask nums',
    num_acknowledged_subtasks int comment 'acknowledged subtask nums',
    latest_ack_timestamp      bigint comment 'latest acknowledged subtask timestamp',
    creator                   varchar(32) comment '创建人',
    create_time               timestamp default current_timestamp comment '创建时间',
    editor                    varchar(32) comment '修改人',
    update_time               timestamp default current_timestamp on update current_timestamp comment '修改时间',
    PRIMARY KEY (id),
    UNIQUE KEY uniq_job (flink_job_instance_id, flink_checkpoint_id)
) ENGINE = INNODB COMMENT = 'flink checkpoint';

DROP TABLE IF EXISTS ws_flink_kubernetes_template;
CREATE TABLE ws_flink_kubernetes_template
(
    id                  bigint       not null auto_increment,
    project_id          bigint       not null comment '项目id',
    `name`              varchar(64)  not null,
    template_id         varchar(64)  not null,
    namespace           varchar(255) not null,
    kubernetes_options  varchar(255),
    job_manager         text,
    task_manager        text,
    pod_template        text,
    flink_configuration text,
    log_configuration   text,
    ingress             text,
    remark              varchar(255),
    creator             varchar(32),
    create_time         datetime     not null default current_timestamp,
    editor              varchar(32),
    update_time         datetime     not null default current_timestamp on update current_timestamp,
    PRIMARY KEY (id),
    UNIQUE KEY uniq_name (`name`)
) ENGINE = INNODB COMMENT = 'flink kubernetes deployment template';
INSERT INTO `ws_flink_kubernetes_template`(`id`, `project_id`, `name`, `template_id`, `namespace`, `kubernetes_options`,
                                           `job_manager`, `task_manager`, `pod_template`, `flink_configuration`,
                                           `log_configuration`, `ingress`, `remark`, `creator`, `editor`)
VALUES (1, 1, 'default', '3f0c6600-b6d7-4e2c-b2e5-4a0b3cdb3cbb', 'default',
        '{\"image\":\"flink:1.16\",\"imagePullPolicy\":\"IfNotPresent\",\"flinkVersion\":\"v1_16\",\"serviceAccount\":\"flink\"}',
        '{\"resource\":{\"cpu\":1.0,\"memory\":\"1G\"},\"replicas\":1}',
        '{\"resource\":{\"cpu\":1.0,\"memory\":\"1G\"},\"replicas\":1}', NULL,
        '{\"kubernetes.operator.savepoint.history.max.count\":\"10\",\"execution.checkpointing.mode\":\"exactly_once\",\"state.checkpoints.num-retained\":\"10\",\"restart-strategy.failure-rate.delay\":\"10s\",\"restart-strategy.failure-rate.max-failures-per-interval\":\"30\",\"kubernetes.operator.savepoint.format.type\":\"NATIVE\",\"web.cancel.enable\":\"false\",\"kubernetes.operator.cluster.health-check.enabled\":\"true\",\"execution.checkpointing.interval\":\"180s\",\"execution.checkpointing.timeout\":\"10min\",\"kubernetes.operator.savepoint.history.max.age\":\"72h\",\"execution.checkpointing.externalized-checkpoint-retention\":\"RETAIN_ON_CANCELLATION\",\"kubernetes.operator.cluster.health-check.restarts.threshold\":\"30\",\"restart-strategy\":\"failurerate\",\"restart-strategy.failure-rate.failure-rate-interval\":\"10min\",\"execution.checkpointing.min-pause\":\"180s\",\"kubernetes.operator.cluster.health-check.restarts.window\":\"10min\",\"execution.checkpointing.max-concurrent-checkpoints\":\"1\",\"kubernetes.operator.periodic.savepoint.interval\":\"1h\",\"kubernetes.operator.savepoint.trigger.grace-period\":\"20min\",\"execution.checkpointing.alignment-timeout\":\"120s\"}',
        NULL,
        '{"template":"/{{namespace}}/{{name}}(/|$)(.*)","className":"nginx","annotations":{"nginx.ingress.kubernetes.io/rewrite-target":"/$2"}}',
        NULL, 'sys', 'sys');

DROP TABLE IF EXISTS ws_flink_kubernetes_deployment;
CREATE TABLE ws_flink_kubernetes_deployment
(
    id                    bigint       not null auto_increment,
    project_id            bigint       not null comment '项目id',
    cluster_credential_id bigint       not null,
    `name`                varchar(255) not null,
    deployment_id         varchar(64)  not null,
    namespace             varchar(255) not null,
    kubernetes_options    varchar(255),
    job_manager           text,
    task_manager          text,
    pod_template          text,
    flink_configuration   text,
    log_configuration     text,
    ingress               text,
    remark                varchar(255),
    creator               varchar(32),
    create_time           datetime     not null default current_timestamp,
    editor                varchar(32),
    update_time           datetime     not null default current_timestamp on update current_timestamp,
    PRIMARY KEY (id),
    UNIQUE KEY uniq_name (cluster_credential_id, `name`)
) ENGINE = INNODB COMMENT = 'flink kubernetes deployment';

-- INSERT INTO `ws_flink_kubernetes_deployment` (`id`, `project_id`, `kind`, `name`, `deployment_id`, `namespace`,
--                                               `kubernetes_options`, `job_manager`, `task_manager`, `pod_template`,
--                                               `flink_configuration`, `log_configuration`, `ingress`, `deployment_name`,
--                                               `job`, `remark`, `creator`, `editor`)
-- VALUES (1, 1, 'FlinkDeployment', 'basic-example', 'default', 'b9d1f4e5-e508-44b1-a775-ed19d05b4a1f',
--         '{\"image\":\"flink:1.15\",\"flinkVersion\":\"v1_15\",\"serviceAccount\":\"flink\"}',
--         '{\"resource\":{\"memory\":\"2048m\",\"cpu\":1}}', '{\"resource\":{\"memory\":\"2048m\",\"cpu\":1}}', NULL,
--         '{\"taskmanager.numberOfTaskSlots\":\"2\"}', NULL, NULL, NULL,
--         '{\"jarURI\":\"local:///opt/flink/examples/streaming/StateMachineExample.jar\",\"entryClass\":\"org.apache.flink.streaming.examples.statemachine.StateMachineExample\",\"parallelism\":2,\"upgradeMode\":\"stateless\"}',
--         NULL, 'sys', 'sys');

-- INSERT INTO `ws_flink_kubernetes_deployment` (`id`, `project_id`, `kind`, `name`, `deployment_id`, `namespace`,
--                                               `kubernetes_options`, `job_manager`, `task_manager`, `pod_template`,
--                                               `flink_configuration`, `log_configuration`, `ingress`, `deployment_name`,
--                                               `job`, `remark`, `creator`, `editor`)
-- VALUES (2, 1, 'FlinkDeployment', 'stateful-example', 'default', 'deda1bdc-f90e-41f5-a3aa-d8a7c31b22e6',
--         '{\"image\":\"flink:1.15\",\"flinkVersion\":\"v1_15\",\"serviceAccount\":\"flink\"}',
--         '{\"resource\":{\"cpu\":1.0,\"memory\":\"2048m\"},\"replicas\":1}',
--         '{\"resource\":{\"cpu\":1.0,\"memory\":\"2048m\"},\"replicas\":1}',
--         '{\"apiVersion\":\"v1\",\"kind\":\"Pod\",\"spec\":{\"containers\":[{\"name\":\"flink-main-container\",\"volumeMounts\":[{\"mountPath\":\"/flink-data\",\"name\":\"flink-volume\"}]}],\"volumes\":[{\"emptyDir\":{\"sizeLimit\":\"500Mi\"},\"name\":\"flink-volume\"}]}}',
--         '{\"taskmanager.numberOfTaskSlots\":\"2\",\"state.savepoints.dir\":\"file:///flink-data/savepoints\",\"state.checkpoints.dir\":\"file:///flink-data/checkpoints\",\"high-availability\":\"org.apache.flink.kubernetes.highavailability.KubernetesHaServicesFactory\",\"high-availability.storageDir\":\"file:///flink-data/ha\"}',
--         NULL, NULL, NULL,
--         '{\"jarURI\":\"local:///opt/flink/examples/streaming/StateMachineExample.jar\",\"parallelism\":2,\"entryClass\":\"org.apache.flink.streaming.examples.statemachine.StateMachineExample\",\"args\":[],\"state\":\"running\",\"upgradeMode\":\"last-state\"}',
--         NULL, 'sys', 'sys');

-- INSERT INTO `ws_flink_kubernetes_deployment` (`id`, `project_id`, `kind`, `name`, `deployment_id`, `namespace`,
--                                               `kubernetes_options`, `job_manager`, `task_manager`, `pod_template`,
--                                               `flink_configuration`, `log_configuration`, `ingress`, `deployment_name`,
--                                               `job`, `remark`, `creator`, `editor`)
-- VALUES (3, 1, 'FlinkDeployment', 'stateful-example2', 'default', '756d4946-f642-4b58-808e-d0a73378ee88',
--         '{\"image\":\"flink:1.15\",\"flinkVersion\":\"v1_15\",\"serviceAccount\":\"flink\"}',
--         '{\"resource\":{\"cpu\":1.0,\"memory\":\"2048m\"},\"replicas\":1}',
--         '{\"resource\":{\"cpu\":1.0,\"memory\":\"2048m\"},\"replicas\":1}',
--         '{\"apiVersion\":\"v1\",\"kind\":\"Pod\",\"spec\":{\"containers\":[{\"name\":\"flink-main-container\",\"volumeMounts\":[{\"mountPath\":\"/flink-data\",\"name\":\"flink-volume\"}]}],\"volumes\":[{\"hostPath\":{\"path\":\"/tmp/flink\",\"type\":\"DirectoryOrCreate\"},\"name\":\"flink-volume\"}]}}',
--         '{\"taskmanager.numberOfTaskSlots\":\"2\",\"state.savepoints.dir\":\"file:///flink-data/savepoints\",\"state.checkpoints.dir\":\"file:///flink-data/checkpoints\",\"high-availability\":\"org.apache.flink.kubernetes.highavailability.KubernetesHaServicesFactory\",\"high-availability.storageDir\":\"file:///flink-data/ha\"}',
--         NULL, NULL, NULL,
--         '{\"jarURI\":\"local:///opt/flink/examples/streaming/StateMachineExample.jar\",\"parallelism\":2,\"entryClass\":\"org.apache.flink.streaming.examples.statemachine.StateMachineExample\",\"args\":[],\"state\":\"running\",\"upgradeMode\":\"last-state\"}',
--         NULL, 'sys', 'sys');

DROP TABLE IF EXISTS ws_flink_kubernetes_session_cluster;
CREATE TABLE ws_flink_kubernetes_session_cluster
(
    id                    bigint       not null auto_increment,
    project_id            bigint       not null comment '项目id',
    cluster_credential_id bigint       not null,
    `name`                varchar(255) not null,
    session_cluster_id    varchar(64)  not null,
    namespace             varchar(255) not null,
    kubernetes_options    varchar(255),
    job_manager           text,
    task_manager          text,
    pod_template          text,
    flink_configuration   text,
    log_configuration     text,
    ingress               text,
    state                 varchar(64),
    error                 text,
    cluster_info          text,
    task_manager_info     text,
    remark                varchar(255),
    creator               varchar(32),
    create_time           datetime     not null default current_timestamp,
    editor                varchar(32),
    update_time           datetime     not null default current_timestamp on update current_timestamp,
    PRIMARY KEY (id),
    UNIQUE KEY uniq_name (cluster_credential_id, `name`)
) ENGINE = INNODB COMMENT = 'flink kubernetes session cluster';

DROP TABLE IF EXISTS ws_flink_kubernetes_job;
CREATE TABLE ws_flink_kubernetes_job
(
    id                       bigint       not null auto_increment,
    project_id               bigint       not null comment '项目id',
    `name`                   varchar(255) not null,
    job_id                   varchar(64)  not null,
    execution_mode           varchar(16)  not null,
    deployment_kind          varchar(16)  not null,
    flink_deployment_id      bigint,
    flink_session_cluster_id bigint,
    `type`                   varchar(4)   not null comment '作业artifact类型',
    flink_artifact_jar_id    bigint,
    flink_artifact_sql_id    bigint,
    ws_di_job_id             bigint,
    remark                   varchar(255),
    creator                  varchar(32),
    create_time              datetime     not null default current_timestamp,
    editor                   varchar(32),
    update_time              datetime     not null default current_timestamp on update current_timestamp,
    PRIMARY KEY (id),
    UNIQUE KEY uniq_name (deployment_kind, flink_deployment_id, `name`)
) ENGINE = INNODB COMMENT = 'flink kubernetes job';

DROP TABLE IF EXISTS ws_flink_kubernetes_job_instance;
CREATE TABLE ws_flink_kubernetes_job_instance
(
    id                         bigint      not null auto_increment,
    ws_flink_kubernetes_job_id bigint      not null,
    job_id                     varchar(32) not null,
    job_manager                text,
    task_manager               text,
    user_flink_configuration   text,
    status                     text,
    state                      varchar(32) not null,
    start_time                 datetime comment '开始时间',
    end_time                   datetime comment '结束时间',
    duration                   bigint comment '耗时',
    creator                    varchar(32),
    create_time                datetime    not null default current_timestamp,
    editor                     varchar(32),
    update_time                datetime    not null default current_timestamp on update current_timestamp,
    PRIMARY KEY (id),
    UNIQUE KEY uniq_name (ws_flink_kubernetes_job_id, job_id)
) ENGINE = INNODB COMMENT = 'flink kubernetes job instance';