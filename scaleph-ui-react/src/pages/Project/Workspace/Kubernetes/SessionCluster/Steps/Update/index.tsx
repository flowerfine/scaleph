import {connect, history, useIntl, useLocation} from "umi";
import React, {useEffect, useRef} from "react";
import {PageContainer, ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import SessionClusterOptionsStepForm
  from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Steps/New/OptionsStepForm";
import SessionClusterYAMLStepForm from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Steps/New/YAMLStepForm";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";
import {WsFlinkKubernetesSessionCluster} from "@/services/project/typings";

const FlinkKubernetesSessionClusterUpdateSteps: React.FC = (props: any) => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();
  const data = useLocation().state as WsFlinkKubernetesSessionCluster

  useEffect(() => {
    editSessionCluster(data)
    formRef.current?.setFieldsValue(WsFlinkKubernetesTemplateService.parseData(data))
  }, []);

  const onOptionsStepFinish = (values: Record<string, any>) => {
    try {
      const newTemplate = WsFlinkKubernetesTemplateService.formatData({}, values)
      const sessionCluster: WsFlinkKubernetesSessionCluster = {
        ...props.flinkKubernetesSessionClusterSteps.sessionCluster,
        kubernetesOptions: newTemplate.kubernetesOptions,
        jobManager: newTemplate.jobManager,
        taskManager: newTemplate.taskManager,
        flinkConfiguration: newTemplate.flinkConfiguration,
        additionalDependencies: newTemplate.additionalDependencies
      }
      editSessionCluster(sessionCluster)
    } catch (unused) {
    }
    return Promise.resolve(true)
  }

  const editSessionCluster = (sessionCluster: WsFlinkKubernetesSessionCluster) => {
    props.dispatch({
      type: 'flinkKubernetesSessionClusterSteps/editSessionCluster',
      payload: sessionCluster
    })
  }

  const onAllFinish = (values: Record<string, any>) => {
    return WsFlinkKubernetesSessionClusterService.update(props.flinkKubernetesSessionClusterSteps.sessionCluster).then((response) => {
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
          onFinish={onAllFinish}>
          <StepsForm.StepForm
            name="options"
            title={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.options'})}
            style={{width: 1000}}
            onFinish={onOptionsStepFinish}>
            <SessionClusterOptionsStepForm/>
          </StepsForm.StepForm>
          <StepsForm.StepForm
            name="yaml"
            title={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.yaml'})}
            style={{width: 1000}}>
            <SessionClusterYAMLStepForm/>
          </StepsForm.StepForm>
        </StepsForm>
      </ProCard>
    </PageContainer>
  )
}

const mapModelToProps = ({flinkKubernetesSessionClusterSteps}: any) => ({flinkKubernetesSessionClusterSteps})
export default connect(mapModelToProps)(FlinkKubernetesSessionClusterUpdateSteps);
