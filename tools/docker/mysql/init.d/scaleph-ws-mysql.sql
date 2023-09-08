create database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
use scaleph;

drop table if exists ws_project;
create table ws_project
(
    id           bigint      not null auto_increment comment '自增主键',
    project_code varchar(32) not null comment '项目编码',
    project_name varchar(64) comment '项目名称',
    remark       varchar(256) comment '备注',
    creator      varchar(32) comment '创建人',
    create_time  datetime    not null default current_timestamp comment '创建时间',
    editor       varchar(32) comment '修改人',
    update_time  datetime    not null default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key uniq_project (project_code)
) engine = innodb comment '项目信息';

insert into ws_project(id, project_code, project_name, remark, creator, editor)
VALUES (1, 'seatunnel', 'seatunnel-examples', NULL, 'sys', 'sys');

drop table if exists ws_flink_artifact;
create table ws_flink_artifact
(
    id          bigint       not null auto_increment comment '自增主键',
    project_id  bigint       not null comment '项目id',
    `type`      varchar(4)   not null comment '作业artifact类型',
    name        varchar(255) not null comment '作业artifact名称',
    remark      varchar(255) comment '备注',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique uniq_name (project_id, `type`, name)
) engine = innodb comment = 'flink artifact';

INSERT INTO `ws_flink_artifact` (`id`, `project_id`, `type`, `name`, `remark`, `creator`, `editor`)
VALUES (1, 1, '0', 'simple sql', NULL, 'sys', 'sys');
INSERT INTO `ws_flink_artifact` (`id`, `project_id`, `type`, `name`, `remark`, `creator`, `editor`)
VALUES (2, 1, '0', 'sql-runner1', NULL, 'sys', 'sys');
INSERT INTO `ws_flink_artifact` (`id`, `project_id`, `type`, `name`, `remark`, `creator`, `editor`)
VALUES (3, 1, '0', 'sql-runner2', NULL, 'sys', 'sys');
INSERT INTO `ws_flink_artifact` (`id`, `project_id`, `type`, `name`, `remark`, `creator`, `editor`)
VALUES (4, 1, '2', 'e_commerce', NULL, 'sys', 'sys');
INSERT INTO `ws_flink_artifact` (`id`, `project_id`, `type`, `name`, `remark`, `creator`, `editor`)
VALUES (5, 1, '2', 'fake', NULL, 'sys', 'sys');
INSERT INTO `ws_flink_artifact`(`id`, `project_id`, `type`, `name`, `remark`, `creator`, `editor`)
VALUES (6, 1, '0', 'catalog-example', NULL, 'sys', 'sys');
INSERT INTO `ws_flink_artifact` (`id`, `project_id`, `type`, `name`, `remark`, `creator`, `editor`)
VALUES (7, 1, '0', 'select-example', NULL, 'sys', 'sys');
INSERT INTO `ws_flink_artifact` (`id`, `project_id`, `type`, `name`, `remark`, `creator`, `editor`)
VALUES (8, 1, '0', 'jdbc&paimon-example', 'jdbc 和 paimon catalog example', 'sys', 'sys');
INSERT INTO `ws_flink_artifact` (`id`, `project_id`, `type`, `name`, `remark`, `creator`, `editor`)
VALUES (9, 1, '0', 'sakura-example', 'sakura catalog example', 'sys', 'sys');

drop table if exists ws_flink_artifact_jar;
create table ws_flink_artifact_jar
(
    id                bigint       not null auto_increment comment '自增主键',
    flink_artifact_id bigint       not null comment '作业artifact id',
    flink_version     varchar(32)  not null comment 'flink版本',
    entry_class       varchar(255) not null comment 'main class',
    file_name         varchar(255) not null comment '文件名称',
    path              varchar(255) not null comment '文件路径',
    jar_params        text comment 'jar 运行参数',
    current           varchar(16)  not null comment 'current artifact',
    creator           varchar(32) comment '创建人',
    create_time       timestamp default current_timestamp comment '创建时间',
    editor            varchar(32) comment '修改人',
    update_time       timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    key idx_flink_artifact (flink_artifact_id)
) engine = innodb comment = 'flink artifact jar';

DROP TABLE IF EXISTS ws_flink_artifact_sql;
CREATE TABLE ws_flink_artifact_sql
(
    id                bigint      not null auto_increment,
    flink_artifact_id bigint      not null comment '作业artifact id',
    flink_version     varchar(32) not null comment 'flink版本',
    script            mediumtext comment 'sql script',
    current           varchar(16) not null comment 'current artifact',
    creator           varchar(32),
    create_time       datetime    not null default current_timestamp,
    editor            varchar(32),
    update_time       datetime    not null default current_timestamp on update current_timestamp,
    PRIMARY KEY (id),
    key idx_flink_artifact (flink_artifact_id)
) ENGINE = INNODB COMMENT = 'flink artifact sql';

