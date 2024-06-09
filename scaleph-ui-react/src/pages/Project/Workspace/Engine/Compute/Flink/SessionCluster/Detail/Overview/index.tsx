import React, {useEffect, useRef} from "react";
import {ProCard, ProFormInstance} from "@ant-design/pro-components";
import {connect, useIntl} from "@umijs/max";
import {WsFlinkKubernetesTemplateService} from "@/services/project/WsFlinkKubernetesTemplateService";
import FlinkKubernetesSessionClusterResourceWeb
  from "@/pages/Project/Workspace/Engine/Compute/Flink/SessionCluster/Detail/Overview/FlinkKubernetesSessionClusterResourceWeb";
import FlinkKubernetesSessionClusterDetailConfigurationWeb
  from "@/pages/Project/Workspace/Engine/Compute/Flink/SessionCluster/Detail/Overview/FlinkKubernetesSessionClusterConfiguration";
import FlinkKubernetesSessionClusterDetailLogWeb
  from "@/pages/Project/Workspace/Engine/Compute/Flink/SessionCluster/Detail/Overview/FlinkKubernetesSessionClusterLog";
import FlinkKubernetesSessionClusterDetailPodTemplateWeb
  from "@/pages/Project/Workspace/Engine/Compute/Flink/SessionCluster/Detail/Overview/FlinkKubernetesSessionClusterPodTemplate";

const FlinkKubernetesSessinClusterDetailOverviewWeb: React.FC = (props: any) => {
  const intl = useIntl();
  const formRef = useRef<ProFormInstance>();

  useEffect(() => {
    if (props.flinkKubernetesSessionClusterDetail.sessionCluster) {
      formRef.current?.setFieldsValue(WsFlinkKubernetesTemplateService.parseData({...props.flinkKubernetesSessionClusterDetail.sessionCluster}))
    }
  }, [props.flinkKubernetesSessionClusterDetail.sessionCluster]);

  return (
    <ProCard.Group direction={"column"}>
      <ProCard
        title={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.overview.resource'})}
        bordered
      >
        <FlinkKubernetesSessionClusterResourceWeb/>
      </ProCard>
      <ProCard
        title={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.overview.configuration'})}
        bordered
      >
        <FlinkKubernetesSessionClusterDetailConfigurationWeb/>
      </ProCard>
      <ProCard
        title={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.overview.log'})}
        bordered
      >
        <FlinkKubernetesSessionClusterDetailLogWeb/>
      </ProCard>
      <ProCard
        title={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.overview.podTemplate'})}
        bordered
      >
        <FlinkKubernetesSessionClusterDetailPodTemplateWeb/>
      </ProCard>
    </ProCard.Group>
  );
}

const mapModelToProps = ({flinkKubernetesSessionClusterDetail}: any) => ({flinkKubernetesSessionClusterDetail})
export default connect(mapModelToProps)(FlinkKubernetesSessinClusterDetailOverviewWeb);
