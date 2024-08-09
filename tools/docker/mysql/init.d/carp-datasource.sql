create database if not exists carp default character set utf8mb4 collate utf8mb4_unicode_ci;
use carp;

DROP TABLE IF EXISTS carp_ds_category;
CREATE TABLE `carp_ds_category`
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
INSERT INTO `carp_ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (1, '常用', 10, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (2, '关系型', 11, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (3, 'NoSQL', 12, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (4, '消息队列', 13, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (5, '文件存储', 14, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (6, '大数据', 15, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (7, '数据湖', 16, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (8, 'IM通讯', 17, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (9, 'SAAS', 18, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_category`(`id`, `name`, `order`, `remark`, `creator`, `editor`)
VALUES (10, '其他', 19, NULL, 'sys', 'sys');


DROP TABLE IF EXISTS carp_ds_type;
CREATE TABLE `carp_ds_type`
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
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (1, 'MySQL', '/images/DataSource/MySQL.png', 10, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (2, 'Oracle', '/images/DataSource/Oracle.png', 11, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (3, 'PostgreSQL', '/images/DataSource/PostgreSQL.png', 12, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (4, 'SQLServer', '/images/DataSource/SQLServer.png', 13, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (5, 'DmDB', '/images/DataSource/DmDB.png', 14, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (6, 'GBase8a', '/images/DataSource/GBase8a.png', 15, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (7, 'Greenplum', '/images/DataSource/Greenplum.png', 16, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (8, 'Phoenix', '/images/DataSource/Phoenix.png', 17, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (9, 'Redis', '/images/DataSource/Redis.png', 18, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (10, 'Elasticsearch', '/images/DataSource/Elasticsearch.png', 19, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (11, 'Solr', '/images/DataSource/Solr.png', 17, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (12, 'MongoDB', '/images/DataSource/MongoDB.png', 18, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (13, 'TiDB', '/images/DataSource/TiDB.png', 19, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (14, 'Kafka', '/images/DataSource/Kafka.png', 20, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (15, 'Pulsar', '/images/DataSource/Pulsar.png', 21, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (16, 'DataHub', '/images/DataSource/DataHub.png', 22, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (17, 'Ftp', '/images/DataSource/Ftp.png', 23, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (18, 'Sftp', '/images/DataSource/Sftp.png', 24, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (19, 'OSS', '/images/DataSource/OSS.png', 25, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (20, 'S3', '/images/DataSource/S3.png', 26, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (21, 'HDFS', '/images/DataSource/HDFS.png', 27, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (22, 'Hive', '/images/DataSource/Hive.png', 28, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (23, 'HBase', '/images/DataSource/HBase.png', 29, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (24, 'Impala', '/images/DataSource/Impala.png', 30, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (25, 'Doris', '/images/DataSource/Doris.png', 31, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (26, 'ClickHouse', '/images/DataSource/ClickHouse.png', 32, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (27, 'Kudu', '/images/DataSource/Kudu.png', 33, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (28, 'Kylin', '/images/DataSource/Kylin.png', 34, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (29, 'Druid', '/images/DataSource/Druid.png', 35, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (30, 'IoTDB', '/images/DataSource/IoTDB.png', 36, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (31, 'Neo4j', '/images/DataSource/Neo4j.png', 37, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (32, 'Hudi', '/images/DataSource/Hudi.png', 38, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (33, 'Iceberg', '/images/DataSource/Iceberg.png', 39, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (34, 'InfluxDB', '/images/DataSource/InfluxDB.png', 40, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (35, 'Email', NULL, 41, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (36, 'Socket', '/images/DataSource/Socket.png', 42, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (37, 'Http', '/images/DataSource/Http.png', 42, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (38, 'OSSJindo', NULL, 43, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (39, 'Cassandra', '/images/DataSource/Cassandra.png', 44, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (40, 'StarRocks', NULL, 45, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_type`(`id`, `type`, `logo`, `order`, `remark`, `creator`, `editor`)
VALUES (41, 'MaxCompute', '/images/DataSource/MaxCompute.png', 46, NULL, 'sys', 'sys');

DROP TABLE IF EXISTS carp_ds_category_type_relation;
CREATE TABLE `carp_ds_category_type_relation`
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
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (1, 1, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (1, 10, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (1, 14, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (1, 15, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (1, 21, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (1, 25, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (1, 32, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (1, 33, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (2, 1, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (2, 2, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (2, 3, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (2, 4, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (2, 5, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (2, 6, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (2, 7, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (2, 8, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (3, 9, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (3, 10, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (3, 11, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (3, 12, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (3, 13, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (4, 14, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (4, 15, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (4, 16, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (5, 17, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (5, 18, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (5, 19, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (5, 20, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (5, 21, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 22, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 23, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 24, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 25, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 26, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 27, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 28, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 29, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 30, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 31, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (7, 32, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (7, 33, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (8, 35, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (10, 34, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (10, 36, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (10, 37, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (5, 38, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (3, 39, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 40, 'sys', 'sys');
INSERT INTO `carp_ds_category_type_relation`(`ds_category_id`, `ds_type_id`, `creator`, `editor`)
VALUES (6, 41, 'sys', 'sys');

DROP TABLE IF EXISTS carp_ds_info;
CREATE TABLE `carp_ds_info`
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
    KEY              idx_datasource (`name`)
) ENGINE = InnoDB COMMENT ='data source info';

INSERT INTO `carp_ds_info`(`id`, `ds_type_id`, `version`, `name`, `props`, `additional_props`, `remark`, `creator`, `editor`)
VALUES (1, 1, NULL, 'docker_data_service',
        'eyJkcml2ZXJDbGFzc05hbWUiOiJjb20ubXlzcWwuY2ouamRiYy5Ecml2ZXIiLCJ1c2VyIjoicm9vdCIsInBhc3N3b3JkIjoiMTIzNDU2IiwidXJsIjoiamRiYzpteXNxbDovL215c3FsOjMzMDYvZGF0YV9zZXJ2aWNlIn0=',
        NULL, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_info`(`id`, `ds_type_id`, `version`, `name`, `props`, `additional_props`, `remark`, `creator`, `editor`)
VALUES (2, 1, NULL, 'local_data_service',
        'eyJkcml2ZXJDbGFzc05hbWUiOiJjb20ubXlzcWwuY2ouamRiYy5Ecml2ZXIiLCJ1c2VyIjoicm9vdCIsInBhc3N3b3JkIjoiMTIzNDU2IiwidXJsIjoiamRiYzpteXNxbDovL2xvY2FsaG9zdDozMzA2L2RhdGFfc2VydmljZSJ9',
        NULL, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_info`(`id`, `ds_type_id`, `version`, `name`, `props`, `additional_props`, `remark`, `creator`, `editor`)
VALUES (3, 9, NULL, 'docker_redis', 'eyJwYXNzd29yZCI6IkVuY3J5cHRlZDpNVEl6TkRVMiIsInBvcnQiOjYzNzksImhvc3QiOiJyZWRpcyJ9',
        NULL, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_info`(`id`, `ds_type_id`, `version`, `name`, `props`, `additional_props`, `remark`, `creator`, `editor`)
VALUES (4, 9, NULL, 'local_redis',
        'eyJwYXNzd29yZCI6IkVuY3J5cHRlZDpNVEl6TkRVMiIsInBvcnQiOjYzNzksImhvc3QiOiJsb2NhbGhvc3QifQ==', NULL, NULL, 'sys',
        'sys');
INSERT INTO `carp_ds_info`(`id`, `ds_type_id`, `version`, `name`, `props`, `additional_props`, `remark`, `creator`, `editor`)
VALUES (5, 20, NULL, 'docker_minio',
        'eyJhY2Nlc3NTZWNyZXQiOiJFbmNyeXB0ZWQ6Y0dGemMzZHZjbVE9IiwiYnVja2V0IjoiczNuOi8vbWluaW86OTAwMC9zY2FsZXBoIiwiYWNjZXNzS2V5IjoiRW5jcnlwdGVkOllXUnRhVzQ9In0=',
        NULL, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_info`(`id`, `ds_type_id`, `version`, `name`, `props`, `additional_props`, `remark`, `creator`, `editor`)
VALUES (6, 20, NULL, 'local_minio',
        'eyJhY2Nlc3NTZWNyZXQiOiJFbmNyeXB0ZWQ6Y0dGemMzZHZjbVE9IiwiYnVja2V0IjoiczNuOi8vbG9jYWxob3N0OjkwMDAvc2NhbGVwaCIsImFjY2Vzc0tleSI6IkVuY3J5cHRlZDpZV1J0YVc0PSJ9',
        NULL, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_info`(`id`, `ds_type_id`, `version`, `name`, `props`, `additional_props`, `remark`, `creator`, `editor`)
VALUES (7, 14, NULL, 'local_kafka', 'eyJib290c3RyYXBTZXJ2ZXJzIjoibG9jYWxob3N0OjkwOTIifQ==', NULL, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_info`(`id`, `ds_type_id`, `version`, `name`, `props`, `additional_props`, `remark`, `creator`, `editor`)
VALUES (8, 10, NULL, 'local_elasticsearch', 'eyJob3N0cyI6ImxvY2FsaG9zdDo5MjAwIn0=', NULL, NULL, 'sys', 'sys');
INSERT INTO `carp_ds_info`(`id`, `ds_type_id`, `version`, `name`, `props`, `additional_props`, `remark`, `creator`, `editor`)
VALUES (9, 25, NULL, 'local_doris',
        'eyJwYXNzd29yZCI6IkVuY3J5cHRlZDpjR0Z6YzNkdmNtUT0iLCJxdWVyeVBvcnQiOjkwMzAsIm5vZGVVcmxzIjoibG9jYWxob3N0OjgwMzAiLCJ1c2VybmFtZSI6InJvb3QifQ==',
        NULL, NULL, 'sys', 'sys');