INSERT INTO `ws_flink_artifact_sql` (`id`, `flink_artifact_id`, `flink_version`, `script`, `current`, `creator`,
                                     `editor`)
VALUES (1, 1, '1.17.1',
        'CREATE TEMPORARY TABLE source_table (\n  `id` bigint,\n  `name` string,\n  `age` int,\n  `address` string,\n  `create_time`TIMESTAMP(3),\n  `update_time`TIMESTAMP(3),\n  WATERMARK FOR `update_time` AS update_time - INTERVAL \'1\' MINUTE\n)\nCOMMENT \'\'\nWITH (\n  \'connector\' = \'datagen\',\n  \'number-of-rows\' = \'100\'\n);\n\nCREATE TEMPORARY TABLE `sink_table` (\n  `id` BIGINT,\n  `name` VARCHAR(2147483647),\n  `age` INT,\n  `address` VARCHAR(2147483647),\n  `create_time` TIMESTAMP(3),\n  `update_time` TIMESTAMP(3)\n)\nCOMMENT \'\'\nWITH (\n  \'connector\' = \'print\'\n);\n\ninsert into sink_table\nselect id, name, age, address, create_time, update_time from source_table;',
        '1', 'sys', 'sys');
INSERT INTO `ws_flink_artifact_sql` (`id`, `flink_artifact_id`, `flink_version`, `script`, `current`, `creator`,
                                     `editor`)
VALUES (2, 2, '1.17.1',
        'CREATE TABLE orders (\n  order_number BIGINT,\n  price        DECIMAL(32,2),\n  buyer        ROW<first_name STRING, last_name STRING>,\n  order_time   TIMESTAMP(3)\n) WITH (\n  \'connector\' = \'datagen\'\n);\n\nCREATE TABLE print_table WITH (\'connector\' = \'print\')\n  LIKE orders;\n\nINSERT INTO print_table SELECT * FROM orders;',
        '1', 'sys', 'sys');
INSERT INTO `ws_flink_artifact_sql` (`id`, `flink_artifact_id`, `flink_version`, `script`, `current`, `creator`,
                                     `editor`)
VALUES (3, 3, '1.17.1',
        'CREATE TABLE orders (\n  order_number BIGINT,\n  price        DECIMAL(32,2),\n  buyer        ROW<first_name STRING, last_name STRING>,\n  order_time   TIMESTAMP(3)\n) WITH (\n  \'connector\' = \'datagen\'\n);\n\nCREATE TABLE print_table WITH (\'connector\' = \'print\')\n    LIKE orders;\nCREATE TABLE blackhole_table WITH (\'connector\' = \'blackhole\')\n    LIKE orders;\n\nEXECUTE STATEMENT SET\nBEGIN\nINSERT INTO print_table SELECT * FROM orders;\nINSERT INTO blackhole_table SELECT * FROM orders;\nEND;',
        '1', 'sys', 'sys');
INSERT INTO `ws_flink_artifact_sql`(`id`, `flink_artifact_id`, `flink_version`, `script`, `current`, `creator`,
                                    `editor`)
VALUES (4, 6, '1.17.1',
        'CREATE CATALOG my_catalog WITH (\n    \'type\' = \'generic_in_memory\'\n);\n\nCREATE DATABASE my_catalog.my_database;\n\n\nCREATE TABLE my_catalog.my_database.source_table (\n  `id` bigint,\n  `name` string,\n  `age` int,\n  `address` string,\n  `create_time`TIMESTAMP(3),\n  `update_time`TIMESTAMP(3),\n  WATERMARK FOR `update_time` AS update_time - INTERVAL \'1\' MINUTE\n)\nCOMMENT \'\'\nWITH (\n  \'connector\' = \'datagen\',\n  \'number-of-rows\' = \'100\'\n);\n\nCREATE TABLE my_catalog.my_database.sink_table (\n  `id` BIGINT,\n  `name` VARCHAR(2147483647),\n  `age` INT,\n  `address` VARCHAR(2147483647),\n  `create_time` TIMESTAMP(3),\n  `update_time` TIMESTAMP(3)\n)\nCOMMENT \'\'\nWITH (\n  \'connector\' = \'print\'\n);\n\nINSERT INTO my_catalog.my_database.sink_table\nSELECT id, name, age, address, create_time, update_time FROM my_catalog.my_database.source_table;',
        '1', 'sys', 'sys');
INSERT INTO `ws_flink_artifact_sql` (`id`, `flink_artifact_id`, `flink_version`, `script`, `current`, `creator`,
                                     `editor`)
