# 使用说明

使用 `values.yaml` 安装

```shell
helm install flink-kubernetes-operator flink-kubernetes-operator-1.8.0/flink-kubernetes-operator \
	--values values.yaml
```

使用 `--set` 安装

```shell
helm install flink-kubernetes-operator flink-kubernetes-operator-1.8.0/flink-kubernetes-operator 
	--set webhook.create=false \
	--set image.repository=apache/flink-kubernetes-operator \
	--set operatorPod.env[0].name=TZ,operatorPod.env[0].value=Asia/Shanghai \
	--set postStart.exec.command={ /bin/sh, -c, wget, https://repo.maven.apache.org/maven2/org/apache/flink/flink-s3-fs-hadoop/1.18.1/flink-s3-fs-hadoop-1.18.1.jar, -O, /opt/flink/plugins/flink-s3-fs-hadoop-1.18.1.jar }
```

使用 `--repo` 安装

```shell
helm upgrade --install flink-kubernetes-operator flink-kubernetes-operator-1.8.0 \
	--repo https://archive.apache.org/dist/flink/flink-kubernetes-operator-1.8.0/ \
	--values tools/kubernetes/flink/values.yaml
```

