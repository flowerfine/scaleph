import {useLocation} from "umi";
import React from "react";
import {Tabs} from "antd";
import {PageContainer} from "@ant-design/pro-components";
import {WsFlinkKubernetesDeployment} from "@/services/project/typings";
import FlinkKubernetesDeploymentDetailYAMLWeb from "@/pages/Project/Workspace/Kubernetes/Deployment/Detail/YAML";

const FlinkKubernetesDeploymentDetailWeb: React.FC = () => {
  const data = useLocation().state as WsFlinkKubernetesDeployment

  const items = [
    {label: 'YAML', key: 'yaml', children: <FlinkKubernetesDeploymentDetailYAMLWeb data={data}/>},
  ]
  return (
    <PageContainer>
      <Tabs items={items}/>
    </PageContainer>
  );
}

export default FlinkKubernetesDeploymentDetailWeb;
