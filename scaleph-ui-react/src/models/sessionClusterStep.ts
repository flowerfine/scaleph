import {useState} from 'react';

export default () => {
  const [sessionCluster, setSessionCluster] = useState<string>()

  return {
    sessionCluster,
    setSessionCluster
  };
};
