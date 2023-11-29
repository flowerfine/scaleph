import {connect, history, useIntl} from "umi";
import React, {useRef} from "react";
import {PageContainer, ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import {WORKSPACE_CONF} from "@/constant";
import {WsDorisInstance, WsDorisTemplate} from "@/services/project/typings";
import {WsDorisTemplateService} from "@/services/project/WsDorisTemplateService";
import DorisInstanceBase from "@/pages/Project/Workspace/Doris/Instance/Steps/BaseStepForm";

const DorisInstanceSteps: React.FC = (props: any) => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();
  const localProjectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  const onBaseStepFinish = (values: Record<string, any>) => {
    const template: WsDorisInstance = {
      projectId: localProjectId,
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
      type: 'dorisInstanceSteps/editTemplate',
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
    <PageContainer title={intl.formatMessage({id: 'pages.project.doris.instance.steps'})}>
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
            title={intl.formatMessage({id: 'pages.project.doris.instance.steps.base'})}
            style={{width: 1000}}
            onFinish={onBaseStepFinish}>
            <DorisInstanceBase/>
          </StepsForm.StepForm>
        </StepsForm>
      </ProCard>
    </PageContainer>
  )
}

const mapModelToProps = ({dorisInstanceSteps}: any) => ({dorisInstanceSteps})
export default connect(mapModelToProps)(DorisInstanceSteps);
