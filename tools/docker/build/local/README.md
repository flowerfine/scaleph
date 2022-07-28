# scaleph docker image



scaleph 是依靠 flink 和 seatunnel 实现数据操作的 admin 后台系统，拖拉拽的数据集成功能需要依赖 flink 和 seatunnel 环境。seatunnel 任务最终会作为一个 flink 任务提交，而 seatunnel-core-flink.jar + 任务配置文件就是一个完整的 flink 任务。

scaleph 目前对于 flink 和 seatunnel 的依赖是通过 `FLINK_HOME` 和 `SEATUNNEL_HOME` 来实现的，需要提供服务端 flink 和 seatunnel 安装包的位置。而 scaleph-api 又是运行在容器中，无法感知到容器所在的宿主机的 flink 和 seatunnel 环境，因此 scaleph-api 镜像就有了一个很大的负担：内部打进了 flink-1.13.6 和 seatunnel-2.1.2 版本的安装包，这也是造成 scaleph-api 镜像体积过大的原因。

另外 scaleph 相关的镜像都发布在 github packages，对于国内网络不佳或者运行在云服务厂商（如阿里云）内网网络环境不好的地方，从 github 加载镜像就是一场漫长的等待。

所幸，scaleph 提供了源码镜像编译功能。开发者将代码从 github clone 下来，进行本地编译、制作镜像。

本地编译镜像时，用户无需准备 JDK、maven 或 node 环境，所有的编译过程都是运行在容器中，保证任何时候、任何地点、任何环境编译过程和结果的一致性。

## scaleph-api

scaleph-api 是服务端镜像。

制作 flink 和 seatunne 的镜像

```shell
docker build \
    --no-cache \
    --push \
    -f tools/docker/build/flink/Dockerfile \
    --build-arg FLINK_VERSION=${{ env.FLINK_VERSION }} \
    --build-arg SCALA_VERSION=${{ env.SCALA_VERSION }} \
    -t ${{ env.HUB }}:${{ env.FLINK_VERSION }}_${{ env.SCALA_VERSION }} \
    .

docker build \
    --no-cache \
    --push \
    -f tools/docker/build/seatunnel/Dockerfile \
    --build-arg SEATUNNEL_VERSION=${{ env.SEATUNNEL_VERSION }} \
    -t ${{ env.HUB }}:${{ env.SEATUNNEL_VERSION }} \
    .
```



## scaleph-ui

scaleph-ui 是服务端镜像。



### 后续

现如今 scaleph 的代码是和 flink-1.13.6 和 seatunnel-2.1.2 版本强耦合的（因为 seatunnel-2.1.2 依赖的是 flink-1.13.6 版本），scaleph 正在进行 flink 和 seatunnel 多版本支持，重新设计提交任务时对于 flink 和 seatunnel 依赖的获取方式。基于新的多版本支持的设计，scaleph-api 会摆脱镜像对于 flink 和 seatunnel 安装包的强依赖。

目前 flink 多版本支持的功能已经初步支持，很快就可以推出。

