import React, {useRef} from "react";
import {PageContainer, ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import {connect, history, useIntl} from "@umijs/max";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import {WORKSPACE_CONF} from "@/constants/constant";
import {WsFlinkKubernetesDeployment} from "@/services/project/typings";
import DeploymentClusterStepForm
    from "@/pages/Project/Workspace/Engine/Compute/Flink/Deployment/Steps/New/ClusterStepForm";
import {WsFlinkKubernetesDeploymentService} from "@/services/project/WsFlinkKubernetesDeploymentService";
import DeploymentOptionsStepForm
    from "@/pages/Project/Workspace/Engine/Compute/Flink/Deployment/Steps/New/OptionsStepForm";
import DeploymentYAMLStepForm from "@/pages/Project/Workspace/Engine/Compute/Flink/Deployment/Steps/New/YAMLStepForm";

const FlinkKubernetesDeploymentNewSteps: React.FC = (props: any) => {
    const intl = useIntl();
    const formRef = useRef<ProFormInstance>();
    const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

    const onClusterStepFinish = (values: Record<string, any>) => {
        return WsFlinkKubernetesDeploymentService.fromTemplate(values.templateId).then(response => {
            const deployment: WsFlinkKubernetesDeployment = response.data
            deployment.projectId = projectId
            deployment.name = values.name
            deployment.clusterCredentialId = values.clusterCredentialId
            deployment.namespace = values.namespace
            deployment.remark = values.remark
            editDeployment(deployment)
            return true
        })
    }

    const onOptionsStepFinish = (values: Record<string, any>) => {
        try {
            const newTemplate = WsFlinkKubernetesTemplateService.formatData({}, values)
            const deployment: WsFlinkKubernetesDeployment = {
                ...props.flinkKubernetesDeploymentSteps.deployment,
                kubernetesOptions: newTemplate.kubernetesOptions,
                jobManager: newTemplate.jobManager,
                taskManager: newTemplate.taskManager,
                flinkConfiguration: newTemplate.flinkConfiguration,
                additionalDependencies: newTemplate.additionalDependencies
            }
            editDeployment(deployment)
        } catch (unused) {
        }
        return Promise.resolve(true)
    }

    const editDeployment = (deployment: WsFlinkKubernetesDeployment) => {
        props.dispatch({
            type: 'flinkKubernetesDeploymentSteps/editDeployment',
            payload: deployment
        })
    }

    const onAllFinish = (values: Record<string, any>) => {
        return WsFlinkKubernetesDeploymentService.add(props.flinkKubernetesDeploymentSteps.deployment).then((response) => {
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
                        name="cluster"
                        title={intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.steps.cluster'})}
                        style={{width: 1000}}
                        onFinish={onClusterStepFinish}>
                        <DeploymentClusterStepForm/>
                    </StepsForm.StepForm>

                    <StepsForm.StepForm
                        name="options"
                        title={intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.steps.options'})}
                        style={{width: 1000}}
                        onFinish={onOptionsStepFinish}>
                        <DeploymentOptionsStepForm/>
                    </StepsForm.StepForm>

                    <StepsForm.StepForm
                        name="yaml"
                        title={intl.formatMessage({id: 'pages.project.flink.kubernetes.deployment.steps.yaml'})}
                        style={{width: 1000}}>
                        <DeploymentYAMLStepForm/>
                    </StepsForm.StepForm>
                </StepsForm>
            </ProCard>
        </PageContainer>
    )
}

const mapModelToProps = ({flinkKubernetesDeploymentSteps}: any) => ({flinkKubernetesDeploymentSteps})
export default connect(mapModelToProps)(FlinkKubernetesDeploymentNewSteps);
