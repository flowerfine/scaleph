create database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
use scaleph;

DROP TABLE IF EXISTS `workflow_schedule`;
CREATE TABLE `workflow_schedule`
(
    `id`                     BIGINT       NOT NULL AUTO_INCREMENT,
    `workflow_definition_id` BIGINT       NOT NULL,
    `timezone`               VARCHAR(64)  NOT NULL,
    `crontab`                VARCHAR(255) NOT NULL,
    `start_time`             DATETIME,
    `end_time`               DATETIME,
    `status`                 VARCHAR(4)   NOT NULL DEFAULT '0' COMMENT '0: stop, 1: running',
    `remark`                 VARCHAR(255),
    `creator`                VARCHAR(32),
    `create_time`            DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `editor`                 VARCHAR(32),
    `update_time`            DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY                      `idx_workflow_definition` (`workflow_definition_id`)
) ENGINE = InnoDB COMMENT ='workflow schedule';

INSERT INTO `workflow_schedule`(`id`, `workflow_definition_id`, `timezone`, `crontab`, `start_time`, `end_time`,
                                `status`, `remark`, `creator`, `editor`)
VALUES (1, 1, 'UTC', '0/3 * * * * ? ', '2022-01-01 00:00:00', '2099-01-01 00:00:00', '0', NULL, 'sys', 'sys');
INSERT INTO `workflow_schedule`(`id`, `workflow_definition_id`, `timezone`, `crontab`, `start_time`, `end_time`,
                                `status`, `remark`, `creator`, `editor`)
VALUES (2, 2, 'UTC', '0/3 * * * * ? ', '2022-01-01 00:00:00', '2099-01-01 00:00:00', '0', NULL, 'sys', 'sys');
INSERT INTO `workflow_schedule`(`id`, `workflow_definition_id`, `timezone`, `crontab`, `start_time`, `end_time`,
                                `status`, `remark`, `creator`, `editor`)
VALUES (3, 3, 'UTC', '0/3 * * * * ? ', '2022-01-01 00:00:00', '2099-01-01 00:00:00', '0', NULL, 'sys', 'sys');
INSERT INTO `workflow_schedule`(`id`, `workflow_definition_id`, `timezone`, `crontab`, `start_time`, `end_time`,
                                `status`, `remark`, `creator`, `editor`)
VALUES (4, 4, 'UTC', '0/15 * * * * ? ', '2022-01-01 00:00:00', '2099-01-01 00:00:00', '0', NULL, 'sys', 'sys');

DROP TABLE IF EXISTS `workflow_definition`;
CREATE TABLE `workflow_definition`
(
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `type`         VARCHAR(4)   NOT NULL DEFAULT '1' COMMENT '0: system, 1: user',
    `name`         VARCHAR(255) NOT NULL,
    `execute_type` VARCHAR(255) NOT NULL COMMENT '0: sequential, 1: parallel, 2: dependent, 3: if, 4: switch, 5: while',
    `status`       VARCHAR(4)   NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: enabled',
    `param`        TEXT,
    `remark`       VARCHAR(255),
    `dag_id`       BIGINT       NOT NULL,
    `creator`      VARCHAR(32),
    `create_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `editor`       VARCHAR(32),
    `update_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY            `idx_name` (`name`)
) ENGINE = InnoDB COMMENT ='workflow definition';

INSERT INTO `workflow_definition`(`id`, `type`, `name`, `execute_type`, `status`, `param`, `remark`, `dag_id`,
                                  `creator`, `editor`)
VALUES (1, '0', 'FlinkSessionClusterStatusSyncJob ', '1', '0', NULL, NULL, 4, 'sys', 'sys');
INSERT INTO `workflow_definition`(`id`, `type`, `name`, `execute_type`, `status`, `param`, `remark`, `dag_id`,
                                  `creator`, `editor`)
VALUES (2, '0', 'FlinkJobStatusSyncJob', '1', '0', NULL, NULL, 5, 'sys', 'sys');
INSERT INTO `workflow_definition` (`id`, `type`, `name`, `execute_type`, `status`, `param`, `remark`, `dag_id`,
                                   `creator`, `editor`)
VALUES (3, '0', 'DorisOperatorInstanceStatusSyncJob', '1', '0', NULL, NULL, 6, 'sys', 'sys');
INSERT INTO `workflow_definition` (`id`, `type`, `name`, `execute_type`, `status`, `param`, `remark`, `dag_id`,
                                   `creator`, `editor`)
VALUES (4, '0', 'FlinkJobStatusSyncJob2', '1', '0', NULL, NULL, 7, 'sys', 'sys');