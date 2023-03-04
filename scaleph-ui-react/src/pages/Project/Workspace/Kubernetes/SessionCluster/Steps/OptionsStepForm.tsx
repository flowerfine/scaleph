import React from "react";
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

const SessionClusterOptionsStepForm: React.FC = () => {
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
