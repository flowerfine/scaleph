import React from "react";
import {Props} from '@/app.d';
import {WsFlinkKubernetesSessionCluster} from "@/services/project/typings";
import {useAccess, useIntl} from "umi";

const FlinkKubernetesSessinClusterDetailLogWeb: React.FC<Props<WsFlinkKubernetesSessionCluster>> = ({data}) => {
  const intl = useIntl();
  const access = useAccess();

  return (
    <div>FlinkKubernetesSessinClusterDetailLogWeb</div>
  );
}

export default FlinkKubernetesSessinClusterDetailLogWeb;
