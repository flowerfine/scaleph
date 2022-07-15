# Hadoop

This doc describes that how to bootstrap a Hadoop cluster based on Docker, and built on top of [big-data-europe/docker-hadoop](https://github.com/big-data-europe/docker-hadoop).

To start the cluster simploy run:

```shell
cd tools/docker/hadoop

docker compose up -d
```

User can find and access Hadoop interfaces with the following URLs:

- Namenode: http://localhost:9870/dfshealth.html#tab-overview
- History server: http://localhost:8188/applicationhistory
- Datanode: http://localhost:9864/
- Nodemanager: http://localhost:8042/node
- Resource manager: http://localhost:8088/



`docker-compose` creates a docker network that can be found by running `docker network list`, e.g. `dockerhadoop_default`.

```shell
docker network list

docker inspect network hadoop_hadoop

```



https://github.com/big-data-europe/docker-hadoop/issues/38