import {connect, history, useIntl, useLocation} from "umi";
import React from "react";
import {Button, Tabs} from "antd";
import {FooterToolbar, PageContainer} from "@ant-design/pro-components";
import DeploymentTemplateAdvanced from "src/pages/Project/Workspace/Kubernetes/Advanced";
import FlinkKubernetesDeploymentTemplateYAML from "@/pages/Project/Workspace/Kubernetes/Template/Update/YAML";
import {WsFlinkKubernetesTemplate} from "@/services/project/typings";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";

const FlinkKubernetesDeploymentTemplateUpdateWeb: React.FC = (props: any) => {
  const intl = useIntl();
  const data = useLocation().state as WsFlinkKubernetesTemplate

  const onCancel = () => {
    history.back()
  }

  const onSubmit = () => {
    if (props.flinkKubernetesTemplateUpdate.template) {
      WsFlinkKubernetesTemplateService.updateTemplate(props.flinkKubernetesTemplateUpdate.template).then((response) => {
        if (response.success) {
          history.back()
        }
      })
    }
  }

  const items = [
    {label: 'Advanced', key: 'advanced', children: <DeploymentTemplateAdvanced data={data}/>},
    {label: 'YAML', key: 'yaml', children: <FlinkKubernetesDeploymentTemplateYAML data={data}/>},
  ]
  return (
    <PageContainer title={intl.formatMessage({id: 'pages.project.flink.kubernetes.template.update'})}>
      <Tabs items={items}/>
      <FooterToolbar>
        <Button onClick={onCancel}>{intl.formatMessage({id: 'app.common.operate.cancel.label'})}</Button>
        <Button type="primary" onClick={onSubmit}>{intl.formatMessage({id: 'app.common.operate.submit.label'})}</Button>
      </FooterToolbar>
    </PageContainer>
  );
}

const mapModelToProps = ({flinkKubernetesTemplateUpdate}: any) => ({flinkKubernetesTemplateUpdate})
export default connect(mapModelToProps)(FlinkKubernetesDeploymentTemplateUpdateWeb);
