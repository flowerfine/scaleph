create database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
use scaleph;

DROP TABLE IF EXISTS ds_category;
CREATE TABLE `ds_category`
(
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    `name`      VARCHAR(64) NOT NULL,
    `order`     INT                  DEFAULT 0,
    remark      VARCHAR(256),
    creator     VARCHAR(32),
    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    editor      VARCHAR(32),
    update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB COMMENT ='data source category';
INSERT INTO `ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (1, '常用', 10, NULL, 'sys', 'sys');
INSERT INTO `ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (2, '关系型', 11, NULL, 'sys', 'sys');
INSERT INTO `ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (3, 'NoSQL', 12, NULL, 'sys', 'sys');
INSERT INTO `ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (4, '消息队列', 13, NULL, 'sys', 'sys');
INSERT INTO `ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (5, '文件存储', 14, NULL, 'sys', 'sys');
INSERT INTO `ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (6, '大数据', 15, NULL, 'sys', 'sys');
INSERT INTO `ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (7, '数据湖', 16, NULL, 'sys', 'sys');
INSERT INTO `ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (8, 'IM通讯', 17, NULL, 'sys', 'sys');
INSERT INTO `ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (9, 'SAAS', 18, NULL, 'sys', 'sys');
INSERT INTO `ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (10, '其他', 19, NULL, 'sys', 'sys');


DROP TABLE IF EXISTS ds_type;
CREATE TABLE `ds_type`
(
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    `type`      VARCHAR(64) NOT NULL,
    `logo`      VARCHAR(255),
    `order`     INT                  DEFAULT 0,
    remark      VARCHAR(256),
    creator     VARCHAR(32),
    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    editor      VARCHAR(32),
    update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB COMMENT ='data source type';
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (1, 'MySQL', '/images/MySQL.png', 10, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (2, 'Oracle', '/images/Oracle.png', 11, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (3, 'PostgreSQL', '/images/PostgreSQL.png', 12, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (4, 'SQLServer', '/images/SQLServer.png', 13, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (5, 'DmDB', '/images/DmDB.png', 14, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (6, 'GBase8a', '/images/GBase8a.jpg', 15, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (7, 'Greenplum', '/images/Greenplum.png', 16, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (8, 'Phoenix', '/images/Phoenix.png', 17, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (9, 'Redis', '/images/Redis.png', 18, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (10, 'Elasticsearch', '/images/Elasticsearch.png', 19, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (11, 'Solr', '/images/Solr.png', 17, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (12, 'MongoDB', '/images/MongoDB.png', 18, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (13, 'TiDB', '/images/TiDB.png', 19, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (14, 'Kafka', '/images/Kafka.png', 20, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (15, 'Pulsar', '/images/Pulsar.svg', 21, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (16, 'DataHub', '/images/DataHub.webp', 22, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (17, 'Ftp', '/images/Ftp.png', 23, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (18, 'Sftp', '/images/Sftp.svg', 24, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (19, 'OSS', '/images/OSS.webp', 25, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (20, 'S3', '/images/S3.svg', 26, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (21, 'HDFS', '/images/HDFS.png', 27, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (22, 'Hive', '/images/Hive.png', 28, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (23, 'HBase', '/images/HBase.png', 29, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (24, 'Impala', '/images/Impala.png', 30, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (25, 'Doris', '/images/Doris.png', 31, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (26, 'ClickHouse', '/images/ClickHouse.png', 32, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (27, 'Kudu', '/images/Kudu.png', 33, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (28, 'Kylin', '/images/Kylin.png', 34, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (29, 'Druid', '/images/Druid.png', 35, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (30, 'IoTDB', '/images/IoTDB.png', 36, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (31, 'Neo4j', '/images/Neo4j.svg', 37, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (32, 'Hudi', '/images/Hudi.png', 38, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (33, 'Iceberg', '/images/Iceberg.png', 39, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (34, 'InfluxDB', '/images/InfluxDB.png', 40, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (35, 'Email', NULL, 41, NULL, 'sys', 'sys');
INSERT INTO `ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (36, 'Socket', '/images/Socket.png', 42, NULL, 'sys', 'sys');

DROP TABLE IF EXISTS ds_category_type_relation;
CREATE TABLE `ds_category_type_relation`
(
    id             BIGINT   NOT NULL AUTO_INCREMENT,
    ds_category_id BIGINT   NOT NULL,
    ds_type_id     BIGINT   NOT NULL,
    creator        VARCHAR(32),
    create_time    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    editor         VARCHAR(32),
    update_time    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB COMMENT ='data source category and type relation';
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (1, 1, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (1, 10, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (1, 14, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (1, 15, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (1, 21, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (1, 25, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (1, 32, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (1, 33, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (2, 1, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (2, 2, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (2, 3, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (2, 4, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (2, 5, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (2, 6, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (2, 7, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (2, 8, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (3, 9, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (3, 10, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (3, 11, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (3, 12, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (3, 13, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (4, 14, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (4, 15, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (4, 16, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (5, 17, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (5, 18, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (5, 19, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (5, 20, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (5, 21, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 22, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 23, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 24, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 25, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 26, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 27, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 28, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 29, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 30, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 31, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (7, 32, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (7, 33, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (8, 35, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (10, 34, 'sys', 'sys');
INSERT INTO `ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (10, 36, 'sys', 'sys');

DROP TABLE IF EXISTS ds_info;
CREATE TABLE `ds_info`
(
    id               BIGINT      NOT NULL AUTO_INCREMENT,
    ds_type_id       BIGINT      NOT NULL,
    version          VARCHAR(64),
    `name`           VARCHAR(64) NOT NULL,
    props            TEXT,
    additional_props TEXT,
    remark           VARCHAR(256),
    creator          VARCHAR(32),
    create_time      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    editor           VARCHAR(32),
    update_time      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_datasource (`name`)
) ENGINE = InnoDB COMMENT ='data source info';

INSERT INTO `ds_info`(`id`, `ds_type_id`, `version`, `name`, `props`, `additional_props`, `remark`, `creator`, `editor`)
VALUES (1, 1, NULL, 'docker_data_service',
        'eyJkcml2ZXJDbGFzc05hbWUiOiJjb20ubXlzcWwuY2ouamRiYy5Ecml2ZXIiLCJ1c2VyIjoicm9vdCIsInBhc3N3b3JkIjoiMTIzNDU2IiwidXJsIjoiamRiYzpteXNxbDovL215c3FsOjMzMDYvZGF0YV9zZXJ2aWNlIn0=',
        NULL, NULL, 'sys', 'sys');
INSERT INTO `ds_info`(`id`, `ds_type_id`, `version`, `name`, `props`, `additional_props`, `remark`, `creator`, `editor`)
VALUES (2, 1, NULL, 'local_data_service',
        'eyJkcml2ZXJDbGFzc05hbWUiOiJjb20ubXlzcWwuY2ouamRiYy5Ecml2ZXIiLCJ1c2VyIjoicm9vdCIsInBhc3N3b3JkIjoiMTIzNDU2IiwidXJsIjoiamRiYzpteXNxbDovL2xvY2FsaG9zdDozMzA2L2RhdGFfc2VydmljZSJ9',
        NULL, NULL, 'sys', 'sys');
INSERT INTO `ds_info`(`id`, `ds_type_id`, `version`, `name`, `props`, `additional_props`, `remark`, `creator`, `editor`)
VALUES (3, 9, NULL, 'docker_redis', 'eyJwYXNzd29yZCI6IkVuY3J5cHRlZDpNVEl6TkRVMiIsInBvcnQiOjYzNzksImhvc3QiOiJyZWRpcyJ9',
        NULL, NULL, 'sys', 'sys');
INSERT INTO `ds_info`(`id`, `ds_type_id`, `version`, `name`, `props`, `additional_props`, `remark`, `creator`, `editor`)
VALUES (4, 9, NULL, 'local_redis',
        'eyJwYXNzd29yZCI6IkVuY3J5cHRlZDpNVEl6TkRVMiIsInBvcnQiOjYzNzksImhvc3QiOiJsb2NhbGhvc3QifQ==', NULL, NULL, 'sys',
        'sys');
INSERT INTO `ds_info`(`id`, `ds_type_id`, `version`, `name`, `props`, `additional_props`, `remark`, `creator`, `editor`)
VALUES (5, 20, NULL, 'docker_minio',
        'eyJhY2Nlc3NTZWNyZXQiOiJFbmNyeXB0ZWQ6Y0dGemMzZHZjbVE9IiwiYnVja2V0IjoiczNuOi8vbWluaW86OTAwMC9zY2FsZXBoIiwiYWNjZXNzS2V5IjoiRW5jcnlwdGVkOllXUnRhVzQ9In0=',
        NULL, NULL, 'sys', 'sys');
INSERT INTO `ds_info`(`id`, `ds_type_id`, `version`, `name`, `props`, `additional_props`, `remark`, `creator`, `editor`)
VALUES (6, 20, NULL, 'local_minio',
        'eyJhY2Nlc3NTZWNyZXQiOiJFbmNyeXB0ZWQ6Y0dGemMzZHZjbVE9IiwiYnVja2V0IjoiczNuOi8vbG9jYWxob3N0OjkwMDAvc2NhbGVwaCIsImFjY2Vzc0tleSI6IkVuY3J5cHRlZDpZV1J0YVc0PSJ9',
        NULL, NULL, 'sys', 'sys');

/* 元数据-数据源连接信息 */
drop table if exists meta_datasource;
create TABLE meta_datasource
(
    id               bigint      NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    datasource_name  varchar(64) NOT NULL COMMENT '数据源名称',
    datasource_type  varchar(32) not null comment '数据源类型',
    props            text COMMENT '数据源支持的属性',
    additional_props text COMMENT '数据源支持的额外属性',
    remark           varchar(256)     DEFAULT NULL COMMENT '备注描述',
    creator          varchar(32)      DEFAULT NULL COMMENT '创建人',
    create_time      timestamp   NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    editor           varchar(32)      DEFAULT NULL COMMENT '修改人',
    update_time      timestamp   NULL DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id),
    unique key (datasource_type, datasource_name)
) ENGINE = InnoDB COMMENT ='元数据-数据源信息';
insert into meta_datasource (id, datasource_name, datasource_type, props, additional_props, remark, creator, editor)
values (1, 'docker_data_service', 'MySQL',
        '{"host":"mysql","port":"3306","databaseName":"data_service","username":"root","password":"Encrypted:MTIzNDU2"}',
        '{"serverTimezone":"Asia/Shanghai","zeroDateTimeBehavior":"convertToNull","characterEncoding":"utf8"}', null,
        'sys', 'sys');
insert into meta_datasource (id, datasource_name, datasource_type, props, additional_props, remark, creator, editor)
values (2, 'local_data_service', 'MySQL',
        '{"host":"localhost","port":"3306","databaseName":"data_service","username":"root","password":"Encrypted:MTIzNDU2"}',
        '{"serverTimezone":"Asia/Shanghai","zeroDateTimeBehavior":"convertToNull","characterEncoding":"utf8"}', null,
        'sys', 'sys');
insert into meta_datasource(id, datasource_name, datasource_type, props, additional_props, remark, creator, editor)
values (3, 'local', 'Elasticsearch', '{"hosts":"localhost:9200"}', 'null', NULL, 'sys', 'sys');
insert into meta_datasource (id, datasource_name, datasource_type, props, additional_props, remark,
                             creator, editor)
VALUES (4, 'local', 'Druid', '{"jdbc_url":"jdbc:avatica:remote:url=http://localhost:8082/druid/v2/sql/avatica/"}',
        'null', NULL, 'sys', 'sys');
/*元数据-数据表信息*/
drop table if exists meta_table;
create table meta_table
(
    id                bigint       not null auto_increment comment '自增主键',
    datasource_id     bigint       not null comment '数据源id',
    table_catalog     varchar(64) comment '表目录',
    table_schema      varchar(64)  not null comment '表模式',
    table_name        varchar(128) not null comment '表名',
    table_type        varchar(12)  not null default 'TABLE' comment '表类型',
    table_space       VARCHAR(64) comment '表空间',
    table_comment     VARCHAR(1024) COMMENT '表描述',
    table_rows        bigint comment '表数据行数',
    data_bytes        bigint comment '数据空间大小，单位(byte)',
    index_bytes       bigint comment '索引空间大小，单位(byte)',
    table_create_time datetime comment '表创建时间',
    last_ddl_time     datetime comment '最后ddl操作时间',
    last_access_time  datetime comment '最后数据访问时间',
    life_cycle        int comment '生命周期，单位(天)',
    is_partitioned    varchar(1)            default '0' comment '是否分区表,0否1是',
    attrs             varchar(1024) comment '表扩展属性',
    creator           varchar(32) comment '创建人',
    create_time       timestamp             default current_timestamp comment '创建时间',
    editor            varchar(32) comment '修改人',
    update_time       timestamp             default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique (datasource_id, table_schema, table_name),
    key (update_time)
) engine = innodb comment '元数据-数据表信息';

/*元数据-数据表字段信息*/
drop table if exists meta_column;
create table meta_column
(
    id             bigint      not null auto_increment comment '自增主键',
    table_id       bigint      not null comment '数据表id',
    column_name    varchar(64) not null comment '列名',
    data_type      varchar(32) not null comment '数据类型',
    data_length    bigint comment '长度',
    data_precision int comment '数据精度，有效位',
    data_scale     int comment '小数位数',
    nullable       varchar(1)  not null default '0' comment '是否可以为空,1-是;0-否',
    data_default   varchar(512) comment '默认值',
    low_value      varchar(512) comment '最小值',
    high_value     varchar(512) comment '最大值',
    column_ordinal int comment '列顺序',
    column_comment varchar(1024) comment '列描述',
    is_primary_key varchar(4) comment '是否主键',
    creator        varchar(32) comment '创建人',
    create_time    timestamp            default current_timestamp comment '创建时间',
    editor         varchar(32) comment '修改人',
    update_time    timestamp            default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique (table_id, column_name),
    key (update_time)
) engine = innodb comment '元数据-数据表字段信息';

/* 元数据-数据元信息 */
drop table if exists meta_data_element;
create table meta_data_element
(
    id               bigint       not null auto_increment comment '自增主键',
    element_code     varchar(32)  not null comment '数据元标识',
    element_name     varchar(256) not null comment '数据元名称',
    data_type        varchar(10)  not null comment '数据类型',
    data_length      bigint comment '长度',
    data_precision   int comment '数据精度，有效位',
    data_scale       int comment '小数位数',
    nullable         varchar(1)   not null default '0' comment '是否可以为空,1-是;0-否',
    data_default     varchar(512) comment '默认值',
    low_value        varchar(512) comment '最小值',
    high_value       varchar(512) comment '最大值',
    data_set_type_id bigint comment '参考数据类型id',
    creator          varchar(32) comment '创建人',
    create_time      timestamp             default current_timestamp comment '创建时间',
    editor           varchar(32) comment '修改人',
    update_time      timestamp             default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (element_code),
    key (element_name),
    key (update_time)
) engine = innodb comment '元数据-数据元信息';
-- init sample data
INSERT INTO meta_data_element (id, element_code, element_name, data_type, data_length, data_precision, data_scale,
                               nullable, data_default, low_value, high_value, data_set_type_id, creator, editor)
VALUES (5, 'id', 'ID', 'bigint', 0, 0, 0, '0', '0', '0', null, null, 'sys', 'sys');
INSERT INTO meta_data_element (id, element_code, element_name, data_type, data_length, data_precision, data_scale,
                               nullable, data_default, low_value, high_value, data_set_type_id, creator, editor)
VALUES (6, 'user_name', '用户名', 'string', 32, 0, 0, '0', null, null, null, 0, 'sys', 'sys');
INSERT INTO meta_data_element (id, element_code, element_name, data_type, data_length, data_precision, data_scale,
                               nullable, data_default, low_value, high_value, data_set_type_id, creator, editor)
VALUES (7, 'nick_name', '昵称', 'string', 50, 0, 0, '1', null, null, null, null, 'sys', 'sys');
INSERT INTO meta_data_element (id, element_code, element_name, data_type, data_length, data_precision, data_scale,
                               nullable, data_default, low_value, high_value, data_set_type_id, creator, editor)
VALUES (8, 'create_time', '创建时间', 'timestamp', 0, 0, 0, '0', null, null, null, null, 'sys', 'sys');
INSERT INTO meta_data_element (id, element_code, element_name, data_type, data_length, data_precision, data_scale,
                               nullable, data_default, low_value, high_value, data_set_type_id, creator, editor)
VALUES (9, 'update_time', '更新时间', 'timestamp', 0, 0, 0, '0', null, null, null, null, 'sys', 'sys');
INSERT INTO meta_data_element (id, element_code, element_name, data_type, data_length, data_precision, data_scale,
                               nullable, data_default, low_value, high_value, data_set_type_id, creator, editor)
VALUES (10, 'gender', '性别', 'string', 4, 0, 0, '1', null, null, null, 2, 'sys', 'sys');
INSERT INTO meta_data_element (id, element_code, element_name, data_type, data_length, data_precision, data_scale,
                               nullable, data_default, low_value, high_value, data_set_type_id, creator, editor)
VALUES (11, 'user_status', '用户状态', 'string', 4, 0, 0, '0', null, null, null, 3, 'sys', 'sys');
INSERT INTO meta_data_element (id, element_code, element_name, data_type, data_length, data_precision, data_scale,
                               nullable, data_default, low_value, high_value, data_set_type_id, creator, editor)
VALUES (12, 'order_status', '订单状态', 'string', 4, 0, 0, '0', null, null, null, 4, 'sys', 'sys');

/* 元数据-业务系统信息 */
drop table if exists meta_system;
create table meta_system
(
    id             bigint       not null auto_increment comment '系统id',
    system_code    varchar(32)  not null comment '系统编码',
    system_name    varchar(128) not null comment '系统名称',
    contacts       varchar(24) comment '联系人',
    contacts_phone varchar(15) comment '联系人手机号码',
    remark         varchar(256) comment '备注',
    creator        varchar(32) comment '创建人',
    create_time    timestamp default current_timestamp comment '创建时间',
    editor         varchar(32) comment '修改人',
    update_time    timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (system_code),
    key (update_time)
) engine = innodb comment '元数据-业务系统信息';
-- init sample data
INSERT INTO meta_system (id, system_code, system_name, contacts, contacts_phone, remark, creator, editor)
VALUES (1, 'STD_SAMR', '全国标准信息公共服务平台', null, null, 'http://std.samr.gov.cn/', 'sys', 'sys');
INSERT INTO meta_system (id, system_code, system_name, contacts, contacts_phone, remark, creator, editor)
VALUES (2, 'TRADE', '电商交易系统', null, null, null, 'sys', 'sys');
INSERT INTO meta_system (id, system_code, system_name, contacts, contacts_phone, remark, creator, editor)
VALUES (3, 'KS_TRADE', '快手电商', null, null, null, 'sys', 'sys');
INSERT INTO meta_system (id, system_code, system_name, contacts, contacts_phone, remark, creator, editor)
VALUES (4, 'DY_TRADE', '抖音电商', null, null, null, 'sys', 'sys');
INSERT INTO meta_system (id, system_code, system_name, contacts, contacts_phone, remark, creator, editor)
VALUES (5, 'TB_TRADE', '淘宝电商', null, null, null, 'sys', 'sys');


/* 元数据-参考数据类型 */
drop table if exists meta_data_set_type;
create table meta_data_set_type
(
    id                 bigint       not null auto_increment comment '自增主键',
    data_set_type_code varchar(32)  not null comment '参考数据类型编码',
    data_set_type_name varchar(128) not null comment '参考数据类型名称',
    remark             varchar(256) comment '备注',
    creator            varchar(32) comment '创建人',
    create_time        timestamp default current_timestamp comment '创建时间',
    editor             varchar(32) comment '修改人',
    update_time        timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (data_set_type_code),
    key (data_set_type_name),
    key (update_time)
) engine = innodb comment '元数据-参考数据类型';
-- init sample data
INSERT INTO meta_data_set_type (id, data_set_type_code, data_set_type_name, remark, creator, editor)
VALUES (2, 'gender', '性别', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set_type (id, data_set_type_code, data_set_type_name, remark, creator, editor)
VALUES (3, 'user_status', '用户状态', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set_type (id, data_set_type_code, data_set_type_name, remark, creator, editor)
VALUES (4, 'order_status', '订单状态', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set_type (id, data_set_type_code, data_set_type_name, remark, creator, editor)
VALUES (5, 'ks_order_status', '快手订单状态', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set_type (id, data_set_type_code, data_set_type_name, remark, creator, editor)
VALUES (6, 'dy_order_status', '抖音订单状态', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set_type (id, data_set_type_code, data_set_type_name, remark, creator, editor)
VALUES (7, 'tb_order_status', '淘宝订单状态', null, 'sys_admin', 'sys_admin');

/* 元数据-参考数据 */
drop table if exists meta_data_set;
create table meta_data_set
(
    id               bigint       not null auto_increment comment '自增主键',
    data_set_type_id bigint       not null comment '参考数据类型id',
    data_set_code    varchar(32)  not null comment '代码code',
    data_set_value   varchar(128) not null comment '代码值',
    system_id        bigint comment '业务系统id',
    is_standard      varchar(1)   not null comment '是否标准',
    remark           varchar(256) comment '备注',
    creator          varchar(32) comment '创建人',
    create_time      timestamp default current_timestamp comment '创建时间',
    editor           varchar(32) comment '修改人',
    update_time      timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique key (data_set_code, data_set_type_id, system_id),
    key (update_time)
) engine = innodb comment '元数据-参考数据';
-- init sample data
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (1, 2, '0', '未知性别', 1, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (2, 2, '1', '男性', 1, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (3, 2, '2', '女性', 1, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (4, 2, '9', '未说明性别', 1, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (5, 3, '10', '正常', null, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (6, 3, '90', '禁用', null, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (7, 3, '91', '注销', null, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (8, 4, '10', '待付款', 2, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (9, 4, '20', '已支付', 2, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (10, 4, '30', '待发货', 2, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (11, 4, '32', '待收货', 2, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (12, 4, '49', '交易成功', 2, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (13, 4, '50', '已完成', 2, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (14, 4, '60', '已关闭', 2, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (15, 4, '31', '部分发货', 2, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (16, 4, '33', '部分收货', 2, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (17, 4, '34', '全部收货', 2, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (18, 4, '40', '退款中', 2, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (19, 4, '42', '退款成功', 2, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (20, 4, '43', '退款失败', 2, '1', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (21, 7, 'TRADE_NO_CREATE_PAY', '没有创建支付宝交易', 5, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (22, 7, 'WAIT_BUYER_PAY', '等待买家付款', 5, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (23, 7, 'SELLER_CONSIGNED_PART', '卖家部分发货', 5, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (24, 7, 'WAIT_SELLER_SEND_GOODS', '等待卖家发货', 5, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (25, 7, 'WAIT_BUYER_CONFIRM_GOODS', '等待买家确认收货', 5, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (26, 7, 'TRADE_BUYER_SIGNED', '买家已签收', 5, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (27, 7, 'TRADE_FINISHED', '交易成功', 5, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (28, 7, 'TRADE_CLOSED', '交易自动关闭', 5, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (29, 7, 'TRADE_CLOSED_BY_TAOBAO', '主动关闭交易', 5, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (30, 7, 'PAY_PENDING', '付款确认中', 5, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (31, 6, '10', '待付款', 4, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (32, 5, '10', '待付款', 3, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (33, 5, '30', '已付款', 3, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (34, 5, '40', '已发货', 3, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (35, 5, '50', '已签收', 3, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (36, 5, '60', '已结算', 3, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (37, 5, '70', '订单成功', 3, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (38, 5, '80', '订单失败', 3, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (39, 6, '20', '已付款', 4, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (40, 6, '30', '已发货', 4, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (41, 6, '40', '已完成', 4, '0', null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_set (id, data_set_type_id, data_set_code, data_set_value, system_id, is_standard, remark, creator,
                           editor)
VALUES (42, 6, '50', '已关闭', 4, '0', null, 'sys_admin', 'sys_admin');


/* 元数据-参考数据映射 */
drop table if exists meta_data_map;
create table meta_data_map
(
    id              bigint auto_increment comment '自增主键',
    src_data_set_id bigint not null comment '原始数据代码',
    tgt_data_set_id bigint not null comment '目标数据代码',
    remark          varchar(256) comment '备注',
    creator         varchar(32) comment '创建人',
    create_time     timestamp default current_timestamp comment '创建时间',
    editor          varchar(32) comment '修改人',
    update_time     timestamp default current_timestamp on update current_timestamp comment '修改时间',
    primary key (id),
    unique (src_data_set_id, tgt_data_set_id),
    key (tgt_data_set_id),
    key (update_time)
) engine = innodb comment '元数据-参考数据映射';
-- init sample data
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (11, 21, 8, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (12, 22, 8, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (13, 23, 15, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (14, 24, 10, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (15, 25, 11, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (16, 26, 12, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (17, 27, 12, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (18, 28, 14, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (19, 29, 14, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (20, 30, 8, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (21, 32, 8, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (22, 33, 9, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (23, 34, 11, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (24, 35, 12, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (26, 36, 13, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (27, 37, 13, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (28, 38, 14, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (29, 31, 8, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (30, 39, 9, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (31, 40, 11, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (32, 41, 13, null, 'sys_admin', 'sys_admin');
INSERT INTO meta_data_map (id, src_data_set_id, tgt_data_set_id, remark, creator, editor)
VALUES (33, 42, 14, null, 'sys_admin', 'sys_admin');