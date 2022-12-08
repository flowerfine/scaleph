create database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
use scaleph;

DROP TABLE IF EXISTS flink_cluster_config;
CREATE TABLE flink_cluster_config
(
    id                    bigint       not null auto_increment comment '自增主键',
    project_id            bigint       not null comment '项目id',
    name                  varchar(255) not null comment 'flink配置名称',
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

DROP TABLE IF EXISTS flink_cluster_instance;
CREATE TABLE flink_cluster_instance
(
    id                      bigint       not null auto_increment comment '自增主键',
    project_id              bigint       not null comment '项目id',
    flink_cluster_config_id bigint       not null comment 'flink集群配置id',
    name                    varchar(255) not null comment 'flink实例名称',
    cluster_id              varchar(64) comment 'flink集群内部id',
    web_interface_url       varchar(255) comment 'WEB UI',
    status                  varchar(4) comment '集群状态',
    creator                 varchar(32) comment '创建人',
    create_time             timestamp default current_timestamp comment '创建时间',
    editor                  varchar(32) comment '修改人',
    update_time             timestamp default current_timestamp on update current_timestamp comment '修改时间',
    PRIMARY KEY (id),
    unique key (project_id, flink_cluster_config_id),
    KEY idx_name (name)
) ENGINE = INNODB COMMENT = 'flink cluster instance';

drop table if exists flink_artifact;
create table flink_artifact
(
    id          bigint       not null auto_increment comment '自增主键',
    project_id  bigint       not null comment '项目id',
    name        varchar(255) not null comment '作业artifact名称',
    remark      varchar(255) comment '备注',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique idx_name (project_id, name)
) engine = innodb comment = 'flink artifact';

drop table if exists flink_artifact_jar;
create table flink_artifact_jar
(
    id                bigint       not null auto_increment comment '自增主键',
    flink_artifact_id bigint       not null comment '作业artifact id',
    flink_version     varchar(32)  not null comment 'flink版本',
    entry_class       varchar(255) not null comment 'main class',
    file_name         varchar(255) not null comment '文件名称',
    path              varchar(255) not null comment '文件路径',
    group_id          varchar(128) comment 'group id',
    artifact_id       varchar(128) comment 'artifact id',
    version           varchar(128) not null comment '版本',
    jar_params        text comment 'jar 运行参数',
    creator           varchar(32) comment '创建人',
    create_time       timestamp default current_timestamp comment '创建时间',
    editor            varchar(32) comment '修改人',
    update_time       timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    key idx_flink_artifact (flink_artifact_id, version)
) engine = innodb comment = 'flink artifact jar';

drop table if exists flink_job;
create table flink_job
(
    id                        bigint       not null auto_increment comment '自增主键',
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

drop table if exists flink_job_instance;
create table flink_job_instance
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

DROP TABLE IF EXISTS flink_job_log;
CREATE TABLE flink_job_log
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