create database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
use scaleph;

/*用户基本信息表 */
drop table if exists sec_user;
create table sec_user
(
    id               bigint       not null auto_increment comment '自增主键',
    user_name        varchar(32)  not null comment '用户名',
    nick_name        varchar(50) comment '昵称',
    email            varchar(128) not null comment '邮箱',
    password         varchar(64)  not null comment '密码',
    real_name        varchar(64) comment '真实姓名',
    id_card_type     varchar(4) comment '证件类型',
    id_card_no       varchar(18) comment '证件号码',
    gender           varchar(4) default '0' comment '性别',
    nation           varchar(4) comment '民族',
    birthday         date comment '出生日期',
    qq               varchar(18) comment 'qq号码',
    wechat           varchar(64) comment '微信号码',
    mobile_phone     varchar(16) comment '手机号码',
    user_status      varchar(4)   not null comment '用户状态',
    summary          varchar(512) comment '用户简介',
    register_channel varchar(4)   not null comment '注册渠道',
    register_time    timestamp  default current_timestamp comment '注册时间',
    register_ip      varchar(16) comment '注册ip',
    creator          varchar(32) comment '创建人',
    create_time      timestamp  default current_timestamp comment '创建时间',
    editor           varchar(32) comment '修改人',
    update_time      timestamp  default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id),
    unique key (user_name),
    unique key (email),
    unique key (mobile_phone),
    key (update_time)
) engine = innodb comment = '用户基本信息表';
-- init data
insert into sec_user (id, user_name, nick_name, email, password, real_name, id_card_type, id_card_no, gender, nation,
                      birthday, qq, wechat, mobile_phone, user_status, summary, register_channel, register_time,
                      register_ip, creator, editor)
values (1, 'sys_admin', '超级管理员', 'test@admin.com', '$2a$10$QX2DBrOBGLuhEmboliW66ulvQ5Hiy9GCdhsqqs1HgJVgslYhZEC6q',
        null,
        null, null, '0', null, null, null, null, null, '10', null, '01', '2021-12-25 21:51:17', '127.0.0.1', 'sys',
        'sys');

/* 角色表 */
drop table if exists sec_role;
create table sec_role
(
    id          bigint      not null auto_increment comment '角色id',
    role_code   varchar(32) not null comment '角色编码',
    role_name   varchar(64) not null comment '角色名称',
    role_type   varchar(4)  not null comment '角色类型',
    role_status varchar(4)  not null comment '角色状态',
    role_desc   varchar(128) comment '角色备注',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id),
    unique key (role_code),
    key (update_time)
) engine = innodb comment = '角色表';

insert into sec_role (role_code, role_name, role_type, role_status)
values ('sys_super_admin', '超级系统管理员', '01', '1');
insert into sec_role (role_code, role_name, role_type, role_status)
values ('sys_admin', '系统管理员', '01', '1');
insert into sec_role (role_code, role_name, role_type, role_status)
values ('sys_normal', '普通用户', '01', '1');

