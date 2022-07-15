# Hadoop

## quick start

This doc describes that how to bootstrap a Hadoop cluster based on Docker, and built on top of [big-data-europe/docker-hadoop](https://github.com/big-data-europe/docker-hadoop).

To start the cluster simploy run:

```shell
cd tools/docker/hadoop

docker compose up -d
```

## access Hadoop cluster

`docker-compose` creates a docker network that can be found by running `docker network list`, e.g. `hadoop_hadoop`.

```shell
docker network list

docker inspect network hadoop_hadoop
```

User can find and access Hadoop interfaces with the following URLs:

- Namenode: http://<dockerhadoop_IP_address>:9870/dfshealth.html#tab-overview
- History server: http://<dockerhadoop_IP_address>:8188/applicationhistory
- Datanode: http://<dockerhadoop_IP_address>:9864/
- Nodemanager: http://<dockerhadoop_IP_address>:8042/node
- Resource manager: http://<dockerhadoop_IP_address>:8088/

## deploy flink job

If user want to deploy flink job on Hadoop cluster, then more works has to do：

* Add `${host ip} datanode namenode ${namenode container id} ${datanode container id}` to your local hosts file.
* Set client configuration parameter `dfs.client.use.datanode.hostname` to `true`
* Set client configuration parameter `dfs.datanode.use.datanode.hostname` to `true`

If you do not want to add `${namenode container id} ${datanode container id}` to your local hosts file, you can set datanode container hostname to `datanode` and namenode container hostname to `namenode` by `hostname` instruction to container configuraion in `docker-compose.yaml`:

```yaml
...
namenode:
    image: bde2020/hadoop-namenode:2.0.0-hadoop3.2.1-java8
    hostname: namenode
...
datanode:
    image: bde2020/hadoop-datanode:2.0.0-hadoop3.2.1-java8
    hostname: datanode
...
```

Then you should add  `${host ip} datanode namenode` to your local hosts file.

But user should realize that flinkful client can get `${container id}` only from deployed flink cluster and always throw DNS resolve exception.

## hadoop conf

We provide `core-site.xml` and `hdfs-site.xml` on `tools/docker/hadoop/etc` when you need hadoop conf