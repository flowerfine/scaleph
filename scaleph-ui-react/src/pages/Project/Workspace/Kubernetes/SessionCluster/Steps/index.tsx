import {connect, history, useIntl} from "umi";
import React, {useRef} from "react";
import {ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import SessionClusterClusterStepForm from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Steps/ClusterStepForm";
import SessionClusterOptionsStepForm from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Steps/OptionsStepForm";
import SessionClusterYAMLStepForm from "@/pages/Project/Workspace/Kubernetes/SessionCluster/Steps/YAMLStepForm";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import {WsFlinkKubernetesSessionClusterService} from "@/services/project/WsFlinkKubernetesSessionClusterService";
import {WORKSPACE_CONF} from "@/constants/constant";
import {FieldData} from "rc-field-form/lib/interface";
import {WsFlinkKubernetesSessionCluster} from "@/services/project/typings";

const FlinkKubernetesSessionClusterSteps: React.FC = (props: any) => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();
  const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

  const onClusterStepFinish = (values: Record<string, any>) => {
    if (values.templateId) {
      return WsFlinkKubernetesSessionClusterService.fromTemplate(values.templateId).then(response => {
        const sessionCluster: WsFlinkKubernetesSessionCluster = response.data
        sessionCluster.projectId = projectId
        sessionCluster.name = values.name
        sessionCluster.clusterCredentialId = values.clusterCredentialId
        sessionCluster.namespace = values.namespace
        sessionCluster.remark = values.remark
        editSessionCluster(sessionCluster)
        return true
      })
    } else {
      const sessionCluster: WsFlinkKubernetesSessionCluster = {
        projectId: projectId,
        name: values.name,
        clusterCredentialId: values.clusterCredentialId,
        namespace: values.namespace,
        remark: values.remark,
      }
      editSessionCluster(sessionCluster)
      return Promise.resolve(true)
    }
  }

  const onFieldsChange = (changedFields: FieldData[], allFields: FieldData[]) => {
    try {
      const newTemplate = WsFlinkKubernetesTemplateService.formatData({}, formRef.current.getFieldsValue(true))
      const sessionCluster: WsFlinkKubernetesSessionCluster = {
        ...props.sessionClusterStep2.sessionCluster,
        kubernetesOptions: newTemplate.kubernetesOptions,
        jobManager: newTemplate.jobManager,
        taskManager: newTemplate.taskManager,
        flinkConfiguration: newTemplate.flinkConfiguration,
        additionalDependencies: newTemplate.additionalDependencies
      }
      editSessionCluster(sessionCluster)
    } catch (unused) {
    }
  }

  const editSessionCluster = (sessionCluster: WsFlinkKubernetesSessionCluster) => {
    props.dispatch({
      type: 'sessionClusterStep2/editSessionCluster',
      payload: sessionCluster
    })
  }

  const onAllFinish = (values: Record<string, any>) => {
    return WsFlinkKubernetesSessionClusterService.add(props.sessionClusterStep2.sessionCluster).then((response) => {
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
        onFinish={onAllFinish}>

        <StepsForm.StepForm
          name="cluster"
          title={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.cluster'})}
          style={{width: 1000}}
          onFinish={onClusterStepFinish}>
          <SessionClusterClusterStepForm/>
        </StepsForm.StepForm>

        <StepsForm.StepForm
          name="options"
          title={intl.formatMessage({id: 'pages.project.flink.kubernetes.session-cluster.steps.options'})}
          style={{width: 1000}}
          onFieldsChange={onFieldsChange}>
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
  )
}

const mapModelToProps = ({sessionClusterStep2}: any) => ({sessionClusterStep2})
export default connect(mapModelToProps)(FlinkKubernetesSessionClusterSteps);
