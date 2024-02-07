create database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
use scaleph;

drop table if exists dataservice_config;
create table dataservice_config
(
    id               bigint      not null auto_increment comment '自增主键',
    project_id       bigint      not null comment '项目id',
    name             varchar(64) not null comment 'name',
    path             varchar(64) not null comment 'uri path',
    method           varchar(16) not null comment 'http method',
    content_type     varchar(64) not null comment 'http content type',
    status           varchar(4)  not null comment 'status, disabled or enabled',
    parameter_map_id bigint comment 'paramter map id',
    result_map_id    bigint comment 'result map id',
    type             varchar(8) comment 'query type',
    query            text comment 'query',
    remark           varchar(256) comment '备注',
    creator          varchar(32) comment '创建人',
    create_time      timestamp default current_timestamp comment '创建时间',
    editor           varchar(32) comment '修改人',
    update_time      timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key uniq_name (project_id, `name`),
    unique key uniq_path (project_id, `path`, `method`)
) engine = innodb comment = '数据服务 配置';

drop table if exists dataservice_parameter_map;
create table dataservice_parameter_map
(
    id          bigint      not null auto_increment comment '自增主键',
    project_id  bigint      not null comment '项目id',
    name        varchar(64) not null comment 'name',
    remark      varchar(256) comment '备注',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key uniq_param (project_id, name)
) engine = innodb comment = '数据服务 请求参数集';

drop table if exists dataservice_parameter_mapping;
create table dataservice_parameter_mapping
(
    id               bigint       not null auto_increment comment '自增主键',
    parameter_map_id bigint       not null comment '请求参数集id',
    property         varchar(64)  not null comment '属性',
    java_type        varchar(256) not null comment 'java 类型',
    jdbc_type        varchar(256) comment 'jdbc 类型',
    type_handler     varchar(256) comment '类型转换器',
    creator          varchar(32) comment '创建人',
    create_time      timestamp default current_timestamp comment '创建时间',
    editor           varchar(32) comment '修改人',
    update_time      timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key uniq_property (parameter_map_id, property)
) engine = innodb comment = '数据服务 请求参数映射';

drop table if exists dataservice_result_map;
create table dataservice_result_map
(
    id          bigint      not null auto_increment comment '自增主键',
    project_id  bigint      not null comment '项目id',
    name        varchar(64) not null comment 'name',
    remark      varchar(256) comment '备注',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key uniq_param (project_id, name)
) engine = innodb comment = '数据服务 返回结果集';

drop table if exists dataservice_result_mapping;
create table dataservice_result_mapping
(
    id            bigint       not null auto_increment comment '自增主键',
    result_map_id bigint       not null comment '请求参数集id',
    property      varchar(64)  not null comment '属性',
    java_type     varchar(256) comment 'java 类型',
    `column`      varchar(128) not null comment '列',
    jdbc_type     varchar(256) comment 'jdbc 类型',
    type_handler  varchar(256) comment '类型转换器',
    creator       varchar(32) comment '创建人',
    create_time   timestamp default current_timestamp comment '创建时间',
    editor        varchar(32) comment '修改人',
    update_time   timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key uniq_property (result_map_id, property, `column`)
) engine = innodb comment = '数据服务 返回结果映射';