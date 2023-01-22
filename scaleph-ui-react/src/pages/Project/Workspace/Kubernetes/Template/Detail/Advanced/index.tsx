import React from "react";
import {ProForm} from "@ant-design/pro-components";
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

const DeploymentTemplateAdvanced: React.FC = () => {

  return (
    <ProForm grid={true} submitter={false}>
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
