create database if not exists sakura default character set utf8mb4 collate utf8mb4_unicode_ci;
use sakura;

DROP TABLE IF EXISTS `catalog_database`;
CREATE TABLE `catalog_database`
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    catalog     VARCHAR(256) NOT NULL,
    `name`      VARCHAR(256) NOT NULL,
    properties  TEXT,
    remark      VARCHAR(256),
    creator     VARCHAR(32),
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    editor      VARCHAR(32),
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB COMMENT ='database';

DROP TABLE IF EXISTS `catalog_table`;
CREATE TABLE `catalog_table`
(
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    database_id    BIGINT       NOT NULL,
    kind           VARCHAR(4)   NOT NULL,
    `name`         VARCHAR(256) NOT NULL,
    properties     TEXT,
    `schema`       TEXT,
    original_query TEXT,
    expanded_query TEXT,
    remark         VARCHAR(256),
    creator        VARCHAR(32),
    create_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    editor         VARCHAR(32),
    update_time    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB COMMENT ='table';

DROP TABLE IF EXISTS `catalog_column`;
CREATE TABLE `catalog_column`
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    table_id    BIGINT       NOT NULL,
    `name`      VARCHAR(256) NOT NULL,
    `type`      VARCHAR(4)   NOT NULL,
    data_type   VARCHAR(4)   NOT NULL,
    expression  VARCHAR(256),
    `primary`   VARCHAR(4),
    remark      VARCHAR(256),
    creator     VARCHAR(32),
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    editor      VARCHAR(32),
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB COMMENT = 'column';

DROP TABLE IF EXISTS catalog_function;
CREATE TABLE `catalog_function`
(
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    database_id BIGINT       NOT NULL,
    `name`      VARCHAR(256) NOT NULL,
    class_name       VARCHAR(256) NOT NULL,
    function_language       VARCHAR(256) NOT NULL,
    function_type       VARCHAR(256) NOT NULL,
    remark      VARCHAR(256),
    creator     VARCHAR(32),
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    editor      VARCHAR(32),
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB COMMENT ='function';