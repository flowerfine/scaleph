import React from "react";
import {ProCard} from "@ant-design/pro-components";
import AdvancedBasic from "@/pages/Project/Workspace/Engine/Compute/Flink/Advanced/AdvancedBasic";
import AdvancedResource from "@/pages/Project/Workspace/Engine/Compute/Flink/Advanced/AdvancedResource";
import AdvancedCheckpoint from "@/pages/Project/Workspace/Engine/Compute/Flink/Advanced/AdvancedCheckpoint";
import AdvancedPeriodicSavepoint
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Advanced/AdvancedPeriodicSavepoint";
import AdvancedRestart from "@/pages/Project/Workspace/Engine/Compute/Flink/Advanced/AdvancedRestart";
import AdvancedFaultTolerance
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Advanced/AdvancedFaultTolerance";
import AdvancedHighAvailability
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Advanced/AdvancedHighAvailability";
import AdvancedAdditionalDependencies
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Advanced/AdvancedAdditionalDependencies";
import AdvancedAdditional from "@/pages/Project/Workspace/Engine/Compute/Flink/Advanced/AdvancedAdditional";

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
