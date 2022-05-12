# develop

Within this doc, we describe how to set up development environment for Breeze and run the server and web on IDE.

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

You would find sql scripts on `${breeze_home}/tools/docker/mysql/init.d`, 

## start backend



## start frontend

