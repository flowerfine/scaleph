import {connect} from "umi";
import React, {useEffect, useRef} from "react";
import {ProForm, ProFormInstance} from "@ant-design/pro-components";
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
import AdvancedAdditional from "@/pages/Project/Workspace/Kubernetes/Advanced/AdvancedAdditional";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import AdvancedAdditionalDependencies
  from "@/pages/Project/Workspace/Kubernetes/Advanced/AdvancedAdditionalDependencies";

const FlinkKubernetesSessinClusterDetailOptionsWeb: React.FC = (props: any) => {
  const formRef = useRef<ProFormInstance>();

  useEffect(() => {
    if (props.flinkKubernetesSessionClusterDetail.sessionCluster) {
      formRef.current?.setFieldsValue(WsFlinkKubernetesTemplateService.parseData({...props.flinkKubernetesSessionClusterDetail.sessionCluster}))
    }
  }, [props.flinkKubernetesSessionClusterDetail.sessionCluster]);

  return (
    <ProForm
      formRef={formRef}
      grid={true}
      disabled
      submitter={false}
    >
      <AdvancedBasic/>
      <AdvancedResource/>
      <AdvancedCheckpoint/>
      <AdvancedPeriodicSavepoint/>
      <AdvancedRestart/>
      <AdvancedFaultTolerance/>
      <AdvancedHighAvailability/>
      <AdvancedAdditionalDependencies/>
      <AdvancedAdditional/>
    </ProForm>
  );
}

const mapModelToProps = ({flinkKubernetesSessionClusterDetail}: any) => ({flinkKubernetesSessionClusterDetail})
export default connect(mapModelToProps)(FlinkKubernetesSessinClusterDetailOptionsWeb);
