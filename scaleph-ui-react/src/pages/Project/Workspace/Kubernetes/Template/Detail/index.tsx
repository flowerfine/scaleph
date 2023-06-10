import {history, useIntl, useLocation} from "umi";
import React from "react";
import {Button, Tabs} from "antd";
import {FooterToolbar, PageContainer} from "@ant-design/pro-components";
import DeploymentTemplateAdvanced from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced";
import DeploymentTemplateYAML from "@/pages/Project/Workspace/Kubernetes/Template/Detail/YAML";
import {WsFlinkKubernetesTemplate} from "@/services/project/typings";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import {connect} from "@@/exports";

const FlinkKubernetesDeploymentTemplateDetailWeb: React.FC = (props: any) => {
  const intl = useIntl();
  const data = useLocation().state as WsFlinkKubernetesTemplate

  const onCancel = () => {
    history.back()
  }

  const onSubmit = () => {
    if (props.templateDetail.template) {
      WsFlinkKubernetesTemplateService.updateTemplate(props.templateDetail.template).then((response) => {
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

const mapModelToProps = ({templateDetail}: any) => ({templateDetail})
export default connect(mapModelToProps)(FlinkKubernetesDeploymentTemplateDetailWeb);
