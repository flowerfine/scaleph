import React, {useEffect, useRef} from "react";
import {PageContainer, ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import {connect, history, useIntl, useLocation} from "@umijs/max";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import {WsFlinkKubernetesDeployment, WsFlinkKubernetesSessionCluster} from "@/services/project/typings";
import {WsFlinkKubernetesDeploymentService} from "@/services/project/WsFlinkKubernetesDeploymentService";
import DeploymentOptionsStepForm
    from "@/pages/Project/Workspace/Engine/Compute/Flink/Deployment/Steps/New/OptionsStepForm";
import DeploymentYAMLStepForm from "@/pages/Project/Workspace/Engine/Compute/Flink/Deployment/Steps/New/YAMLStepForm";

const FlinkKubernetesDeploymentUpdateSteps: React.FC = (props: any) => {
    const intl = useIntl();
    const formRef = useRef<ProFormInstance>();
    const data = useLocation().state as WsFlinkKubernetesSessionCluster

    useEffect(() => {
        editDeployment(data)
        formRef.current?.setFieldsValue(WsFlinkKubernetesTemplateService.parseData(data))
    }, []);

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
        return WsFlinkKubernetesDeploymentService.update(props.flinkKubernetesDeploymentSteps.deployment).then((response) => {
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
export default connect(mapModelToProps)(FlinkKubernetesDeploymentUpdateSteps);
