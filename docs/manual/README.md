
#
# 初始化
1. 上传 Flink release,当前仅支持 flink 1.13.6.
   - 资源 -> Flink Release -> 上传

   ![](../image/manual/upload_flink.png)
   ![](../image/manual/flink_release_list.png)
2. 上传 Seatunnel release,当前仅支持 v2.3.0
   - 资源 -> Seatunnel Release -> 上传
   
   ![](../image/manual/upload_seatunnel.png)
   ![](../image/manual/seatunnel_release_list.png)
   3. 上传[Seatunnel Connectors](https://repo1.maven.org/maven2/org/apache/seatunnel)
   
   ![](../image/manual/seatunnel_connectors.png)
3. 创建集群凭证

   ![](../image/manual/cluster_cred_01.png)
   ![](../image/manual/cluster_cred_02.png)

# 创建项目

1. 项目 -> 创建项目

   ![](../image/manual/create_project.png)

# 创建集群

1. 进入项目 -> 集群管理 -> 集群配置

![](../image/manual/cluster_config_01.png)
![](../image/manual/cluster_config_02.png)

2. 集群配置 -> 生成集群实例

![](../image/manual/cluster_instance_01.png)
![](../image/manual/cluster_instance_02.png)

# 创建作业

## Seatunnel 

1. 进入项目 -> 作业管理 -> Seatunnel

![](../image/manual/st_job_01.png)

2. 编辑并保存作业

![](../image/manual/st_job_02.png)

## Jar

1. 进入项目 -> 作业管理 -> Artifact -> 新增

![](../image/manual/artifact_job_01.png)

2. 上传Jar

![](../image/manual/artifact_job_02.png)

# 部署作业

1. 进入项目 -> 作业管理 -> 作业列表 -> 创建作业

![](../image/manual/deploy_job_01.png)
![](../image/manual/deploy_job_02.png)
![](../image/manual/deploy_job_03.png)
![](../image/manual/deploy_job_04.png)

2. 进入作业详情 -> 启动作业

![](../image/manual/deploy_job_05.png)
![](../image/manual/deploy_job_06.png)

3. 查看flink集群任务信息

![](../image/manual/deploy_job_07.png)