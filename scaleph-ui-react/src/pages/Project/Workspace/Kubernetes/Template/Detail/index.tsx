import {useLocation} from "umi";
import React from "react";
import {Tabs} from "antd";
import DeploymentTemplateAdvanced from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/index.tsx";
import DeploymentTemplateYAML from "@/pages/Project/Workspace/Kubernetes/Template/Detail/YAML";
import {WsFlinkKubernetesDeploymentTemplate} from "@/services/project/typings";

const FlinkKubernetesDeploymentTemplateDetail: React.FC = () => {
  const data = useLocation().state as WsFlinkKubernetesDeploymentTemplate

  return (
    <Tabs>
      <Tabs.TabPane tab="Advanced" key="advanced">
        <DeploymentTemplateAdvanced/>
      </Tabs.TabPane>
      <Tabs.TabPane tab="YAML" key="yaml">
        <DeploymentTemplateYAML data={data}/>
      </Tabs.TabPane>
    </Tabs>
  );
}

export default FlinkKubernetesDeploymentTemplateDetail;
