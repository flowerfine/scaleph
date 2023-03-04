import {useIntl, useModel} from "umi";
import React, {useRef} from "react";
import {ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import YAML from "yaml";
import SessionClusterClusterStepForm from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Steps/ClusterStepForm";
import SessionClusterOptionsStepForm from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Steps/OptionsStepForm";
import SessionClusterYAMLStepForm from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Steps/YAMLStepForm";
import {
  WsFlinkKubernetesDeploymentTemplateService
} from "@/services/project/WsFlinkKubernetesDeploymentTemplateService";

const FlinkKubernetesSessionClusterSteps: React.FC = () => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();

  const {template, setTemplate, setDeploymentTemplate} = useModel('sessionClusterStep', (model) => ({
    template: model.template,
    setTemplate: model.setTemplate,
    setDeploymentTemplate: model.setDeploymentTemplate
  }));

  return (
    <ProCard className={'step-form-submitter'}>
      <StepsForm
        formRef={formRef}
        formProps={{
          grid: true,
          rowProps: {gutter: [16, 8]}
        }}>
        <StepsForm.StepForm
          name="cluster"
          title={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.cluster'})}
          style={{width: 1000}}
          onFinish={() => {
            const templateId = formRef.current?.getFieldsValue(true).template
            WsFlinkKubernetesDeploymentTemplateService.selectOne(templateId).then((response) => {
              if (response.data) {
                setTemplate(response.data)
              }
            })
            return Promise.resolve(true)
          }}>
          <SessionClusterClusterStepForm/>
        </StepsForm.StepForm>

        <StepsForm.StepForm
          name="options"
          title={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.options'})}
          style={{width: 1000}}
          onFinish={() => {
            const newTemplate = WsFlinkKubernetesDeploymentTemplateService.formatData(template, formRef.current?.getFieldsValue(true))
            WsFlinkKubernetesDeploymentTemplateService.asTemplate(newTemplate).then((response) => {
              if (response.data) {
                setDeploymentTemplate(YAML.stringify(response.data))
              }
            })
            return Promise.resolve(true)
          }}>
          <SessionClusterOptionsStepForm/>
        </StepsForm.StepForm>

        <StepsForm.StepForm
          name="yaml"
          title={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.yaml'})}
          style={{width: 1000}}>
          <SessionClusterYAMLStepForm/>
        </StepsForm.StepForm>
      </StepsForm>
    </ProCard>
  )
}

export default FlinkKubernetesSessionClusterSteps;
