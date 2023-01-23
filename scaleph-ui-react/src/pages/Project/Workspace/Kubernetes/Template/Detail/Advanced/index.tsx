import {useModel} from "umi";
import React, {useEffect} from "react";
import {Form} from "antd";
import {ProForm} from "@ant-design/pro-components";
import {FieldData} from "rc-field-form/lib/interface";
import YAML from "yaml";
import {Props} from '@/app.d';
import AdvancedCheckpoint from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedCheckpoint";
import AdvancedBasic from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedBasic";
import AdvancedResource from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedResource";
import AdvancedSavepoint from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedSavepoint";
import AdvancedRestart from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedRestart";
import AdvancedFaultTolerance
  from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedFaultTolerance";
import AdvancedHighAvailability
  from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedHighAvailability";
import AdvancedAdditional from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedAdditional";
import {
  WsFlinkKubernetesDeploymentTemplateService
} from "@/services/project/WsFlinkKubernetesDeploymentTemplateService";
import {WsFlinkKubernetesDeploymentTemplate} from "@/services/project/typings";

const DeploymentTemplateAdvanced: React.FC<Props<WsFlinkKubernetesDeploymentTemplate>> = ({data}) => {
  const [form] = Form.useForm()
  const {deploymentTemplate, setDeploymentTemplate} = useModel('deploymentTemplateYAMLEditor');

  useEffect(() => {
    if (deploymentTemplate) {
      try {
        const json = YAML.parse(deploymentTemplate)
        const template = {
          name: json.metadata?.name,
          metadata: json.metadata,
          spec: json.spec
        }
        form.setFieldsValue(WsFlinkKubernetesDeploymentTemplateService.parseData(template))
      } catch (unused) {
      }
    }
  }, [deploymentTemplate]);

  const onFieldsChange = (changedFields: FieldData[], allFields: FieldData[]) => {
    if (deploymentTemplate) {
      try {
        const json = YAML.parse(deploymentTemplate)
        const template = {
          name: json.metadata?.name,
          metadata: json.metadata,
          spec: json.spec
        }
        const newTemplate = WsFlinkKubernetesDeploymentTemplateService.formatData(template, form.getFieldsValue(true))
        WsFlinkKubernetesDeploymentTemplateService.asTemplate(newTemplate).then((response) => {
          if (response.data) {
            setDeploymentTemplate(YAML.stringify(response.data))
          }
        })
      } catch (unused) {
      }
    }
  }

  return (
    <ProForm
      form={form}
      initialValues={WsFlinkKubernetesDeploymentTemplateService.parseData(data)}
      grid={true}
      submitter={false}
      onFieldsChange={onFieldsChange}>
      <AdvancedBasic/>
      <AdvancedCheckpoint/>
      <AdvancedSavepoint/>
      <AdvancedRestart/>
      <AdvancedFaultTolerance/>
      <AdvancedHighAvailability/>
      <AdvancedResource/>
      <AdvancedAdditional/>
    </ProForm>
  );
}

export default DeploymentTemplateAdvanced;
