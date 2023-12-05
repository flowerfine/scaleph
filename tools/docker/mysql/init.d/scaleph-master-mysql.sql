create database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
use scaleph;

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

/* kubernetes kubeconfig */
DROP TABLE IF EXISTS resource_cluster_credential;
CREATE TABLE resource_cluster_credential
(
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `name`      VARCHAR(64)  NOT NULL COMMENT 'name',
    `context`   VARCHAR(64) COMMENT 'current context',
    file_name   VARCHAR(64) COMMENT 'kube config file name',
    `path`      VARCHAR(255) NOT NULL COMMENT 'kube config path',
    remark      VARCHAR(256) COMMENT '备注',
    creator     VARCHAR(32) COMMENT '创建人',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    editor      VARCHAR(32) COMMENT '修改人',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id),
    KEY idx_name (`name`)
) ENGINE = INNODB COMMENT = 'cluster credential';

/* 公共 java jar */
DROP TABLE IF EXISTS resource_jar;
CREATE TABLE resource_jar
(
    id          bigint       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
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