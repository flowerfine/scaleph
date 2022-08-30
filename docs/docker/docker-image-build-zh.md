# docker image build

Scaleph 基于 docker 提供快速的开发和测试运行环境，本文将介绍如何在本地构建镜像，测试镜像。

在 `$SCALEPH_HOME/tools/docker/build` 目录下有 `docker-compose-build-api.yml` 和 `docker-compose-build-ui.yml`文件，分别用于构建 `scaleph-api` 和 `scaleph-ui` 模块。docker compose 中添加了程序运行依赖的 `mysql`、`redis` 、`minio` 等环境。

## `scaleph-api`

### 编译打包

运行如下命令，从源码中打包 scaleph-api.jar。

```shell
cd $SCALEPH_HOME
mvn clean package -DskipTests -Dfast -am --projects scaleph-api 
```

构建结束后在 `scaleph-api` 模块 `target` 目录存在 `scaleph-api.jar` 文件

### 构建镜像

通过 docker compose 和 `Dockerfile` 构建 docker 镜像

```shell
cd $SCALEPH_HOME/tools/docker/build/scaleph
docker compose -f docker-compose-build-api.yml build
```

### 测试镜像

使用 docker compose 启动项目，访问 `http://localhost` 进入前端页面，其中用户名密码为 `sys_admin/123456`。

```shell
docker compose -f docker-compose-build-api.yml up -d
```

在 `docker-compose-build-api.yml` 中使用 CI/CD 构建的 `scaleph-ui` 镜像启动前端容器。

## `scaleph-ui`

### 编译打包

运行如下命令，编译前端项目

```shell
cd $SCALEPH_HOME/scaleph-ui-react

npm install --force

npm run build --prod
```

构建结束后前端项目文件在 `scaleph-ui-react` 模块 `dist` 目录中。

### 构建镜像

通过 docker compose 和 `Dockerfile` 构建 docker 镜像

```shell
cd $SCALEPH_HOME/tools/docker/build/scaleph
docker compose -f docker-compose-build-ui.yml build
```

### 测试镜像

使用 docker compose 启动项目，访问 `http://localhost` 进入前端页面，其中用户名密码为 `sys_admin/123456`。

```shell
docker compose -f docker-compose-build-ui.yml up -d
```

在 `docker-compose-build-ui.yml` 中使用 CI/CD 构建的 `scaleph-api` 镜像启动前端容器。
