import {useAccess, useIntl} from "umi";
import React from "react";
import {StepsForm} from "@ant-design/pro-components";

const FlinkKubernetesSessionClusterSteps: React.FC = () => {
  const intl = useIntl();
  const access = useAccess();

  return (
    <StepsForm>

      <StepsForm.StepForm
        name="resource"
        title={intl.formatMessage({ id: 'pages.project.job.create.step1.title' })}
      >

      </StepsForm.StepForm>

      <StepsForm.StepForm
        name="options"
        title={intl.formatMessage({ id: 'pages.project.job.create.step1.title' })}
      >

      </StepsForm.StepForm>
    </StepsForm>
  )
}

export default FlinkKubernetesSessionClusterSteps;
