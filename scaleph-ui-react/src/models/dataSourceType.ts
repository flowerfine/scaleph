import {useState} from 'react';
import {DsType} from "@/services/datasource/typings";

export default () => {
  const [dsType, setDsType] = useState<DsType>()

  return {
    dsType,
    setDsType
  };
};
