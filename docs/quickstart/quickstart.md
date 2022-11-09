## 配置系统环境

1. 管理员登录系统
   > 地址：http://localhost    
   > 账号：sys_admin    
   > 密码：123456

   ![login image](image/login.png)

2. 设置seatunnel home
   > 下载[seatunnel](https://github.com/apache/incubator-seatunnel/releases)本地解压，进入 *系统管理 -> 系统设置*  功能中配置seatunnel home目录

   ![seatunnel_home image](image/seatunnel_home.png)
## 运行内置example任务
   > scaleph使用docker内置了一个seatunnel-examples项目，用户通过docker compose启动项目后可直接运行任务，体验项目功能。    
   > 详细操作参考[安装部署](deploy.md#docker模式)

## 开发一个数据集成任务
1. 新建数据源
   > 进入 *数据开发 -> 数据源* 功能中新建数据源，以mysql为例

   ![datasource image](image/datasource.png)
2. 新建项目
   > 进入 *数据开发 -> 项目管理* 功能中新建项目

   ![project image](image/project.png)
3. 新建作业
   > 进入 *数据开发 -> 项目管理 -> 进入项目 -> 作业管理* 功能中新建作业,成功后自动进入新标签页编辑作业流程    
   > 从左侧组件中拖入输入/输出组件，填写组件相应属性信息并进行连线    
   > 保存并发布任务    

   ![job image](image/job.png)
   ![job step image](image/job_step.png)
4. 配置flink集群
   > 进入 *数据开发 -> 集群管理* 功能中新建flink集群，当前只支持standalone模式

   ![cluster image](image/cluster.png)
5. 配置作业资源
   > 进入 *数据开发 -> 项目管理 -> 上传资源* 功能中上传作业运行需要依赖的三方jar文件，以mysql jdbc驱动为例    
   > 上传成功后，在*数据开发 -> 资源管理* 中可查看并下载资源文件    
   
   ![resource_upload image](image/resource_upload.png)
   ![resource image](image/resource.png)
6. 启动作业
   > 进入 *数据开发 -> 项目管理 -> 进入项目 -> 作业管理* 功能中配置作业定时计划    
   > 选择集群和资源，启动作业    

   ![crontab image](image/crontab.png)
   ![job run.png](image/job_run.png)
7. 查看作业日志
   > 进入 *运维中心 -> 周期任务* 功能中查看任务运行日志    
   > 点击 *详细日志* 查看flink集群中任务详细日志    

   ![job log image](image/job_log.png)
   ![job log detail image](image/job_log_detail.png)
