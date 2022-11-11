# build-local

Build requirements:

- Git
- Java 11
- Maven
- node >= v16.14.0

Run requirements:

* mysql
* redis
* minio

## clone

```shell
https://github.com/flowerfine/scaleph.git
cd scaleph
```

## development

### backend

#### build the project

```shell
mvn -B -U -T 4 clean package -DskipTests -Dfast
```

#### bootstrap backend server

```shell
java -jar scaleph-api.jar
```

### frontend

#### install dependencies

```shell
cd scaleph-ui-react
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

