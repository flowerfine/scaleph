# 使用注意

通过 `zookeeper`、`kafka` 和 `canal-server` 建立了一个简易的 demo，通过 `canal-server` 读取 `mysql` 的 `data_service` 数据库的 binlog 数据，发送至 `kafka` 中，`kafka` 中的 topic 设置为动态 topic，默认为 `$schema_$table`，即 `data_service` 下的 `sample_data_e_commerce` 表在 `kafka` 中的 topic 为 `data_service_sample_data_e_commerce`。

用户可以在 scaleph 预定义的 `data_service` 数据中修改表中的数据，观察 `kafka` 中数据的变化。

通过 `docker exec -it kafka bin/bash` 进入 kafka 容器

```shell
# 进入 kafka 目录
cd /opt/bitnami/kafka

# 查看 topic
bin/kafka-topics.sh --list --bootstrap-server kafka:9092

# 读取 topic 中数据
# 手动修改 mysql data_service.sample_data_e_commerce 表中数据，监听 topic 中数据
kafka-console-consumer.sh --consumer.config /opt/bitnami/kafka/config/consumer.properties --bootstrap-server kafka:9092 --topic data_service_sample_data_e_commerce --from-beginning
```