VALUES (5, 7, '1.17.1',
        'CREATE CATALOG my_catalog WITH (\n    \'type\' = \'generic_in_memory\'\n);\n\nCREATE DATABASE my_catalog.my_database;\n\n\nCREATE TABLE my_catalog.my_database.source_table (\n  `id` bigint,\n  `name` string,\n  `age` int,\n  `address` string,\n  `money` decimal(64, 4),\n  `create_time`TIMESTAMP(3),\n  `update_time`TIMESTAMP(3),\n  WATERMARK FOR `update_time` AS update_time - INTERVAL \'1\' MINUTE\n)\nCOMMENT \'\'\nWITH (\n  \'connector\' = \'datagen\',\n  \'number-of-rows\' = \'100000\'\n);\n\nSELECT * FROM my_catalog.my_database.source_table;',
        '1', 'sys', 'sys');

INSERT INTO `ws_flink_artifact_sql` (`id`, `flink_artifact_id`, `flink_version`, `script`, `current`, `creator`,
                                     `editor`)
VALUES (6, 8, '1.17.1',
        'CREATE CATALOG jdbc_scaleph WITH(\n  \'type\' = \'jdbc\',\n  \'base-url\' = \'jdbc:mysql://localhost:3306\',\n  \'username\' = \'root\',\n  \'password\' = \'123456\',\n  \'default-database\' = \'data_service\'\n);\n\nCREATE CATALOG paimon_minio WITH (\n    \'type\'=\'paimon\',\n    \'warehouse\'=\'s3a://scaleph/paimon\',\n    \'s3.endpoint\' = \'http://localhost:9000\',\n    \'s3.access-key\' = \'admin\',\n    \'s3.secret-key\' = \'password\',\n    \'s3.path.style.access\' = \'true\'\n);\n\nCREATE DATABASE paimon_minio.jdbc_data_service;\n\nCREATE TABLE paimon_minio.jdbc_data_service.sample_data_e_commerce (\n  `id` BIGINT COMMENT \'自增主键\',\n  `invoice_no` STRING COMMENT \'发票号码，每笔交易分配唯一的6位整数，而退货订单的代码以字母\'\'c\'\'开头\',\n  `stock_code` STRING COMMENT \'产品代码，每个不同的产品分配唯一的5位整数\',\n  `description` STRING COMMENT \'产品描述，对每件产品的简略描述\',\n  `quantity` INT COMMENT \'产品数量，每笔交易的每件产品的数量\',\n  `invoice_date` STRING COMMENT \'发票日期和时间，每笔交易发生的日期和时间\',\n  `unit_price` DECIMAL(20, 2) COMMENT \'单价（英镑），单位产品价格\',\n  `customer_id`STRING COMMENT \'顾客号码，每个客户分配唯一的5位整数\',\n  `country`STRING COMMENT \'国家的名字，每个客户所在国家/地区的名称\',\n  PRIMARY KEY(`invoice_no`) NOT ENFORCED\n)\nCOMMENT \'E-Commerce 数据集\'\n;\n\nINSERT INTO paimon_minio.jdbc_data_service.sample_data_e_commerce\nSELECT * FROM jdbc_scaleph.data_service.sample_data_e_commerce;',
        '1', 'sys', 'sys');
INSERT INTO `ws_flink_artifact_sql` (`id`, `flink_artifact_id`, `flink_version`, `script`, `current`, `creator`,
                                     `editor`)
VALUES (7, 9, '1.17.1',
        'CREATE CATALOG sakura WITH(\n  \'type\' = \'sakura\',\n  \'jdbcUrl\' = \'jdbc:mysql://localhost:3306/sakura\',\n  \'username\' = \'root\',\n  \'password\' = \'123456\',\n  \'driver\' = \'com.mysql.cj.jdbc.Driver\'\n);\n\nCREATE DATABASE sakura.dev;\n\nCREATE TABLE sakura.dev.orders (\n  order_number BIGINT,\n  price        DECIMAL(32,2),\n  buyer        ROW<first_name STRING, last_name STRING>,\n  order_time   TIMESTAMP(3)\n) WITH (\n  \'connector\' = \'datagen\'\n);\n\nCREATE TABLE sakura.dev.print_table WITH (\'connector\' = \'print\')\n  LIKE sakura.dev.orders;\n\nINSERT INTO sakura.dev.print_table \nSELECT * FROM sakura.dev.orders;',
        '1', 'sysc', 'sys');

