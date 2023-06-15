import React from "react";
import {Props} from '@/app.d';
import {WsFlinkKubernetesSessionCluster} from "@/services/project/typings";
import {useAccess, useIntl} from "umi";

const FlinkKubernetesSessinClusterDetailPodTemplateWeb: React.FC<Props<WsFlinkKubernetesSessionCluster>> = ({data}) => {
  const intl = useIntl();
  const access = useAccess();

  return (
    <div>FlinkKubernetesSessinClusterDetailPodTemplateWeb</div>
  );
}

export default FlinkKubernetesSessinClusterDetailPodTemplateWeb;
