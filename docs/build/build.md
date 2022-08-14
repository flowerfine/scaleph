# build

Requirements:

- Git
- Java 8
- Maven
- Mysql 8.0
- redis
- node v16.14.0
- IDE。lombok

## clone

```shell
https://github.com/flowerfine/scaleph.git
cd scaleph
```

## development

### backend

#### build the project

```shell
mvn clean package -DskipTests
```

#### bootstrap backend server

```shell
java -jar scaleph-api.jar
```

### frontend

#### install dependencies

```shell
npm install --location=global @angular/cli
npm install --force
```

#### build the web

```shell
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

