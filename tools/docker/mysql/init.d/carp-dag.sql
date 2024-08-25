create database if not exists carp default character set utf8mb4 collate utf8mb4_unicode_ci;
use carp;

drop table if exists carp_dag_config;
create table carp_dag_config
(
    id             bigint      not null auto_increment comment '自增主键',
    type           varchar(16) not null comment 'DAG 类型',
    name           varchar(128) comment 'DAG名称',
    uuid           varchar(64) not null comment 'DAG ID',
    dag_meta       varchar(128) comment 'DAG元信息',
    dag_attrs      text comment 'DAG属性',
    intput_options text comment '输入参数声明',
    output_options text comment '输出参数声明',
    version        int         not null default 0 comment '版本号',
    remark         varchar(255) comment 'remark',
    creator        varchar(32) comment 'creator',
    create_time    datetime    not null default current_timestamp comment 'create time',
    editor         varchar(32) comment 'editor',
    update_time    datetime    not null default current_timestamp on update current_timestamp comment 'update time',
    primary key (id)
) engine = innodb comment 'dag config';

INSERT INTO `carp_dag_config` (`id`, `type`, `name`, `uuid`, `dag_meta`, `dag_attrs`, `intput_options`,
                               `output_options`, `version`, `remark`, `creator`, `editor`)
VALUES (1, 'SeaTunnel', 'e_commerce', 'bsag8e409f8c81a64edc8f0e1b27d6a010cb', NULL, NULL, NULL, NULL, 0, NULL, 'sys',
        'sys');
INSERT INTO `carp_dag_config` (`id`, `type`, `name`, `uuid`, `dag_meta`, `dag_attrs`, `intput_options`,
                               `output_options`, `version`, `remark`, `creator`, `editor`)
