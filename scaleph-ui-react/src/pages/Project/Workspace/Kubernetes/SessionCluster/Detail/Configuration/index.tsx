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
    if (props.sessionClusterDetail.sessionCluster) {
      const config: Array<Config> = []
      Object.entries<[string, any][]>(props.sessionClusterDetail.sessionCluster.flinkConfiguration ? {...props.sessionClusterDetail.sessionCluster.flinkConfiguration} : {}).forEach(([key, value]) => {
        config.push({
          key: key,
          value: value
        })
      });
      setDataSource(config)
    }
  }, [props.sessionClusterDetail.sessionCluster]);

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

const mapModelToProps = ({sessionClusterDetail}: any) => ({sessionClusterDetail})
export default connect(mapModelToProps)(FlinkKubernetesSessinClusterDetailFlinkConfigurationWeb);
