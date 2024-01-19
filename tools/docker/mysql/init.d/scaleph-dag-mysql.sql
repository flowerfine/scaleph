create database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
use scaleph;

drop table if exists dag_instance;
create table dag_instance
(
    id          bigint not null auto_increment comment '自增主键',
    dag_meta    varchar(128) comment 'DAG元信息',
    dag_attrs   mediumtext comment 'DAG属性',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id)
) engine = innodb comment 'DAG 实例';

INSERT INTO `dag_instance` (`id`, `dag_meta`, `dag_attrs`, `creator`, `editor`)
VALUES (1, NULL, NULL, 'sys', 'sys');
INSERT INTO `dag_instance` (`id`, `dag_meta`, `dag_attrs`, `creator`, `editor`)
VALUES (2, NULL, NULL, 'sys', 'sys');

drop table if exists dag_step;
create table dag_step
(
    id          bigint      not null auto_increment comment '自增主键',
    dag_id      bigint      not null comment 'DAG id',
    step_id     varchar(36) not null comment '步骤id',
    step_name   varchar(128) comment '步骤名称',
    position_x  int         not null comment 'x坐标',
    position_y  int         not null comment 'y坐标',
    step_meta   varchar(128) comment '步骤元信息',
    step_attrs  mediumtext comment '步骤属性',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key uniq_step (dag_id, step_id)
) engine = innodb comment 'DAG 步骤';

INSERT INTO `dag_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `step_meta`, `step_attrs`, `creator`, `editor`) VALUES (1, 1, '68834928-2a32-427a-a864-83b6b5848e04', 'Jdbc Sink', 360, 290, '{"name":"Jdbc","type":"sink","engine":"seatunnel"}', '{"stepTitle":"Jdbc Sink","dataSourceType":"MySQL","dataSource":2,"batch_size":300,"batch_interval_ms":1000,"max_retries":3,"is_exactly_once":false,"query":"insert into sample_data_e_commerce_duplicate ( id, invoice_no, stock_code, description, quantity, invoice_date, unit_price, customer_id, country )\nvalues (?,?,?,?,?,?,?,?,?)"}', 'sys', 'sys');
INSERT INTO `dag_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `step_meta`, `step_attrs`, `creator`, `editor`) VALUES (2, 1, 'f3e02087-91fa-494d-86f4-694970a49ebd', 'Jdbc Source', 380, 140, '{"name":"Jdbc","type":"source","engine":"seatunnel"}', '{"stepTitle":"Jdbc Source","dataSourceType":"MySQL","dataSource":2,"query":"select * from sample_data_e_commerce"}', 'sys', 'sys');
INSERT INTO `dag_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `step_meta`, `step_attrs`, `creator`, `editor`) VALUES (3, 2, '6223c6c3-b552-4c69-adab-5300b7514fad', 'Fake Source', 380, 140, '{"name":"FakeSource","type":"source","engine":"seatunnel"}', '{"stepTitle":"Fake Source","fields":[{"field":"c_string","type":"string"},{"field":"c_boolean","type":"boolean"},{"field":"c_tinyint","type":"tinyint"},{"field":"c_smallint","type":"smallint"},{"field":"c_int","type":"int"},{"field":"c_bigint","type":"bigint"},{"field":"c_float","type":"float"},{"field":"c_double","type":"double"},{"field":"c_decimal","type":"decimal(30, 8)"},{"field":"c_bytes","type":"bytes"},{"field":"c_map","type":"map<string, string>"},{"field":"c_date","type":"date"},{"field":"c_time","type":"time"},{"field":"c_timestamp","type":"timestamp"}],"schema":"{\\\"fields\\\":{\\\"c_string\\\":\\\"string\\\",\\\"c_boolean\\\":\\\"boolean\\\",\\\"c_tinyint\\\":\\\"tinyint\\\",\\\"c_smallint\\\":\\\"smallint\\\",\\\"c_int\\\":\\\"int\\\",\\\"c_bigint\\\":\\\"bigint\\\",\\\"c_float\\\":\\\"float\\\",\\\"c_double\\\":\\\"double\\\",\\\"c_decimal\\\":\\\"decimal(30, 8)\\\",\\\"c_bytes\\\":\\\"bytes\\\",\\\"c_map\\\":\\\"map<string, string>\\\",\\\"c_date\\\":\\\"date\\\",\\\"c_time\\\":\\\"time\\\",\\\"c_timestamp\\\":\\\"timestamp\\\"}}"}', 'sys', 'sys');
INSERT INTO `dag_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `step_meta`, `step_attrs`, `creator`, `editor`) VALUES (4, 2, 'f08143b4-34dc-4190-8723-e8d8ce49738f', 'Console Sink', 360, 290, '{"name":"Console","type":"sink","engine":"seatunnel"}', '{"stepTitle":"Console Sink"}', 'sys', 'sys');

drop table if exists dag_link;
create table dag_link
(
    id           bigint      not null auto_increment comment '自增主键',
    dag_id       bigint      not null comment 'DAG id',
    link_id      varchar(36) not null comment '连线id',
    link_name    varchar(128) comment '连线名称',
    from_step_id varchar(36) not null comment '源步骤id',
    to_step_id   varchar(36) not null comment '目标步骤id',
    link_meta    varchar(128) comment '连线元信息',
    link_attrs   mediumtext comment '连线属性',
    creator      varchar(32) comment '创建人',
    create_time  timestamp default current_timestamp comment '创建时间',
    editor       varchar(32) comment '修改人',
    update_time  timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key uniq_link (dag_id, link_id)
) engine = innodb comment 'DAG 连线';

INSERT INTO `dag_link` (`id`, `dag_id`, `link_id`, `link_name`, `from_step_id`, `to_step_id`, `link_meta`, `link_attrs`,
                        `creator`, `editor`)
VALUES (1, 1, 'fabfda41-aacb-4a19-b5ef-9e84a75ed4e9', NULL, 'f3e02087-91fa-494d-86f4-694970a49ebd',
        '68834928-2a32-427a-a864-83b6b5848e04', NULL, NULL, 'sys', 'sys');
INSERT INTO `dag_link` (`id`, `dag_id`, `link_id`, `link_name`, `from_step_id`, `to_step_id`, `link_meta`, `link_attrs`,
                        `creator`, `editor`)
VALUES (2, 2, 'd57021a1-65c7-4dfe-ae89-3b73d00fcf72', NULL, '6223c6c3-b552-4c69-adab-5300b7514fad',
        'f08143b4-34dc-4190-8723-e8d8ce49738f', NULL, NULL, 'sys', 'sys');