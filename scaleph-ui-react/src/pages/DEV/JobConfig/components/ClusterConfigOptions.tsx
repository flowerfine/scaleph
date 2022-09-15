import {
  ProCard,
  ProForm,
  ProFormDependency,
  ProFormGroup,
  ProFormSelect,
  ProFormText
} from "@ant-design/pro-components";
import {useIntl} from 'umi';
import {Form} from "antd";
import {listDictDataByType} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constant";
import {FlinkClusterConfigParam, FlinkClusterInstanceParam} from "@/services/dev/typings";
import {list as listClusterInstance} from "@/services/dev/flinkClusterInstance.service";
import {list as listClusterConfig} from "@/services/dev/flinkClusterConfig.service";
import HighAvailability from "@/pages/DEV/ClusterConfigOptions/components/HA";

const JobClusterConfigOptions: React.FC = () => {
  const intl = useIntl();
  const form = Form.useFormInstance();

  return (<div>
    <ProCard
      title={"集群信息"}
      headerBordered={true}
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
          } else if (deployMode == '0' || deployMode == '1') {
            return (<ProFormSelect
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
              }}/>);
          }
          return (<ProFormGroup/>);
        }}
      </ProFormDependency>
    </ProCard>
    <ProCard
      title={"High Availability"}
      headerBordered
      collapsible={true}
      style={{
        marginBlockEnd: 16,
        minWidth: 800,
        maxWidth: '100%',
      }}>
      <ProFormGroup>
        <ProFormSelect
          name="ha"
          label={"high-availability"}
          showSearch={true}
          request={() => listDictDataByType(DICT_TYPE.flinkHA)}
        />
        <ProFormText
          name="high-availability.storageDir"
          label={'high-availability.storageDir'}
        />
        <ProFormText
          name="high-availability.cluster-id"
          label={'high-availability.cluster-id'}
        />

        <ProFormDependency name={['ha']}>
          {({ha}) => {
            if (ha == 'zookeeper') {
              return (
                <ProFormGroup>
                  <ProFormText
                    name="high-availability.zookeeper.path.root"
                    label={'high-availability.zookeeper.path.root'}
                  />
                  <ProFormText
                    name="high-availability.zookeeper.quorum"
                    label={'high-availability.zookeeper.quorum'}
                  />
                </ProFormGroup>
              )
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
      </ProFormGroup>
    </ProCard>

  </div>);
}

export default JobClusterConfigOptions;
