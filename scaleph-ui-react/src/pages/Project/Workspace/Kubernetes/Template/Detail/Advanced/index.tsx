import {connect} from "umi";
import React, {useEffect} from "react";
import {Form} from "antd";
import {ProForm} from "@ant-design/pro-components";
import {FieldData} from "rc-field-form/lib/interface";
import {Props} from '@/app.d';
import AdvancedCheckpoint from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedCheckpoint";
import AdvancedBasic from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedBasic";
import AdvancedResource from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedResource";
import AdvancedPeriodicSavepoint from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedPeriodicSavepoint";
import AdvancedRestart from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedRestart";
import AdvancedFaultTolerance
  from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedFaultTolerance";
import AdvancedHighAvailability
  from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedHighAvailability";
import AdvancedAdditional from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedAdditional";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import {WsFlinkKubernetesTemplate} from "@/services/project/typings";

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
      type: 'templateDetail/editTemplate',
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
      <AdvancedAdditional/>
    </ProForm>
  );
}


const mapModelToProps = ({templateDetail}: any) => ({templateDetail})
export default connect(mapModelToProps)(DeploymentTemplateAdvanced);
