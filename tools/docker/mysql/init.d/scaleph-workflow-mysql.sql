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
    KEY `idx_workflow_definition` (`workflow_definition_id`)
) ENGINE = InnoDB COMMENT ='workflow schedule';

INSERT INTO `workflow_schedule`(`id`, `workflow_definition_id`, `timezone`, `crontab`, `start_time`, `end_time`,
                                `status`, `remark`, `creator`, `editor`)
VALUES (1, 1, 'UTC', '0/3 * * * * ? ', '2022-01-01 00:00:00', '2099-01-01 00:00:00', '0', NULL, 'sys', 'sys');

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
    `creator`      VARCHAR(32),
    `create_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `editor`       VARCHAR(32),
    `update_time`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_name` (`name`)
) ENGINE = InnoDB COMMENT ='workflow definition';

INSERT INTO `workflow_definition`(`id`, `type`, `name`, `execute_type`, `status`, `param`, `remark`, `creator`,
                                  `editor`)
VALUES (1, '0', 'Flink 状态', '1', '0', NULL, NULL, 'sys', 'sys');
INSERT INTO `workflow_definition`(`id`, `type`, `name`, `execute_type`, `status`, `param`, `remark`, `creator`,
                                  `editor`)
VALUES (2, '0', 'Flink savepoints', '1', '0', NULL, NULL, 'sys', 'sys');

DROP TABLE IF EXISTS `workflow_instance`;
CREATE TABLE `workflow_instance`
(
    `id`                     BIGINT     NOT NULL AUTO_INCREMENT,
    `workflow_definition_id` BIGINT     NOT NULL,
    `state`                  VARCHAR(4) NOT NULL,
    `start_time`             DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `end_time`               DATETIME,
    `message`                VARCHAR(255),
    `creator`                VARCHAR(32),
    `create_time`            DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `editor`                 VARCHAR(32),
    `update_time`            DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_workflow_definition` (`workflow_definition_id`)
) ENGINE = InnoDB COMMENT ='workflow instance';

DROP TABLE IF EXISTS `workflow_task_definition`;
CREATE TABLE `workflow_task_definition`
(
    `id`                     BIGINT       NOT NULL AUTO_INCREMENT,
    `workflow_definition_id` BIGINT       NOT NULL,
    `type`                   VARCHAR(4)   NOT NULL,
    `name`                   VARCHAR(255) NOT NULL,
    `handler`                VARCHAR(255) NOT NULL,
    `param`                  TEXT,
    `remark`                 VARCHAR(255),
    `creator`                VARCHAR(32),
    `create_time`            DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `editor`                 VARCHAR(32),
    `update_time`            DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_workflow_definition` (`workflow_definition_id`)
) ENGINE = InnoDB COMMENT ='workflow task definition';

INSERT INTO `workflow_task_definition`(`id`, `workflow_definition_id`, `type`, `name`, `handler`, `param`, `remark`,
                                       `creator`, `editor`)
VALUES (1, 1, '1', 'FlinkJobStatus', 'cn.sliew.scaleph.workflow.scheduler.quartz.LogAction', NULL, NULL, 'sys', 'sys');

DROP TABLE IF EXISTS `workflow_task_instance`;
CREATE TABLE `workflow_task_instance`
(
    `id`                          BIGINT     NOT NULL AUTO_INCREMENT,
    `workflow_task_definition_id` BIGINT     NOT NULL,
    `state`                       VARCHAR(4) NOT NULL,
    `stage`                       VARCHAR(4) NOT NULL,
    `start_time`                  DATETIME,
    `end_time`                    DATETIME,
    `message`                     VARCHAR(255),
    `creator`                     VARCHAR(32),
    `create_time`                 DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `editor`                      VARCHAR(32),
    `update_time`                 DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_workflow_task_definition` (`workflow_task_definition_id`)
) ENGINE = InnoDB COMMENT ='workflow task instance';
