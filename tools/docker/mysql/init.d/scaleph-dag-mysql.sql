create database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
use scaleph;

drop table if exists dag_instance;
create table dag_instance
(
    id          bigint       not null auto_increment comment '自增主键',
    type        varchar(32)  not null comment '类型',
    dag_id      varchar(64)  not null,
    dag_meta    varchar(128) not null comment 'DAG元信息',
    dag_attrs   mediumtext comment 'DAG属性',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id)
) engine = innodb comment 'DAG 实例';

drop table if exists dag_step;
create table dag_step
(
    id          bigint       not null auto_increment comment '自增主键',
    dag_id      bigint       not null comment 'DAG id',
    step_id     varchar(36)  not null comment '步骤id',
    step_name   varchar(128) not null comment '步骤名称',
    position_x  int          not null comment 'x坐标',
    position_y  int          not null comment 'y坐标',
    step_meta   varchar(128) not null comment '步骤元信息',
    step_attrs  mediumtext comment '步骤属性',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (job_id, step_id)
) engine = innodb comment 'DAG 步骤';

drop table if exists dag_link;
create table dag_link
(
    id           bigint       not null auto_increment comment '自增主键',
    dag_id       bigint       not null comment 'DAG id',
    link_id      varchar(36)  not null comment '连线id',
    link_name   varchar(128) not null comment '连线名称',
    from_step_id varchar(36)  not null comment '源步骤id',
    to_step_id   varchar(36)  not null comment '目标步骤id',
    link_meta    varchar(128) not null comment '连线元信息',
    link_attrs   mediumtext comment '连线属性',
    creator      varchar(32) comment '创建人',
    create_time  timestamp default current_timestamp comment '创建时间',
    editor       varchar(32) comment '修改人',
    update_time  timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (job_id, link_id)
) engine = innodb comment 'DAG 连线';