drop table if exists ws_di_job;
create table ws_di_job
(
    id                bigint      not null auto_increment comment '自增主键',
    flink_artifact_id bigint      not null comment '作业artifact id',
    job_engine        varchar(16) not null comment '作业引擎',
    job_id            varchar(64) not null,
    current           varchar(16) not null comment 'current artifact',
    creator           varchar(32) comment '创建人',
    create_time       timestamp default current_timestamp comment '创建时间',
    editor            varchar(32) comment '修改人',
    update_time       timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    key idx_flink_artifact (flink_artifact_id)
) engine = innodb comment '数据集成-作业信息';
INSERT INTO ws_di_job (id, flink_artifact_id, job_engine, job_id, current, creator, editor)
VALUES (1, 4, 'seatunnel', 'b8e16c94-258c-4487-a88c-8aad40a38b35', 1, 'sys', 'sys');
INSERT INTO ws_di_job(id, flink_artifact_id, job_engine, job_id, current, creator, editor)
VALUES (2, 5, 'seatunnel', '0a6d475e-ed50-46ee-82af-3ef90b7d8509', 1, 'sys', 'sys');

drop table if exists ws_di_job_attr;
create table ws_di_job_attr
(
    id             bigint       not null auto_increment comment '自增主键',
    job_id         bigint       not null comment '作业id',
    job_attr_type  varchar(4)   not null comment '作业参数类型',
    job_attr_key   varchar(128) not null comment '作业参数key',
    job_attr_value varchar(512) comment '作业参数value',
    creator        varchar(32) comment '创建人',
    create_time    timestamp default current_timestamp comment '创建时间',
    editor         varchar(32) comment '修改人',
    update_time    timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (job_id, job_attr_type, job_attr_key)
) engine = innodb comment '数据集成-作业参数';

/* 作业步骤信息 包含source，transform，sink 等*/
drop table if exists ws_di_job_step;
create table ws_di_job_step
(
    id          bigint       not null auto_increment comment '自增主键',
    job_id      bigint       not null comment '作业id',
    step_code   varchar(36)  not null comment '步骤编码',
    step_title  varchar(128) not null comment '步骤标题',
    step_type   varchar(12)  not null comment '步骤类型',
    step_name   varchar(128) not null comment '步骤名称',
    position_x  int          not null comment 'x坐标',
    position_y  int          not null comment 'y坐标',
    step_attrs  mediumtext comment '作业步骤属性',
    creator     varchar(32) comment '创建人',
    create_time timestamp default current_timestamp comment '创建时间',
    editor      varchar(32) comment '修改人',
    update_time timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (job_id, step_code)
) engine = innodb comment '数据集成-作业步骤信息';
INSERT INTO ws_di_job_step (job_id, step_code, step_title, step_type, step_name, position_x, position_y,
                            step_attrs, creator, editor)
VALUES (1, 'f3e02087-91fa-494d-86f4-694970a49ebd', 'Jdbc Source', 'source', 'Jdbc', -400, -320,
        '{\"stepTitle\":\"Jdbc Source\",\"dataSourceType\":\"MySQL\",\"dataSource\":2,\"query\":\"select * from sample_data_e_commerce\"}',
        'sys', 'sys');
INSERT INTO ws_di_job_step (job_id, step_code, step_title, step_type, step_name, position_x, position_y,
                            step_attrs, creator, editor)
VALUES (1, '68834928-2a32-427a-a864-83b6b5848e04', 'Jdbc Sink', 'sink', 'Jdbc', -310, -120,
        '{\"stepTitle\":\"Jdbc Sink\",\"dataSourceType\":\"MySQL\",\"dataSource\":2,\"batch_size\":300,\"batch_interval_ms\":1000,\"max_retries\":3,\"is_exactly_once\":false,\"query\":\"insert into sample_data_e_commerce_duplicate ( id, invoice_no, stock_code, description, quantity, invoice_date, unit_price, customer_id, country )\\nvalues (?,?,?,?,?,?,?,?,?)\"}',
        'sys', 'sys');
INSERT INTO ws_di_job_step(job_id, step_code, step_title, step_type, step_name, position_x, position_y,
                           step_attrs, creator, editor)
VALUES (2, '6223c6c3-b552-4c69-adab-5300b7514fad', 'Fake Source', 'source', 'FakeSource', -400, -320,
        '{\"stepTitle\":\"Fake Source\",\"fields\":[{\"field\":\"c_string\",\"type\":\"string\"},{\"field\":\"c_boolean\",\"type\":\"boolean\"},{\"field\":\"c_tinyint\",\"type\":\"tinyint\"},{\"field\":\"c_smallint\",\"type\":\"smallint\"},{\"field\":\"c_int\",\"type\":\"int\"},{\"field\":\"c_bigint\",\"type\":\"bigint\"},{\"field\":\"c_float\",\"type\":\"float\"},{\"field\":\"c_double\",\"type\":\"double\"},{\"field\":\"c_decimal\",\"type\":\"decimal(30, 8)\"},{\"field\":\"c_bytes\",\"type\":\"bytes\"},{\"field\":\"c_map\",\"type\":\"map<string, string>\"},{\"field\":\"c_date\",\"type\":\"date\"},{\"field\":\"c_time\",\"type\":\"time\"},{\"field\":\"c_timestamp\",\"type\":\"timestamp\"}],\"schema\":\"{\\\"fields\\\":{\\\"c_string\\\":\\\"string\\\",\\\"c_boolean\\\":\\\"boolean\\\",\\\"c_tinyint\\\":\\\"tinyint\\\",\\\"c_smallint\\\":\\\"smallint\\\",\\\"c_int\\\":\\\"int\\\",\\\"c_bigint\\\":\\\"bigint\\\",\\\"c_float\\\":\\\"float\\\",\\\"c_double\\\":\\\"double\\\",\\\"c_decimal\\\":\\\"decimal(30, 8)\\\",\\\"c_bytes\\\":\\\"bytes\\\",\\\"c_map\\\":\\\"map<string, string>\\\",\\\"c_date\\\":\\\"date\\\",\\\"c_time\\\":\\\"time\\\",\\\"c_timestamp\\\":\\\"timestamp\\\"}}\"}',
        'sys', 'sys');
