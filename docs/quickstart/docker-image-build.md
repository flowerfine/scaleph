# local deploy

Scaleph release contains image only and people can deploy through container.

If people wants to deploy on bare metal machine, just bootstrap `scaleph-ui` by `npm start` and bootstrap `scaelph-api` how you bootstrap springboot project. We recommend people try to launch scaleph ui and api server by their devops infrastructures.

Scaleph local deployment would build image from source code directly, then bootstrap through `docker compose`, includes project dependency: mysql, redis and minio.

```shell
# clone 仓库
git clone https://github.com/flowerfine/scaleph.git --depth 1

# 编译并启动容器
cd scaleph/tools/docker/build/scaleph
docker compose up -d
```

Scaleph image contains flink and seatunnel release, that's why them such big. If flink and seatunnel release downloads are too slow, peopele can replace it as follows:

```shell
cd $SCALEPH_HOME/tools/docker/build/scaleph-api

vim Dockerfile
```

such as:

```
https://dlcdn.apache.org/flink/flink-1.13.6/flink-1.13.6-bin-scala_2.11.tgz
https://dlcdn.apache.org/incubator/seatunnel/2.2.0-beta/apache-seatunnel-incubating-2.2.0-beta-bin.tar.gz
```

