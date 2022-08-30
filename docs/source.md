## WEB IDE

工欲利其事，必先利其器，一个好的源码阅读工具是必不可少的。

开发者阅读源码时，习惯于先 `fork`，在 `clone` 到本地，导入 IDE，受限于网络环境，很多人的源码阅读可能直接在 `fork` 环节阻碍而直接胎死腹中。

在这里，推荐几种在浏览器中进行源码阅读的方式：

* [github1s](https://github.com/conwnet/github1s)
  * 将 https://github.com/flowerfine/scaleph 更改为 https://github1s.com/flowerfine/scaleph ，即可在浏览器中获取 vscode 风格的源码阅读体验
* [github.dev](https://docs.github.com/cn/codespaces)
  * 在项目页面，键盘点击点号(`.`)，github 会为用户创建免费的代码空间，将用户重定向到 https://github.dev/flowerfine/scaleph
* vscode 
  * 与 github.dev 类似，在浏览器中打开 https://vscode.dev/github/flowerfine/scaleph
* 浏览器插件
  * 在浏览器应用商店中搜索 `GitHub Web IDE`，安装后可以在 github 页面 `Open in Web IDE` 按钮，点击下拉，上面介绍的几种在浏览器阅读源码方法都能支持

## 源码结构

源码目录如下：

```
scaleph
├── docs
├── scaleph-api
│   └── Dockerfile
├── scaleph-common
├── scaleph-core
│   ├── scaleph-core-di
│   └── scaleph-core-scheduler
├── scaleph-dao
├── scaleph-engine
│   ├── scaleph-engine-flink
│   └── scaleph-engine-seatunnel
├── scaleph-meta
├── scaleph-plugins
│   ├── scaleph-plugin-datasource
│   ├── scaleph-plugin-framework
│   └── scaleph-plugin-seatunnel-native-flink
├── scaleph-security
├── scaleph-support
│   ├── scaleph-cache
│   ├── scaleph-generator
│   ├── scaleph-log
│   ├── scaleph-mail
│   ├── scaleph-privilege
│   ├── scaleph-storage
│   └── scaleph-system
├── scaleph-ui
├── scaleph-ui-react
└── tools
    ├── checkstyle
    ├── docker
    ├── kubernetes
    └── spotbugs
```

### `scaleph-ui`

前端项目代码,使用Angular + DevUI编写，在 `tools/docker/build/scaleph-ui`内部包含了 `Dockerfile` 和 `nginx.conf.template` 文件，用于生成基于 nginx 的前端项目镜像。

`scaleph-ui-react`

使用 react + antd 编写的前端代码，在 `tools/docker/build/scaleph-ui-react`内部包含了 `Dockerfile` 和 `nginx.conf.template` 文件，用于生成基于 nginx 的前端项目镜像。

### `scaleph-api`

服务端项目入口，内部包含了前后端交互的所有 http endpoints。

### `scaleph-dao`

ORM 入口，项目使用 `mybatis-plus` 作为 ORM 框架，entity、mapper 和 xml 按照库名和功能组织，与数据库表名一一对应。

### `scaleph-core`

项目核心 feature，包含数据集成、调度

* `scaleph-core-di`：基于 apache seatunnel 实现的数据集成。
* `scaleph-core-scheduler`：调度系统。

### `scaleph-meta`

元数据功能，与 `scaleph-plugin-datasource` 一起实现数据源管理功能。

### `scaleph-engine`

大数据引擎模块

* `scaleph-engine-flink` ：flink 任务管理，集群管理模块。
* `scaleph-engine-seatunnel`：seatunnel 任务管理模块。

### `scaleph-plugins`

插件系统，scaleph 的插件系统以 `SPI` 为核心，提供 feature 扩展点，快速增添 feature。

* `scaleph-plugin-datasource`：在大数据平台中，数据源管理作为一个基础功能，会向上支撑数据集成、数据开发、元数据等多个核心功能。数据集成、数据开发等使用不同的引擎，引擎对外暴露的数据源参数也各不相同，数据源插件提供了快速的数据源定义功能，同时以统一的标准和形式为数据集成、数据开发和元数据提供支持。
* `scaleph-plugin-seatunnel-native-flink`：seatunnel 的 flink-connector 插件的参数定义。

### `scaleph-support`

系统通用支撑模块。

* `scaleph-cache`：缓存模块
* `scaleph-generator`：代码生成模块，现用于 mybatis-plus 的 entity、mapper、xml 生成。
* `scaleph-log`：日志模块，存储用户登录、操作日志，站内信，调度日志等。
* `scaleph-mail`：邮箱模块。如发送用户激活邮件。
* `scaleph-privilege`：系统权限模块。主要用于插件安全，如防止插件调用 `System.exit()` 导致系统退出。
* `scaleph-storage`：存储模块。基于 flink 文件系统实现的存储模块。
* `scaleph-system`：系统配置，字典等。

### `scaleph-security`

用户，组织，登录登出，权限等功能。
