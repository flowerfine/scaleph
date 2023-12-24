import React, {useEffect, useState} from "react";
import {connect, useAccess, useIntl} from "umi";
import {ProColumns, ProTable} from "@ant-design/pro-components";

type Config = {
  key: string;
  value?: any;
};

const FlinkKubernetesSessinClusterDetailFlinkConfigurationWeb: React.FC = (props: any) => {
  const intl = useIntl();
  const access = useAccess();
  const [dataSource, setDataSource] = useState<Config[]>([])

  useEffect(() => {
    if (props.flinkKubernetesSessionClusterDetail.sessionCluster) {
      const config: Array<Config> = []
      Object.entries<[string, any][]>(props.flinkKubernetesSessionClusterDetail.sessionCluster?.flinkConfiguration ? {...props.flinkKubernetesSessionClusterDetail.sessionCluster?.flinkConfiguration} : {}).forEach(([key, value]) => {
        config.push({
          key: key,
          value: value
        })
      });
      setDataSource(config)
    }
  }, [props.flinkKubernetesSessionClusterDetail.sessionCluster]);

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

const mapModelToProps = ({flinkKubernetesSessionClusterDetail}: any) => ({flinkKubernetesSessionClusterDetail})
export default connect(mapModelToProps)(FlinkKubernetesSessinClusterDetailFlinkConfigurationWeb);
