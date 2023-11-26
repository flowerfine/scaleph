import {useIntl} from "umi";
import React, {useRef} from "react";
import {ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import {WORKSPACE_CONF} from "@/constant";
import DorisTemplateComponent from "@/pages/Project/Workspace/Doris/Template/Steps/ComponentStepForm";
import DorisTemplateBase from "@/pages/Project/Workspace/Doris/Template/Steps/BaseStepForm";
import DorisTemplateYAML from "@/pages/Project/Workspace/Doris/Template/Steps/YAMLStepForm";

const DorisTemplateSteps: React.FC = () => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  return (
    <ProCard className={'step-form-submitter'}>
      <StepsForm
        formRef={formRef}
        formProps={{
          grid: true,
          rowProps: {gutter: [16, 8]}
        }}
      >
        <StepsForm.StepForm
          name="base"
          title={intl.formatMessage({id: 'pages.project.doris.template.steps.base'})}
          style={{width: 1000}}>
          <DorisTemplateBase/>
        </StepsForm.StepForm>
        <StepsForm.StepForm
          name="component"
          title={intl.formatMessage({id: 'pages.project.doris.template.steps.component'})}
          style={{width: 1000}}>
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

export default DorisTemplateSteps;
