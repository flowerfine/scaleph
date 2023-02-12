import {useLocation} from "umi";
import React from "react";
import {WsFlinkKubernetesDeployment} from "@/services/project/typings";

const FlinkKubernetesDeploymentDetailWeb: React.FC = () => {
  const data = useLocation().state as WsFlinkKubernetesDeployment

  return (<div>{JSON.stringify(data)}</div>);
}

export default FlinkKubernetesDeploymentDetailWeb;
