import {useIntl} from "umi";
import React from "react";
import {ProCard, ProFormSelect} from "@ant-design/pro-components";
import {
  WsFlinkKubernetesTemplateService
} from "@/services/project/WsFlinkKubernetesTemplateService";
import {WsFlinkKubernetesTemplateParam} from "@/services/project/typings";
import {ClusterCredentialService} from "@/services/resource/clusterCredential.service";
import {ClusterCredentialListParam} from "@/services/resource/typings";

const SessionClusterClusterStepForm: React.FC = () => {
  const intl = useIntl();

  return (
    <ProCard>
      <ProFormSelect
        name="template"
        label={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.cluster.template'})}
        allowClear={false}
        fieldProps={{
          // onSelect: handleTemplateChange
        }}
        request={((params, props) => {
          const param: WsFlinkKubernetesTemplateParam = {
            name: params.keyWords,
          };
          return WsFlinkKubernetesTemplateService.list(param).then((response) => {
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
