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

Docker outputs network info to Terminal like this:

```json
[
    {
        "Name": "hadoop_hadoop",
        "Id": "6356095b94fa1dfd0ddf2d6eb4aba53c1e7f166febe2ef9dfd3eaec7153a01ba",
        "Created": "2022-07-16T08:04:46.884396098Z",
        "Scope": "local",
        "Driver": "bridge",
        "EnableIPv6": false,
        "IPAM": {
            "Driver": "default",
            "Options": null,
            "Config": [
                {
                    "Subnet": "172.18.0.0/16",
                    "Gateway": "172.18.0.1"
                }
            ]
        },
        "Internal": false,
        "Attachable": false,
        "Ingress": false,
        "ConfigFrom": {
            "Network": ""
        },
        "ConfigOnly": false,
        "Containers": {
            "1e3974bf4bd8358f3ffa7404bd34218bc66811eaa849f493c816160c488613f5": {
                "Name": "hadoop-nodemanager-1",
                "EndpointID": "f8829a47a988292a41650975375d31253347c4e454fabefb1a1eb2f54387f2c3",
                "MacAddress": "02:42:ac:12:00:05",
                "IPv4Address": "172.18.0.5/16",
                "IPv6Address": ""
            },
            "40adcdd1c79351e43ce255389b9a8187629553cf3f5d3f7dfd2c30d4124ed036": {
                "Name": "hadoop-namenode-1",
                "EndpointID": "d39839f6673ba5ce9e65c8d386a2b2c1a3cc9bedd1fd1f3f39890dfb349cef0c",
                "MacAddress": "02:42:ac:12:00:04",
                "IPv4Address": "172.18.0.4/16",
                "IPv6Address": ""
            },
            "78c36a73f43a78cd3b3995b7f851ce6ab64496bf3999c0a3d1780fab378bed4b": {
                "Name": "hadoop-datanode-1",
                "EndpointID": "43b3d060d76b1296db7960e9d967c0288efeaeb8b5f45c10a52ac09a533894fe",
                "MacAddress": "02:42:ac:12:00:06",
                "IPv4Address": "172.18.0.6/16",
                "IPv6Address": ""
            },
            "8051dc34d80f8bce377c7f12a9ac26d1a79cd462da6aba4bf80e8059ba5b5c81": {
                "Name": "hadoop-historyserver-1",
                "EndpointID": "cb962fff60e8fb6c0aa7cf833b7926ad6d88400bdd09cd0893fec6b2755c6d6d",
                "MacAddress": "02:42:ac:12:00:03",
                "IPv4Address": "172.18.0.3/16",
                "IPv6Address": ""
            },
            "c8a7196cb9de1c126485037b748dc359a1ade5bce2846625d563a685910a43a3": {
                "Name": "hadoop-resourcemanager-1",
                "EndpointID": "e2cb8312cfb91dc83d95766beeaf632bb30b035e0e2b38fbae09c1757205ff42",
                "MacAddress": "02:42:ac:12:00:02",
                "IPv4Address": "172.18.0.2/16",
                "IPv6Address": ""
            }
        },
        "Options": {},
        "Labels": {
            "com.docker.compose.network": "hadoop",
            "com.docker.compose.project": "hadoop",
            "com.docker.compose.version": "2.2.3"
        }
    }
]
```

User can find and access Hadoop interfaces with the following URLs:

- Namenode: http://<dockerhadoop_IP_address>:9870/dfshealth.html#tab-overview
- History server: http://<dockerhadoop_IP_address>:8188/applicationhistory
- Datanode: http://<dockerhadoop_IP_address>:9864/
- Nodemanager: http://<dockerhadoop_IP_address>:8042/node
- Resource manager: http://<dockerhadoop_IP_address>:8088/

Unluckily, this doesn't work on macOS, you can find more on https://github.com/big-data-europe/docker-hadoop/issues/98.

If user wants to access Hadoop cluster, then more works has to do：

* Exec `docker ps -a` find namenode, datanode and resourcemanager container id.
* Add `${host ip} datanode namenode ... ${namenode container id} ${datanode container id} ...` to your local hosts file.

The final `hosts` file like this:

```shell
##
# Host Database
#
# localhost is used to configure the loopback interface
# when the system is booting.  Do not change this entry.
##
127.0.0.1       localhost namenode datanode nodemanager resourcemanager historyserver 40adcdd1c793 78c36a73f43a 1e3974bf4bd8 c8a7196cb9de 8051dc34d80f
255.255.255.255 broadcasthost
::1             localhost namenode datanode nodemanager resourcemanager historyserver 40adcdd1c793 78c36a73f43a 1e3974bf4bd8 c8a7196cb9de 8051dc34d80f
```

User can find and access Hadoop interfaces with the following URLs:

- Namenode: http://localhost:9870/dfshealth.html#tab-overview
- History server: http://localhost:8188/applicationhistory
- Datanode: http://localhost:9864/
- Nodemanager: http://localhost:8042/node
- Resource manager: http://localhost:8088/

## deploy flink job

If user want to deploy flink job on Hadoop cluster, then more works has to do：

* Set client configuration parameter `dfs.client.use.datanode.hostname` to `true`
* Set client configuration parameter `dfs.datanode.use.datanode.hostname` to `true`

If you do not want to add `${namenode container id} ${datanode container id} ...` to your local hosts file, you can set datanode container hostname to `datanode` and namenode container hostname to `namenode` by `hostname` instruction to container configuraion in `docker-compose.yaml`:

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

Then you should add  `${host ip} datanode namenode nodemanager resourcemanager historyserver` to your local hosts file.

But user should realize that flinkful client can get `${container id}` only from deployed flink cluster and always throw DNS resolve exception:

```
22/07/16 16:52:36 INFO YarnClusterDescriptor: Found Web Interface 1e3974bf4bd8:39055 of application 'application_1657958703044_0003'.
Exception in thread "main" java.lang.RuntimeException: Error while creating RestClusterClient.
	at org.apache.flink.yarn.YarnClusterDescriptor.lambda$deployInternal$2(YarnClusterDescriptor.java:614)
	at cn.sliew.flink.demo.submit.JarYarnPerJobSubmitDemo.createClusterClient(JarYarnPerJobSubmitDemo.java:72)
	at cn.sliew.flink.demo.submit.JarYarnPerJobSubmitDemo.main(JarYarnPerJobSubmitDemo.java:32)
Caused by: java.net.UnknownHostException: 1e3974bf4bd8: nodename nor servname provided, or not known
	at java.base/java.net.Inet6AddressImpl.lookupAllHostAddr(Native Method)
	at java.base/java.net.InetAddress$PlatformNameService.lookupAllHostAddr(InetAddress.java:928)
```

## hadoop conf

We provide `core-site.xml` and `hdfs-site.xml` on `tools/docker/hadoop/etc` when you need hadoop conf