VALUES (2, 'SeaTunnel', 'fake', 'ewykdb10bd1e437346369e027a437473d483', NULL, NULL, NULL, NULL, 0, NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config`(`id`, `type`, `name`, `uuid`, `dag_meta`, `dag_attrs`, `intput_options`, `output_options`,
                              `version`, `remark`, `creator`, `editor`)
VALUES (3, 'Flink-CDC', 'flink-cdc-example', 'nlly3ab39bb296a34c5888dd6509ffe588e4', NULL, NULL, NULL, NULL, 0, NULL,
        'sys', 'sys');
INSERT INTO `carp_dag_config`(`id`, `type`, `name`, `uuid`, `dag_meta`, `dag_attrs`, `intput_options`, `output_options`,
                              `version`, `remark`, `creator`, `editor`)
VALUES (4, 'WorkFlow', 'FlinkSessionClusterStatusSyncJob', 'rnsp52fdd5edd77044a9acc0c2f24c42d760', NULL, NULL, NULL,
        NULL, 0, NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config`(`id`, `type`, `name`, `uuid`, `dag_meta`, `dag_attrs`, `intput_options`, `output_options`,
                              `version`, `remark`, `creator`, `editor`)
VALUES (5, 'WorkFlow', 'FlinkJobStatusSyncJob', 'kvqfebc60efa8def410ebfe30f70fd8f1768', NULL, NULL, NULL, NULL, 0, NULL,
        'sys', 'sys');
INSERT INTO `carp_dag_config`(`id`, `type`, `name`, `uuid`, `dag_meta`, `dag_attrs`, `intput_options`, `output_options`,
                              `version`, `remark`, `creator`, `editor`)
VALUES (6, 'WorkFlow', 'DorisOperatorInstanceStatusSyncJob', 'kepa00f4fdb5e8794cbb931067244caf5ef2', NULL, NULL, NULL,
        NULL, 0, NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config`(`id`, `type`, `name`, `uuid`, `dag_meta`, `dag_attrs`, `intput_options`, `output_options`,
                              `version`, `remark`, `creator`, `editor`)
VALUES (7, 'WorkFlow', 'Demo', 'fssxbe099903bf174c11bf64b0d486383784', NULL, '{"foo":"bar"}', NULL, NULL, 0, NULL,
        'sys', 'sys');
INSERT INTO `carp_dag_config`(`id`, `type`, `name`, `uuid`, `dag_meta`, `dag_attrs`, `intput_options`, `output_options`,
                              `version`, `remark`, `creator`, `editor`)
VALUES (8, 'SeaTunnel', 'mysql_binlog_kafka_es', 'zzbk202837c4529d47d2ab09fa7ccf84fd81', NULL, NULL, NULL, NULL, 0,
        NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config`(`id`, `type`, `name`, `uuid`, `dag_meta`, `dag_attrs`, `intput_options`, `output_options`,
                              `version`, `remark`, `creator`, `editor`)
VALUES (9, 'SeaTunnel', 'mysql_binlog_cdc_kafka', 'vbhp505d7884833344fbac6111a9923a1f28', NULL, NULL, NULL, NULL, 0,
        NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config`(`id`, `type`, `name`, `uuid`, `dag_meta`, `dag_attrs`, `intput_options`, `output_options`,
                              `version`, `remark`, `creator`, `editor`)
VALUES (10, 'SeaTunnel', 'mysql_binlog_cdc_paimon', 'upyq0705acb88edf430f85dba099a08ff31e', NULL, NULL, NULL, NULL, 0,
        NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config`(`id`, `type`, `name`, `uuid`, `dag_meta`, `dag_attrs`, `intput_options`, `output_options`,
                              `version`, `remark`, `creator`, `editor`)
VALUES (11, 'SeaTunnel', 'mysql_binlog_cdc_kafka_doris', 'zvux4b76b0255a1b4a4f946821d2c0c9d77c', NULL, NULL, NULL, NULL,
        0, NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config`(`id`, `type`, `name`, `uuid`, `dag_meta`, `dag_attrs`, `intput_options`, `output_options`,
                              `version`, `remark`, `creator`, `editor`)
VALUES (12, 'SeaTunnel', 'mysql_binlog_cdc_iceberg', 'qygebf7fdfc91e5f49ceadea025b0d6139a4', NULL, NULL, NULL, NULL, 0,
        NULL, 'sys', 'sys');

drop table if exists carp_dag_config_history;
create table carp_dag_config_history
(
    id             bigint      not null auto_increment comment '自增主键',
    dag_config_id  bigint      not null comment 'DAG 配置ID',
    type           varchar(16) not null comment 'DAG 类型',
    name           varchar(128) comment 'DAG名称',
    uuid           varchar(64) not null comment 'DAG ID',
    dag_meta       varchar(128) comment 'DAG元信息',
    dag_attrs      text comment 'DAG属性',
    intput_options text comment '输入参数声明',
    output_options text comment '输出参数声明',
    version        int         not null default 0 comment '版本号',
    remark         varchar(255) comment 'remark',
    creator        varchar(32) comment 'creator',
    create_time    datetime    not null default current_timestamp comment 'create time',
    editor         varchar(32) comment 'editor',
    update_time    datetime    not null default current_timestamp on update current_timestamp comment 'update time',
    primary key (id),
    key            idx_dag_config (`dag_config_id`)
) engine = innodb comment 'dag config history';

drop table if exists carp_dag_config_step;
create table carp_dag_config_step
(
    id          bigint      not null auto_increment comment '自增主键',
    dag_id      bigint      not null comment 'DAG id',
    step_id     varchar(36) not null comment '步骤id',
    step_name   varchar(128) comment '步骤名称',
    position_x  int         not null comment 'x坐标',
    position_y  int         not null comment 'y坐标',
    shape       varchar(64),
    style       text,
    step_meta   varchar(128) comment '步骤元信息',
    step_attrs  text comment '步骤属性',
    creator     varchar(32) comment 'creator',
    create_time datetime    not null default current_timestamp comment 'create time',
    editor      varchar(32) comment 'editor',
    update_time datetime    not null default current_timestamp on update current_timestamp comment 'update time',
    primary key (id),
    unique key uniq_step (dag_id, step_id)
) engine = innodb comment 'dag config step';

INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (1, 1, '157f118c-9b6c-4d18-a919-fce824676696', 'Jdbc Source', 520, 150, NULL, NULL,
        '{\"name\":\"Jdbc\",\"type\":\"source\",\"engine\":\"seatunnel\"}',
        '{\"stepTitle\":\"Jdbc Source\",\"dataSourceType\":\"MySQL\",\"dataSource\":1,\"fetch_size\":0,\"query\":\"select * from sample_data_e_commerce\"}',
        'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (2, 1, 'e69dbf5a-76ad-47be-aa16-175b733a7df2', 'Jdbc Sink', 460, 400, NULL, NULL,
        '{\"name\":\"Jdbc\",\"type\":\"sink\",\"engine\":\"seatunnel\"}',
        '{\"stepTitle\":\"Jdbc Sink\",\"dataSourceType\":\"MySQL\",\"dataSource\":1,\"generate_sink_sql\":false,\"batch_size\":300,\"max_retries\":3,\"is_exactly_once\":false,\"query\":\"insert into sample_data_e_commerce_duplicate \\n( id, invoice_no, stock_code, description, quantity, invoice_date, unit_price, customer_id, country )\\nvalues (?,?,?,?,?,?,?,?,?)\",\"primary_keys\":\"[]\",\"schema_save_mode\":\"CREATE_SCHEMA_WHEN_NOT_EXIST\",\"data_save_mode\":\"APPEND_DATA\"}',
        'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (3, 2, '6223c6c3-b552-4c69-adab-5300b7514fad', 'Fake Source', 380, 140, NULL, NULL,
        '{"name":"FakeSource","type":"source","engine":"seatunnel"}',
        '{"stepTitle":"Fake Source","fields":[{"field":"c_string","type":"string"},{"field":"c_boolean","type":"boolean"},{"field":"c_tinyint","type":"tinyint"},{"field":"c_smallint","type":"smallint"},{"field":"c_int","type":"int"},{"field":"c_bigint","type":"bigint"},{"field":"c_float","type":"float"},{"field":"c_double","type":"double"},{"field":"c_decimal","type":"decimal(30, 8)"},{"field":"c_bytes","type":"bytes"},{"field":"c_map","type":"map<string, string>"},{"field":"c_date","type":"date"},{"field":"c_time","type":"time"},{"field":"c_timestamp","type":"timestamp"}],"schema":"{\\\"fields\\\":{\\\"c_string\\\":\\\"string\\\",\\\"c_boolean\\\":\\\"boolean\\\",\\\"c_tinyint\\\":\\\"tinyint\\\",\\\"c_smallint\\\":\\\"smallint\\\",\\\"c_int\\\":\\\"int\\\",\\\"c_bigint\\\":\\\"bigint\\\",\\\"c_float\\\":\\\"float\\\",\\\"c_double\\\":\\\"double\\\",\\\"c_decimal\\\":\\\"decimal(30, 8)\\\",\\\"c_bytes\\\":\\\"bytes\\\",\\\"c_map\\\":\\\"map<string, string>\\\",\\\"c_date\\\":\\\"date\\\",\\\"c_time\\\":\\\"time\\\",\\\"c_timestamp\\\":\\\"timestamp\\\"}}"}',
        'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (4, 2, 'f08143b4-34dc-4190-8723-e8d8ce49738f', 'Console Sink', 360, 290, NULL, NULL,
        '{"name":"Console","type":"sink","engine":"seatunnel"}', '{"stepTitle":"Console Sink"}', 'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (5, 3, '1d0b15ba-12b9-4698-ab21-4c5cfe0b2e64', 'MySQL Source', 380, 140, NULL, NULL,
        '{\"name\":\"MySQL\",\"type\":\"source\"}', '{\"stepTitle\":\"MySQL Source\"}', 'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (6, 3, '640069cb-4f12-4c84-aefb-2731d4a82d78', 'Doris Sink', 360, 290, NULL, NULL,
        '{\"name\":\"Doris\",\"type\":\"sink\"}', '{\"stepTitle\":\"Doris Sink\"}', 'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (7, 4, '7f7ced76-7771-4870-91d9-435ef1c4e623', 'FlinkSessionClusterStatus', 460, 400, NULL, NULL,
        '{\"handler\":\"cn.sliew.scaleph.application.flink.action.FlinkSessionClusterStatusSyncJob\",\"type\":\"1\"}',
        NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (8, 5, '5d5d67c5-ade3-4005-a0db-d514bf11616d', 'FlinkJobStatus', 460, 400, NULL, NULL,
        '{\"handler\":\"cn.sliew.scaleph.application.flink.action.FlinkJobStatusSyncJob\",\"type\":\"1\"}', NULL,
        'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (9, 6, '8c7b171c-f232-4b96-b842-5f4fbef34bc1', 'DorisOperatorInstanceStatus', 460, 400, NULL, NULL,
        '{\"handler\":\"cn.sliew.scaleph.application.doris.action.DorisOperatorInstanceStatusSyncJob\",\"type\":\"1\"}',
        NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (10, 7, 'cae1a622-6c96-4cec-81d3-883510c17702', 'FlinkJobStatus-1', 460, 400, NULL, NULL,
        '{\"handler\":\"cn.sliew.scaleph.application.flink.action.FlinkJobStatusSyncJobStepOne\",\"type\":\"1\"}',
        '{"key1":"value1"}', 'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (11, 7, '2c2cb6c8-794b-4cc1-8258-cd1898912744', 'FlinkJobStatus-2', 460, 400, NULL, NULL,
        '{\"handler\":\"cn.sliew.scaleph.application.flink.action.FlinkJobStatusSyncJobStepTwo\",\"type\":\"1\"}',
        '{"key2":"value2"}', 'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (12, 7, 'd82a947b-f414-4273-973a-06f20fe33f0d', 'FlinkJobStatus-3-1', 460, 400, NULL, NULL,
        '{\"handler\":\"cn.sliew.scaleph.application.flink.action.FlinkJobStatusSyncJobStepThreeOne\",\"type\":\"1\"}',
        '{"key3-1":"value3-1"}', 'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (13, 7, '027db10b-9150-403d-9d11-e4a36c99e1db', 'FlinkJobStatus-3-2', 460, 400, NULL, NULL,
        '{\"handler\":\"cn.sliew.scaleph.application.flink.action.FlinkJobStatusSyncJobStepThreeTwo\",\"type\":\"1\"}',
        '{"key3-2":"value3-2"}', 'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (14, 8, 'cfddc076-db37-41b1-a0f5-26430184805d', 'Kafka Source', 640, 160, NULL, NULL,
        '{\"name\":\"Kafka\",\"type\":\"source\",\"engine\":\"seatunnel\"}',
        '{\"stepTitle\":\"Kafka Source\",\"dataSourceType\":\"Kafka\",\"dataSource\":7,\"topic\":\"data_service_sample_data_e_commerce\",\"pattern\":false,\"consumer.group\":\"mysql_binlog_kafka_es_1\",\"commit_on_checkpoint\":true,\"format_error_handle_way\":\"fail\",\"format\":\"canal_json\",\"start_mode\":\"earliest\",\"schema\":\"{\\\"fields\\\":{}}\",\"kafka.config\":\"{}\"}',
        'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (15, 8, '8ababac2-725c-46c4-96b7-75ebc94621db', 'Elasticsearch Sink', 640, 334, NULL, NULL,
        '{\"name\":\"Elasticsearch\",\"type\":\"sink\",\"engine\":\"seatunnel\"}',
        '{\"stepTitle\":\"Elasticsearch Sink\",\"dataSourceType\":\"Elasticsearch\",\"dataSource\":8,\"index\":\"data_service_sample_data_e_commerce\",\"schema_save_mode\":\"CREATE_SCHEMA_WHEN_NOT_EXIST\",\"data_save_mode\":\"APPEND_DATA\",\"max_batch_size\":10,\"max_retry_count\":3,\"primary_keys\":\"[]\"}',
        'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (16, 8, 'c2e9413a-3aa8-4e04-82ec-77da8f6c12eb', 'Kafka Source', 210, 160, NULL, NULL,
        '{\"name\":\"Kafka\",\"type\":\"source\",\"engine\":\"seatunnel\"}',
        '{\"stepTitle\":\"Kafka Source\",\"dataSourceType\":\"Kafka\",\"dataSource\":7,\"topic\":\"data_service_sample_data_e_commerce\",\"pattern\":false,\"consumer.group\":\"mysql_binlog_kafka_es_2\",\"commit_on_checkpoint\":true,\"format_error_handle_way\":\"fail\",\"format\":\"canal_json\",\"start_mode\":\"earliest\",\"schema\":\"{\\\"fields\\\":{}}\",\"kafka.config\":\"{}\"}',
        'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (17, 8, '7cc271ae-d7e9-4d8c-8568-c2a50492ab77', 'Kafka Sink', 210, 334, NULL, NULL,
        '{\"name\":\"Kafka\",\"type\":\"sink\",\"engine\":\"seatunnel\"}',
        '{\"stepTitle\":\"Kafka Sink\",\"dataSourceType\":\"Kafka\",\"dataSource\":7,\"topic\":\"data_service_sample_data_e_commerce_duplicate\",\"semantic\":\"AT_LEAST_ONCE\",\"format\":\"canal_json\"}',
        'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (18, 9, '297d303d-faa8-4405-b104-b438847e35c9', 'MySQL-CDC Source', 370, 110, NULL, NULL,
        '{\"name\":\"MySQL-CDC\",\"type\":\"source\",\"engine\":\"seatunnel\"}',
        '{\"stepTitle\":\"MySQL-CDC Source\",\"base-url\":\"jdbc:mysql://localhost:3306/data_service\",\"username\":\"root\",\"password\":\"123456\",\"database-names\":\"data_service\",\"table-names\":\"data_service.sample_data_e_commerce\",\"startupMode\":\"initial\",\"stopMode\":\"never\",\"snapshot.split.size\":8096,\"snapshot.fetch.size\":1024,\"incremental.parallelism\":1,\"server-time-zone\":\"UTC\",\"connection.pool.size\":20,\"connect.timeout\":\"30s\",\"connect.max-retries\":3,\"chunk-key.even-distribution.factor.lower-bound\":0.05,\"chunk-key.even-distribution.factor.upper-bound\":1000,\"sample-sharding.threshold\":1000,\"inverse-sampling.rate\":1000,\"exactly_once\":true,\"format\":\"DEFAULT\",\"debezium\":\"{}\"}',
        'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (19, 9, 'b125d246-a5ae-44cb-8280-ee4f8bfe9f1e', 'Kafka Sink', 370, 250, NULL, NULL,
        '{\"name\":\"Kafka\",\"type\":\"sink\",\"engine\":\"seatunnel\"}',
        '{\"stepTitle\":\"Kafka Sink\",\"dataSourceType\":\"Kafka\",\"dataSource\":7,\"topic\":\"binlog_cdc_sample_data_e_commerce\",\"semantic\":\"AT_LEAST_ONCE\",\"format\":\"debezium_json\",\"schema\":\"{\\\"fields\\\":{}}\",\"kafka.config\":\"{}\"}',
        'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (20, 10, '65a74232-1d62-4a48-951c-606c62c4bc20', 'MySQL-CDC Source', 430, 190, NULL, NULL,
        '{\"name\":\"MySQL-CDC\",\"type\":\"source\",\"engine\":\"seatunnel\"}',
        '{\"stepTitle\":\"MySQL-CDC Source\",\"base-url\":\"jdbc:mysql://localhost:3306/data_service\",\"username\":\"root\",\"password\":\"123456\",\"table-names\":\"data_service.sample_data_e_commerce\",\"startupMode\":\"initial\",\"stopMode\":\"never\",\"snapshot.split.size\":8096,\"snapshot.fetch.size\":1024,\"incremental.parallelism\":1,\"server-time-zone\":\"UTC\",\"connection.pool.size\":20,\"connect.timeout\":\"30s\",\"connect.max-retries\":3,\"chunk-key.even-distribution.factor.lower-bound\":0.05,\"chunk-key.even-distribution.factor.upper-bound\":1000,\"sample-sharding.threshold\":1000,\"inverse-sampling.rate\":1000,\"exactly_once\":true,\"format\":\"DEFAULT\",\"debezium\":\"{}\"}',
        'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (21, 10, '75e6ddb1-5146-46c1-9cfe-39f3a1be196a', 'Paimon Sink', 430, 325, NULL, NULL,
        '{\"name\":\"Paimon\",\"type\":\"sink\",\"engine\":\"seatunnel\"}',
        '{\"stepTitle\":\"Paimon Sink\",\"warehouse\":\"s3a:///scaleph/seatunnel/paimon/\",\"database\":\"mysql_data_service\",\"table\":\"sample_data_e_commerce\",\"paimon.hadoop.conf_common_config_\":[{\"paimon.hadoop.conf_common_config_key_\":\"s3.endpoint\",\"paimon.hadoop.conf_common_config_value_\":\"http://localhost:9000\"},{\"paimon.hadoop.conf_common_config_key_\":\"s3.access-key\",\"paimon.hadoop.conf_common_config_value_\":\"admin\"},{\"paimon.hadoop.conf_common_config_key_\":\"s3.secret-key\",\"paimon.hadoop.conf_common_config_value_\":\"password\"},{\"paimon.hadoop.conf_common_config_key_\":\"s3.path.style.access\",\"paimon.hadoop.conf_common_config_value_\":\"true\"}],\"schema_save_mode\":\"CREATE_SCHEMA_WHEN_NOT_EXIST\",\"data_save_mode\":\"APPEND_DATA\",\"paimon.hadoop.conf\":\"{\\\"s3.endpoint\\\":\\\"http://localhost:9000\\\",\\\"s3.access-key\\\":\\\"admin\\\",\\\"s3.secret-key\\\":\\\"password\\\",\\\"s3.path.style.access\\\":\\\"true\\\"}\",\"paimon.table.write-props\":\"{}\"}',
        'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (22, 11, 'ffa948e5-d728-4b1a-abb1-b4058f353118', 'MySQL-CDC Source', 380, 40, NULL, NULL,
        '{\"name\":\"MySQL-CDC\",\"type\":\"source\",\"engine\":\"seatunnel\"}',
        '{\"stepTitle\":\"MySQL-CDC Source\",\"base-url\":\"jdbc:mysql://localhost:3306/data_service\",\"username\":\"root\",\"password\":\"123456\",\"table-names\":\"data_service.sample_data_e_commerce\",\"startupMode\":\"initial\",\"stopMode\":\"never\",\"snapshot.split.size\":8096,\"snapshot.fetch.size\":1024,\"incremental.parallelism\":1,\"server-time-zone\":\"UTC\",\"connection.pool.size\":20,\"connect.timeout\":\"30s\",\"connect.max-retries\":3,\"chunk-key.even-distribution.factor.lower-bound\":0.05,\"chunk-key.even-distribution.factor.upper-bound\":1000,\"sample-sharding.threshold\":1000,\"inverse-sampling.rate\":1000,\"exactly_once\":true,\"format\":\"DEFAULT\",\"debezium\":\"{}\"}',
        'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (23, 11, '6faa1187-093e-4a1f-867c-ebe5c6ed1251', 'Kafka Sink', 380, 163, NULL, NULL,
        '{\"name\":\"Kafka\",\"type\":\"sink\",\"engine\":\"seatunnel\"}',
        '{\"stepTitle\":\"Kafka Sink\",\"dataSourceType\":\"Kafka\",\"dataSource\":7,\"topic\":\"sample_data_e_commerce\",\"semantic\":\"AT_LEAST_ONCE\",\"format\":\"debezium_json\",\"schema\":\"{\\\"fields\\\":{}}\"}',
        'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (24, 11, '66e6337a-e9e3-49ff-90ae-28672147c938', 'Kafka Source', 380, 266, NULL, NULL,
        '{\"name\":\"Kafka\",\"type\":\"source\",\"engine\":\"seatunnel\"}',
        '{\"stepTitle\":\"Kafka Source\",\"dataSourceType\":\"Kafka\",\"dataSource\":7,\"topic\":\"sample_data_e_commerce\",\"pattern\":false,\"consumer.group\":\"SeaTunnel-Consumer-Group-678\",\"commit_on_checkpoint\":true,\"format_error_handle_way\":\"fail\",\"format\":\"debezium_json\",\"start_mode\":\"earliest\",\"schema\":\"{\\\"fields\\\":{}}\",\"kafka.config\":\"{}\"}',
        'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (25, 11, 'ed9c7440-e6b6-47c7-bf97-1051392174eb', 'Doris Sink', 380, 383, NULL, NULL,
        '{\"name\":\"Doris\",\"type\":\"sink\",\"engine\":\"seatunnel\"}',
        '{\"stepTitle\":\"Doris Sink\",\"dataSourceType\":\"Doris\",\"dataSource\":9,\"database\":\"ods\",\"table\":\"ods_data_service_mysql_data_service\",\"sink.label-prefix\":\"test_\",\"sink.enable-2pc\":true,\"sink.enable-delete\":false,\"needs_unsupported_type_casting\":false,\"sink.check-interval\":10000,\"sink.max-retries\":3,\"sink.buffer-size\":262144,\"sink.buffer-count\":3,\"doris.batch.size\":1024,\"schema_save_mode\":\"CREATE_SCHEMA_WHEN_NOT_EXIST\",\"data_save_mode\":\"APPEND_DATA\",\"doris.config_common_config_\":[{\"doris.config_common_config_key_\":\"format\",\"doris.config_common_config_value_\":\"json\"},{\"doris.config_common_config_key_\":\"read_json_by_line\",\"doris.config_common_config_value_\":\"true\"}],\"doris.config\":\"{\\\"format\\\":\\\"json\\\",\\\"read_json_by_line\\\":\\\"true\\\"}\"}',
        'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (26, 12, '1a7dc268-5620-4f17-974a-8b22e51690a5', 'MySQL-CDC Source', 420, 80, NULL, NULL,
        '{\"name\":\"MySQL-CDC\",\"type\":\"source\",\"engine\":\"seatunnel\"}',
        '{\"stepTitle\":\"MySQL-CDC Source\",\"base-url\":\"jdbc:mysql://localhost:3306/data_service\",\"username\":\"root\",\"password\":\"123456\",\"table-names\":\"data_service.sample_data_e_commerce\",\"startupMode\":\"initial\",\"stopMode\":\"never\",\"snapshot.split.size\":8096,\"snapshot.fetch.size\":1024,\"incremental.parallelism\":1,\"server-time-zone\":\"UTC\",\"connection.pool.size\":20,\"connect.timeout\":\"30s\",\"connect.max-retries\":3,\"chunk-key.even-distribution.factor.lower-bound\":0.05,\"chunk-key.even-distribution.factor.upper-bound\":1000,\"sample-sharding.threshold\":1000,\"inverse-sampling.rate\":1000,\"exactly_once\":true,\"format\":\"DEFAULT\",\"debezium\":\"{}\"}',
        'sys', 'sys');
INSERT INTO `carp_dag_config_step` (`id`, `dag_id`, `step_id`, `step_name`, `position_x`, `position_y`, `shape`,
                                    `style`, `step_meta`, `step_attrs`, `creator`, `editor`)
VALUES (27, 12, '763e5f0a-fea0-400b-a5d0-42f72fed63f9', 'Iceberg Sink', 420, 210, NULL, NULL,
        '{\"name\":\"Iceberg\",\"type\":\"sink\",\"engine\":\"seatunnel\"}',
        '{\"stepTitle\":\"Iceberg Sink\",\"catalog_name\":\"ods\",\"namespace\":\"data_service\",\"table\":\"sample_data_e_commerce\",\"type\":\"hadoop\",\"warehouse\":\"s3a:///tmp/seatunnel/iceberg/scaleph/\",\"schema_save_mode\":\"CREATE_SCHEMA_WHEN_NOT_EXIST\",\"data_save_mode\":\"APPEND_DATA\",\"iceberg.catalog.config\":\"{\\\"type\\\":\\\"hadoop\\\",\\\"warehouse\\\":\\\"s3a:///tmp/seatunnel/iceberg/scaleph/\\\"}\",\"hadoop.config\":\"{}\",\"iceberg.table.write-props\":\"{}\",\"iceberg.table.auto-create-props\":\"{}\"}',
        'sys', 'sys');

drop table if exists carp_dag_config_link;
create table carp_dag_config_link
(
    id           bigint      not null auto_increment comment '自增主键',
    dag_id       bigint      not null comment 'DAG id',
    link_id      varchar(36) not null comment '连线id',
    link_name    varchar(128) comment '连线名称',
    from_step_id varchar(36) not null comment '源步骤id',
    to_step_id   varchar(36) not null comment '目标步骤id',
    shape        varchar(64),
    style        text,
    link_meta    varchar(128) comment '连线元信息',
    link_attrs   text comment '连线属性',
    creator      varchar(32) comment 'creator',
    create_time  datetime    not null default current_timestamp comment 'create time',
    editor       varchar(32) comment 'editor',
    update_time  datetime    not null default current_timestamp on update current_timestamp comment 'update time',
    primary key (id),
    unique key uniq_link (dag_id, link_id)
) engine = innodb comment 'dag config link';

INSERT INTO `carp_dag_config_link` (`id`, `dag_id`, `link_id`, `link_name`, `from_step_id`, `to_step_id`, `shape`,
                                    `style`, `link_meta`, `link_attrs`, `creator`, `editor`)
VALUES (1, 1, '78ca5c31-0eaa-4d43-8f30-0d8f7d0ec317', NULL, '157f118c-9b6c-4d18-a919-fce824676696',
        'e69dbf5a-76ad-47be-aa16-175b733a7df2', NULL, NULL, NULL, NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config_link` (`id`, `dag_id`, `link_id`, `link_name`, `from_step_id`, `to_step_id`, `shape`,
                                    `style`, `link_meta`, `link_attrs`, `creator`, `editor`)
VALUES (2, 2, 'd57021a1-65c7-4dfe-ae89-3b73d00fcf72', NULL, '6223c6c3-b552-4c69-adab-5300b7514fad',
        'f08143b4-34dc-4190-8723-e8d8ce49738f', NULL, NULL, NULL, NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config_link` (`id`, `dag_id`, `link_id`, `link_name`, `from_step_id`, `to_step_id`, `shape`,
                                    `style`, `link_meta`, `link_attrs`, `creator`, `editor`)
VALUES (4, 7, '2d172e1a-ef92-431c-9889-7461bccae7a5', NULL, 'cae1a622-6c96-4cec-81d3-883510c17702',
        '2c2cb6c8-794b-4cc1-8258-cd1898912744', NULL, NULL, NULL, NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config_link` (`id`, `dag_id`, `link_id`, `link_name`, `from_step_id`, `to_step_id`, `shape`,
                                    `style`, `link_meta`, `link_attrs`, `creator`, `editor`)
VALUES (5, 7, 'af16c8ee-0abf-4555-aa0e-98ec01964ce1', NULL, '2c2cb6c8-794b-4cc1-8258-cd1898912744',
        'd82a947b-f414-4273-973a-06f20fe33f0d', NULL, NULL, NULL, NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config_link` (`id`, `dag_id`, `link_id`, `link_name`, `from_step_id`, `to_step_id`, `shape`,
                                    `style`, `link_meta`, `link_attrs`, `creator`, `editor`)
VALUES (6, 7, '027db10b-9150-403d-9d11-e4a36c99e1db', NULL, '2c2cb6c8-794b-4cc1-8258-cd1898912744',
        '027db10b-9150-403d-9d11-e4a36c99e1db', NULL, NULL, NULL, NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config_link` (`id`, `dag_id`, `link_id`, `link_name`, `from_step_id`, `to_step_id`, `shape`,
                                    `style`, `link_meta`, `link_attrs`, `creator`, `editor`)
VALUES (7, 8, '0c46e06b-31fe-458c-b27e-5b8a6fe5c70e', NULL, 'cfddc076-db37-41b1-a0f5-26430184805d',
        '8ababac2-725c-46c4-96b7-75ebc94621db', NULL, NULL, NULL, NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config_link` (`id`, `dag_id`, `link_id`, `link_name`, `from_step_id`, `to_step_id`, `shape`,
                                    `style`, `link_meta`, `link_attrs`, `creator`, `editor`)
VALUES (8, 8, 'f64150b7-7374-4fdc-a71b-26f4cda4abe7', NULL, 'c2e9413a-3aa8-4e04-82ec-77da8f6c12eb',
        '7cc271ae-d7e9-4d8c-8568-c2a50492ab77', NULL, NULL, NULL, NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config_link` (`id`, `dag_id`, `link_id`, `link_name`, `from_step_id`, `to_step_id`, `shape`,
                                    `style`, `link_meta`, `link_attrs`, `creator`, `editor`)
VALUES (9, 9, 'eca3a0ef-70eb-4f77-b474-768bdbf68477', NULL, '297d303d-faa8-4405-b104-b438847e35c9',
        'b125d246-a5ae-44cb-8280-ee4f8bfe9f1e', NULL, NULL, NULL, NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config_link` (`id`, `dag_id`, `link_id`, `link_name`, `from_step_id`, `to_step_id`, `shape`,
                                    `style`, `link_meta`, `link_attrs`, `creator`, `editor`)
VALUES (10, 10, '7d6d6f11-b7eb-4d03-97a1-2c6f7bf38d83', NULL, '65a74232-1d62-4a48-951c-606c62c4bc20',
        '75e6ddb1-5146-46c1-9cfe-39f3a1be196a', NULL, NULL, NULL, NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config_link` (`id`, `dag_id`, `link_id`, `link_name`, `from_step_id`, `to_step_id`, `shape`,
                                    `style`, `link_meta`, `link_attrs`, `creator`, `editor`)
VALUES (11, 11, '9e4d5226-d393-43ab-b724-18f12daef4e7', NULL, 'ffa948e5-d728-4b1a-abb1-b4058f353118',
        '6faa1187-093e-4a1f-867c-ebe5c6ed1251', NULL, NULL, NULL, NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config_link` (`id`, `dag_id`, `link_id`, `link_name`, `from_step_id`, `to_step_id`, `shape`,
                                    `style`, `link_meta`, `link_attrs`, `creator`, `editor`)
VALUES (12, 11, 'edf4ad48-58b7-4a0e-9e63-f4e74c5a3516', NULL, '66e6337a-e9e3-49ff-90ae-28672147c938',
        'ed9c7440-e6b6-47c7-bf97-1051392174eb', NULL, NULL, NULL, NULL, 'sys', 'sys');
INSERT INTO `carp_dag_config_link` (`id`, `dag_id`, `link_id`, `link_name`, `from_step_id`, `to_step_id`, `shape`,
                                    `style`, `link_meta`, `link_attrs`, `creator`, `editor`)
VALUES (13, 12, 'ac8ac824-3a43-4f0b-ba44-bd2dfbf45282', NULL, '1a7dc268-5620-4f17-974a-8b22e51690a5',
        '763e5f0a-fea0-400b-a5d0-42f72fed63f9', NULL, NULL, NULL, NULL, 'sys', 'sys');

drop table if exists carp_dag_instance;
create table carp_dag_instance
(
    id            bigint      not null auto_increment comment '自增主键',
    dag_config_id bigint      not null comment 'DAG配置id',
    uuid          varchar(36) not null comment 'instance id',
    inputs        text comment '输入参数',
    outputs       text comment '输出参数',
    status        varchar(8) comment '状态',
    start_time    timestamp   not null comment '启动时间',
    end_time      timestamp comment '结束时间',
    creator       varchar(32) comment 'creator',
    create_time   datetime    not null default current_timestamp comment 'create time',
    editor        varchar(32) comment 'editor',
    update_time   datetime    not null default current_timestamp on update current_timestamp comment 'update time',
    primary key (id),
    key           idx_dag_config_id (`dag_config_id`)
) engine = innodb comment 'dag instance';

drop table if exists carp_dag_step;
create table carp_dag_step
(
    id                 bigint      not null auto_increment comment '自增主键',
    dag_instance_id    bigint      not null comment 'DAG id',
    dag_config_step_id bigint      not null comment '步骤id',
    uuid               varchar(36) not null comment 'instance id',
    inputs             text comment '输入参数',
    outputs            text comment '输出参数',
    status             varchar(8) comment '状态',
    start_time         timestamp   not null comment '启动时间',
    end_time           timestamp comment '结束时间',
    creator            varchar(32) comment 'creator',
    create_time        datetime    not null default current_timestamp comment 'create time',
    editor             varchar(32) comment 'editor',
    update_time        datetime    not null default current_timestamp on update current_timestamp comment 'update time',
    primary key (id),
    unique key uniq_step (dag_instance_id, dag_config_step_id)
) engine = innodb comment 'dag instance step';

drop table if exists carp_dag_link;
create table carp_dag_link
(
    id                 bigint      not null auto_increment comment '自增主键',
    dag_instance_id    bigint      not null comment 'DAG id',
    dag_config_link_id bigint      not null comment '连线id',
    uuid               varchar(36) not null comment 'instance id',
    inputs             text comment '输入参数',
    outputs            text comment '输出参数',
    status             varchar(8) comment '状态',
    start_time         timestamp comment '启动时间',
    end_time           timestamp comment '结束时间',
    creator            varchar(32) comment 'creator',
    create_time        datetime    not null default current_timestamp comment 'create time',
    editor             varchar(32) comment 'editor',
    update_time        datetime    not null default current_timestamp on update current_timestamp comment 'update time',
    primary key (id),
    unique key uniq_link (dag_instance_id, dag_config_link_id)
) engine = innodb comment 'dag instance link';