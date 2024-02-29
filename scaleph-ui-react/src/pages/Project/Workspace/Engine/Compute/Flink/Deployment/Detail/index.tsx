import React from "react";
import {Tabs} from "antd";
import {useIntl, useLocation} from "@umijs/max";
import {WsFlinkKubernetesDeployment} from "@/services/project/typings";
import FlinkKubernetesDeploymentDetailYAMLWeb from "@/pages/Project/Workspace/Engine/Compute/Flink/Deployment/Detail/YAML";
import FlinkKubernetesDeploymentDetailConfigWeb from "@/pages/Project/Workspace/Engine/Compute/Flink/Deployment/Detail/Config";
import FlinkKubernetesDeploymentDetailSnapshotWeb
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Deployment/Detail/Snapshot";

const FlinkKubernetesDeploymentDetailWeb: React.FC = () => {
  const intl = useIntl();
  const data = useLocation().state as WsFlinkKubernetesDeployment

  const items = [
    {
      label: intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.detail.config'}),
      key: 'config',
      children: <FlinkKubernetesDeploymentDetailConfigWeb data={data}/>
    },
    {
      label: intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.detail.snapshot'}),
      key: 'snapshot',
      children: <FlinkKubernetesDeploymentDetailSnapshotWeb data={data}/>
    },
    {
      label: intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.detail.yaml'}),
      key: 'yaml',
      children: <FlinkKubernetesDeploymentDetailYAMLWeb data={data}/>
    },
  ]
  return (
    <Tabs items={items}/>
  );
}

export default FlinkKubernetesDeploymentDetailWeb;
