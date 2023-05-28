import {connect, history, useIntl, useModel} from "umi";
import React, {useRef} from "react";
import {ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import YAML from "yaml";
import SessionClusterClusterStepForm from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Steps/ClusterStepForm";
import SessionClusterOptionsStepForm from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Steps/OptionsStepForm";
import SessionClusterYAMLStepForm from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Steps/YAMLStepForm";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";
import {WORKSPACE_CONF} from "@/constant";

const FlinkKubernetesSessionClusterSteps: React.FC = (props: any) => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();
  const { sessionCluster, setSessionCluster} = useModel('sessionClusterStep');
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  const onTemplate = (templateId: number) => {
    props.dispatch({
      type: 'sessionClusterStep2/queryTemplate',
      payload: templateId
    })
  }

  return (
    <ProCard className={'step-form-submitter'}>
      <StepsForm
        formRef={formRef}
        formProps={{
          grid: true,
          rowProps: {gutter: [16, 8]}
        }}
        onFinish={(values) => {
          const newTemplate = WsFlinkKubernetesTemplateService.formatData(props.sessionClusterStep2.template, values)
          const param = {
            projectId: projectId,
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
            onTemplate(formRef.current?.getFieldsValue(true).template)
            return Promise.resolve(true)
          }}>
          <SessionClusterClusterStepForm/>
        </StepsForm.StepForm>

        <StepsForm.StepForm
          name="options"
          title={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.options'})}
          style={{width: 1000}}
          onFinish={() => {
            const newTemplate = WsFlinkKubernetesTemplateService.formatData(props.sessionClusterStep2.template, formRef.current?.getFieldsValue(true))
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

const mapModelToProps = ({sessionClusterStep2}: any) => ({sessionClusterStep2})
export default connect(mapModelToProps)(FlinkKubernetesSessionClusterSteps);
