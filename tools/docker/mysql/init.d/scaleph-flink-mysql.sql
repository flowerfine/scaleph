create database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
use scaleph;

/* flink 集群配置 */
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
    PRIMARY KEY (`id`)
) ENGINE = INNODB COMMENT = 'flink cluster config';

/* flink 集群实例 */
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
    KEY idx_flink_cluster_config_id (`flink_cluster_config_id`)
) ENGINE = INNODB COMMENT = 'flink cluster instance';

/* flink artifact */
DROP TABLE IF EXISTS flink_artifact;
CREATE TABLE flink_artifact
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(255) NOT NULL,
    `path`        VARCHAR(255) NOT NULL,
    `entry_class` VARCHAR(255),
    `remark`      VARCHAR(255),
    `creator`     VARCHAR(32),
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `editor`      VARCHAR(32),
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = INNODB COMMENT = 'flink artifact';

CREATE TABLE `flink_job_config_jar`
(
    `id`                        bigint       NOT NULL AUTO_INCREMENT,
    `name`                      varchar(255) NOT NULL,
    `flink_artifact_id`         bigint       NOT NULL,
    `flink_cluster_config_id`   bigint       NOT NULL,
    `flink_cluster_instance_id` bigint       NOT NULL,
    `job_config`                text,
    `flink_config`              text,
    `remark`                    varchar(255),
    `creator`                   varchar(32),
    `create_time`               datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `editor`                    varchar(32),
    `update_time`               datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB COMMENT ='flink job for jar';

/* flink job instance */
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