import React, {useRef} from "react";
import {ActionType, ProCard, ProFormInstance} from "@ant-design/pro-components";
import {connect, useAccess, useIntl} from "@umijs/max";
import {Props} from '@/typings';
import {WsFlinkKubernetesJob} from "@/services/project/typings";
import ArtifactFlinkJarWeb
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/Overview/ArtifactFlinkJarWeb";
import FlinkKubernetesJobResourceWeb
    from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/Overview/FlinkKubernetesJobResourceWeb";

const FlinkKubernetesJobDetailOverviewWeb: React.FC<Props<WsFlinkKubernetesJob>> = (props: any) => {
  const intl = useIntl();
  const access = useAccess();
  const actionRef = useRef<ActionType>();
  const formRef = useRef<ProFormInstance>();

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
