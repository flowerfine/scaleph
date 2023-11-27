import {connect, history, useIntl} from "umi";
import React, {useRef} from "react";
import {ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import {WORKSPACE_CONF} from "@/constant";
import DorisTemplateComponent from "@/pages/Project/Workspace/Doris/Template/Steps/ComponentStepForm";
import DorisTemplateBase from "@/pages/Project/Workspace/Doris/Template/Steps/BaseStepForm";
import DorisTemplateYAML from "@/pages/Project/Workspace/Doris/Template/Steps/YAMLStepForm";
import {WsDorisTemplate} from "@/services/project/typings";
import {WsDorisTemplateService} from "@/services/project/WsDorisTemplateService";

const DorisTemplateSteps: React.FC = (props: any) => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  const onBaseStepFinish = (values: Record<string, any>) => {
    const template: WsDorisTemplate = {
      projectId: projectId,
      name: values.name,
      namespace: values.namespace,
      remark: values.remark,
    }
    editDorisTemplate(template)
    return Promise.resolve(true)
  }

  const onComponentStepFinish = (values: Record<string, any>) => {
    try {
      const template: WsDorisTemplate = WsDorisTemplateService.formatData(props.dorisTemplateSteps.template, values)
      editDorisTemplate(template)
    } catch (unused) {
    }
    return Promise.resolve(true)
  }

  const editDorisTemplate = (template: WsDorisTemplate) => {
    props.dispatch({
      type: 'dorisTemplateSteps/editTemplate',
      payload: template
    })
  }

  const onAllFinish = (values: Record<string, any>) => {
    return WsDorisTemplateService.add(props.dorisTemplateSteps.template).then((response) => {
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
        onFinish={onAllFinish}
      >
        <StepsForm.StepForm
          name="base"
          title={intl.formatMessage({id: 'pages.project.doris.template.steps.base'})}
          style={{width: 1000}}
          onFinish={onBaseStepFinish}>
          <DorisTemplateBase/>
        </StepsForm.StepForm>
        <StepsForm.StepForm
          name="component"
          title={intl.formatMessage({id: 'pages.project.doris.template.steps.component'})}
          style={{width: 1000}}
          onFinish={onComponentStepFinish}>
          <DorisTemplateComponent/>
        </StepsForm.StepForm>
        <StepsForm.StepForm
          name="yaml"
          title={intl.formatMessage({id: 'pages.project.doris.template.steps.yaml'})}
          style={{width: 1000}}>
          <DorisTemplateYAML/>
        </StepsForm.StepForm>
      </StepsForm>
    </ProCard>
  )
}

const mapModelToProps = ({dorisTemplateSteps}: any) => ({dorisTemplateSteps})
export default connect(mapModelToProps)(DorisTemplateSteps);
