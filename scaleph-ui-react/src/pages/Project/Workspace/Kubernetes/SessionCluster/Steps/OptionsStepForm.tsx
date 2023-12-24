import {connect} from "umi";
import React, {useEffect} from "react";
import {Form} from "antd";
import {ProCard} from "@ant-design/pro-components";
import AdvancedBasic from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedBasic";
import AdvancedCheckpoint from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedCheckpoint";
import AdvancedPeriodicSavepoint
  from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedPeriodicSavepoint";
import AdvancedRestart from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedRestart";
import AdvancedFaultTolerance
  from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedFaultTolerance";
import AdvancedHighAvailability
  from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedHighAvailability";
import AdvancedResource from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedResource";
import AdvancedAdditional from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedAdditional";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import AdvancedAdditionalDependencies
  from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedAdditionalDependencies";

const SessionClusterOptionsStepForm: React.FC = (props: any) => {
  const form = Form.useFormInstance()

  useEffect(() => {
    if (props.flinkKubernetesSessionClusterSteps.sessionCluster) {
      form.setFieldsValue(WsFlinkKubernetesTemplateService.parseData({...props.flinkKubernetesSessionClusterSteps.sessionCluster}))
    }
  }, [props.flinkKubernetesSessionClusterSteps.sessionCluster]);

  return (
    <ProCard>
      <AdvancedBasic/>
      <AdvancedResource/>
      <AdvancedCheckpoint/>
      <AdvancedPeriodicSavepoint/>
      <AdvancedRestart/>
      <AdvancedFaultTolerance/>
      <AdvancedHighAvailability/>
      <AdvancedAdditionalDependencies/>
      <AdvancedAdditional/>
    </ProCard>
  )
}

const mapModelToProps = ({flinkKubernetesSessionClusterSteps}: any) => ({flinkKubernetesSessionClusterSteps})
export default connect(mapModelToProps)(SessionClusterOptionsStepForm);
