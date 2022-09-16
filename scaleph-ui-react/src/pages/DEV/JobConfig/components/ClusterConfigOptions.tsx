import {ProCard, ProFormDependency, ProFormGroup, ProFormSelect} from "@ant-design/pro-components";
import {useIntl} from 'umi';
import {Form} from "antd";
import {listDictDataByType} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constant";
import {FlinkClusterConfigParam, FlinkClusterInstanceParam} from "@/services/dev/typings";
import {list as listClusterInstance} from "@/services/dev/flinkClusterInstance.service";
import {list as listClusterConfig} from "@/services/dev/flinkClusterConfig.service";
import HighAvailability from "@/pages/DEV/ClusterConfigOptions/components/HA";
import State from "@/pages/DEV/ClusterConfigOptions/components/State";
import FaultTolerance from "@/pages/DEV/ClusterConfigOptions/components/FaultTolerance";
import Resource from "@/pages/DEV/ClusterConfigOptions/components/Resource";
import Additional from "@/pages/DEV/ClusterConfigOptions/components/Additional";

const JobClusterConfigOptions: React.FC = () => {
  const intl = useIntl();
  const form = Form.useFormInstance();

  return (
    <div>
      <ProCard
        title={"集群信息"}
        headerBordered={true}>
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
            if (deployMode == '2') {
              return (
                <ProFormSelect
                  name={"flinkClusterInstance"}
                  label={intl.formatMessage({id: 'pages.dev.clusterInstance'})}
                  colProps={{span: 21, offset: 1}}
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
                colProps={{span: 21, offset: 1}}
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
      <State/>
      <FaultTolerance/>
      <HighAvailability/>
      <Resource/>
      <Additional/>
    </div>);
}

export default JobClusterConfigOptions;
