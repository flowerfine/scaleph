import React, {useEffect} from "react";
import {Form} from "antd";
import {ProForm} from "@ant-design/pro-components";
import {connect} from "@umijs/max";
import {FieldData} from "rc-field-form/lib/interface";
import {Props} from '@/typings';
import {WsFlinkKubernetesTemplate} from "@/services/project/typings";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import AdvancedCheckpoint from "./AdvancedCheckpoint";
import AdvancedBasic from "./AdvancedBasic";
import AdvancedResource from "./AdvancedResource";
import AdvancedPeriodicSavepoint from "./AdvancedPeriodicSavepoint";
import AdvancedRestart from "./AdvancedRestart";
import AdvancedFaultTolerance from "./AdvancedFaultTolerance";
import AdvancedHighAvailability from "./AdvancedHighAvailability";
import AdvancedAdditional from "./AdvancedAdditional";
import AdvancedAdditionalDependencies from "./AdvancedAdditionalDependencies";

const DeploymentTemplateAdvanced: React.FC<Props<WsFlinkKubernetesTemplate>> = (props: any) => {
    const [form] = Form.useForm()

    useEffect(() => {
        editTemplate(props.data)
    }, []);

    const onFieldsChange = (changedFields: FieldData[], allFields: FieldData[]) => {
        try {
            const newTemplate = WsFlinkKubernetesTemplateService.formatData(props.data, form.getFieldsValue(true))
            editTemplate(newTemplate)
        } catch (unused) {
        }
    }

    const editTemplate = (template: WsFlinkKubernetesTemplate) => {
        props.dispatch({
            type: 'flinkKubernetesTemplateUpdate/editTemplate',
            payload: template
        })
    }

    return (
        <ProForm
            form={form}
            initialValues={WsFlinkKubernetesTemplateService.parseData(props.data)}
            grid={true}
            submitter={false}
            onFieldsChange={onFieldsChange}>
            <AdvancedBasic/>
            <AdvancedResource/>
            <AdvancedCheckpoint/>
            <AdvancedPeriodicSavepoint/>
            <AdvancedRestart/>
            <AdvancedFaultTolerance/>
            <AdvancedHighAvailability/>
            <AdvancedAdditionalDependencies/>
            <AdvancedAdditional/>
        </ProForm>
    );
}


const mapModelToProps = ({flinkKubernetesTemplateUpdate}: any) => ({flinkKubernetesTemplateUpdate})
export default connect(mapModelToProps)(DeploymentTemplateAdvanced);
