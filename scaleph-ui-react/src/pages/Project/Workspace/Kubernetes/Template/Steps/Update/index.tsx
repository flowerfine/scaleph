import {connect, history, useIntl, useLocation} from "umi";
import React, {useEffect, useRef} from "react";
import {PageContainer, ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import {WsFlinkKubernetesTemplate} from "@/services/project/typings";
import FlinkKubernetesTemplateAdvancedStep
  from "@/pages/Project/Workspace/Kubernetes/Template/Steps/New/AdvancedStepForm";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import FlinkKubernetesTemplateYAMLStep from "@/pages/Project/Workspace/Kubernetes/Template/Steps/New/YAMLStepForm";

const FlinkKubernetesTemplateUpdateSteps: React.FC = (props: any) => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();
  const data = useLocation().state as WsFlinkKubernetesTemplate

  useEffect(() => {
    editFlinkKubernetesTemplate(data)
    formRef.current?.setFieldsValue(WsFlinkKubernetesTemplateService.parseData(data))
  }, []);

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
    return WsFlinkKubernetesTemplateService.updateTemplate(props.flinkKubernetesTemplateSteps.template).then((response) => {
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
export default connect(mapModelToProps)(FlinkKubernetesTemplateUpdateSteps);
