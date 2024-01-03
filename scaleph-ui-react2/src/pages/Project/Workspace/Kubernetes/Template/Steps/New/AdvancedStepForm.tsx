import React from "react";
import {ProCard} from "@ant-design/pro-components";
import AdvancedBasic from "@/pages/Project/Workspace/Kubernetes/Advanced/AdvancedBasic";
import AdvancedResource from "@/pages/Project/Workspace/Kubernetes/Advanced/AdvancedResource";
import AdvancedCheckpoint from "@/pages/Project/Workspace/Kubernetes/Advanced/AdvancedCheckpoint";
import AdvancedPeriodicSavepoint
  from "@/pages/Project/Workspace/Kubernetes/Advanced/AdvancedPeriodicSavepoint";
import AdvancedRestart from "@/pages/Project/Workspace/Kubernetes/Advanced/AdvancedRestart";
import AdvancedFaultTolerance
  from "@/pages/Project/Workspace/Kubernetes/Advanced/AdvancedFaultTolerance";
import AdvancedHighAvailability
  from "@/pages/Project/Workspace/Kubernetes/Advanced/AdvancedHighAvailability";
import AdvancedAdditionalDependencies
  from "@/pages/Project/Workspace/Kubernetes/Advanced/AdvancedAdditionalDependencies";
import AdvancedAdditional from "@/pages/Project/Workspace/Kubernetes/Advanced/AdvancedAdditional";

const FlinkKubernetesTemplateAdvancedStep: React.FC = () => {
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
  );
}

export default FlinkKubernetesTemplateAdvancedStep;
