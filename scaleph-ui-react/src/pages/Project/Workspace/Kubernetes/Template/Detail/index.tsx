import {history, useIntl, useLocation, useModel} from "umi";
import React from "react";
import {Button, Tabs} from "antd";
import {FooterToolbar, PageContainer} from "@ant-design/pro-components";
import DeploymentTemplateAdvanced from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced";
import DeploymentTemplateYAML from "@/pages/Project/Workspace/Kubernetes/Template/Detail/YAML";
import {WsFlinkKubernetesDeploymentTemplate} from "@/services/project/typings";

const FlinkKubernetesDeploymentTemplateDetailWeb: React.FC = () => {
  const intl = useIntl();
  const data = useLocation().state as WsFlinkKubernetesDeploymentTemplate

  const {deploymentTemplate} = useModel('deploymentTemplateYAMLEditor', (model) => ({
    deploymentTemplate: model.deploymentTemplate
  }));

  const onCancel = () => {
    history.back()
  }

  const onSubmit = () => {
    console.log('FlinkKubernetesDeploymentTemplateDetailWeb', deploymentTemplate)
    history.back()
  }

  const items = [
    {label: 'Advanced', key: 'advanced', children: <DeploymentTemplateAdvanced data={data}/>},
    {label: 'YAML', key: 'yaml', children: <DeploymentTemplateYAML data={data}/>},
  ]
  return (
    <PageContainer>
      <Tabs items={items}/>
      <FooterToolbar>
        <Button onClick={onCancel}>{intl.formatMessage({id: 'app.common.operate.cancel.label'})}</Button>
        <Button type="primary" onClick={onSubmit}>{intl.formatMessage({id: 'app.common.operate.submit.label'})}</Button>
      </FooterToolbar>
    </PageContainer>
  );
}

export default FlinkKubernetesDeploymentTemplateDetailWeb;
