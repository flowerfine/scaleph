import {useIntl} from "umi";
import React from "react";
import {ProCard, ProFormSelect} from "@ant-design/pro-components";
import {
  WsFlinkKubernetesDeploymentTemplateService
} from "@/services/project/WsFlinkKubernetesDeploymentTemplateService";

const SessionClusterClusterStepForm: React.FC = () => {
  const intl = useIntl();

  return (
    <div>
      <ProCard>
        <ProFormSelect
          name="template"
          label={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.cluster.template'})}
          request={(params) => {
            return WsFlinkKubernetesDeploymentTemplateService.list(params).then((response) => {
              return response.data
            })
          }}
        />
      </ProCard>
    </div>
  )
}

export default SessionClusterClusterStepForm;
