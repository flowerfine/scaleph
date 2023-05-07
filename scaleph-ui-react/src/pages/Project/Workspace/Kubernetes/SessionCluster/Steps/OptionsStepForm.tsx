import {useModel} from "umi";
import React, {useEffect} from "react";
import {Form} from "antd";
import {ProCard} from "@ant-design/pro-components";
import AdvancedBasic from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedBasic";
import AdvancedCheckpoint from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedCheckpoint";
import AdvancedSavepoint from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedSavepoint";
import AdvancedRestart from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedRestart";
import AdvancedFaultTolerance
  from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedFaultTolerance";
import AdvancedHighAvailability
  from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedHighAvailability";
import AdvancedResource from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedResource";
import AdvancedAdditional from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedAdditional";
import {
  WsFlinkKubernetesTemplateService
} from "@/services/project/WsFlinkKubernetesTemplateService";

const SessionClusterOptionsStepForm: React.FC = () => {
  const form = Form.useFormInstance()

  const {template} = useModel('sessionClusterStep', (model) => ({
    template: model.template
  }));

  useEffect(() => {
    if (template) {
      form.setFieldsValue(WsFlinkKubernetesTemplateService.parseData(template))
    }
  }, [template]);

  return (
    <ProCard>
      <AdvancedBasic/>
      <AdvancedCheckpoint/>
      <AdvancedSavepoint/>
      <AdvancedRestart/>
      <AdvancedFaultTolerance/>
      <AdvancedHighAvailability/>
      <AdvancedResource/>
      <AdvancedAdditional/>
    </ProCard>
  )
}

export default SessionClusterOptionsStepForm
