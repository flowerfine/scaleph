import { useState } from 'react';

const useGlobalModel = () => {
  const [executionData, setExecutionData] = useState<string>('');

  return {
    executionData,
    setExecutionData,
  };
};
export default useGlobalModel;
