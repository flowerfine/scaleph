create database if not exists carp default character set utf8mb4 collate utf8mb4_unicode_ci;
use carp;

drop table if exists carp_sec_application;
create table carp_sec_application
(
    `id`          bigint       not null auto_increment comment '自增主键',
    `type`        varchar(4)   not null comment '用户类型。系统，用户自定义',
    `code`        varchar(32)  not null comment '应用标识',
    `name`        varchar(64)  not null comment '应用名称',
    `logo`        varchar(255) not null comment '应用logo',
    `url`         varchar(255) not null comment '应用 url',
    `order`       int          not null default 0 comment '排序',
    `status`      varchar(4)   not null comment '应用状态。启用，禁用',
    `remark`      varchar(255) comment '备注',
    `creator`     varchar(32) comment '创建人',
    `create_time` datetime     not null default current_timestamp comment '创建时间',
    `editor`      varchar(32) comment '修改人',
    `update_time` datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id),
    unique key (code)
) engine = innodb comment = 'security application';

drop table if exists carp_sec_user;
create table carp_sec_user
(
    `id`          bigint      not null auto_increment comment '自增主键',
    `type`        varchar(4)  not null comment '用户类型。系统，用户自定义',
    `user_name`   varchar(32) not null comment '用户名',
    `nick_name`   varchar(50) comment '昵称',
    `avatar`      varchar(255) comment '头像',
    `email`       varchar(128) comment '邮箱',
    `phone`       varchar(16) comment '手机',
    `password`    varchar(64) not null comment '密码',
    `salt`        varchar(64) not null comment '密码盐值',
    `order`       int         not null default 0 comment '排序',
    `status`      varchar(4)  not null comment '用户状态。启用，禁用',
    `remark`      varchar(255) comment '备注',
    `creator`     varchar(32) comment '创建人',
    `create_time` datetime    not null default current_timestamp comment '创建时间',
    `editor`      varchar(32) comment '修改人',
    `update_time` datetime    not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id),
    unique key (user_name),
    key (update_time)
) engine = innodb comment = 'security user';

insert into carp_sec_user (id, type, user_name, nick_name, avatar, email, phone, password, `salt`, `order`, `status`,
                           remark, creator, editor)
values (1, '0', 'sys_admin', '超级管理员', null, 'test@admin.com', null, 'dfed150b0806844c2533c9c0ed70df51',
        'ce5gT8lVxGdFN8RnSNAcjFUz8dMrRd7B', 0, '0', null, 'sys', 'sys');
insert into `carp_sec_user` (`id`, `type`, `user_name`, `nick_name`, `avatar`, `email`, `phone`, `password`, `salt`,
                             `order`, `status`, `remark`, `creator`, `editor`)
values (2, '1', 'kalencaya', '王奇', null, '1942460489@qq.com', null, '4a84974911765dc9b3591076581feeb6',
        '06qyF4E9U7XM5QIZmATZjnmTG1b3X1uN', 1, '0', null, 'sys', 'sys');
insert into `carp_sec_user` (`id`, `type`, `user_name`, `nick_name`, `avatar`, `email`, `phone`, `password`, `salt`,
                             `order`, `status`, `remark`, `creator`, `editor`)
values (3, '1', 'gleiyu', '耿雷雨', null, 'gleiyu@sina.cn', null, '2407140fcdd211fa971a73f11cf99af3',
        'SVnSHgI82hUXzLTdhNjD1YrpkgnSjSsi', 2, '0', null, 'sys', 'sys');
insert into `carp_sec_user` (`id`, `type`, `user_name`, `nick_name`, `avatar`, `email`, `phone`, `password`, `salt`,
                             `order`, `status`, `remark`, `creator`, `editor`)
values (4, '1', 'LiuBodong', 'LiuBodong', null, 'liubodong2010@126.com', null, '84ca56252f9843ef538b59e265a344e9',
        'Lsv4doPPAuciFmcFLqfcj8fXZK7ef421', 3, '0', null, 'sys', 'sys');
insert into `carp_sec_user` (`id`, `type`, `user_name`, `nick_name`, `avatar`, `email`, `phone`, `password`, `salt`,
                             `order`, `status`, `remark`, `creator`, `editor`)
