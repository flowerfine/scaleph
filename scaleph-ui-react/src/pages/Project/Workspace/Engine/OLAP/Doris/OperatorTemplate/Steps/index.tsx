import React, {useRef} from "react";
import {PageContainer, ProCard, ProFormInstance, StepsForm} from "@ant-design/pro-components";
import {connect, history, useIntl} from "@umijs/max";
import {WORKSPACE_CONF} from "@/constants/constant";
import {WsDorisOperatorTemplate} from "@/services/project/typings";
import {WsDorisOperatorTemplateService} from "@/services/project/WsDorisOperatorTemplateService";
import DorisTemplateBase from "./BaseStepForm";
import DorisTemplateComponent from "./ComponentStepForm";
import DorisTemplateYAML from "./YAMLStepForm";

const EngineOLAPDorisTemplateSteps: React.FC = (props: any) => {
    const intl = useIntl();
    const formRef = useRef<ProFormInstance>();
    const projectId = localStorage.getItem(WORKSPACE_CONF.projectId);

    const onBaseStepFinish = (values: Record<string, any>) => {
        const template: WsDorisOperatorTemplate = {
            projectId: projectId,
            name: values.name,
            remark: values.remark,
        }
        editDorisTemplate(template)
        return Promise.resolve(true)
    }

    const onComponentStepFinish = (values: Record<string, any>) => {
        try {
            const template: WsDorisOperatorTemplate = WsDorisOperatorTemplateService.formatData(props.dorisTemplateSteps.template, values)
            editDorisTemplate(template)
        } catch (unused) {
        }
        return Promise.resolve(true)
    }

    const editDorisTemplate = (template: WsDorisOperatorTemplate) => {
        props.dispatch({
            type: 'dorisTemplateSteps/editTemplate',
            payload: template
        })
    }

    const onAllFinish = (values: Record<string, any>) => {
        return WsDorisOperatorTemplateService.add(props.dorisTemplateSteps.template).then((response) => {
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
                        onFinish={onComponentStepFinish}>
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
        </PageContainer>
    )
}

const mapModelToProps = ({dorisTemplateSteps}: any) => ({dorisTemplateSteps})
export default connect(mapModelToProps)(EngineOLAPDorisTemplateSteps);
