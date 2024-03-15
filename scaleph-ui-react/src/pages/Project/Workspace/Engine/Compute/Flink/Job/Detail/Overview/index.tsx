import React from "react";
import {ProCard} from "@ant-design/pro-components";
import {connect, useIntl} from "@umijs/max";
import {Props} from '@/typings';
import {WsFlinkKubernetesJob} from "@/services/project/typings";
import ArtifactFlinkJarWeb
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/Overview/ArtifactFlinkJarWeb";
import FlinkKubernetesJobResourceWeb
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/Overview/FlinkKubernetesJobResourceWeb";

const FlinkKubernetesJobDetailOverviewWeb: React.FC<Props<WsFlinkKubernetesJob>> = (props: any) => {
  const intl = useIntl();

  return (
    <ProCard.Group direction={"column"}>
      <ProCard
        title={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.overview.artifact'})}
        bordered
      >
        <ArtifactFlinkJarWeb/>
      </ProCard>
      <ProCard
        title={intl.formatMessage({id: 'pages.project.flink.kubernetes.job.detail.overview.resource'})}
        bordered
      >
        <FlinkKubernetesJobResourceWeb/>
      </ProCard>
    </ProCard.Group>
  );
}


const mapModelToProps = ({flinkKubernetesJobDetail}: any) => ({flinkKubernetesJobDetail})
export default connect(mapModelToProps)(FlinkKubernetesJobDetailOverviewWeb);
