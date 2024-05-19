# 使用注意

通过 `docker exec -it kafka bin/bash` 进入 kafka 容器

```shell
# 进入 kafka 目录
cd /opt/bitnami/kafka

# 查看 topic
bin/kafka-topics.sh --list --bootstrap-server kafka:9092

# 读取 topic 中数据
kafka-console-consumer.sh --consumer.config /opt/bitnami/kafka/config/consumer.properties --bootstrap-server kafka:9092 --topic binlog_data_service --from-beginning
```

