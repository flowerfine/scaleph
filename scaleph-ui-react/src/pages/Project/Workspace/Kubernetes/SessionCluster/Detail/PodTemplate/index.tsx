import React, {useEffect} from "react";
import {connect, useAccess, useIntl} from "umi";

const FlinkKubernetesSessinClusterDetailPodTemplateWeb: React.FC = (props: any) => {
  const intl = useIntl();
  const access = useAccess();

  useEffect(() => {
    if (props.sessionClusterDetail.sessionCluster) {

    }
  }, [props.sessionClusterDetail.sessionCluster]);

  return (
    <div>FlinkKubernetesSessinClusterDetailPodTemplateWeb</div>
  );
}

const mapModelToProps = ({sessionClusterDetail}: any) => ({sessionClusterDetail})
export default connect(mapModelToProps)(FlinkKubernetesSessinClusterDetailPodTemplateWeb);
