import {useIntl} from "umi";
import React, {useRef} from "react";
import {ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import SessionClusterClusterStepForm from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Steps/ClusterStepForm";
import SessionClusterOptionsStepForm from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Steps/Options";
import SessionClusterYAMLStepForm from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Steps/YAML";

const FlinkKubernetesSessionClusterSteps: React.FC = () => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();

  return (
    <ProCard className={'step-form-submitter'}>
      <StepsForm
        formRef={formRef}
        formProps={{
          grid: true,
          rowProps: {gutter: [16, 8]}
        }}>
        <StepsForm.StepForm
          name="cluster"
          title={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.cluster'})}
          style={{width: 1000}}>
          <SessionClusterClusterStepForm/>
        </StepsForm.StepForm>

        <StepsForm.StepForm
          name="options"
          title={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.options'})}
          style={{width: 1000}}>
          <SessionClusterOptionsStepForm/>
        </StepsForm.StepForm>

        <StepsForm.StepForm
          name="yaml"
          title={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.yaml'})}>
          <SessionClusterYAMLStepForm/>
        </StepsForm.StepForm>
      </StepsForm>
    </ProCard>
  )
}

export default FlinkKubernetesSessionClusterSteps;
