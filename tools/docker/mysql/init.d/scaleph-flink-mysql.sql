create database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
use scaleph;

DROP TABLE IF EXISTS flink_cluster_config;
CREATE TABLE `flink_cluster_config`
(
    `id`                    BIGINT       NOT NULL AUTO_INCREMENT,
    `name`                  VARCHAR(255) NOT NULL,
    `flink_version`         VARCHAR(32)  NOT NULL,
    `resource_provider`     VARCHAR(4)   NOT NULL,
    `deploy_mode`           VARCHAR(4)   NOT NULL,
    `flink_release_id`      BIGINT       NOT NULL,
    `cluster_credential_id` BIGINT       NOT NULL,
    `config_options`        TEXT,
    `remark`                VARCHAR(255),
    `creator`               VARCHAR(32),
    `create_time`           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `editor`                VARCHAR(32),
    `update_time`           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_name` (`name`)
) ENGINE = INNODB COMMENT = 'flink cluster config';

DROP TABLE IF EXISTS flink_cluster_instance;
CREATE TABLE `flink_cluster_instance`
(
    `id`                      BIGINT       NOT NULL AUTO_INCREMENT,
    `flink_cluster_config_id` BIGINT       NOT NULL,
    `name`                    VARCHAR(255) NOT NULL,
    `cluster_id`              VARCHAR(64)  NOT NULL,
    `web_interface_url`       VARCHAR(255) NOT NULL,
    `status`                  VARCHAR(4)   NOT NULL,
    `remark`                  VARCHAR(255),
    `creator`                 VARCHAR(32),
    `create_time`             DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `editor`                  VARCHAR(32),
    `update_time`             DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_flink_cluster_config` (`flink_cluster_config_id`),
    KEY `idx_name` (`name`)
) ENGINE = INNODB COMMENT = 'flink cluster instance';

DROP TABLE IF EXISTS flink_artifact;
CREATE TABLE flink_artifact
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(255) NOT NULL,
    `type`        VARCHAR(4)   NOT NULL COMMENT '0: Jar, 1: UDF, 2: SQL',
    `remark`      VARCHAR(255),
    `creator`     VARCHAR(32),
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `editor`      VARCHAR(32),
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_name` (`name`)
) ENGINE = INNODB COMMENT = 'flink artifact';


DROP TABLE IF EXISTS flink_artifact_jar;
CREATE TABLE flink_artifact_jar
(
    `id`                BIGINT       NOT NULL AUTO_INCREMENT,
    `flink_artifact_id` BIGINT       NOT NULL,
    `version`           varchar(32)  NOT NULL,
    `flink_version`     VARCHAR(32)  NOT NULL,
    `entry_class`       VARCHAR(255) NOT NULL,
    `file_name`         VARCHAR(255) NOT NULL,
    `path`              VARCHAR(255) NOT NULL,
    `creator`           VARCHAR(32),
    `create_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `editor`            VARCHAR(32),
    `update_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_flink_artifact` (`flink_artifact_id`)
) ENGINE = INNODB COMMENT = 'flink artifact jar';

CREATE TABLE `flink_job_config_jar`
(
    `id`                        BIGINT       NOT NULL AUTO_INCREMENT,
    `name`                      VARCHAR(255) NOT NULL,
    `flink_artifact_jar_id`     BIGINT       NOT NULL,
    `flink_cluster_config_id`   BIGINT       NOT NULL,
    `flink_cluster_instance_id` BIGINT,
    `job_config`                TEXT,
    `flink_config`              TEXT,
    `remark`                    VARCHAR(255),
    `creator`                   VARCHAR(32),
    `create_time`               DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `editor`                    VARCHAR(32),
    `update_time`               DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_name` (`name`),
    KEY `idx_flink_artifact_jar` (`flink_artifact_jar_id`),
    KEY `idx_flink_cluster_config` (`flink_cluster_config_id`),
    KEY `idx_flink_cluster_instance` (`flink_cluster_instance_id`)
) ENGINE = InnoDB COMMENT ='flink job for jar';

DROP TABLE IF EXISTS flink_job_instance;
CREATE TABLE flink_job_instance
(
    `id`                        BIGINT      NOT NULL AUTO_INCREMENT,
    `type`                      VARCHAR(4)  NOT NULL COMMENT 'job type. 0: jar, 1: sql+udf, 2: seatunnel',
    `flink_job_config_id`       BIGINT      NOT NULL,
    `flink_cluster_instance_id` BIGINT      NOT NULL,
    `job_id`                    VARCHAR(64) NOT NULL,
    `status`                    VARCHAR(4)  NOT NULL,
    `remark`                    VARCHAR(255),
    `creator`                   VARCHAR(32),
    `create_time`               DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `editor`                    VARCHAR(32),
    `update_time`               DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = INNODB COMMENT = 'flink job instance';