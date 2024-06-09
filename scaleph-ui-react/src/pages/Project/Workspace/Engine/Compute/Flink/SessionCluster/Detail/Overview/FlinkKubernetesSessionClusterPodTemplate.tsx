import React, {useEffect} from "react";
import {connect, useAccess, useIntl} from "@umijs/max";

const FlinkKubernetesSessionClusterDetailPodTemplateWeb: React.FC = (props: any) => {
  const intl = useIntl();
  const access = useAccess();

  useEffect(() => {
    if (props.flinkKubernetesSessionClusterDetail.sessionCluster) {

    }
  }, [props.flinkKubernetesSessionClusterDetail.sessionCluster]);


  return (
    <div>FlinkKubernetesSessinClusterDetailPodTemplateWeb</div>
  );
}

const mapModelToProps = ({flinkKubernetesSessionClusterDetail}: any) => ({flinkKubernetesSessionClusterDetail})
export default connect(mapModelToProps)(FlinkKubernetesSessionClusterDetailPodTemplateWeb);
