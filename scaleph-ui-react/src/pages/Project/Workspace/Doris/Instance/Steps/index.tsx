import {connect, history, useIntl} from "umi";
import React, {useRef} from "react";
import {PageContainer, ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import {WORKSPACE_CONF} from "@/constants/constant";
import {WsDorisInstance, WsDorisTemplate} from "@/services/project/typings";
import {WsDorisTemplateService} from "@/services/project/WsDorisTemplateService";
import DorisInstanceBase from "@/pages/Project/Workspace/Doris/Instance/Steps/BaseStepForm";
import {WsDorisInstanceService} from "@/services/project/WsDorisInstanceService";
import DorisInstanceComponent from "@/pages/Project/Workspace/Doris/Instance/Steps/ComponentStepForm";
import DorisInstanceYAML from "@/pages/Project/Workspace/Doris/Instance/Steps/YAMLStepForm";

const DorisInstanceSteps: React.FC = (props: any) => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();
  const localProjectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  const onBaseStepFinish = (values: Record<string, any>) => {
    return WsDorisInstanceService.fromTemplate(values.templateId).then(response => {
      const instance: WsDorisInstance = response.data
      instance.projectId = localProjectId
      instance.name = values.name
      instance.clusterCredentialId = values.clusterCredentialId
      instance.namespace = values.namespace
      instance.remark = values.remark
      editDorisInstance(instance)
      return true
    })
  }

  const onComponentStepFinish = (values: Record<string, any>) => {
    try {
      const template: WsDorisTemplate = WsDorisTemplateService.formatData({}, values)
      const instance: WsDorisInstance = {
        ...props.dorisInstanceSteps.instance,
        admin: template.admin,
        feSpec: template.feSpec,
        beSpec: template.beSpec,
        cnSpec: template.cnSpec,
        brokerSpec: template.brokerSpec,
      }
      editDorisInstance(instance)
    } catch (unused) {
    }
    return Promise.resolve(true)
  }

  const editDorisInstance = (template: WsDorisInstance) => {
    props.dispatch({
      type: 'dorisInstanceSteps/editInstance',
      payload: template
    })
  }

  const onAllFinish = (values: Record<string, any>) => {
    return WsDorisInstanceService.add(props.dorisInstanceSteps.instance).then((response) => {
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
          <StepsForm.StepForm
            name="component"
            title={intl.formatMessage({id: 'pages.project.doris.instance.steps.component'})}
            style={{width: 1000}}
            onFinish={onComponentStepFinish}>
            <DorisInstanceComponent/>
          </StepsForm.StepForm>
          <StepsForm.StepForm
            name="yaml"
            title={intl.formatMessage({id: 'pages.project.doris.instance.steps.yaml'})}
            style={{width: 1000}}>
            <DorisInstanceYAML/>
          </StepsForm.StepForm>
        </StepsForm>
      </ProCard>
    </PageContainer>
  )
}

const mapModelToProps = ({dorisInstanceSteps}: any) => ({dorisInstanceSteps})
export default connect(mapModelToProps)(DorisInstanceSteps);
