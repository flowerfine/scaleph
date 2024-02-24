import React from "react";
import {useIntl} from "umi";
import {ProCard} from "@ant-design/pro-components";
import {Divider} from "antd";
import FlinkKubernetesSessionClusterDetailYaml
  from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Detail/YAML/FlinkKubernetesSessionClusterYaml";
import FlinkKubernetesSessionClusterDetailStatusYaml
  from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Detail/YAML/FlinkKubernetesSessionClusterStatusYaml";

const FlinkKubernetesSessionClusterDetailYAMLWeb: React.FC = () => {
  const intl = useIntl();

  return (
    <ProCard.Group title={false} direction={'row'}>
      <ProCard title={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.yaml.instance'})}>
        <FlinkKubernetesSessionClusterDetailYaml/>
      </ProCard>
      <Divider type={'vertical'}/>
      <ProCard title={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.yaml.status'})}>
        <FlinkKubernetesSessionClusterDetailStatusYaml/>
      </ProCard>
    </ProCard.Group>
  );
}

export default FlinkKubernetesSessionClusterDetailYAMLWeb;
