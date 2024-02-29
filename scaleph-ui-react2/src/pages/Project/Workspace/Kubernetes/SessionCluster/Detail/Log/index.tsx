import React, {useEffect} from "react";
import {connect, useAccess, useIntl} from "umi";

const FlinkKubernetesSessinClusterDetailLogWeb: React.FC = (props: any) => {
  const intl = useIntl();
  const access = useAccess();

  useEffect(() => {
    if (props.flinkKubernetesSessionClusterDetail.sessionCluster) {

    }
  }, [props.flinkKubernetesSessionClusterDetail.sessionCluster]);

  return (
    <div>FlinkKubernetesSessinClusterDetailLogWeb</div>
  );
}

const mapModelToProps = ({flinkKubernetesSessionClusterDetail}: any) => ({flinkKubernetesSessionClusterDetail})
export default connect(mapModelToProps)(FlinkKubernetesSessinClusterDetailLogWeb);
