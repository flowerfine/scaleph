create database if not exists scaleph default character set utf8mb4 collate utf8mb4_unicode_ci;
use scaleph;

/* flink 部署配置文件 */
DROP TABLE IF EXISTS flink_deploy_config_file;
CREATE TABLE flink_deploy_config_file
(
    id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    config_type TINYINT(4)  NOT NULL COMMENT '配置文件类型。0: hadoop conf, 1: kubeconfig, 2: flink-conf.yaml',
    `name`      VARCHAR(64) NOT NULL COMMENT '配置名称',
    remark      VARCHAR(256) COMMENT '备注',
    creator     VARCHAR(32) COMMENT '创建人',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    editor      VARCHAR(32) COMMENT '修改人',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (id)
) ENGINE = INNODB COMMENT = 'flink 部署配置文件';

/* flink artifact */
DROP TABLE IF EXISTS flink_artifact;
CREATE TABLE flink_artifact
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `name`        VARCHAR(255) NOT NULL COMMENT '名称',
    `path`        VARCHAR(255) NOT NULL COMMENT '存储路径',
    `entry_class` VARCHAR(255) COMMENT 'entry point class',
    `remark`      VARCHAR(256) COMMENT '备注',
    `creator`     VARCHAR(32) COMMENT '创建人',
    `create_time` TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `editor`      VARCHAR(32) COMMENT '修改人',
    `update_time` TIMESTAMP    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB COMMENT = 'flink artifact';