INSERT INTO ws_di_job_step(job_id, step_code, step_title, step_type, step_name, position_x, position_y,
                           step_attrs, creator, editor)
VALUES (2, 'f08143b4-34dc-4190-8723-e8d8ce49738f', 'Console Sink', 'sink', 'Console', -320, -120,
        '{\"stepTitle\":\"Console Sink\"}', 'sys', 'sys');

/* 作业连线信息 */
drop table if exists ws_di_job_link;
create table ws_di_job_link
(
    id             bigint      not null auto_increment comment '自增主键',
    job_id         bigint      not null comment '作业id',
    link_code      varchar(36) not null comment '作业连线编码',
    from_step_code varchar(36) not null comment '源步骤编码',
    to_step_code   varchar(36) not null comment '目标步骤编码',
    creator        varchar(32) comment '创建人',
    create_time    timestamp default current_timestamp comment '创建时间',
    editor         varchar(32) comment '修改人',
    update_time    timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    key (job_id)
) engine = innodb comment '数据集成-作业连线';
INSERT INTO ws_di_job_link (job_id, link_code, from_step_code, to_step_code, creator, editor)
VALUES (1, 'fabfda41-aacb-4a19-b5ef-9e84a75ed4e9', 'f3e02087-91fa-494d-86f4-694970a49ebd',
        '68834928-2a32-427a-a864-83b6b5848e04', 'sys', 'sys');
INSERT INTO ws_di_job_link(job_id, link_code, from_step_code, to_step_code, creator, editor)
VALUES (2, 'd57021a1-65c7-4dfe-ae89-3b73d00fcf72', '6223c6c3-b552-4c69-adab-5300b7514fad',
        'f08143b4-34dc-4190-8723-e8d8ce49738f', 'sys', 'sys');

DROP TABLE IF EXISTS ws_flink_kubernetes_template;
CREATE TABLE ws_flink_kubernetes_template
(
    id                      bigint       not null auto_increment,
    project_id              bigint       not null comment '项目id',
    `name`                  varchar(64)  not null,
    template_id             varchar(64)  not null,
    deployment_kind         varchar(16)  not null,
    namespace               varchar(255) not null,
    kubernetes_options      varchar(255),
    job_manager             text,
    task_manager            text,
    pod_template            text,
    flink_configuration     text,
    log_configuration       text,
    ingress                 text,
    additional_dependencies text,
    remark                  varchar(255),
    creator                 varchar(32),
    create_time             datetime     not null default current_timestamp,
    editor                  varchar(32),
    update_time             datetime     not null default current_timestamp on update current_timestamp,
    PRIMARY KEY (id),
    UNIQUE KEY uniq_name (project_id, `name`)
) ENGINE = INNODB COMMENT = 'flink kubernetes deployment template';

INSERT INTO `ws_flink_kubernetes_template`(`id`, `project_id`, `name`, `template_id`, `deployment_kind`,
                                           `namespace`, `kubernetes_options`, `job_manager`, `task_manager`,
                                           `pod_template`, `flink_configuration`,
                                           `log_configuration`, `ingress`, additional_dependencies, `remark`, `creator`,
                                           `editor`)
VALUES (1, 1, 'simple-sessioin-cluster', '19b77b47-b9e4-418c-90a1-533ea6121c16', 'FlinkSessionJob', 'default',
        '{"image":"flink:1.17","imagePullPolicy":"IfNotPresent","flinkVersion":"v1_17","serviceAccount":"flink"}',
        '{"resource":{"cpu":1.0,"memory":"1G"},"replicas":1}',
        '{"resource":{"cpu":1.0,"memory":"1G"},"replicas":1}',
        NULL,
        '{"web.cancel.enable":"false","taskmanager.numberOfTaskSlots":"8","kubernetes.rest-service.exposed.type":"LoadBalancer"}',
        NULL, NULL, NULL, NULL, 'sys', 'sys');

