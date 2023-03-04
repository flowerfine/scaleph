import {useState} from 'react';
import {WsFlinkKubernetesDeploymentTemplate} from "@/services/project/typings";

export default () => {
  const [template, setTemplate] = useState<WsFlinkKubernetesDeploymentTemplate>()
  const [deploymentTemplate, setDeploymentTemplate] = useState<string>()

  return {
    template,
    setTemplate,
    deploymentTemplate,
    setDeploymentTemplate
  };
};
