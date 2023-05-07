import {useState} from 'react';
import {WsFlinkKubernetesTemplate} from "@/services/project/typings";

export default () => {
  const [template, setTemplate] = useState<WsFlinkKubernetesTemplate>()
  const [sessionCluster, setSessionCluster] = useState<string>()

  return {
    template,
    setTemplate,
    sessionCluster,
    setSessionCluster
  };
};
