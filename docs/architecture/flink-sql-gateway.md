# Flink Sql Gateway

起源于 [FLIP-91: Support SQL Gateway](https://cwiki.apache.org/confluence/display/FLINK/FLIP-91%3A+Support+SQL+Gateway)，Flink 1.16 版本推出了 Flink SQL Gateway，作为一个独立的进程提供 Flink SQL 任务提供服务。

通过 Flink SQL Gateway，用户所有使用 Flink SQL 的场景都能受益，比如提交 SQL 任务，查询分析数据。 

Flink SQL Gateway 提供了可插拔的 endpoint，对于 Flink SQL，使用 REST API，对于 Hive，提供 Thrift API。

## 核心概念

### Session

在 gateway 中，session 表示一个用户。每个用户在提交，关闭，获取 SQL 任务结果时，都必须先创建一个 session，在创建 session 时，可以提供配置，作为 session 的上下文，仅限 session 内生效。

### SessionManager

SessionManager 负责管理 session，用户需负责创建，关闭 session，session 有一个默认的超时时间，SessionManager 会自动清理过期的 session，同时 Gateway 也提供了 session 的心跳机制，通过心跳，客户端可维持 gateway 内 session 的存活。

现在的 SessionManager 将 session 存储在本地内存中，用户需注意限制 session 数量，防止出现 OOM。

### Operation

创建 session 后，用户在 session 内的操作即为 operation。

目前的 operation 只支持提交 Flink SQL，提交 SQL 任务后，即可获取 SQL 任务结果，查看任务状态，删除任务。

每个 session 内部，可反复提交多个 Flink SQL 任务。

## 使用方式

gateway 需要提前配置好 `FLINK_CONF_DIR` 环境变量。在 `FLINK_CONF_DIR` 配置中需要配置 Flink 任务的执行环境。

而 gateway 目前只支持 `session` 和 `per-job` 模式，不支持 `application` 模式，`application` 模式仍需要使用通用 jar。

启动 gateway 最简单的方式是使用 `Standalone` 模式的 Flink cluster：

```shell
./bin/start-cluster.sh
```

对于其他模式，需要在 `conf/flink-conf.yaml` 文件中配置 `execution.target`：

```yaml
# yarn per-job
execution.target: yarn-per-job

# yarn session
execution.target: yarn-session
yarn.application.id: xxx

# kubernetes session
execution.target: kubernetes-session
kubernetes.cluster-id: xxx
```

启动 gateway 实例：

```shell
./bin/sql-gateway.sh start -Dsql-gateway.endpoint.rest.address=localhost
```

之后即可创建 session，提交 sql 任务。

flinkful 提供了相应的 REST 方法封装，用户可以使用 swagger 提交 http 请求

## 缺陷

* 单点问题。Flink SQL Gateway 作为一个独立的进程运行，不同的 gateway 实例之间数据无法共享，存在单点故障风险
* 数据持久化功能。gateway 数据存储在本地内存中，存在 OOM 风险，以及 gateway 故障后数据无法恢复问题
* 认证功能。缺少认证和授权功能

## 集成

* gateway 如何配置 hadoop 和 kubernetes 集群信息？
* gateway 如何解决多实例部署问题？

