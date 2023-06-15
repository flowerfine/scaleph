import React from "react";
import {Props} from '@/app.d';
import {WsFlinkKubernetesSessionCluster} from "@/services/project/typings";
import {useAccess, useIntl} from "umi";
import {ProForm} from "@ant-design/pro-components";
import AdvancedBasic from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedBasic";
import AdvancedResource from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedResource";
import AdvancedCheckpoint from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedCheckpoint";
import AdvancedPeriodicSavepoint
  from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedPeriodicSavepoint";
import AdvancedRestart from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedRestart";
import AdvancedFaultTolerance
  from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedFaultTolerance";
import AdvancedHighAvailability
  from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedHighAvailability";
import AdvancedAdditional from "@/pages/Project/Workspace/Kubernetes/Template/Detail/Advanced/AdvancedAdditional";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";

const FlinkKubernetesSessinClusterDetailOptionsWeb: React.FC<Props<WsFlinkKubernetesSessionCluster>> = ({data}) => {
  const intl = useIntl();
  const access = useAccess();

  return (
    <ProForm
      grid={true}
      disabled
      submitter={false}
      initialValues={WsFlinkKubernetesTemplateService.parseData({...data})}
    >
      <AdvancedBasic/>
      <AdvancedResource/>
      <AdvancedCheckpoint/>
      <AdvancedPeriodicSavepoint/>
      <AdvancedRestart/>
      <AdvancedFaultTolerance/>
      <AdvancedHighAvailability/>
      <AdvancedAdditional/>
    </ProForm>
  );
}

export default FlinkKubernetesSessinClusterDetailOptionsWeb;
