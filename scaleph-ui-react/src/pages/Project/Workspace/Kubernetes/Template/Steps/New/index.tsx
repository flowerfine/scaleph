import {connect, history, useIntl} from "umi";
import React, {useRef} from "react";
import {PageContainer, ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import {WORKSPACE_CONF} from "@/constants/constant";
import {WsFlinkKubernetesTemplate, WsFlinkKubernetesTemplateAddParam} from "@/services/project/typings";
import FlinkKubernetesTemplateBase from "@/pages/Project/Workspace/Kubernetes/Template/Steps/New/BaseStepForm";
import FlinkKubernetesTemplateAdvancedStep from "@/pages/Project/Workspace/Kubernetes/Template/Steps/New/AdvancedStepForm";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import FlinkKubernetesTemplateYAMLStep from "@/pages/Project/Workspace/Kubernetes/Template/Steps/New/YAMLStepForm";

const FlinkKubernetesTemplateNewSteps: React.FC = (props: any) => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();
  const localProjectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  const onBaseStepFinish = (values: Record<string, any>) => {
    const template: WsFlinkKubernetesTemplate = {
      projectId: localProjectId,
      deploymentKind: {value: values.deploymentKind},
      name: values.name,
      namespace: values.namespace,
      remark: values.remark
    }
    editFlinkKubernetesTemplate(template)
    return Promise.resolve(true)
  }

  const onAdvancedStepFinish = (values: Record<string, any>) => {
    try {
      const template: WsFlinkKubernetesTemplate = WsFlinkKubernetesTemplateService.formatData(props.flinkKubernetesTemplateSteps.template, values)
      editFlinkKubernetesTemplate(template)
    } catch (unused) {
    }
    return Promise.resolve(true)
  }

  const editFlinkKubernetesTemplate = (template: WsFlinkKubernetesTemplate) => {
    props.dispatch({
      type: 'flinkKubernetesTemplateSteps/editTemplate',
      payload: template
    })
  }

  const onAllFinish = (values: Record<string, any>) => {
    const param: WsFlinkKubernetesTemplateAddParam = {...props.flinkKubernetesTemplateSteps.template}
    param.deploymentKind = props.flinkKubernetesTemplateSteps.template.deploymentKind?.value
    return WsFlinkKubernetesTemplateService.add(param).then((response) => {
      if (response.success) {
        history.back()
      }
    })
  }

  return (
    <PageContainer title={false}>
      <ProCard className={'step-form-submitter'}>
        <StepsForm
          formRef={formRef}
          formProps={{
            grid: true,
            rowProps: {gutter: [16, 8]}
          }}
          onFinish={onAllFinish}
        >
          <StepsForm.StepForm
            name="base"
            title={intl.formatMessage({id: 'pages.project.flink.kubernetes.template.step.base'})}
            style={{width: 1000}}
            onFinish={onBaseStepFinish}>
            <FlinkKubernetesTemplateBase/>
          </StepsForm.StepForm>
          <StepsForm.StepForm
            name="component"
            title={intl.formatMessage({id: 'pages.project.flink.kubernetes.template.step.advanced'})}
            style={{width: 1000}}
            onFinish={onAdvancedStepFinish}>
            <FlinkKubernetesTemplateAdvancedStep/>
          </StepsForm.StepForm>
          <StepsForm.StepForm
            name="yaml"
            title={intl.formatMessage({id: 'pages.project.flink.kubernetes.template.step.yaml'})}
            style={{width: 1100}}>
            <FlinkKubernetesTemplateYAMLStep/>
          </StepsForm.StepForm>
        </StepsForm>
      </ProCard>
    </PageContainer>
  )
}

const mapModelToProps = ({flinkKubernetesTemplateSteps}: any) => ({flinkKubernetesTemplateSteps})
export default connect(mapModelToProps)(FlinkKubernetesTemplateNewSteps);