values (5, '1', 'bailongsen1027', 'bailongsen1027', null, null, null, 'c5c336e804c91da5d943df05a8dc2a17',
        '3qgmeXlVYQGPfalaaOUDA5iuT45A6eMK', 4, '0', null, 'sys', 'sys');

/* 角色表 */
drop table if exists carp_sec_role;
create table carp_sec_role
(
    `id`          bigint      not null auto_increment comment '自增主键',
    `type`        varchar(4)  not null comment '角色类型。系统，用户自定义',
    `code`        varchar(32) not null comment '角色编码',
    `name`        varchar(64) not null comment '角色名称',
    `order`       int         not null default 0 comment '排序',
    `status`      varchar(4)  not null comment '角色状态',
    `remark`      varchar(255) comment '备注',
    `creator`     varchar(32) comment '创建人',
    `create_time` datetime    not null default current_timestamp comment '创建时间',
    `editor`      varchar(32) comment '修改人',
    `update_time` datetime    not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id),
    unique key (code),
    key (update_time)
) engine = innodb comment = 'security role';

insert into carp_sec_role (id, type, `code`, `name`, `order`, `status`, `creator`, `editor`)
values (1, '01', 'sys_super_admin', '超级系统管理员', 0, '1', 'sys', 'sys');
insert into carp_sec_role (id, type, `code`, `name`, `order`, `status`, `creator`, `editor`)
values (2, '01', 'sys_admin', '系统管理员', 1, '1', 'sys', 'sys');
insert into carp_sec_role (id, type, `code`, `name`, `order`, `status`, `creator`, `editor`)
values (3, '01', 'sys_normal', '普通用户', 2, '1', 'sys', 'sys');

drop table if exists carp_sec_resource_web;
create table carp_sec_resource_web
(
    `id`          bigint       not null auto_increment comment '自增主键',
    `type`        varchar(128) not null comment '资源类型。导航，菜单，页面，按钮',
    `pid`         bigint       not null default '0' comment '上级资源id',
    `value`       varchar(128) comment '资源code',
    `label`       varchar(128) comment '资源名称',
    `path`        varchar(128) comment '路由路径',
    `order`       int          not null default 0 comment '排序',
    `status`      varchar(4)   not null comment '角色状态',
    `remark`      varchar(256) comment '备注',
    `creator`     varchar(32) comment '创建人',
    `create_time` datetime     not null default current_timestamp comment '创建时间',
    `editor`      varchar(32) comment '修改人',
    `update_time` datetime     not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (type, pid, path)
) engine = innodb comment = 'security web resource';

drop table if exists carp_sec_resource_web_role;
create table carp_sec_resource_web_role
(
    `id`              bigint   not null auto_increment comment '自增主键',
    `resource_web_id` bigint   not null,
    `role_id`         bigint   not null,
    `creator`         varchar(32) comment '创建人',
    `create_time`     datetime not null default current_timestamp comment '创建时间',
    `editor`          varchar(32) comment '修改人',
    `update_time`     datetime not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (resource_web_id, role_id)
) engine = innodb comment = 'security resource-web and role relation';

insert into carp_sec_resource_web_role (resource_web_id, role_id, creator, editor)
select t1.id as resource_web_id,
       t2.id as role_id,
       'sys' as creator,
       'sys' as editor
from carp_sec_resource_web t1,
     carp_sec_role t2
where t2.`code` in ('sys_admin', 'sys_super_admin');

/* 用户角色关联表 */
drop table if exists carp_sec_user_role;
create table carp_sec_user_role
(
    `id`          bigint   not null auto_increment comment '自增主键',
    `user_id`     bigint   not null comment '用户id',
    `role_id`     bigint   not null comment '角色id',
    `creator`     varchar(32) comment '创建人',
    `create_time` datetime not null default current_timestamp comment '创建时间',
    `editor`      varchar(32) comment '修改人',
    `update_time` datetime not null default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id),
    unique key (user_id, role_id),
    key (update_time)
) engine = innodb comment = 'security user and role relation';

insert into carp_sec_user_role (id, user_id, role_id, creator, editor)
values (1, 1, 1, 'sys', 'sys');