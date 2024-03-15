import React, {useEffect, useState} from "react";
import {ProColumns, ProTable} from "@ant-design/pro-components";
import {connect, useAccess, useIntl} from "@umijs/max";

type Config = {
  key: string;
  value?: any;
};

const FlinkKubernetesJobDetailConfigurationWeb: React.FC = (props: any) => {
  const intl = useIntl();
  const access = useAccess();
  const [dataSource, setDataSource] = useState<Config[]>([])

  useEffect(() => {
    if (props.flinkKubernetesJobDetail.job?.jobInstance) {
      const config: Array<Config> = []
      Object.entries<[string, any][]>(props.flinkKubernetesJobDetail.job?.jobInstance?.mergedFlinkConfiguration ? {...props.flinkKubernetesJobDetail.job?.jobInstance?.mergedFlinkConfiguration} : {}).forEach(([key, value]) => {
        config.push({
          key: key,
          value: value
        })
      });
      setDataSource(config)
    }
  }, [props.flinkKubernetesJobDetail.job]);

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

const mapModelToProps = ({flinkKubernetesJobDetail}: any) => ({flinkKubernetesJobDetail})
export default connect(mapModelToProps)(FlinkKubernetesJobDetailConfigurationWeb);
