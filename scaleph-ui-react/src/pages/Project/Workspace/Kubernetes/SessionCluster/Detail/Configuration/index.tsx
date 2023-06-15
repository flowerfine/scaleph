import React, {useEffect, useState} from "react";
import {Props} from '@/app.d';
import {WsFlinkKubernetesSessionCluster} from "@/services/project/typings";
import {useAccess, useIntl} from "umi";
import {ProColumns, ProTable} from "@ant-design/pro-components";

type Config = {
  key: string;
  value?: any;
};

const FlinkKubernetesSessinClusterDetailFlinkConfigurationWeb: React.FC<Props<WsFlinkKubernetesSessionCluster>> = ({data}) => {
  const intl = useIntl();
  const access = useAccess();
  const [dataSource, setDataSource] = useState<Config[]>([])

  useEffect(() => {
    const config: Array<Config> = []
    Object.entries<[string, any][]>(data.flinkConfiguration ? {...data.flinkConfiguration} : {}).forEach(([key, value]) => {
      config.push({
        key: key,
        value: value
      })
    });
    setDataSource(config)
  }, []);

  const tableColumns: ProColumns<Config>[] = [
    {
      dataIndex: 'key',
      width: '40%'
    },
    {
      dataIndex: 'value',
    },
  ]

  return (
    <ProTable<Config>
      rowKey="key"
      columns={tableColumns}
      dataSource={dataSource}
      bordered
      options={false}
      search={false}
      showHeader={false}
    />
  );
}

export default FlinkKubernetesSessinClusterDetailFlinkConfigurationWeb;
