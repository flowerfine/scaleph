import {connect} from "umi";
import React, {useEffect, useRef} from "react";
import {ProForm, ProFormInstance} from "@ant-design/pro-components";
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

const FlinkKubernetesSessinClusterDetailOptionsWeb: React.FC = (props: any) => {
  const formRef = useRef<ProFormInstance>();

  useEffect(() => {
    if (props.sessionClusterDetail.sessionCluster) {
      formRef.current?.setFieldsValue(WsFlinkKubernetesTemplateService.parseData({...props.sessionClusterDetail.sessionCluster}))
    }
  }, [props.sessionClusterDetail.sessionCluster]);

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
      <AdvancedAdditional/>
    </ProForm>
  );
}

const mapModelToProps = ({sessionClusterDetail}: any) => ({sessionClusterDetail})
export default connect(mapModelToProps)(FlinkKubernetesSessinClusterDetailOptionsWeb);
