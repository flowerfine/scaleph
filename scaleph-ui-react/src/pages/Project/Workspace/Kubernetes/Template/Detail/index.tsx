import {history, useIntl, useLocation, useModel} from "umi";
import React from "react";
import {Button, Tabs} from "antd";
import {FooterToolbar, PageContainer} from "@ant-design/pro-components";
import YAML from "yaml";
import DeploymentTemplateAdvanced from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced";
import DeploymentTemplateYAML from "@/pages/Project/Workspace/Kubernetes/Template/Detail/YAML";
import {WsFlinkKubernetesDeploymentTemplate} from "@/services/project/typings";
import {
  WsFlinkKubernetesDeploymentTemplateService
} from "@/services/project/WsFlinkKubernetesDeploymentTemplateService";

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
    if (deploymentTemplate) {
      const json = YAML.parse(deploymentTemplate)
      const template = {
        id: data.id,
        name: json.metadata?.name,
        metadata: json.metadata,
        spec: json.spec
      }
      WsFlinkKubernetesDeploymentTemplateService.update(template).then((response) => {
        if (response.success) {
          history.back()
        }
      })
    }
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
