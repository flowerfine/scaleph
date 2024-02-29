import React from "react";
import {Divider} from "antd";
import {ProCard} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";
import FlinkKubernetesSessionClusterDetailYaml
  from "@/pages/Project/Workspace/Engine/Compute/Flink/SessionCluster/Detail/YAML/FlinkKubernetesSessionClusterYaml";
import FlinkKubernetesSessionClusterDetailStatusYaml
  from "@/pages/Project/Workspace/Engine/Compute/Flink/SessionCluster/Detail/YAML/FlinkKubernetesSessionClusterStatusYaml";

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
