import {connect, useIntl} from "umi";
import React, {useRef} from "react";
import {ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import {WORKSPACE_CONF} from "@/constant";
import DorisTemplateComponent from "@/pages/Project/Workspace/Doris/Template/Steps/ComponentStepForm";
import DorisTemplateBase from "@/pages/Project/Workspace/Doris/Template/Steps/BaseStepForm";
import DorisTemplateYAML from "@/pages/Project/Workspace/Doris/Template/Steps/YAMLStepForm";
import {WsDorisTemplate} from "@/services/project/typings";
import {FieldData} from "rc-field-form/lib/interface";
import {WsDorisTemplateService} from "@/services/project/WsDorisTemplateService";
import {history} from "@@/core/history";

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

  const onFieldsChange = (changedFields: FieldData[], allFields: FieldData[]) => {
    try {
      const template: WsDorisTemplate = WsDorisTemplateService.formatData(props.dorisTemplateDetail.template, formRef.current.getFieldsValue(true))
      editDorisTemplate(template)
    } catch (unused) {
    }
  }

  const editDorisTemplate = (template: WsDorisTemplate) => {
    props.dispatch({
      type: 'dorisTemplateDetail/editTemplate',
      payload: template
    })
  }

  const onAllFinish = (values: Record<string, any>) => {
    return WsDorisTemplateService.add(props.dorisTemplateDetail.template).then((response) => {
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
          onFieldsChange={onFieldsChange}>
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

const mapModelToProps = ({dorisTemplateDetail}: any) => ({dorisTemplateDetail})
export default connect(mapModelToProps)(DorisTemplateSteps);
