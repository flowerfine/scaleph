# develop

Under this doc, we describe how to set up development environment for Breeze and run the server and web on IDE.

## requirements

- Git
- Java 8
- Maven
- Mysql 8.0
- redis
- node v16.14.0
- flink
- IDE. eg. IDEA
- IDE plugin. lombok

## clone

Clone the source code repository from [github](https://github.com/flowerfine/breeze):

```shell
git clone https://github.com/flowerfine/breeze.git
mvn install -DskipTests
```

## environment

If users are familiar with docker, Breeze provides mysql, redis and flink cluster environment by docker container. People can launch container environment follows:

```shell
cd tools/docker

docker compose up -d
```

If docker is unavailable for user, you has to create database and start flink cluster by yourself.

You would find sql scripts on `${breeze_home}/tools/docker/mysql/init.d` and execute `scaleph-mysql.sql` 和 `qutz-mysql.sql` on your mysql.

Next, download [flink](https://flink.apache.org/downloads.html#apache-flink-1136) (version 1.13.6) and prepare flink environment, you could get more information on [Standalone](https://nightlies.apache.org/flink/flink-docs-release-1.13/docs/deployment/resource-providers/standalone/overview/#standalone).

## start backend

Import the Breeze project into an IDE for the development itself.

Start backend server through `cn.sliew.scalegh.Application` on `breeze-api` module.

## start frontend

Install dependencies:

```shell
cd breeze-ui

npm install -g @angular/cli
npm install
```

Start backend server:

```shell
node start
```

After web server started, user can open http://localhost:4200/ in browser and admin account is `sys_admin/123456`.

## swagger ui

After backend and frontend server started, user can access http://localhost:8080/breeze/doc.html in browser, where Breeze uses [knife4j](https://doc.xiaominfo.com/knife4j/documentation/) and [swagger](https://swagger.io/) build open api documentation.

knife4 features useful http api debug functionality supporting send request to backend server. Breeze's authentication and authorization requires that http request must contain `u_token` header, which will cause that backend server rejects knife4j requests. The solution is that user adds a global param setting within path `文档管理/全局参数设置/添加参数`, the `u_token` header value can be found on browser develop console.