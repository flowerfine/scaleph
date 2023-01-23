import {useLocation} from "umi";
import React from "react";
import {Tabs} from "antd";
import DeploymentTemplateAdvanced from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced";
import DeploymentTemplateYAML from "@/pages/Project/Workspace/Kubernetes/Template/Detail/YAML";
import {WsFlinkKubernetesDeploymentTemplate} from "@/services/project/typings";

const FlinkKubernetesDeploymentTemplateDetail: React.FC = () => {
  const data = useLocation().state as WsFlinkKubernetesDeploymentTemplate

  const items = [
    {label: 'Advanced', key: 'advanced', children: <DeploymentTemplateAdvanced data={data}/>},
    {label: 'YAML', key: 'yaml', children: <DeploymentTemplateYAML data={data}/>},
  ]
  return (
    <Tabs items={items}/>
  );
}

export default FlinkKubernetesDeploymentTemplateDetail;
