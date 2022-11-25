create database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
use scaleph;

DROP TABLE IF EXISTS flink_cluster_config;
CREATE TABLE flink_cluster_config
(
    id                    BIGINT       NOT NULL AUTO_INCREMENT,
    name                  VARCHAR(255) NOT NULL,
    flink_version         VARCHAR(32)  NOT NULL,
    resource_provider     VARCHAR(4)   NOT NULL,
    deploy_mode           VARCHAR(4)   NOT NULL,
    flink_release_id      BIGINT       NOT NULL,
    cluster_credential_id BIGINT       NOT NULL,
    kubernetes_options    TEXT,
    config_options        TEXT,
    remark                VARCHAR(255),
    creator               VARCHAR(32),
    create_time           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    editor                VARCHAR(32),
    update_time           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_name (name)
) ENGINE = INNODB COMMENT = 'flink cluster config';

DROP TABLE IF EXISTS flink_cluster_instance;
CREATE TABLE flink_cluster_instance
(
    id                      BIGINT       NOT NULL AUTO_INCREMENT,
    flink_cluster_config_id BIGINT       NOT NULL,
    name                    VARCHAR(255) NOT NULL,
    cluster_id              VARCHAR(64),
    web_interface_url       VARCHAR(255),
    status                  VARCHAR(4),
    remark                  VARCHAR(255),
    creator                 VARCHAR(32),
    create_time             DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    editor                  VARCHAR(32),
    update_time             DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_flink_cluster_config (flink_cluster_config_id),
    KEY idx_name (name)
) ENGINE = INNODB COMMENT = 'flink cluster instance';

DROP TABLE IF EXISTS flink_artifact;
CREATE TABLE flink_artifact
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    type        VARCHAR(4)   NOT NULL COMMENT '0: Jar, 1: UDF, 2: SQL',
    name        VARCHAR(255) NOT NULL,
    remark      VARCHAR(255),
    creator     VARCHAR(32),
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    editor      VARCHAR(32),
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_name (type, name)
) ENGINE = INNODB COMMENT = 'flink artifact';

DROP TABLE IF EXISTS flink_artifact_jar;
CREATE TABLE flink_artifact_jar
(
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    flink_artifact_id BIGINT       NOT NULL,
    version           varchar(32)  NOT NULL,
    flink_version     VARCHAR(32)  NOT NULL,
    entry_class       VARCHAR(255) NOT NULL,
    file_name         VARCHAR(255) NOT NULL,
    path              VARCHAR(255) NOT NULL,
    creator           VARCHAR(32),
    create_time       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    editor            VARCHAR(32),
    update_time       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_flink_artifact (flink_artifact_id)
) ENGINE = INNODB COMMENT = 'flink artifact jar';

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