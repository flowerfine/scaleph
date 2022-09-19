import {ProCard, ProFormDependency, ProFormGroup, ProFormSelect} from "@ant-design/pro-components";
import {useIntl} from 'umi';
import {Form} from "antd";
import {listDictDataByType} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constant";
import {FlinkClusterConfig, FlinkClusterConfigParam, FlinkClusterInstanceParam} from "@/services/dev/typings";
import {list as listClusterInstance} from "@/services/dev/flinkClusterInstance.service";
import {list as listClusterConfig, selectOne, setData} from "@/services/dev/flinkClusterConfig.service";
import HighAvailability from "@/pages/DEV/ClusterConfigOptions/components/HA";
import State from "@/pages/DEV/ClusterConfigOptions/components/State";
import FaultTolerance from "@/pages/DEV/ClusterConfigOptions/components/FaultTolerance";
import Resource from "@/pages/DEV/ClusterConfigOptions/components/Resource";
import Additional from "@/pages/DEV/ClusterConfigOptions/components/Additional";

const JobClusterConfigOptions: React.FC = () => {
  const intl = useIntl();
  const form = Form.useFormInstance();

  const handleClusterInstanceChange = (value: any, option: any) => {
    if (!option) {
      return
    }
    selectOne(option.item.flinkClusterConfigId).then((response) => {
      handleConfigOptionsChange(response)
    })
  }

  const handleClusterConfigChange = (value: any, option: any) => {
    if (!option) {
      return
    }
    handleConfigOptionsChange(option.item)
  }

  const handleConfigOptionsChange = (config: FlinkClusterConfig) => {
    form.setFieldValue("flinkClusterConfig", config.id)
    console.log('flinkClusterConfig', form.getFieldValue('flinkClusterConfig'))
    if (!config || !config.configOptions) {
      return
    }
    setData(form, new Map(Object.entries(config.configOptions)))
  }

  return (
    <div>
      <ProCard
        title={"集群信息"}
        headerBordered={true}
        style={{width: 1000}}>
        <ProFormSelect
          name="deployMode"
          label={intl.formatMessage({id: 'pages.dev.clusterConfig.deployMode'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          showSearch={false}
          request={() => {
            return listDictDataByType(DICT_TYPE.flinkDeploymentMode)
          }}
        />
        <ProFormDependency name={['deployMode']}>
          {({deployMode}) => {

            return (
              <ProFormGroup>
                <ProFormSelect
                  name={"flinkClusterConfig"}
                  label={intl.formatMessage({id: 'pages.dev.clusterConfig'})}
                  colProps={{span: 21, offset: 1}}
                  rules={[{required: true}]}
                  readonly={(deployMode == '2')}
                  showSearch={true}
                  fieldProps={{
                    onChange: handleClusterConfigChange
                  }}
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
                  }}/>
                <ProFormSelect
                  name={"flinkClusterInstance"}
                  label={intl.formatMessage({id: 'pages.dev.clusterInstance'})}
                  colProps={{span: 21, offset: 1}}
                  rules={[{required: true}]}
                  showSearch={true}
                  hidden={(deployMode != '2')}
                  required={(deployMode != '2')}
                  fieldProps={{
                    onChange: handleClusterInstanceChange
                  }}
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
              </ProFormGroup>
            )
          }}
        </ProFormDependency>
      </ProCard>
      <State/>
      <FaultTolerance/>
      <HighAvailability/>
      <Resource/>
      <Additional/>
    </div>);
}

export default JobClusterConfigOptions;
