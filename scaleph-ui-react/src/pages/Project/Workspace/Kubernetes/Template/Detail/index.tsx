import {useAccess, useIntl} from "umi";
import React from "react";
import {Tabs} from "antd";
import DeploymentTemplateAdvanced from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/index.tsx";
import DeploymentTemplateYAML from "@/pages/Project/Workspace/Kubernetes/Template/Detail/YAML";

const FlinkKubernetesDeploymentTemplateDetail: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();

  return (<div>
    <Tabs>
      <Tabs.TabPane tab="Advanced" key="advanced">
        <DeploymentTemplateAdvanced/>
      </Tabs.TabPane>
      <Tabs.TabPane tab="YAML" key="yaml">
        <DeploymentTemplateYAML/>
      </Tabs.TabPane>
    </Tabs>
  </div>);
}

export default FlinkKubernetesDeploymentTemplateDetail;
