import { ProFormSelect, ProFormText } from '@ant-design/pro-components';
import { DictDataService } from '@/services/admin/dictData.service';
import { DICT_TYPE, RESOURCE_TYPE } from '@/constant';
import { useIntl } from 'umi';
import { ResourceListParam } from '@/services/resource/typings';
import { ResourceService } from '@/services/resource/resource.service';
import { Dict } from '@/app.d';

const BaseOptions: React.FC = () => {
  const intl = useIntl();
  return (
    <>
      <ProFormText name="projectId" hidden></ProFormText>
      <ProFormText
        name="name"
        label={intl.formatMessage({ id: 'page.project.cluster.config.name' })}
        rules={[
          { required: true },
          { max: 30 },
          {
            pattern: /^[\w\s-_.]+$/,
            message: intl.formatMessage({ id: 'app.common.validate.characterWord3' }),
          },
        ]}
      />
      <ProFormSelect
        name="resourceProvider"
        label={intl.formatMessage({ id: 'page.project.cluster.config.resourceProvider' })}
        rules={[{ required: true }]}
        showSearch={true}
        request={() => DictDataService.listDictDataByType2(DICT_TYPE.flinkResourceProvider)}
      />
      <ProFormSelect
        name="deployMode"
        label={intl.formatMessage({ id: 'page.project.cluster.config.deployMode' })}
        rules={[{ required: true }]}
        showSearch={true}
        dependencies={['resourceProvider']}
        request={(params) => {
          if (params.resourceProvider == '0') {
            const dict: Dict = { label: 'Session', value: '2' };
            return Promise.resolve([dict]);
          }
          return DictDataService.listDictDataByType2(DICT_TYPE.flinkDeploymentMode);
        }}
      />
      <ProFormSelect
        name="clusterCredentialId"
        label={intl.formatMessage({ id: 'page.project.cluster.config.clusterCredential' })}
        rules={[{ required: true }]}
        showSearch={true}
        dependencies={['resourceProvider']}
        request={(params) => {
          const resourceParam: ResourceListParam = {
            resourceType: RESOURCE_TYPE.clusterCredential,
            label: params.resourceProvider,
          };
          return ResourceService.list(resourceParam).then((response) => {
            return response.records.map((item) => {
              return { label: item.name, value: item.id };
            });
          });
        }}
      />
      <ProFormSelect
        name="flinkReleaseId"
        label={intl.formatMessage({ id: 'page.project.cluster.config.flinkRelease' })}
        rules={[{ required: true }]}
        showSearch={true}
        dependencies={['flinkVersion']}
        request={(params) => {
          const resourceParam: ResourceListParam = {
            resourceType: RESOURCE_TYPE.flinkRelease,
            label: params.flinkVersion,
          };
          return ResourceService.list(resourceParam).then((response) => {
            return response.records.map((item) => {
              return { label: item.fileName, value: item.id };
            });
          });
        }}
      />
      <ProFormText
        name="remark"
        label={intl.formatMessage({ id: 'pages.project.remark' })}
        rules={[{ max: 200 }]}
      />
    </>
  );
};

export default BaseOptions;
