# Job Overview

A complete flink job lifecycle consists of `develop`, `submit`, `manage`, `monitor` and `history` stages, and flink job self will require or yield different data on different stage.

On `develop` stage, scaleph designs and develops corresponding webs and services for jar, sql and SeaTunnel job, obviously for all of them have different usage way.

* Jar needs people to upload jar artifact firstly then submit jar job. 
* On sql way, scaleph provides web sql editor for developer, who develops sql job and submit sql scripts.
* Scaleph provides click-and-drag DAG web for SeaTunnel and developer can develop data integration job visually and easily.

But on `submit`, `manage`, `monitor` and `history` stages, flink job has similar or completely same handle way.

## `develop`

On `develop` stage, different usage will require or yield different artifact:

* Jar。jar
* SQL。sql and udf
* SeaTunnel。connector DAG

### jar

When people uploads jar artifact, scaleph inserts jar artifact record into mysql.

```sql
CREATE TABLE `flink_artifact_jar` (
  `version` varchar(32) NOT NULL,
  `flink_version` varchar(32) NOT NULL,
  `entry_class` varchar(255) NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `path` varchar(255) NOT NULL
) ENGINE=InnoDB COMMENT='flink artifact jar';
```

### SeaTunnel

Scaleph provides click-and-drag web for developing SeaTunnel data integration job, which yields a connector DAG and saved as a standard graph.

```sql
CREATE TABLE `di_job` (
  `job_code` varchar(128) NOT NULL COMMENT '作业编码',
  `job_name` varchar(256) NOT NULL COMMENT '作业名称',
  `directory_id` bigint NOT NULL COMMENT '作业目录',
  `job_type` varchar(4) DEFAULT NULL COMMENT '作业类型',
  `job_owner` varchar(32) DEFAULT NULL COMMENT '负责人',
  `job_status` varchar(4) DEFAULT '1' COMMENT '作业状态 草稿、发布、归档',
  `runtime_state` varchar(4) DEFAULT '1' COMMENT '运行状态',
  `job_version` int DEFAULT '1' COMMENT '作业版本号',
  `cluster_id` int DEFAULT NULL COMMENT '集群id',
  `job_crontab` varchar(32) DEFAULT NULL COMMENT '作业调度crontab表达式'
) ENGINE=InnoDB COMMENT='数据集成-作业信息';

CREATE TABLE `di_job_attr` (
  `job_id` bigint NOT NULL COMMENT '作业id',
  `job_attr_type` varchar(4) NOT NULL COMMENT '作业参数类型',
  `job_attr_key` varchar(128) NOT NULL COMMENT '作业参数key',
  `job_attr_value` varchar(512) DEFAULT NULL COMMENT '作业参数value'
) ENGINE=InnoDB COMMENT='数据集成-作业参数';

CREATE TABLE `di_job_step` (
  `job_id` bigint NOT NULL COMMENT '作业id',
  `step_code` varchar(36) NOT NULL COMMENT '步骤编码',
  `step_title` varchar(128) NOT NULL COMMENT '步骤标题',
  `step_type` varchar(12) NOT NULL COMMENT '步骤类型',
  `step_name` varchar(128) NOT NULL COMMENT '步骤名称',
  `position_x` int NOT NULL COMMENT 'x坐标',
  `position_y` int NOT NULL COMMENT 'y坐标'
) ENGINE=InnoDB COMMENT='数据集成-作业步骤信息';

CREATE TABLE `di_job_link` (
  `job_id` bigint NOT NULL COMMENT '作业id',
  `link_code` varchar(36) NOT NULL COMMENT '作业连线编码',
  `from_step_code` varchar(36) NOT NULL COMMENT '源步骤编码',
  `to_step_code` varchar(36) NOT NULL COMMENT '目标步骤编码'
) ENGINE=InnoDB COMMENT='数据集成-作业连线';

CREATE TABLE `di_job_step_attr` (
  `job_id` bigint NOT NULL COMMENT '作业id',
  `step_code` varchar(36) NOT NULL COMMENT '步骤编码',
  `step_attr_key` varchar(128) NOT NULL COMMENT '步骤参数key',
  `step_attr_value` text COMMENT '步骤参数value'
) ENGINE=InnoDB COMMENT='数据集成-作业步骤参数';
```

### sql

not support

### version

Every developer has benefited from git, which supports source code version management. When people develops flink jobs on scaleph, job change log must be supported.

## `submit`

resource provider

deploy mode

flink version

## `manage`

`stop`, `cancel`, `savepoint`

## `monitor`

Flink self metrics plugin such as prometheus. Scaleph retrives flink cluster and job status.

## `history`

Flink job log

## job

A job must contain follow elements:

* artifact + config
* flink version,  resource provider, deploy mode
* instance
* savepoints, checkpoints