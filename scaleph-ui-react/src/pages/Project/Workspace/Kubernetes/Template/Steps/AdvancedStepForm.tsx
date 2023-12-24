import React from "react";
import {ProCard} from "@ant-design/pro-components";
import AdvancedBasic from "@/pages/Project/Workspace/Kubernetes/Template/Update/Advanced/AdvancedBasic";
import AdvancedResource from "@/pages/Project/Workspace/Kubernetes/Template/Update/Advanced/AdvancedResource";
import AdvancedCheckpoint from "@/pages/Project/Workspace/Kubernetes/Template/Update/Advanced/AdvancedCheckpoint";
import AdvancedPeriodicSavepoint
  from "@/pages/Project/Workspace/Kubernetes/Template/Update/Advanced/AdvancedPeriodicSavepoint";
import AdvancedRestart from "@/pages/Project/Workspace/Kubernetes/Template/Update/Advanced/AdvancedRestart";
import AdvancedFaultTolerance
  from "@/pages/Project/Workspace/Kubernetes/Template/Update/Advanced/AdvancedFaultTolerance";
import AdvancedHighAvailability
  from "@/pages/Project/Workspace/Kubernetes/Template/Update/Advanced/AdvancedHighAvailability";
import AdvancedAdditionalDependencies
  from "@/pages/Project/Workspace/Kubernetes/Template/Update/Advanced/AdvancedAdditionalDependencies";
import AdvancedAdditional from "@/pages/Project/Workspace/Kubernetes/Template/Update/Advanced/AdvancedAdditional";

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
