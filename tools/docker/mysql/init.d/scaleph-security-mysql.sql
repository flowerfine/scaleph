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
    gender           varchar(4)            default '0' comment '性别',
    nation           varchar(4) comment '民族',
    birthday         date comment '出生日期',
    qq               varchar(18) comment 'qq号码',
    wechat           varchar(64) comment '微信号码',
    mobile_phone     varchar(16) comment '手机号码',
    user_status      varchar(4)   not null comment '用户状态',
    summary          varchar(512) comment '用户简介',
    register_channel varchar(4)   not null comment '注册渠道',
    register_time    datetime              default current_timestamp comment '注册时间',
    register_ip      varchar(16) comment '注册ip',
    creator          varchar(32) comment '创建人',
    create_time      datetime     not null default current_timestamp comment '创建时间',
    editor           varchar(32) comment '修改人',
    update_time      datetime     not null default current_timestamp on update current_timestamp comment '更新时间',
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
    create_time datetime    not null default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time datetime    not null default current_timestamp on update current_timestamp comment '更新时间',
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
    create_time    datetime     not null default current_timestamp comment '创建时间',
    editor         varchar(32) comment '修改人',
    update_time    datetime     not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (privilege_code),
    key (update_time)
) engine = innodb comment = '权限表';
/* init privilege */
-- truncate table sec_privilege;

INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (1, 'psdo0', '工作台', '0', 0, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (2, 'ppjp0', '项目', '0', 0, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (3, 'pree0', '资源', '0', 0, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (4, 'pdse0', '数据源', '0', 0, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (5, 'pstd0', '数据标准', '0', 0, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (6, 'padm0', '系统管理', '0', 0, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (7, 'pwjm0', '作业管理', '0', 2, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (8, 'pwcm0', '集群管理', '0', 2, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (100000, 'psdb0', '数据看板', '1', 1, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (200000, 'ppjl0', '项目列表', '1', 2, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (200001, 'ppjl4', '进入项目', '2', 200000, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (200002, 'ppjl1', '创建项目', '2', 200000, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (200003, 'ppjl2', '修改', '2', 200000, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (200004, 'ppjl3', '删除', '2', 200000, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (200005, 'pwjl0', '作业列表', '1', 7, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (200006, 'pwja0', 'Artifact', '1', 7, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (200007, 'pwjs0', 'SQL', '1', 7, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (200008, 'pwst0', 'SeaTunnel', '1', 7, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (200009, 'pwcc0', '集群配置', '1', 8, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (200010, 'pwci0', '集群实例', '1', 8, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300000, 'prej0', '公共 Jar', '1', 3, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300001, 'prfr0', 'Flink Release', '1', 3, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300002, 'prsr0', 'SeaTunnel Release', '1', 3, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300003, 'prek0', 'Kerberos', '1', 3, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300004, 'prcc0', 'Cluster Credential', '1', 3, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300005, 'prej7', '上传', '2', 300000, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300006, 'prej8', '下载', '2', 300000, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300007, 'prej3', '删除', '2', 300000, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300008, 'prfr7', '上传', '2', 300001, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300009, 'prfr8', '下载', '2', 300001, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300010, 'prfr3', '删除', '2', 300001, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300011, 'prsr7', '上传', '2', 300002, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300012, 'prsr8', '下载', '2', 300002, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300013, 'prsr3', '删除', '2', 300002, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300014, 'prek7', '上传', '2', 300003, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300015, 'prek8', '下载', '2', 300003, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300016, 'prek3', '删除', '2', 300003, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300017, 'prcc1', '新增', '2', 300004, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300018, 'prcc2', '修改', '2', 300004, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300019, 'prcc3', '删除', '2', 300004, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300020, 'prcf0', '详情页', '1', 300004, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300021, 'prcf7', '上传', '2', 300020, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300022, 'prcf8', '下载', '2', 300020, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (300023, 'prcf3', '删除', '2', 300020, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (400000, 'pdsl0', '数据源列表', '1', 4, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (400001, 'pdsl1', '新增', '2', 400000, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (400002, 'pdsl3', '删除', '2', 400000, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500000, 'psts0', '业务系统', '1', 5, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500001, 'pste0', '数据元', '1', 5, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500002, 'pstt0', '参考数据', '1', 5, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500003, 'pstm0', '数据映射', '1', 5, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500004, 'psts1', '新增', '2', 500000, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500005, 'psts2', '修改', '2', 500000, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500006, 'psts3', '删除', '2', 500000, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500007, 'pste1', '新增', '2', 500001, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500008, 'pste2', '修改', '2', 500001, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500009, 'pste3', '删除', '2', 500001, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500010, 'pstt1', '新增', '2', 500002, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500011, 'pstt2', '修改', '2', 500002, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500012, 'pstt3', '删除', '2', 500002, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500013, 'pstv0', '参考数据值', '2', 500002, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500014, 'pstv1', '新增', '2', 500013, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500015, 'pstv2', '修改', '2', 500013, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500016, 'pstv3', '删除', '2', 500013, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500017, 'pstm1', '新增', '2', 500003, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500018, 'pstm2', '修改', '2', 500003, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (500019, 'pstm3', '删除', '2', 500003, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (600000, 'padp0', '部门管理', '1', 6, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (600001, 'padr0', '角色管理', '1', 6, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (600002, 'padu0', '用户管理', '1', 6, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (600003, 'parw0', 'Web 资源', '1', 6, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (600004, 'pape0', '权限管理', '1', 6, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (600005, 'pawq0', '系统任务', '1', 6, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (600006, 'padd0', '数据字典', '1', 6, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (600007, 'pads0', '系统设置', '1', 6, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (600008, 'padp1', '新增', '2', 600000, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (600009, 'padp2', '修改', '2', 600000, 'sys', 'sys');
INSERT INTO `sec_privilege`(`id`, `privilege_code`, `privilege_name`, `resource_type`, `pid`, `creator`, `editor`)
VALUES (6000010, 'padp3', '删除', '2', 600000, 'sys', 'sys');


/* 角色权限关联表 */
drop table if exists sec_role_privilege;
create table sec_role_privilege
(
    id           bigint   not null auto_increment comment '自增主键',
    role_id      bigint   not null comment '角色id',
    privilege_id bigint   not null comment '权限id',
    creator      varchar(32) comment '创建人',
    create_time  datetime not null default current_timestamp comment '创建时间',
    editor       varchar(32) comment '修改人',
    update_time  datetime not null default current_timestamp on update current_timestamp comment '修改时间',
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
where r.role_code in ('sys_admin', 'sys_super_admin');
insert into sec_role_privilege (role_id, privilege_id, creator, editor)
select r.id  as role_id,
       p.id  as privilege_id,
       'sys' as creator,
       'sys' as editor
from sec_privilege p,
     sec_role r
where r.role_code in ('sys_normal') and p.resource_type in ('0', '1');
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
    create_time datetime    not null default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time datetime    not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique (dept_code),
    unique (dept_name),
    key (pid)
) engine = innodb comment = '部门表';
INSERT INTO `sec_dept`(`id`, `dept_code`, `dept_name`, `pid`, `dept_status`, `creator`, `editor`)
VALUES (1, 'scaleph', '水母文化', 0, '1', 'sys', 'sys');
INSERT INTO `sec_dept`(`id`, `dept_code`, `dept_name`, `pid`, `dept_status`, `creator`, `editor`)
VALUES (2, 'develop', '数据开发团队', 1, '1', 'sys', 'sys');
INSERT INTO `sec_dept`(`id`, `dept_code`, `dept_name`, `pid`, `dept_status`, `creator`, `editor`)
VALUES (3, 'data', '数据治理团队', 1, '1', 'sys', 'sys');
INSERT INTO `sec_dept`(`id`, `dept_code`, `dept_name`, `pid`, `dept_status`, `creator`, `editor`)
VALUES (4, 'application', '数据应用团队', 1, '1', 'sys', 'sys');
INSERT INTO `sec_dept`(`id`, `dept_code`, `dept_name`, `pid`, `dept_status`, `creator`, `editor`)
VALUES (5, 'integration', '数据集成', 2, '1', 'sys', 'sys');
INSERT INTO `sec_dept`(`id`, `dept_code`, `dept_name`, `pid`, `dept_status`, `creator`, `editor`)
VALUES (6, 'compute', '计算组', 2, '1', 'sys', 'sys');
INSERT INTO `sec_dept`(`id`, `dept_code`, `dept_name`, `pid`, `dept_status`, `creator`, `editor`)
VALUES (7, 'scheduler', '调度组', 2, '1', 'sys', 'sys');
INSERT INTO `sec_dept`(`id`, `dept_code`, `dept_name`, `pid`, `dept_status`, `creator`, `editor`)
VALUES (8, 'meta', '元数据', 3, '1', 'sys', 'sys');
INSERT INTO `sec_dept`(`id`, `dept_code`, `dept_name`, `pid`, `dept_status`, `creator`, `editor`)
VALUES (9, 'lineage', '数据血缘', 3, '1', 'sys', 'sys');
INSERT INTO `sec_dept`(`id`, `dept_code`, `dept_name`, `pid`, `dept_status`, `creator`, `editor`)
VALUES (10, 'quality', '数据质量', 3, '1', 'sys', 'sys');
INSERT INTO `sec_dept`(`id`, `dept_code`, `dept_name`, `pid`, `dept_status`, `creator`, `editor`)
VALUES (11, 'standard', '数据标准', 3, '1', 'sys', 'sys');
INSERT INTO `sec_dept`(`id`, `dept_code`, `dept_name`, `pid`, `dept_status`, `creator`, `editor`)
VALUES (12, 'model', '数据建模', 3, '1', 'sys', 'sys');
INSERT INTO `sec_dept`(`id`, `dept_code`, `dept_name`, `pid`, `dept_status`, `creator`, `editor`)
VALUES (13, 'index', '指标系统', 4, '1', 'sys', 'sys');
INSERT INTO `sec_dept`(`id`, `dept_code`, `dept_name`, `pid`, `dept_status`, `creator`, `editor`)
VALUES (14, 'analysis', '数据分析', 4, '1', 'sys', 'sys');
INSERT INTO `sec_dept`(`id`, `dept_code`, `dept_name`, `pid`, `dept_status`, `creator`, `editor`)
VALUES (15, 'ad-hoc', 'Ad-hoc', 4, '1', 'sys', 'sys');
INSERT INTO `sec_dept`(`id`, `dept_code`, `dept_name`, `pid`, `dept_status`, `creator`, `editor`)
VALUES (16, 'service', '数据服务', 4, '1', 'sys', 'sys');

/*用户和部门关联表 */
drop table if exists sec_user_dept;
create table sec_user_dept
(
    id          bigint   not null auto_increment comment '自增主键',
    user_id     bigint   not null comment '用户id',
    dept_id     bigint   not null comment '部门id',
    creator     varchar(32) comment '创建人',
    create_time datetime not null default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time datetime not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (dept_id, user_id),
    key (update_time)
) engine = innodb comment = '用户和部门关联表';


/* 用户角色关联表 */
drop table if exists sec_user_role;
create table sec_user_role
(
    id          bigint   not null auto_increment comment '自增主键',
    user_id     bigint   not null comment '用户id',
    role_id     bigint   not null comment '角色id',
    creator     varchar(32) comment '创建人',
    create_time datetime not null default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time datetime not null default current_timestamp on update current_timestamp comment '更新时间',
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
    id          bigint   not null auto_increment comment '自增主键',
    dept_id     bigint   not null comment '部门id',
    role_id     bigint   not null comment '角色id',
    creator     varchar(32) comment '创建人',
    create_time datetime not null default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time datetime not null default current_timestamp on update current_timestamp comment '修改时间',
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
    create_time datetime    not null default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time datetime    not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (active_code),
    key (user_name),
    key (update_time)
) engine = innodb comment = '用户邮箱激活日志表';