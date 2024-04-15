create database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
use scaleph;

drop table if exists ws_app;
create table ws_app
(
    id          bigint    not null auto_increment comment '自增主键',
    project_id  bigint    not null comment '项目id',
    app_id      varchar(64) comment '应用id。主要用于 kubernetes 中 metadata 使用',
    version     varchar(64) comment '版本',
    remark      varchar(256) comment '备注',
    creator     varchar(32) comment '创建人',
    create_time timestamp not null default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    key         idx_project (project_id)
) engine = innodb comment '应用信息';