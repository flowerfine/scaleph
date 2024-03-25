import React from "react";
import {connect} from "@umijs/max";
import ArtifactFlinkJarWeb
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/Overview/ArtifactFlinkJarWeb";
import ArtifactFlinkSQLWeb
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/Overview/ArtifactFlinkSQLWeb";
import ArtifactSeaTunnelWeb
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/Overview/ArtifactFlinkSeaTunnelWeb";
import ArtifactFlinkCDCWeb
  from "@/pages/Project/Workspace/Engine/Compute/Flink/Job/Detail/Overview/ArtifactFlinkCDCWeb";

const ArtifactWeb: React.FC = (props: any) => {

  const artifact = () => {
    if (props.flinkKubernetesJobDetail.job?.artifactFlinkJar) {
      return <ArtifactFlinkJarWeb/>
    } else if (props.flinkKubernetesJobDetail.job?.artifactFlinkSql) {
      return <ArtifactFlinkSQLWeb/>
    } else if (props.flinkKubernetesJobDetail.job?.artifactSeaTunnel) {
      return <ArtifactSeaTunnelWeb/>
    } else if (props.flinkKubernetesJobDetail.job?.artifactFlinkCDC) {
      return <ArtifactFlinkCDCWeb/>
    } else {
      return <></>
    }
  }
  return (
    artifact()
  );
}


const mapModelToProps = ({flinkKubernetesJobDetail}: any) => ({flinkKubernetesJobDetail})
export default connect(mapModelToProps)(ArtifactWeb);
