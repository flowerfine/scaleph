import React, {useEffect} from "react";
import {connect, useAccess, useIntl} from "umi";

const FlinkKubernetesSessinClusterDetailLogWeb: React.FC = (props: any) => {
  const intl = useIntl();
  const access = useAccess();

  useEffect(() => {
    if (props.sessionClusterDetail.sessionCluster) {

    }
  }, [props.sessionClusterDetail.sessionCluster]);

  return (
    <div>FlinkKubernetesSessinClusterDetailLogWeb</div>
  );
}

const mapModelToProps = ({sessionClusterDetail}: any) => ({sessionClusterDetail})
export default connect(mapModelToProps)(FlinkKubernetesSessinClusterDetailLogWeb);
