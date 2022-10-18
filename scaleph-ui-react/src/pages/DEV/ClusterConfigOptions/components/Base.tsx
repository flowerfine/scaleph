import {ProCard, ProFormGroup, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE, RESOURCE_TYPE} from "@/constant";
import {useIntl} from "umi";
import {ResourceListParam} from "@/services/resource/typings";
import {ResourceService} from "@/services/resource/resource.service";
import {Dict} from '@/app.d';

const BaseOptions: React.FC = () => {
  const intl = useIntl();

  return (
    <ProCard>
      <ProFormGroup>
        <ProFormText
          name="name"
          label={intl.formatMessage({id: 'pages.dev.clusterConfig.name'})}
          colProps={{span: 21, offset: 1}}
          rules={[
            {required: true},
            {max: 30},
            {
              pattern: /^[\w\s-_.]+$/,
              message: intl.formatMessage({id: 'app.common.validate.characterWord3'}),
            },
          ]}
        />
        <ProFormSelect
          name="resourceProvider"
          label={intl.formatMessage({id: 'pages.dev.clusterConfig.resourceProvider'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          showSearch={true}
          request={() => DictDataService.listDictDataByType(DICT_TYPE.flinkResourceProvider)}
        />
        <ProFormSelect
          name="deployMode"
          label={intl.formatMessage({id: 'pages.dev.clusterConfig.deployMode'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          showSearch={true}
          dependencies={['resourceProvider']}
          request={(params) => {
            if (params.resourceProvider == '0') {
              const dict: Dict = {label: 'Session', value: '2'};
              return Promise.resolve([dict]);
            }
            return DictDataService.listDictDataByType(DICT_TYPE.flinkDeploymentMode);
          }}
        />
        <ProFormSelect
          name="clusterCredentialId"
          label={intl.formatMessage({id: 'pages.dev.clusterConfig.clusterCredential'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          showSearch={true}
          dependencies={['resourceProvider']}
          request={(params) => {
            const resourceParam: ResourceListParam = {
              resourceType: RESOURCE_TYPE.clusterCredential,
              label: params.resourceProvider,
            };
            return ResourceService.list(resourceParam).then((response) => {
              return response.records.map((item) => {
                return {label: item.name, value: item.id};
              });
            });
          }}
        />
        <ProFormSelect
          name="flinkVersion"
          label={intl.formatMessage({id: 'pages.dev.clusterConfig.flinkVersion'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          showSearch={true}
          request={() => DictDataService.listDictDataByType(DICT_TYPE.flinkVersion)}
        />
        <ProFormSelect
          name="flinkReleaseId"
          label={intl.formatMessage({id: 'pages.dev.clusterConfig.flinkRelease'})}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}]}
          showSearch={true}
          dependencies={['flinkVersion']}
          request={(params) => {
            const resourceParam: ResourceListParam = {
              resourceType: RESOURCE_TYPE.flinkRelease,
              label: params.flinkVersion,
            };
            return ResourceService.list(resourceParam).then((response) => {
              return response.records.map((item) => {
                return {label: item.fileName, value: item.id};
              });
            });
          }}
        />
        <ProFormText
          name="remark"
          label={intl.formatMessage({id: 'pages.dev.remark'})}
          colProps={{span: 21, offset: 1}}
          rules={[{max: 200}]}
        />
      </ProFormGroup>
    </ProCard>
  );
}

export default BaseOptions;
