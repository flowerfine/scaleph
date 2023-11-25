import {useIntl} from "umi";
import React, {useRef} from "react";
import {ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import {WORKSPACE_CONF} from "@/constant";
import DorisTemplateComponent from "@/pages/Project/Workspace/Doris/Template/Steps/Component";

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
          name="component"
          title={intl.formatMessage({id: 'pages.project.doris.template.steps.component'})}
          style={{width: 1000}}>
          <DorisTemplateComponent/>
        </StepsForm.StepForm>
      </StepsForm>
    </ProCard>
  )
}

export default DorisTemplateSteps;
