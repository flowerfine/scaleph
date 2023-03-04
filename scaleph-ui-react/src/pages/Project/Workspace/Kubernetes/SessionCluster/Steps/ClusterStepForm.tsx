import {useIntl} from "umi";
import React from "react";
import {Form} from "antd";
import {ProCard, ProFormSelect} from "@ant-design/pro-components";
import {
  WsFlinkKubernetesDeploymentTemplateService
} from "@/services/project/WsFlinkKubernetesDeploymentTemplateService";
import {
  WsFlinkKubernetesDeploymentTemplate,
  WsFlinkKubernetesDeploymentTemplateParam
} from "@/services/project/typings";
import {ClusterCredentialService} from "@/services/resource/clusterCredential.service";
import {ClusterCredentialListParam} from "@/services/resource/typings";

const SessionClusterClusterStepForm: React.FC = () => {
  const intl = useIntl();
  const form = Form.useFormInstance()

  const handleTemplateChange = (value: any, option: any) => {
    const item: WsFlinkKubernetesDeploymentTemplate = option.item
    form.setFieldsValue(WsFlinkKubernetesDeploymentTemplateService.parseData(item))
  }

  return (
    <ProCard>
      <ProFormSelect
        name="template"
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.cluster.template'})}
        allowClear={false}
        fieldProps={{
          onSelect: handleTemplateChange
        }}
        request={((params, props) => {
          const param: WsFlinkKubernetesDeploymentTemplateParam = {
            name: params.keyWords,
          };
          return WsFlinkKubernetesDeploymentTemplateService.list(param).then((response) => {
            return response.data.map((item) => {
              return {label: item.name, value: item.id, item: item}
            })
          })
        })}
      />
      <ProFormSelect
        name="cluster"
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.cluster.cluster'})}
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
    </ProCard>
  )
}

export default SessionClusterClusterStepForm;
