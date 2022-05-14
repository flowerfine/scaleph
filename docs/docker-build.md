# 系统构建

`scaleph` 基于 docker 提供快速的开发和测试运行环境。

在 `scaleph/deploy/scaleph` 目录下有 `docker-compose.yml` 文件，其中添加了程序运行依赖的 `mysql` 和 `redis` 环境。

## 打包程序

从源码中构建运行 jar

```shell
cd scaleph
mvn clean package -DskipTests -Dfast -am --projects scaleph-api 
```

构建结束后在 `scaleph-api` 模块 `target` 目录存在 `scaleph-api.jar` 文件

## 构建镜像

通过 `Dockerfile` 构建 docker 镜像

```shell
cd scaleph/deploy/scaleph
docker compose build
```

## 一键启动

一键启动项目

```shell
docker compose up -d
```

