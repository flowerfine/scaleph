import {connect, history, useIntl, useModel} from "umi";
import React, {useRef} from "react";
import {ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import YAML from "yaml";
import SessionClusterClusterStepForm from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Steps/ClusterStepForm";
import SessionClusterOptionsStepForm from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Steps/OptionsStepForm";
import SessionClusterYAMLStepForm from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Steps/YAMLStepForm";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";

const FlinkKubernetesSessionClusterSteps: React.FC = (props: any) => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();
  const {template, setTemplate, sessionCluster, setSessionCluster} = useModel('sessionClusterStep');

  console.log('FlinkKubernetesSessionClusterSteps', props)

  return (
    <ProCard className={'step-form-submitter'}>
      <StepsForm
        formRef={formRef}
        formProps={{
          grid: true,
          rowProps: {gutter: [16, 8]}
        }}
        onFinish={(values) => {
          const newTemplate = WsFlinkKubernetesTemplateService.formatData(template, values)
          const param = {
            clusterCredentialId: values.cluster,
            name: newTemplate.metadata?.name,
            metadata: newTemplate.metadata,
            spec: newTemplate.spec
          }
          return WsFlinkKubernetesSessionClusterService.add(param).then((response) => {
            if (response.success) {
              history.back()
            }
          })

        }}>
        <StepsForm.StepForm
          name="cluster"
          title={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.cluster'})}
          style={{width: 1000}}
          onFinish={() => {
            const templateId = formRef.current?.getFieldsValue(true).template
            WsFlinkKubernetesTemplateService.selectOne(templateId).then((response) => {
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
            const newTemplate = WsFlinkKubernetesTemplateService.formatData(template, formRef.current?.getFieldsValue(true))
            WsFlinkKubernetesSessionClusterService.fromTemplate(newTemplate).then((response) => {
              if (response.data) {
                setSessionCluster(YAML.stringify(response.data))
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

const mapModelToProps = (model: any) => ({model})
export default connect(mapModelToProps)(FlinkKubernetesSessionClusterSteps);
