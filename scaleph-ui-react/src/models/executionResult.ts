import { useState } from 'react';

const useGlobalModel = () => {
  const [executionData, setExecutionData] = useState<string>('123');

  return {
    executionData,
    setExecutionData,
  };
};
export default useGlobalModel;
