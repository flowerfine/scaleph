import {useIntl} from "umi";
import React from "react";
import {StepsForm} from "@ant-design/pro-components";
import SessionClusterClusterStepForm from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Steps/ClusterStepForm";

const FlinkKubernetesSessionClusterSteps: React.FC = () => {
  const intl = useIntl();

  return (
    <StepsForm>

      <StepsForm.StepForm
        name="cluster"
        title={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.cluster'})}
      >
        <SessionClusterClusterStepForm/>
      </StepsForm.StepForm>

      <StepsForm.StepForm
        name="options"
        title={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.options'})}
      >
        <div>StepsForm.StepForm2</div>
      </StepsForm.StepForm>

      <StepsForm.StepForm
        name="yaml"
        title={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.yaml'})}
      >
        <div>StepsForm.StepForm3</div>
      </StepsForm.StepForm>
    </StepsForm>
  )
}

export default FlinkKubernetesSessionClusterSteps;
