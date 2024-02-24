import {connect, useIntl} from "umi";
import React from "react";
import {ProCard, ProFormSelect, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {InfoCircleOutlined} from "@ant-design/icons";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import {WsFlinkKubernetesTemplateParam} from "@/services/project/typings";
import {ClusterCredentialService} from "@/services/resource/clusterCredential.service";
import {ClusterCredentialListParam} from "@/services/resource/typings";
import {WORKSPACE_CONF} from "@/constants/constant";
import {DeploymentKind} from "@/constants/enum";

const DeploymentClusterStepForm: React.FC = (props: any) => {
  const intl = useIntl();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  return (
    <ProCard>
      <ProFormText
        name="name"
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.name'})}
        rules={[{required: true}]}
      />
      <ProFormSelect
        name="clusterCredentialId"
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.steps.cluster.cluster'})}
        tooltip={{
          title: intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.steps.cluster.cluster.tooltip'}),
          icon: <InfoCircleOutlined/>,
        }}
        rules={[{required: true}]}
        allowClear={false}
        request={((params, props) => {
          const param: ClusterCredentialListParam = {
            name: params.keyWords,
          };
          return ClusterCredentialService.list(param).then((response) => {
            return response.data.map((item) => {
              return {label: item.name, value: item.id, item: item}
            })
          })
        })}
      />
      <ProFormText
        name="namespace"
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.namespace'})}
        tooltip={{
          title: intl.formatMessage({id: 'pages.project.flink.kubernetes.template.namespace.tooltip'}),
          icon: <InfoCircleOutlined/>,
        }}
        rules={[{required: true}]}
        initialValue={"default"}
      />
      <ProFormSelect
        name="templateId"
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.steps.cluster.template'})}
        rules={[{required: true}]}
        allowClear={false}
        request={((params, props) => {
          const param: WsFlinkKubernetesTemplateParam = {
            projectId: projectId,
            name: params.keyWords,
            deploymentKind: DeploymentKind.Deployment
          };
          return WsFlinkKubernetesTemplateService.list(param).then((response) => {
            return response.data.map((item) => {
              return {label: item.name, value: item.id, item: item}
            })
          })
        })}
      />
      <ProFormTextArea
        name="remark"
        label={intl.formatMessage({id: 'app.common.data.remark'})}
      />
    </ProCard>
  )
}

const mapModelToProps = ({deploymentStep}: any) => ({deploymentStep})
export default connect(mapModelToProps)(DeploymentClusterStepForm);
