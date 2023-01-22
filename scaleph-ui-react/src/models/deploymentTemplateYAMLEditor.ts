import {useState} from 'react';

export default () => {
  const [deploymentTemplate, setDeploymentTemplate] = useState<string>()

  return {
    deploymentTemplate,
    setDeploymentTemplate
  };
};
