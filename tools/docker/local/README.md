# 使用注意

## gravitino
如果是 `mysql`，需要添加对应的驱动实现类，位置位于 `${gravitino_home}/catalogs/jdbc-mysql/libs`。`postgresql` 或 `doris` 同理：

* mysql。`/path/to/mysql.jar:/root/gravitino/catalogs/jdbc-mysql/libs/mysql.jar`。
* doris。`/path/to/mysql.jar:/root/gravitino/catalogs/jdbc-doris/libs/mysql.jar`。使用 mysql 驱动
* postgresql。`/path/to/postgresql.jar:/root/gravitino/catalogs/jdbc-postgresql/libs/postgresql.jar`。