# build-docker

As different build environment results in different result or exception, it is painful for people to find out the cause and solve that. We recommend that people builds scaleph through docker, which keep consistent build experience for everyone.

Build requirements:

- Git
- Docker

Run requirements:

* mysql
* redis
* minio

## clone

```shell
https://github.com/flowerfine/scaleph.git
cd scaleph
```

## build

### backend

#### build the project

```shell
docker run -it --rm \
--name scaleph-api-build \
-v "$(pwd)":/usr/src/mymaven \
-w /usr/src/mymaven \
maven:3.8-openjdk-8 \
mvn -B -U -T 4 clean package -DskipTests -Dfast -am --projects scaleph-api
```

For speeding up build, people can reuse cached local repository and change `settings.xml` repository settings

```shell
docker run -it --rm \
--name scaleph-api-build \
-v /path/to/settings.xml:/usr/share/maven/ref/settings.xml \
-v /path/to/repository:/usr/share/maven/ref/repository \
-v "$(pwd)":/usr/src/mymaven \
-w /usr/src/mymaven \
maven:3.8-eclipse-temurin-11 \
mvn -B -U -T 4 clean package -DskipTests -Dfast -am --projects scaleph-api
```

people can change maven central repository url as follow:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
    
  <localRepository>/usr/share/maven/ref/repository</localRepository>
  
  <mirrors>
    <mirror>
      <id>nexus-aliyun</id>
      <mirrorOf>central</mirrorOf>
      <name>Nexus aliyun</name>
      <url>https://maven.aliyun.com/repository/central</url>
    </mirror>
  </mirrors>
    
</settings>
```

#### bootstrap backend server

```shell
java -jar scaleph-api/target/scaleph-api.jar
```

### frontend

#### install dependencies

```shell
docker run -it --rm \
--name scaleph-ui-react-build \
-v "$(pwd)/scaleph-ui-react":/usr/src/mymaven \
-w /usr/src/mymaven/ \
node:16 \
npm install --force
```

#### build the web

```shell
docker run -it --rm \
--name scaleph-ui-react-build \
-v "$(pwd)/scaleph-ui-react":/usr/src/mymaven \
-w /usr/src/mymaven/ \
node:16 \
npm run build --prod
```

#### nginx setting

copy web build result which `dist` to nginx's `html` directory and rename `dist` to `scaleph`

setup `nginx.conf` as follows:

```nginx
http {
 # replaced by user ip location
 upstream xxx.com {
     server  xxx.xxx.xxx.xxx:8080;
     } 

 server {
     listen       80;
     server_name  localhost;
     underscores_in_headers on;
     location / {
         root   html;
         index  index.html index.htm;
         try_files $uri $uri/ /index.html;
         }

     location /api {
         proxy_pass  http://xxx.com/scaleph/api;
         proxy_redirect  default;
         }

     location ~ .*\.(js|css|ico|png|jpg|eot|svg|ttf|woff|html) {
         root html/scaleph;
         expires 30d;
         }
     }
}
```

