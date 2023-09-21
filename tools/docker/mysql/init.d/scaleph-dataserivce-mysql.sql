create database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
use scaleph;

drop table if exists dataservice_config;
create table dataservice_config
(
    id           bigint      not null auto_increment comment '自增主键',
    name         varchar(64) not null comment 'name',
    path         varchar(64) not null comment 'uri path',
    method       varchar(16) not null comment 'http method',
    content_type varchar(64) not null comment 'http content type',
    status       varchar(4)  not null comment 'status, disabled or enabled',
    remark       varchar(256) comment '备注',
    creator      varchar(32) comment '创建人',
    create_time  timestamp default current_timestamp comment '创建时间',
    editor       varchar(32) comment '修改人',
    update_time  timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key uniq_name (name),
    unique key uniq_path (path)
) engine = innodb comment = '数据服务 配置';

drop table if exists dataservice_parameter_map;
create table dataservice_parameter_map
(
    id          bigint      not null auto_increment comment '自增主键',
    name        varchar(64) not null comment 'name',
    remark      varchar(256) comment '备注',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key uniq_param (name)
) engine = innodb comment = '数据服务 请求参数集';

drop table if exists dataservice_result_map;
create table dataservice_result_map
(
    id          bigint      not null auto_increment comment '自增主键',
    name        varchar(64) not null comment 'name',
    remark      varchar(256) comment '备注',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key uniq_param (name)
) engine = innodb comment = '数据服务 返回结果集';