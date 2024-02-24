import React, {useEffect} from "react";
import {connect, useAccess, useIntl} from "umi";

const FlinkKubernetesSessinClusterDetailPodTemplateWeb: React.FC = (props: any) => {
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
export default connect(mapModelToProps)(FlinkKubernetesSessinClusterDetailPodTemplateWeb);