/* 权限表 */
drop table if exists sec_privilege;
create table sec_privilege
(
    id             bigint       not null auto_increment comment '自增主键',
    privilege_code varchar(64)  not null comment '权限标识',
    privilege_name varchar(128) not null comment '权限名称',
    resource_type  varchar(4)            default '1' comment '资源类型',
    resource_path  varchar(64) comment '资源路径',
    pid            bigint       not null default '0' comment '上级权限id',
    creator        varchar(32) comment '创建人',
    create_time    timestamp             default current_timestamp comment '创建时间',
    editor         varchar(32) comment '修改人',
    update_time    timestamp             default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (privilege_code),
    key (update_time)
) engine = innodb comment = '权限表';
/* init privilege */
-- truncate table sec_privilege;
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (10, 'psdo0', '工作台', '0', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (1001, 'psdb0', '数据看板', '0', '', 10, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20, 'pdev0', '数据开发', '0', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (2001, 'pdts0', '数据源', '0', '', 20, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (2002, 'pddp0', '项目管理', '0', '', 20, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (2003, 'pdde0', '资源管理', '0', '', 20, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (2004, 'pddj0', '作业管理', '0', '', 20, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (2005, 'pddc0', '集群管理', '0', '', 20, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (30, 'popc0', '运维中心', '0', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (3001, 'pobt0', '周期任务', '0', '', 30, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (3002, 'port0', '实时任务', '0', '', 30, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40, 'pstd0', '数据标准', '0', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (4001, 'pstr0', '参考数据', '0', '', 40, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (4002, 'pstm0', '数据映射', '0', '', 40, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (4003, 'pste0', '数据元', '0', '', 40, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (4004, 'psts0', '业务系统', '0', '', 40, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50, 'padm0', '系统管理', '0', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (5001, 'pusr0', '用户管理', '0', '', 50, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (5002, 'ppvg0', '权限管理', '0', '', 50, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (5003, 'pdic0', '数据字典', '0', '', 50, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (5004, 'pset0', '系统设置', '0', '', 50, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (200101, 'pdts4', '数据源', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20010101, 'pdts1', '新增数据源', '1', '', 200101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20010102, 'pdts3', '删除数据源', '1', '', 200101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20010103, 'pdts2', '修改数据源', '1', '', 200101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20010104, 'pdts6', '查看密码', '1', '', 200101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (200201, 'pddp4', '项目管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20020101, 'pddp1', '新增项目', '1', '', 200201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20020102, 'pddp3', '删除项目', '1', '', 200201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20020103, 'pddp2', '修改项目', '1', '', 200201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (200301, 'pdde4', '资源管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20030101, 'pdde1', '新增资源', '1', '', 200301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20030102, 'pdde3', '删除资源', '1', '', 200301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20030103, 'pdde2', '修改资源', '1', '', 200301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20030104, 'pdde7', '下载资源', '1', '', 200301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (200401, 'pddj4', '作业管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20040101, 'pddj1', '新增作业', '1', '', 200401, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20040102, 'pddj3', '删除作业', '1', '', 200401, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20040103, 'pddj2', '修改作业', '1', '', 200401, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (200402, 'pddr4', '目录管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20040201, 'pddr1', '新增目录', '1', '', 200402, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20040202, 'pddr3', '删除目录', '1', '', 200402, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20040203, 'pddr2', '修改目录', '1', '', 200402, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (200501, 'pddc4', '集群管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20050101, 'pddc1', '新增集群', '1', '', 200501, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20050102, 'pddc3', '删除集群', '1', '', 200501, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (20050103, 'pddc2', '修改集群', '1', '', 200501, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (300101, 'pobt4', '周期任务', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (300201, 'port4', '实时任务', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (400101, 'pstt4', '参考数据类型管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40010101, 'pstt1', '新增参考数据类型', '1', '', 400101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40010102, 'pstt3', '删除参考数据类型', '1', '', 400101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40010103, 'pstt2', '修改参考数据类型', '1', '', 400101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (400102, 'pstr4', '参考数据管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40010201, 'pstr1', '新增参考数据', '1', '', 400102, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40010202, 'pstr3', '删除参考数据', '1', '', 400102, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40010203, 'pstr2', '修改参考数据', '1', '', 400102, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (400201, 'pstm4', '参考数据映射管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40020101, 'pstm1', '新增参考数据映射', '1', '', 400201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40020102, 'pstm3', '删除参考数据映射', '1', '', 400201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40020103, 'pstm2', '修改参考数据映射', '1', '', 400201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (400301, 'pste4', '数据元管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40030101, 'pste1', '新增数据元', '1', '', 400301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40030102, 'pste3', '删除数据元', '1', '', 400301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40030103, 'pste2', '修改数据元', '1', '', 400301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (400401, 'psts4', '业务系统', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40040101, 'psts1', '新增业务系统', '1', '', 400401, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40040102, 'psts3', '删除业务系统', '1', '', 400401, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (40040103, 'psts2', '修改业务系统', '1', '', 400401, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (500101, 'pusr4', '用户管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50010101, 'pusr1', '新增用户', '1', '', 500101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50010102, 'pusr3', '删除用户', '1', '', 500101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50010103, 'pusr2', '修改用户', '1', '', 500101, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (500201, 'prol4', '角色管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50020101, 'prol1', '新增角色', '1', '', 500201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50020102, 'prol3', '删除角色', '1', '', 500201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50020103, 'prol2', '修改角色', '1', '', 500201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50020104, 'prol5', '角色授权', '1', '', 500201, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (500202, 'pdep4', '部门管理', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50020201, 'pdep1', '新增部门', '1', '', 500202, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50020202, 'pdep3', '删除部门', '1', '', 500202, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50020203, 'pdep2', '修改部门', '1', '', 500202, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50020204, 'pdep5', '部门授权', '1', '', 500202, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (500301, 'pdct4', '字典类型', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50030101, 'pdct1', '新增字典类型', '1', '', 500301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50030102, 'pdct3', '删除字典类型', '1', '', 500301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50030103, 'pdct2', '修改字典类型', '1', '', 500301, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (500302, 'pdcd4', '数据字典', '1', '', 0, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50030201, 'pdcd1', '新增数据字典', '1', '', 500302, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50030202, 'pdcd3', '删除数据字典', '1', '', 500302, 'sys', 'sys');
insert into sec_privilege (id, privilege_code, privilege_name, resource_type, resource_path, pid, creator, editor)
values (50030203, 'pdcd2', '修改数据字典', '1', '', 500302, 'sys', 'sys');

/* 角色权限关联表 */
drop table if exists sec_role_privilege;
create table sec_role_privilege
(
    id           bigint not null auto_increment comment '自增主键',
    role_id      bigint not null comment '角色id',
    privilege_id bigint not null comment '权限id',
    creator      varchar(32) comment '创建人',
    create_time  timestamp default current_timestamp comment '创建时间',
    editor       varchar(32) comment '修改人',
    update_time  timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (role_id, privilege_id),
    key (update_time)
) engine = innodb comment = '角色权限关联表';
-- init normal role
-- truncate table sec_role_privilege;
insert into sec_role_privilege (role_id, privilege_id, creator, editor)
select r.id  as role_id,
       p.id  as privilege_id,
       'sys' as creator,
       'sys' as editor
from sec_privilege p,
     sec_role r
where r.role_code in ('sys_normal', 'sys_admin', 'sys_super_admin');
/* 部门表 */
drop table if exists sec_dept;
create table sec_dept
(
    id          bigint      not null auto_increment comment '部门id',
    dept_code   varchar(32) not null comment '部门编号',
    dept_name   varchar(64) not null comment '部门名称',
    pid         bigint      not null default '0' comment '上级部门',
    dept_status varchar(1)  not null default '1' comment '部门状态',
    creator     varchar(32) comment '创建人',
    create_time timestamp            default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp            default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique (dept_code),
    unique (dept_name),
    key (pid)
) engine = innodb comment = '部门表';

/*用户和部门关联表 */
drop table if exists sec_user_dept;
create table sec_user_dept
(
    id          bigint not null auto_increment comment '自增主键',
    user_id     bigint not null comment '用户id',
    dept_id     bigint not null comment '部门id',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (dept_id, user_id),
    key (update_time)
) engine = innodb comment = '用户和部门关联表';


/* 用户角色关联表 */
drop table if exists sec_user_role;
create table sec_user_role
(
    id          bigint not null auto_increment comment '自增主键',
    user_id     bigint not null comment '用户id',
    role_id     bigint not null comment '角色id',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '更新时间',
    primary key (id),
    unique key (user_id, role_id),
    key (update_time)
) engine = innodb comment = '用户角色关联表';

-- init data
insert into sec_user_role (id, user_id, role_id, creator, editor)
values (1, 1, 1, 'sys', 'sys');
/* 部门角色关联表 */
drop table if exists sec_dept_role;
create table sec_dept_role
(
    id          bigint not null auto_increment comment '自增主键',
    dept_id     bigint not null comment '部门id',
    role_id     bigint not null comment '角色id',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (dept_id, role_id),
    key (update_time)
) engine = innodb comment = '部门角色关联表';

/*用户邮箱激活日志表*/
drop table if exists sec_user_active;
create table sec_user_active
(
    id          bigint      not null auto_increment comment '自增主键',
    user_name   varchar(60) not null comment '用户名',
    active_code varchar(36) not null comment '激活码',
    expiry_time bigint      not null comment '激活码过期时间戳',
    active_time timestamp comment '激活时间',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (active_code),
    key (user_name),
    key (update_time)
) engine = innodb comment = '用户邮箱激活日志表';