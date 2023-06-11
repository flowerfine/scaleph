import {connect} from "umi";
import React, {useEffect} from "react";
import {Form} from "antd";
import {ProCard} from "@ant-design/pro-components";
import AdvancedBasic from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedBasic";
import AdvancedCheckpoint from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedCheckpoint";
import AdvancedPeriodicSavepoint from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedPeriodicSavepoint";
import AdvancedRestart from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedRestart";
import AdvancedFaultTolerance
  from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedFaultTolerance";
import AdvancedHighAvailability
  from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedHighAvailability";
import AdvancedResource from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedResource";
import AdvancedAdditional from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedAdditional";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";

const SessionClusterOptionsStepForm: React.FC = (props: any) => {
  const form = Form.useFormInstance()

  console.log('SessionClusterOptionsStepForm', props)

  useEffect(() => {
    if (props.sessionClusterStep2.template) {
      form.setFieldsValue(WsFlinkKubernetesTemplateService.parseData(props.sessionClusterStep2.template))
    }
  }, [props.sessionClusterStep2.template]);

  return (
    <ProCard>
      <AdvancedBasic/>
      <AdvancedCheckpoint/>
      <AdvancedPeriodicSavepoint/>
      <AdvancedRestart/>
      <AdvancedFaultTolerance/>
      <AdvancedHighAvailability/>
      <AdvancedResource/>
      <AdvancedAdditional/>
    </ProCard>
  )
}

const mapModelToProps = ({sessionClusterStep2}: any) => ({sessionClusterStep2})
export default connect(mapModelToProps)(SessionClusterOptionsStepForm);
