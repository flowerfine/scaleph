# Local Hadoop

https://blog.csdn.net/zheng911209/article/details/105389909



配置免密登录



配置环境变量

```shell
export HADOOP_HOME=/Users/wangqi/Development/hadoop/hadoop-3.2.4
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export HADOOP_CLASSPATH="$($HADOOP_HOME/bin/hadoop classpath)"
```

当有多个 java 环境时，需要在 `hadoop-env.sh` 中指定 jdk 目录

```shell
export JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk1.8.0_201.jdk/Contents/Home"
export HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/lib:$HADOOP_HOME/lib/native"
```

修改 `core-site.xml` 文件

```xml
<property>
    <name>fs.defaultFS</name>
    <value>hdfs://hadoop:9000</value>
  </property>

  <property>
    <name>hadoop.tmp.dir</name>
    <value>/Users/wangqi/Development/hadoop/hadoop-3.2.4/data/tmp</value>
  </property>
```

同时在 `/etc/hosts` 中配置 dns

```shell
127.0.0.1	localhost hadoop namenode
255.255.255.255	broadcasthost
::1             localhost hadoop namenode
```

配置 `hdfs-site.xml` 文件

```xml
<configuration>
  <property>
    <name>fs.default.name</name>
    <value>hdfs://0.0.0.0:9000</value>
  </property>
  <!--不是root用户也可以写文件到hdfs-->
  <property>
    <name>dfs.permissions</name>
    <value>false</value>    <!--关闭防火墙-->
  </property>
  <property>
    <name>dfs.replication</name>
    <value>1</value>
  </property>

  <property>
    <name>dfs.namenode.name.dir</name>
    <value>/Users/wangqi/Development/hadoop/hadoop-3.2.4/data/name</value>
  </property>

  <property>
    <name>dfs.namenode.data.dir</name>
    <value>/Users/wangqi/Development/hadoop/hadoop-3.2.4/data/data</value>
  </property>
</configuration>
```

修改 `mapred-site.xml` 配置文件

```xml
<configuration>
  <property>
    <name>mapreduce.framework.name</name>
    <value>yarn</value>
  </property>
</configuration>
```

修改 `yarn-site.xml` 配置文件

```xml
<configuration>

<!-- Site specific YARN configuration properties -->
  <property>
    <name>yarn.nodemanager.aux-services</name>
    <value>mapreduce_shuffle</value>
  </property>
  <property>
    <name>yarn.resourcemanager.address</name>
    <value>0.0.0.0:8032</value>
  </property>
  <property>
    <name>dfs.namenode.rpc-bind-host</name>
    <value>0.0.0.0</value>
  </property>
  <property>
    <name>yarn.nodemanager.pmem-check-enabled</name>
    <value>false</value>
  </property>
  <property>
    <name>yarn.nodemanager.vmem-check-enabled</name>
    <value>false</value>
  </property>

</configuration>
```

格式化 namenode

```shell
bin/hdfs namenode -format
```

