import React from "react";
import {ProCard} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";
import FlinkKubernetesJobResourceWeb
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/Overview/FlinkKubernetesJobResourceWeb";
import FlinkKubernetesJobDetailConfigurationWeb
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/Overview/FlinkKubernetesJobConfiguration";
import ArtifactWeb from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/Overview/ArtifactWeb";

const FlinkKubernetesJobDetailOverviewWeb: React.FC = () => {
  const intl = useIntl();

  return (
    <ProCard.Group direction={"column"}>
      <ProCard
        title={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.overview.artifact'})}
        bordered
      >
        <ArtifactWeb/>
      </ProCard>
      <ProCard
        title={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.overview.resource'})}
        bordered
      >
        <FlinkKubernetesJobResourceWeb/>
      </ProCard>
      <ProCard
        title={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.overview.configuration'})}
        bordered
      >
        <FlinkKubernetesJobDetailConfigurationWeb/>
      </ProCard>
    </ProCard.Group>
  );
}

export default FlinkKubernetesJobDetailOverviewWeb;
