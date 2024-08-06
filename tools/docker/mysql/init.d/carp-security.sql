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
) engine = innodb comment = '角色表';

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
) engine = innodb comment = '资源-web';

INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (1, '0', 0, 'studio', '工作台', '/studio', 0, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (2, '2', 1, 'databoard', '数据看班', '/studio/databoard', 0, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (10000000, '0', 0, 'project', '项目', '/project', 1, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (10000001, '1', 10000000, 'project.engine', '引擎管理', '/workspace/engine', 0, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (10000002, '1', 10000001, 'lake', '数据湖', '/workspace/engine/lake', 0, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (10000003, '2', 10000002, 'iceberg', 'Iceberg', '/workspace/engine/lake/iceberg', 0, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (10000004, '2', 10000002, 'paimon', 'Paimon', '/workspace/engine/lake/paimon', 1, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (10100000, '1', 10000001, 'olap', 'OLAP引擎', '/workspace/engine/olap', 1, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (10100001, '1', 10100000, 'doris', 'Doris', '/workspace/engine/olap/doris', 0, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (10100002, '2', 10100001, 'template', '部署模板', '/workspace/engine/olap/doris/template', 0, '1', NULL, 'sys',
        'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (10100003, '2', 10100001, 'instance', '部署实例', '/workspace/engine/olap/doris/instance', 1, '1', NULL, 'sys',
        'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (10110000, '2', 10100000, 'starrocks', 'StarRocks', '/workspace/engine/olap/starrocks', 1, '1', NULL, 'sys',
        'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (10200000, '1', 10000001, 'compute', '计算引擎', '/workspace/engine/compute', 2, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (10200001, '1', 10200000, 'flink', 'Flink', '/workspace/engine/compute/flink', 0, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (10200002, '2', 10200001, 'template', '部署模板', '/workspace/engine/compute/flink/template', 0, '1', NULL,
        'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (10200003, '2', 10200001, 'session-cluster', 'Session 集群', '/workspace/engine/compute/flink/session-cluster',
        1, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (10200004, '2', 10200001, 'deployment', 'Deployment', '/workspace/engine/compute/flink/deployment', 2, '1', NULL,
        'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (11000000, '1', 10000000, 'project.data-integration', '数据集成', '/workspace/data-integration', 1, '1', NULL,
        'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (11000001, '2', 11000000, 'seatunnel', 'SeaTunnel', '/workspace/data-integration/seatunnel', 0, '1', NULL, 'sys',
        'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (11000002, '2', 11000000, 'flink-cdc', 'Flink CDC', '/workspace/data-integration/flink-cdc', 1, '1', NULL, 'sys',
        'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (12000000, '1', 10000000, 'project.data-develop', '数据开发', '/workspace/data-develop', 2, '1', NULL, 'sys',
        'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (12000001, '2', 12000000, 'flink-jar', 'Flink Jar', '/workspace/data-develop/flink/jar', 0, '1', NULL, 'sys',
        'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (12000002, '2', 12000000, 'flink-sql', 'Flink SQL', '/workspace/data-develop/flink/sql', 1, '1', NULL, 'sys',
        'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (13000000, '1', 10000000, 'project.dag-scheduler', 'DAG 调度', '/workspace/dag-scheduler', 3, '1', NULL, 'sys',
        'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (14000000, '1', 10000000, 'project.data-service', '数据服务', '/workspace/data-service', 4, '1', NULL, 'sys',
        'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (14000001, '2', 14000000, 'config', '接口配置', '/workspace/data-service/config', 0, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (15000000, '1', 10000000, 'project.operation', '运维中心', '/workspace/operation', 5, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (15000001, '2', 15000000, 'flink', 'Flink任务', '/workspace/operation/compute/flink/job', 0, '1', NULL, 'sys',
        'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (20000000, '0', 0, 'oam', 'OAM', '/oam', 2, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (20000001, '2', 20000000, 'definition', 'XDefinition', '/oam/definitin', 0, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (30000000, '0', 0, 'resource', '资源', '/resource', 3, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (30000001, '2', 30000000, 'jar', '公共 Jar', '/resource/jar', 0, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (30000002, '2', 30000000, 'flinkRelease', 'Flink Release', '/resource/flink-release', 1, '1', NULL, 'sys',
        'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (30000003, '2', 30000000, 'seatunnelRelease', 'SeaTunnel Release', '/resource/seatunnel-release', 2, '1', NULL,
        'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (30000004, '2', 30000000, 'kerberos', 'Kerberos', '/resource/kerberos', 3, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (30000005, '2', 30000000, 'clusterCredential', 'Cluster Credential', '/resource/cluster-credential', 4, '1',
        NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (40000000, '0', 0, 'metadata', '元数据', '/metadata', 4, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (40000001, '1', 40000000, 'data-source', '数据源', '/metadata/data-source', 0, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (40000002, '2', 40000001, 'info', '数据源信息', '/metadata/data-source/info', 0, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (41000000, '1', 40000000, 'gravitino', 'Gravitino', '/metadata/gravitino', 1, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (41000001, '2', 41000000, 'catalog', 'Catalog', '/metadata/gravitino/catelog', 0, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (50000000, '0', 0, 'stdata', '数据标准', '/stdata', 5, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (50000001, '2', 50000000, 'system', '业务系统', '/stdata/system', 0, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (50000002, '2', 50000000, 'dataElement', '数据元', '/stdata/dataElement', 1, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (50000003, '2', 50000000, 'refdata', '参考数据', '/stdata/refdata', 2, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (50000004, '2', 50000000, 'refdataMap', '数据映射', '/stdata/refdataMap', 3, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (60000000, '0', 0, 'admin', '系统管理', '/admin', 6, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (60000001, '1', 60000000, 'security', '安全管理', '/admin/security', 0, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (60000002, '2', 60000001, 'dept', '部门管理', '/admin/security/dept', 0, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (60000003, '2', 60000001, 'role', '角色管理', '/admin/security/role', 1, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (60000004, '2', 60000001, 'user', '用户管理', '/admin/security/user', 2, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (60000005, '2', 60000001, 'resource.web', 'Web 资源', '/admin/security/resource/web', 3, '1', NULL, 'sys',
        'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (61000000, '2', 60000000, 'quartz', '系统任务', '/admin/workflow/quartz', 1, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (62000000, '2', 60000000, 'dict', '数据字典', '/admin/dict', 2, '1', NULL, 'sys', 'sys');
INSERT INTO `carp_sec_resource_web` (`id`, `type`, `pid`, `value`, `label`, `path`, `order`, `status`, `remark`,
                                     `creator`, `editor`)
VALUES (63000000, '2', 60000000, 'setting', '系统设置', '/admin/setting', 3, '1', NULL, 'sys', 'sys');

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
) engine = innodb comment = '资源-web与角色关联表';

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
) engine = innodb comment = 'security user-role relation';

insert into carp_sec_user_role (id, user_id, role_id, creator, editor)
values (1, 1, 1, 'sys', 'sys');