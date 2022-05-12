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

You would find sql scripts on `${breeze_home}/tools/docker/mysql/init.d` and execute `breeze-mysql.sql` 和 `qutz-mysql.sql` on your mysql.

Next, download [flink](https://flink.apache.org/downloads.html#apache-flink-1136) (version 1.13.6) and prepare flink environment, you could get more information on [Standalone](https://nightlies.apache.org/flink/flink-docs-release-1.13/docs/deployment/resource-providers/standalone/overview/#standalone).

## start backend

Import the Breeze project into an IDE for the development itself.

Start backend server through `cn.sliew.breeze.BreezeApplication` on `breeze-api` module.

## swagger ui

After backend server started, user can access http://localhost:8080/breeze/doc.html in browser, where Breeze uses [knife4j](https://doc.xiaominfo.com/knife4j/documentation/) and [swagger](https://swagger.io/) build open api documentation.

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