INSERT INTO `ws_flink_kubernetes_template`(`id`, `project_id`, `name`, `template_id`, `deployment_kind`,
                                           `namespace`, `kubernetes_options`, `job_manager`, `task_manager`,
                                           `pod_template`, `flink_configuration`,
                                           `log_configuration`, `ingress`, additional_dependencies, `remark`, `creator`,
                                           `editor`)
VALUES (2, 1, 'simple-jar-deployment', 'b4dc61d0-ad0e-4e39-b1a4-f0692122635f', 'FlinkDeployment', 'default',
        '{"image":"flink:1.17","imagePullPolicy":"IfNotPresent","flinkVersion":"v1_17","serviceAccount":"flink"}',
        '{"resource":{"cpu":1.0,"memory":"1G"},"replicas":1}',
        '{"resource":{"cpu":1.0,"memory":"1G"},"replicas":1}',
        NULL,
        '{"web.cancel.enable":"false","taskmanager.numberOfTaskSlots":"8","kubernetes.rest-service.exposed.type":"NodePort","kubernetes.rest-service.exposed.node-port-address-type":"ExternalIP"}',
        NULL, NULL, NULL, NULL, 'sys', 'sys');

INSERT INTO `ws_flink_kubernetes_template`(`id`, `project_id`, `name`, `template_id`, `deployment_kind`,
                                           `namespace`, `kubernetes_options`, `job_manager`, `task_manager`,
                                           `pod_template`, `flink_configuration`,
                                           `log_configuration`, `ingress`, additional_dependencies, `remark`, `creator`,
                                           `editor`)
VALUES (3, 1, 'simple-sql-deployment', 'bceec5d5-6271-4079-b4d1-9936ab9fe9ca', 'FlinkDeployment', 'default',
        '{"image":"ghcr.io/flowerfine/scaleph-sql-template:1.17","imagePullPolicy":"IfNotPresent","flinkVersion":"v1_17","serviceAccount":"flink"}',
        '{"resource":{"cpu":1.0,"memory":"1G"},"replicas":1}',
        '{"resource":{"cpu":1.0,"memory":"1G"},"replicas":1}',
        NULL,
        '{"web.cancel.enable":"false","taskmanager.numberOfTaskSlots":"8","kubernetes.rest-service.exposed.type":"NodePort","kubernetes.rest-service.exposed.node-port-address-type":"ExternalIP"}',
        NULL, NULL, NULL, NULL, 'sys', 'sys');


INSERT INTO `ws_flink_kubernetes_template`(`id`, `project_id`, `name`, `template_id`, `deployment_kind`,
                                           `namespace`, `kubernetes_options`, `job_manager`, `task_manager`,
                                           `pod_template`, `flink_configuration`,
                                           `log_configuration`, `ingress`, additional_dependencies, `remark`, `creator`,
                                           `editor`)
VALUES (4, 1, 'simple-seatunnel-deployment', '35e4a532-3c7b-4273-8cdb-edbef2cb9e49', 'FlinkDeployment', 'default',
        '{"image":"ghcr.io/flowerfine/scaleph-seatunnel:2.3.1-flink-1.15","imagePullPolicy":"IfNotPresent","flinkVersion":"v1_16","serviceAccount":"flink"}',
        '{"resource":{"cpu":1.0,"memory":"1G"},"replicas":1}',
        '{"resource":{"cpu":1.0,"memory":"1G"},"replicas":1}',
        NULL,
        '{"web.cancel.enable":"false","taskmanager.numberOfTaskSlots":"8","kubernetes.rest-service.exposed.type":"NodePort","kubernetes.rest-service.exposed.node-port-address-type":"ExternalIP"}',
        NULL, NULL, NULL, NULL, 'sys', 'sys');

INSERT INTO `ws_flink_kubernetes_template`(`id`, `project_id`, `name`, `template_id`, `deployment_kind`,
                                           `namespace`, `kubernetes_options`, `job_manager`, `task_manager`,
                                           `pod_template`, `flink_configuration`,
                                           `log_configuration`, `ingress`, additional_dependencies, `remark`, `creator`,
                                           `editor`)
