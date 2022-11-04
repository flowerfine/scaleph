import {useState} from 'react';

export default () => {
  const [dsTypeId, setDsTypeId] = useState<number>(0);

  return {
    dsTypeId,
    setDsTypeId
  };
};
