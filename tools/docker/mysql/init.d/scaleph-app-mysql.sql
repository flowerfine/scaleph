create database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
use scaleph;

drop table if exists oam_component_definition;
create table oam_component_definition
(
    id            bigint    not null auto_increment comment '自增主键',
    definition_id varchar(64) comment '定义id。主要用于 kubernetes 中 metadata 使用',
    name          varchar(54),
    workload_type varchar(512),
    schematic     text,
    extension     text,
    remark        varchar(256) comment '备注',
    schema_json   text comment 'schema json',
    creator       varchar(32) comment '创建人',
    create_time   timestamp not null default current_timestamp comment '创建时间',
    editor        varchar(32) comment '修改人',
    update_time   timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id)
) engine = innodb comment 'Component Definition 信息';

drop table if exists oam_workload_definition;
create table oam_workload_definition
(
    id             bigint    not null auto_increment comment '自增主键',
    definition_id  varchar(64) comment '定义id。主要用于 kubernetes 中 metadata 使用',
    name           varchar(54),
    definition_ref varchar(512),
    schematic      text,
    extension      text,
    remark         varchar(256) comment '备注',
    schema_json    text comment 'schema json',
    creator        varchar(32) comment '创建人',
    create_time    timestamp not null default current_timestamp comment '创建时间',
    editor         varchar(32) comment '修改人',
    update_time    timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id)
) engine = innodb comment 'Workload Definition 信息';

drop table if exists oam_policy_definition;
create table oam_policy_definition
(
    id             bigint    not null auto_increment comment '自增主键',
    definition_id  varchar(64) comment '定义id。主要用于 kubernetes 中 metadata 使用',
    name           varchar(54),
    definition_ref varchar(512),
    schematic      text,
    remark         varchar(256) comment '备注',
    schema_json    text comment 'schema json',
    creator        varchar(32) comment '创建人',
    create_time    timestamp not null default current_timestamp comment '创建时间',
    editor         varchar(32) comment '修改人',
    update_time    timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id)
) engine = innodb comment 'Policy Definition 信息';

drop table if exists oam_trait_definition;
create table oam_trait_definition
(
    id                   bigint    not null auto_increment comment '自增主键',
    definition_id        varchar(64) comment '定义id。主要用于 kubernetes 中 metadata 使用',
    name                 varchar(54),
    definition_ref       varchar(512),
    applies_to_workloads varchar(512),
    schematic            text,
    properties           text,
    remark               varchar(256) comment '备注',
    schema_json          text comment 'schema json',
    creator              varchar(32) comment '创建人',
    create_time          timestamp not null default current_timestamp comment '创建时间',
    editor               varchar(32) comment '修改人',
    update_time          timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id)
) engine = innodb comment 'Trait Definition 信息';

drop table if exists oam_appplication_configuration;
create table oam_appplication_configuration
(
    id            bigint    not null auto_increment comment '自增主键',
    project_id    bigint    not null comment '项目id',
    app_config_id varchar(64) comment '应用配置id。主要用于 kubernetes 中 metadata 使用',
    metadata      varchar(64) comment 'metadata 信息',
    components    varchar(64) comment 'spec.components 信息',
    policies      varchar(64) comment 'spec.policies 信息',
    workflow      varchar(64) comment 'spec.workflow 信息',
    version       varchar(64) comment '版本',
    remark        varchar(256) comment '备注',
    creator       varchar(32) comment '创建人',
    create_time   timestamp not null default current_timestamp comment '创建时间',
    editor        varchar(32) comment '修改人',
    update_time   timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    key           idx_project (project_id)
) engine = innodb comment '应用配置信息';

drop table if exists oam_appplication;
create table oam_appplication
(
    id                    bigint       not null auto_increment comment '自增主键',
    project_id            bigint       not null comment '项目id',
    app_id                varchar(64) comment '应用id。主要用于 kubernetes 中 metadata 使用',
    cluster_credential_id bigint       not null,
    namespace             varchar(255) not null,
    metadata              varchar(64) comment 'metadata 信息',
    components            varchar(64) comment 'spec.components 信息',
    policies              varchar(64) comment 'spec.policies 信息',
    workflow              varchar(64) comment 'spec.workflow 信息',
    version               varchar(64) comment '版本',
    remark                varchar(256) comment '备注',
    creator               varchar(32) comment '创建人',
    create_time           timestamp    not null default current_timestamp comment '创建时间',
    editor                varchar(32) comment '修改人',
    update_time           timestamp    not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    key                   idx_project (project_id)
) engine = innodb comment '应用信息';

drop table if exists oam_appplication_component;
create table oam_appplication_component
(
    id           bigint    not null auto_increment comment '自增主键',
    component_id varchar(64) comment '应用id。主要用于 kubernetes 中 metadata 使用',
    name         varchar(256),
    type         varchar(256),
    properties   text,
    inputs       text,
    outputs      text,
    traits       text,
    creator      varchar(32) comment '创建人',
    create_time  timestamp not null default current_timestamp comment '创建时间',
    editor       varchar(32) comment '修改人',
    update_time  timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id)
) engine = innodb comment '应用-Component 信息';