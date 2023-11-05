create database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
use scaleph;
/* 用户登录登出日志 */
drop table if exists log_login;
create table log_login
(
    id           bigint     not null auto_increment comment '自增主键',
    user_name    varchar(60) comment '用户名',
    login_time   timestamp  not null comment '登录时间',
    ip_address   varchar(255) comment 'ip地址',
    login_type   varchar(4) not null comment '登录类型 1-登录，2-登出，0-未知',
    client_info  text comment '客户端信息',
    os_info      varchar(128) comment '操作系统信息',
    browser_info text comment '浏览器信息',
    action_info  text comment '接口执行信息，包含请求结果',
    creator      varchar(32) comment '创建人',
    create_time  timestamp default current_timestamp comment '创建时间',
    editor       varchar(32) comment '修改人',
    update_time  timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    key (update_time),
    key (ip_address),
    key (login_time)
) engine = innodb comment = '用户登录登出日志';

/* 用户操作日志 */
drop table if exists log_action;
create table log_action
(
    id           bigint    not null auto_increment comment '自增主键',
    user_name    varchar(60) comment '用户名',
    action_time  timestamp not null comment '操作时间',
    ip_address   varchar(255) comment 'ip地址',
    action_url   mediumtext comment '操作接口地址',
    token        varchar(64) comment '会话token字符串',
    client_info  text comment '客户端信息',
    os_info      varchar(128) comment '操作系统信息',
    browser_info text comment '浏览器信息',
    action_info  text comment '接口执行信息，包含请求结果',
    creator      varchar(32) comment '创建人',
    create_time  timestamp default current_timestamp comment '创建时间',
    editor       varchar(32) comment '修改人',
    update_time  timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    key (update_time),
    key (ip_address),
    key (action_time)
) engine = innodb comment = '用户操作日志';

/*站内信日志表 */
drop table if exists log_message;
create table log_message
(
    id           bigint       not null auto_increment comment '自增主键',
    title        varchar(128) not null default '' comment '标题',
    message_type varchar(4)   not null comment '消息类型',
    receiver     varchar(32)  not null comment '收件人',
    sender       varchar(32)  not null comment '发送人',
    content      longtext comment '内容',
    is_read      varchar(1)   not null default '0' comment '是否已读',
    is_delete    varchar(1)   not null default '0' comment '是否删除',
    creator      varchar(32) comment '创建人',
    create_time  timestamp             default current_timestamp comment '创建时间',
    editor       varchar(32) comment '修改人',
    update_time  timestamp             default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    key (receiver, message_type),
    key (sender, message_type),
    key (update_time)
) engine = innodb comment = '站内信日志表';

/*定时任务运行日志表*/
drop table if exists log_schedule;
create table log_schedule
(
    id          bigint       not null auto_increment comment '自增主键',
    task_group  varchar(128) not null comment '任务组',
    task_name   varchar(128) not null comment '任务名称',
    start_time  datetime comment '开始时间',
    end_time    datetime comment '结束时间',
    trace_log   longtext comment '日志内容明细',
    result      varchar(12) comment '任务结果 成功/失败',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    key (task_name, task_group),
    key (start_time),
    key (end_time),
    key (update_time)
) engine = innodb comment '定时任务运行日志表';