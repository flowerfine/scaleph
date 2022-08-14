## 环境依赖

- mysql 8.0
- redis 6.0
- minio 2022
- nginx
- apache seatunnel 2.1.2
- apache flink 1.13.*

## docker模式

Scaleph为用户提供了docker环境，可以快速启动项目以及项目的依赖组件。用户只需要以下几个步骤即可部署并启动scaleph项目。

1. 确保在机器上安装了Docker以及Docker Compose
2. 克隆scaleph git仓库
3. 使用docker compose命令启动运行scaleph
    ```shell
    git clone https://github.com/flowerfine/scaleph.git
    cd scaleph/tools/docker/deploy/scaleph
    docker-compose up -d
    ```
4. 等待容器全部启动完成后，打开浏览器访问 [http://localhost](http://localhost/)即可进入系统，默认用户/密码:sys_admin/123456

## local模式
1. 安装环境依赖 (略)
2. 克隆scaleph git仓库
   ```shell
   git clone https://github.com/flowerfine/scaleph.git
   ```
3. 编译打包
   ```shell
   cd scaleph
   mvn clean package -DskipTests
   ```
4. 启动服务端
   ```shell
   # 打包完成后可在 scaleph/scaleph-api/target 目录发现scaleph-api.jar
   cd scaleph/scaleph-api/target
   java -jar scaleph-api.jar
   ```
5. 启动前端
   ```text
   前端打包后的文件在 scaleph/tools/scaleph-ui/dist 目录中，拷贝部署到nginx中，可参考 scaleph/tools/docker/build/scaleph-ui/nginx.conf.template 配置nginx文件,启动服务，访问页面。
   ```