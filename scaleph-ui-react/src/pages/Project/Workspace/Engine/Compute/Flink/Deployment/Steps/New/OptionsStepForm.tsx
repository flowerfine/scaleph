import React, {useEffect} from "react";
import {Form} from "antd";
import {ProCard} from "@ant-design/pro-components";
import {connect} from "@umijs/max";
import AdvancedBasic from "@/pages/Project/Workspace/Engine/Compute/Flink/Advanced/AdvancedBasic";
import AdvancedCheckpoint from "@/pages/Project/Workspace/Engine/Compute/Flink/Advanced/AdvancedCheckpoint";
import AdvancedPeriodicSavepoint
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Advanced/AdvancedPeriodicSavepoint";
import AdvancedRestart from "@/pages/Project/Workspace/Engine/Compute/Flink/Advanced/AdvancedRestart";
import AdvancedFaultTolerance
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Advanced/AdvancedFaultTolerance";
import AdvancedHighAvailability
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Advanced/AdvancedHighAvailability";
import AdvancedResource from "@/pages/Project/Workspace/Engine/Compute/Flink/Advanced/AdvancedResource";
import AdvancedAdditional from "@/pages/Project/Workspace/Engine/Compute/Flink/Advanced/AdvancedAdditional";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import AdvancedAdditionalDependencies
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Advanced/AdvancedAdditionalDependencies";

const DeploymentOptionsStepForm: React.FC = (props: any) => {
  const form = Form.useFormInstance()

  useEffect(() => {
    if (props.flinkKubernetesDeploymentSteps.deployment) {
      form.setFieldsValue(WsFlinkKubernetesTemplateService.parseData({...props.flinkKubernetesDeploymentSteps.deployment}))
    }
  }, [props.flinkKubernetesDeploymentSteps.deployment]);

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

const mapModelToProps = ({flinkKubernetesDeploymentSteps}: any) => ({flinkKubernetesDeploymentSteps})
export default connect(mapModelToProps)(DeploymentOptionsStepForm);
