import { DICT_TYPE } from '@/constant';
import Additional from '@/pages/DEV/ClusterConfigOptions/components/Additional';
import FaultTolerance from '@/pages/DEV/ClusterConfigOptions/components/FaultTolerance';
import HighAvailability from '@/pages/DEV/ClusterConfigOptions/components/HA';
import Resource from '@/pages/DEV/ClusterConfigOptions/components/Resource';
import State from '@/pages/DEV/ClusterConfigOptions/components/State';
import { DictDataService } from '@/services/admin/dictData.service';
import { FlinkClusterConfigService } from '@/services/dev/flinkClusterConfig.service';
import { FlinkCLusterInstanceService } from '@/services/dev/flinkClusterInstance.service';
import {
  FlinkClusterConfig,
  FlinkClusterConfigParam,
  FlinkClusterInstanceParam,
} from '@/services/dev/typings';
import {
  ProCard,
  ProFormDependency,
  ProFormGroup,
  ProFormSelect,
} from '@ant-design/pro-components';
import { Form } from 'antd';
import { useIntl } from 'umi';

const JobClusterConfigOptions: React.FC = () => {
  const intl = useIntl();
  const form = Form.useFormInstance();

  const handleClusterInstanceChange = (value: any, option: any) => {
    if (!option) {
      return;
    }
    FlinkClusterConfigService.selectOne(option.item.flinkClusterConfigId).then((response) => {
      handleConfigOptionsChange(response);
    });
  };

  const handleClusterConfigChange = (value: any, option: any) => {
    if (!option) {
      return;
    }
    handleConfigOptionsChange(option.item);
  };

  const handleConfigOptionsChange = (config: FlinkClusterConfig) => {
    form.setFieldValue('flinkClusterConfig', config.id);
    if (!config || !config.configOptions) {
      return;
    }
    const flinkConfig = FlinkClusterConfigService.setData(
      new Map(Object.entries(config.configOptions)),
    );
    form.setFieldsValue(flinkConfig);
  };

  return (
    <div>
      <ProCard
        title={intl.formatMessage({ id: 'pages.dev.job.config.cluster' })}
        headerBordered={true}
        style={{ width: 1000 }}
      >
        <ProFormSelect
          name="deployMode"
          label={intl.formatMessage({ id: 'pages.dev.clusterConfig.deployMode' })}
          colProps={{ span: 21, offset: 1 }}
          rules={[{ required: true }]}
          showSearch={false}
          request={() => {
            return DictDataService.listDictDataByType(DICT_TYPE.flinkDeploymentMode);
          }}
        />
        <ProFormDependency name={['deployMode']}>
          {({ deployMode }) => {
            return (
              <ProFormGroup>
                <ProFormSelect
                  name={'flinkClusterConfig'}
                  label={intl.formatMessage({ id: 'pages.dev.clusterConfig' })}
                  colProps={{ span: 21, offset: 1 }}
                  rules={[{ required: true }]}
                  readonly={deployMode == '2'}
                  showSearch={true}
                  fieldProps={{
                    onChange: handleClusterConfigChange,
                  }}
                  dependencies={['deployMode']}
                  request={(params) => {
                    const param: FlinkClusterConfigParam = {
                      name: params.keyWords,
                      deployMode: params.deployMode,
                    };
                    return FlinkClusterConfigService.list(param).then((response) => {
                      return response.data.map((item) => {
                        return { label: item.name, value: item.id, item: item };
                      });
                    });
                  }}
                />
                <ProFormSelect
                  name={'flinkClusterInstance'}
                  label={intl.formatMessage({ id: 'pages.dev.clusterInstance' })}
                  colProps={{ span: 21, offset: 1 }}
                  rules={[{ required: !(deployMode != '2') }]}
                  showSearch={true}
                  hidden={deployMode != '2'}
                  fieldProps={{
                    onChange: handleClusterInstanceChange,
                  }}
                  request={(params) => {
                    const param: FlinkClusterInstanceParam = {
                      name: params.keyWords,
                    };
                    return FlinkCLusterInstanceService.list(param).then((response) => {
                      return response.data.map((item) => {
                        return { label: item.name, value: item.id, item: item };
                      });
                    });
                  }}
                />
              </ProFormGroup>
            );
          }}
        </ProFormDependency>
      </ProCard>
      <State />
      <FaultTolerance />
      <HighAvailability />
      <Resource />
      <Additional />
    </div>
  );
};

export default JobClusterConfigOptions;
