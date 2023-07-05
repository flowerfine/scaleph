import {connect, history, useIntl} from "umi";
import React, {useRef} from "react";
import {ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import {WORKSPACE_CONF} from "@/constant";
import {FieldData} from "rc-field-form/lib/interface";
import {WsFlinkKubernetesDeployment} from "@/services/project/typings";
import DeploymentClusterStepForm from "@/pages/Project/Workspace/Kubernetes/Deployment/Steps/ClusterStepForm";
import {WsFlinkKubernetesDeploymentService} from "@/services/project/WsFlinkKubernetesDeploymentService";
import DeploymentOptionsStepForm from "@/pages/Project/Workspace/Kubernetes/Deployment/Steps/OptionsStepForm";
import DeploymentYAMLStepForm from "@/pages/Project/Workspace/Kubernetes/Deployment/Steps/YAMLStepForm";

const FlinkKubernetesDeploymentSteps: React.FC = (props: any) => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  const onClusterStepFinish = (values: Record<string, any>) => {
    if (values.templateId) {
      return WsFlinkKubernetesDeploymentService.fromTemplate(values.templateId).then(response => {
        const deployment: WsFlinkKubernetesDeployment = response.data
        deployment.projectId = projectId
        deployment.name = values.name
        deployment.clusterCredentialId = values.clusterCredentialId
        deployment.namespace = values.namespace
        deployment.remark = values.remark
        editDeployment(deployment)
        return true
      })
    } else {
      const deployment: WsFlinkKubernetesDeployment = {
        projectId: projectId,
        name: values.name,
        clusterCredentialId: values.clusterCredentialId,
        namespace: values.namespace,
        remark: values.remark,
      }
      editDeployment(deployment)
      return Promise.resolve(true)
    }
  }

  const onFieldsChange = (changedFields: FieldData[], allFields: FieldData[]) => {
    try {
      const newTemplate = WsFlinkKubernetesTemplateService.formatData({}, formRef.current.getFieldsValue(true))
      const deployment: WsFlinkKubernetesDeployment = {
        ...props.deploymentStep.deployment,
        kubernetesOptions: newTemplate.kubernetesOptions,
        jobManager: newTemplate.jobManager,
        taskManager: newTemplate.taskManager,
        flinkConfiguration: newTemplate.flinkConfiguration,
      }
      editDeployment(deployment)
    } catch (unused) {
    }
  }

  const editDeployment = (deployment: WsFlinkKubernetesDeployment) => {
    props.dispatch({
      type: 'deploymentStep/editDeployment',
      payload: deployment
    })
  }

  const onAllFinish = (values: Record<string, any>) => {
    return WsFlinkKubernetesDeploymentService.add(props.deploymentStep.deployment).then((response) => {
      if (response.success) {
        history.back()
      }
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
        onFinish={onAllFinish}>

        <StepsForm.StepForm
          name="cluster"
          title={intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.steps.cluster'})}
          style={{width: 1000}}
          onFinish={onClusterStepFinish}>
          <DeploymentClusterStepForm/>
        </StepsForm.StepForm>

        <StepsForm.StepForm
          name="options"
          title={intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.steps.options'})}
          style={{width: 1000}}
          onFieldsChange={onFieldsChange}>
          <DeploymentOptionsStepForm/>
        </StepsForm.StepForm>

        <StepsForm.StepForm
          name="yaml"
          title={intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.steps.yaml'})}
          style={{width: 1000}}>
          <DeploymentYAMLStepForm/>
        </StepsForm.StepForm>

      </StepsForm>
    </ProCard>
  )
}

const mapModelToProps = ({deploymentStep}: any) => ({deploymentStep})
export default connect(mapModelToProps)(FlinkKubernetesDeploymentSteps);
