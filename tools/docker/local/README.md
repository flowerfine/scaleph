# 使用注意

## gravitino
如果是 `mysql` 或 `postgresql`，需要添加对应的驱动实现类，位置位于 `${gravitino_home}/catalogs/jdbc-mysql/libs`。在 docker 容器中挂载 mysql 驱动时，只需添加：`/path/to/mysql.jar:/root/gravitino/catalogs/jdbc-mysql/libs/mysql.jar`。

注意：gravitino 0.5.0 版本对于 mysql 驱动只支持 5.x，不支持 8.x。