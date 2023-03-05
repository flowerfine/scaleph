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
  WsFlinkKubernetesTemplateService
} from "@/services/project/WsFlinkKubernetesTemplateService";
import {WsFlinkKubernetesTemplate} from "@/services/project/typings";

const DeploymentTemplateAdvanced: React.FC<Props<WsFlinkKubernetesTemplate>> = ({data}) => {
  const [form] = Form.useForm()
  const {deploymentTemplate, setDeploymentTemplate} = useModel('deploymentTemplateYAMLEditor');

  useEffect(() => {
    const newTemplate = WsFlinkKubernetesTemplateService.formatData(data, form.getFieldsValue(true))
    WsFlinkKubernetesTemplateService.asTemplate(newTemplate).then((response) => {
      if (response.data) {
        setDeploymentTemplate(YAML.stringify(response.data))
      }
    })
  }, []);

  useEffect(() => {
    if (deploymentTemplate) {
      try {
        const json = YAML.parse(deploymentTemplate)
        const template = {
          id: data.id,
          name: json.metadata?.name,
          metadata: json.metadata,
          spec: json.spec
        }
        form.setFieldsValue(WsFlinkKubernetesTemplateService.parseData(template))
      } catch (unused) {
      }
    }
  }, [deploymentTemplate]);

  const onFieldsChange = (changedFields: FieldData[], allFields: FieldData[]) => {
    if (!deploymentTemplate) {
      return
    }

    const json = YAML.parse(deploymentTemplate)
    const template = {
      id: data.id,
      name: json.metadata?.name,
      metadata: json.metadata,
      spec: json.spec
    }
    try {
      const newTemplate = WsFlinkKubernetesTemplateService.formatData(template, form.getFieldsValue(true))
      WsFlinkKubernetesTemplateService.asTemplate(newTemplate).then((response) => {
        if (response.data) {
          setDeploymentTemplate(YAML.stringify(response.data))
        }
      })
    } catch (unused) {
    }
  }

  return (
    <ProForm
      form={form}
      initialValues={WsFlinkKubernetesTemplateService.parseData(data)}
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
