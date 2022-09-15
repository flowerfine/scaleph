import {ProCard, ProForm, ProFormDependency, ProFormSelect} from "@ant-design/pro-components";
import {useIntl} from 'umi';
import {Form} from "antd";
import {listDictDataByType} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constant";
import {FlinkClusterConfigParam, FlinkClusterInstanceParam} from "@/services/dev/typings";
import {list as listClusterInstance} from "@/services/dev/flinkClusterInstance.service";
import {list as listClusterConfig} from "@/services/dev/flinkClusterConfig.service";

const JobClusterConfigOptions: React.FC = () => {
  const intl = useIntl();
  const form = Form.useFormInstance();

  return (<ProCard
    style={{
      marginBlockEnd: 16,
      minWidth: 800,
      maxWidth: '100%',
    }}
  >
    <ProFormSelect
      name="deployMode"
      label={intl.formatMessage({id: 'pages.dev.clusterConfig.deployMode'})}
      rules={[{required: true}]}
      showSearch={false}
      request={() => {
        return listDictDataByType(DICT_TYPE.flinkDeploymentMode)
      }}
    />

    <ProFormDependency name={['deployMode']}>
      {({deployMode}) => {
        if (deployMode == '2') {
          return (
            <ProFormSelect
              name={"flinkClusterInstance"}
              label={intl.formatMessage({id: 'pages.dev.clusterInstance'})}
              rules={[{required: true}]}
              showSearch={true}
              request={(params) => {
                const param: FlinkClusterInstanceParam = {
                  name: params.keyWords
                }
                return listClusterInstance(param).then((response) => {
                  return response.data.map((item) => {
                    return {label: item.name, value: item.id, item: item}
                  })
                })
              }}
            />
          )
        }
        return <ProFormSelect
          name={"flinkClusterConfig"}
          label={intl.formatMessage({id: 'pages.dev.clusterConfig'})}
          rules={[{required: true}]}
          showSearch={true}
          dependencies={['deployMode']}
          request={(params) => {
            const param: FlinkClusterConfigParam = {
              name: params.keyWords,
              deployMode: params.deployMode
            }
            return listClusterConfig(param).then((response) => {
              return response.data.map((item) => {
                return {label: item.name, value: item.id, item: item}
              })
            })
          }}
        />;
      }}
    </ProFormDependency>

  </ProCard>);
}

export default JobClusterConfigOptions;