VALUES (5, 1, 'deployment', '3f0c6600-b6d7-4e2c-b2e5-4a0b3cdb3cbb', 'FlinkDeployment', 'default',
        '{"image":"flink:1.17","imagePullPolicy":"IfNotPresent","flinkVersion":"v1_17","serviceAccount":"flink"}',
        '{"resource":{"cpu":1.0,"memory":"1G"},"replicas":1}',
        '{"resource":{"cpu":1.0,"memory":"1G"},"replicas":1}',
        '{"apiVersion":"v1","kind":"Pod","spec":{"containers":[{"name":"flink-main-container","volumeMounts":[{"mountPath":"/flink-data","name":"flink-volume"}]}],"volumes":[{"emptyDir":{"sizeLimit":"500Mi"},"name":"flink-volume"}]}}',
        '{"kubernetes.operator.savepoint.history.max.count":"10","execution.checkpointing.mode":"exactly_once","state.checkpoints.num-retained":"10","restart-strategy.failure-rate.delay":"10s","restart-strategy.failure-rate.max-failures-per-interval":"30","kubernetes.operator.savepoint.format.type":"NATIVE","web.cancel.enable":"false","kubernetes.operator.cluster.health-check.enabled":"true","execution.checkpointing.interval":"180s","execution.checkpointing.timeout":"10min","kubernetes.operator.savepoint.history.max.age":"72h","execution.checkpointing.externalized-checkpoint-retention":"RETAIN_ON_CANCELLATION","kubernetes.operator.cluster.health-check.restarts.threshold":"30","restart-strategy":"failurerate","restart-strategy.failure-rate.failure-rate-interval":"10min","execution.checkpointing.min-pause":"180s","kubernetes.operator.cluster.health-check.restarts.window":"10min","execution.checkpointing.max-concurrent-checkpoints":"1","kubernetes.operator.periodic.savepoint.interval":"1h","kubernetes.operator.savepoint.trigger.grace-period":"20min","execution.checkpointing.alignment-timeout":"120s","kubernetes.rest-service.exposed.type":"LoadBalancer","state.savepoints.dir":"file:///flink-data/savepoints","state.checkpoints.dir":"file:///flink-data/checkpoints"}',
        NULL, NULL, NULL, NULL, 'sys', 'sys');

INSERT INTO `ws_flink_kubernetes_template`(`id`, `project_id`, `name`, `template_id`, `deployment_kind`,
                                           `namespace`, `kubernetes_options`, `job_manager`, `task_manager`,
                                           `pod_template`, `flink_configuration`,
                                           `log_configuration`, `ingress`, additional_dependencies, `remark`, `creator`,
                                           `editor`)
VALUES (6, 1, 'session-cluster', '8b330683-05ec-4c29-b991-df35b2036e2d', 'FlinkSessionJob', 'default',
        '{"image":"flink:1.17","imagePullPolicy":"IfNotPresent","flinkVersion":"v1_17","serviceAccount":"flink"}',
        '{"resource":{"cpu":1.0,"memory":"1G"},"replicas":1}',
        '{"resource":{"cpu":1.0,"memory":"1G"},"replicas":1}',
        '{"apiVersion":"v1","kind":"Pod","spec":{"containers":[{"name":"flink-main-container","volumeMounts":[{"mountPath":"/flink-data","name":"flink-volume"}]}],"volumes":[{"emptyDir":{"sizeLimit":"500Mi"},"name":"flink-volume"}]}}',
        '{"kubernetes.operator.savepoint.history.max.count":"10","execution.checkpointing.mode":"exactly_once","state.checkpoints.num-retained":"10","restart-strategy.failure-rate.delay":"10s","restart-strategy.failure-rate.max-failures-per-interval":"30","kubernetes.operator.savepoint.format.type":"NATIVE","web.cancel.enable":"false","kubernetes.operator.cluster.health-check.enabled":"true","execution.checkpointing.interval":"180s","execution.checkpointing.timeout":"10min","kubernetes.operator.savepoint.history.max.age":"72h","execution.checkpointing.externalized-checkpoint-retention":"RETAIN_ON_CANCELLATION","kubernetes.operator.cluster.health-check.restarts.threshold":"30","restart-strategy":"failurerate","restart-strategy.failure-rate.failure-rate-interval":"10min","execution.checkpointing.min-pause":"180s","kubernetes.operator.cluster.health-check.restarts.window":"10min","execution.checkpointing.max-concurrent-checkpoints":"1","kubernetes.operator.periodic.savepoint.interval":"1h","kubernetes.operator.savepoint.trigger.grace-period":"20min","execution.checkpointing.alignment-timeout":"120s","kubernetes.rest-service.exposed.type":"LoadBalancer","state.savepoints.dir":"file:///flink-data/savepoints","state.checkpoints.dir":"file:///flink-data/checkpoints"}',
        NULL, NULL, NULL, NULL, 'sys', 'sys');

