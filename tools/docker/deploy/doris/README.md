# 使用说明

创建数据库

```sql
CREATE DATABASE IF NOT EXISTS ods
```

创建表

```sql
CREATE TABLE IF NOT EXISTS ods.test (
		`id` BIGINT COMMENT '自增主键',
		`invoice_no` VARCHAR ( 32 ) COMMENT '发票号码，每笔交易分配唯一的6位整数，而退货订单的代码以字母''c''开头',
		`stock_code` VARCHAR ( 32 ) COMMENT '产品代码，每个不同的产品分配唯一的5位整数',
		`description` VARCHAR ( 255 ) COMMENT '产品描述，对每件产品的简略描述',
		`quantity` INT NOT NULL COMMENT '产品数量，每笔交易的每件产品的数量',
		`invoice_date` VARCHAR ( 32 ) COMMENT '发票日期和时间，每笔交易发生的日期和时间',
		`unit_price` DECIMAL ( 20, 2 ) NOT NULL  COMMENT '单价（英镑），单位产品价格',
		`customer_id` VARCHAR ( 32 ) NOT NULL COMMENT '顾客号码，每个客户分配唯一的5位整数',
		`country` VARCHAR ( 32 ) NOT NULL COMMENT '国家的名字，每个客户所在国家/地区的名称'
		) ENGINE = OLAP 
			UNIQUE KEY ( `id` ) 
			DISTRIBUTED BY HASH ( `id` ) BUCKETS 1 
			PROPERTIES (
			"replication_allocation" = "tag.location.default: 1" 
			)
```

