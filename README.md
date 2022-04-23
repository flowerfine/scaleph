# breeze

## 介绍

Breeze System

## 部署
#### 环境要求
- jdk 1.8
- mysql 8.0
- node v16.14.0

#### 前端

1. 安装依赖
   ```shell
   npm install -g @angular/cli
   npm install
   ```
2. 编译打包
   ```shell
   ng build --prod
   ```
3. 配置nginx<br/>
   将上一步打包好的dist文件夹拷贝到nginx服务器所在的html目录中，重命名为breeze。并编辑nginx.conf文件

   ```text
   http {
    # 修改为实际IP地址
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
            proxy_pass  http://xxx.com/breeze/api;
            proxy_redirect  default;
            }

        location ~ .*\.(js|css|ico|png|jpg|eot|svg|ttf|woff|html) {
            root html\breeze;
            expires 30d;
            }
        }
   }
   ```

#### 后端

1. 编译打包
   ``` shell
   mvn clean package -DskipTests
   ```
2. 启动服务
   ```shell
   java -jar breeze-api.jar
   ```