DROP TABLE IF EXISTS ws_flink_kubernetes_deployment;
CREATE TABLE ws_flink_kubernetes_deployment
(
    id                      bigint       not null auto_increment,
    project_id              bigint       not null comment '项目id',
    cluster_credential_id   bigint       not null,
    `name`                  varchar(255) not null,
    deployment_id           varchar(64)  not null,
    namespace               varchar(255) not null,
    kubernetes_options      varchar(255),
    job_manager             text,
    task_manager            text,
    pod_template            text,
    flink_configuration     text,
    log_configuration       text,
    ingress                 text,
    additional_dependencies text,
    remark                  varchar(255),
    creator                 varchar(32),
    create_time             datetime     not null default current_timestamp,
    editor                  varchar(32),
    update_time             datetime     not null default current_timestamp on update current_timestamp,
    PRIMARY KEY (id),
    UNIQUE KEY uniq_name (project_id, `name`, cluster_credential_id)
) ENGINE = INNODB COMMENT = 'flink kubernetes deployment';

DROP TABLE IF EXISTS ws_flink_kubernetes_session_cluster;
CREATE TABLE ws_flink_kubernetes_session_cluster
(
    id                      bigint       not null auto_increment,
    project_id              bigint       not null comment '项目id',
    cluster_credential_id   bigint       not null,
    `name`                  varchar(255) not null,
    session_cluster_id      varchar(64)  not null,
    namespace               varchar(255) not null,
    kubernetes_options      varchar(255),
    job_manager             text,
    task_manager            text,
    pod_template            text,
    flink_configuration     text,
    log_configuration       text,
    ingress                 text,
    additional_dependencies text,
    support_sql_gateway     varchar(16),
    state                   varchar(64),
    error                   text,
    cluster_info            text,
    task_manager_info       text,
    remark                  varchar(255),
    creator                 varchar(32),
    create_time             datetime     not null default current_timestamp,
    editor                  varchar(32),
    update_time             datetime     not null default current_timestamp on update current_timestamp,
    PRIMARY KEY (id),
    UNIQUE KEY uniq_name (project_id, `name`, cluster_credential_id)
) ENGINE = INNODB COMMENT = 'flink kubernetes session cluster';

DROP TABLE IF EXISTS ws_flink_kubernetes_job;
CREATE TABLE ws_flink_kubernetes_job
(
    id                       bigint       not null auto_increment,
    project_id               bigint       not null comment '项目id',
    `name`                   varchar(255) not null,
    job_id                   varchar(64)  not null,
    execution_mode           varchar(16)  not null,
    deployment_kind          varchar(16)  not null,
    flink_deployment_id      bigint,
    flink_session_cluster_id bigint,
    `type`                   varchar(4)   not null comment '作业artifact类型',
    flink_artifact_jar_id    bigint,
    flink_artifact_sql_id    bigint,
    ws_di_job_id             bigint,
    additional_dependencies  text,
    remark                   varchar(255),
    creator                  varchar(32),
    create_time              datetime     not null default current_timestamp,
    editor                   varchar(32),
    update_time              datetime     not null default current_timestamp on update current_timestamp,
    PRIMARY KEY (id),
    UNIQUE KEY uniq_name (project_id, `name`)
) ENGINE = INNODB COMMENT = 'flink kubernetes job';

DROP TABLE IF EXISTS ws_flink_kubernetes_job_instance;
CREATE TABLE ws_flink_kubernetes_job_instance
(
    id                         bigint      not null auto_increment,
    ws_flink_kubernetes_job_id bigint      not null,
    instance_id                varchar(64) not null,
    parallelism                int         not null default 1,
    upgrade_mode               varchar(32),
    allow_non_restored_state   boolean,
    job_manager                text,
    task_manager               text,
    user_flink_configuration   text,
    state                      varchar(64),
    job_state                  varchar(64),
    error                      text,
    cluster_info               text,
    task_manager_info          text,
    start_time                 datetime comment '开始时间',
    end_time                   datetime comment '结束时间',
    duration                   bigint comment '耗时',
    creator                    varchar(32),
    create_time                datetime    not null default current_timestamp,
    editor                     varchar(32),
    update_time                datetime    not null default current_timestamp on update current_timestamp,
    PRIMARY KEY (id),
    UNIQUE KEY uniq_key (ws_flink_kubernetes_job_id, instance_id)
) ENGINE = INNODB COMMENT = 'flink kubernetes job instance';

DROP TABLE IF EXISTS ws_flink_kubernetes_job_instance_savepoint;
CREATE TABLE ws_flink_kubernetes_job_instance_savepoint
(
    id                                  bigint   not null auto_increment,
    ws_flink_kubernetes_job_instance_id bigint   not null,
    time_stamp                          bigint   not null,
    location                            text,
    trigger_type                        varchar(32),
    format_type                         varchar(32),
    creator                             varchar(32),
    create_time                         datetime not null default current_timestamp,
    editor                              varchar(32),
    update_time                         datetime not null default current_timestamp on update current_timestamp,
    PRIMARY KEY (id),
    UNIQUE KEY uniq_key (ws_flink_kubernetes_job_instance_id, time_stamp)
) ENGINE = INNODB COMMENT = 'flink kubernetes job instance savepoint';