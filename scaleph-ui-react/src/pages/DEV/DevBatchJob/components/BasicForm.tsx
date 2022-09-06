import {Card, Form, Select} from "antd";
import {ProForm, ProFormSelect, ProFormText, RequestOptionsType} from "@ant-design/pro-components";
import {useIntl} from "@@/exports";
import {useEffect, useState} from "react";
import {Dict} from '@/app.d';
import {Resource, ResourceListParam} from "@/services/resource/typings";
import {listDictDataByType} from "@/services/admin/dictData.service";
import {DICT_TYPE, RESOURCE_TYPE} from "@/constant";
import {list} from "@/services/resource/resource.service";

const BasicForm: React.FC = () => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const [flinkReleaseList, setFlinkReleaseList] = useState<Resource[]>([]);
  const [clusterCredentialList, setClusterCredentialList] = useState<Resource[]>([]);

  // [key:string]:any

  useEffect(() => {
    const flinkReleaseParam: ResourceListParam = {
      resourceType: RESOURCE_TYPE.flinkRelease,
    }
    list(flinkReleaseParam).then((d) => {
      setFlinkReleaseList(d.records);
    });
    const clusterCredentialParam: ResourceListParam = {
      resourceType: RESOURCE_TYPE.clusterCredential,
    }
    list(clusterCredentialParam).then((d) => {
      setClusterCredentialList(d.records);
    });
  }, []);

  return (<div>
    <ProForm.Group
      title={"Basic"}
      collapsible={true}
    >
      <ProFormText
        name="name"
        label={intl.formatMessage({id: 'pages.dev.clusterConfig.name'})}
        rules={[
          {required: true},
          {max: 30},
          {
            pattern: /^[\w\s_]+$/,
            message: intl.formatMessage({id: 'app.common.validate.characterWord3'}),
          },
        ]}
      />
      <ProFormSelect
        name="flinkVersion"
        label={intl.formatMessage({id: 'pages.dev.clusterConfig.flinkVersion'})}
        colProps={{xl: 8, md: 12}}
        rules={[{required: true}]}
        showSearch={true}
        allowClear={true}
        request={() => listDictDataByType(DICT_TYPE.flinkVersion)}
      />
      <ProFormSelect
        name="flinkRelease"
        label={intl.formatMessage({id: 'pages.dev.clusterConfig.flinkRelease'})}
        colProps={{xl: 8, md: 12}}
        rules={[{required: true}]}
        showSearch={true}
        allowClear={true}
        dependencies={['flinkVersion']}
        request={(params) => {
          const flinkReleaseParam: ResourceListParam = {
            resourceType: RESOURCE_TYPE.flinkRelease,
            label: params.flinkVersion
          }
          return list(flinkReleaseParam).then((response) => response.records)
        }}
      />
      <ProFormSelect
        name="deployMode"
        label={intl.formatMessage({id: 'pages.dev.clusterConfig.deployMode'})}
        colProps={{xl: 8, md: 12}}
        rules={[{required: true}]}
        showSearch={true}
        allowClear={true}
        request={() => listDictDataByType(DICT_TYPE.flinkDeploymentMode)}
      />
      <ProFormSelect
        name="resourceProvider"
        label={intl.formatMessage({id: 'pages.dev.clusterConfig.resourceProvider'})}
        colProps={{xl: 8, md: 12}}
        rules={[{required: true}]}
        showSearch={true}
        allowClear={true}
        request={() => listDictDataByType(DICT_TYPE.flinkResourceProvider)}
      />

      <ProFormSelect
        name="clusterCredential"
        label={intl.formatMessage({id: 'pages.dev.clusterConfig.clusterCredential'})}
        colProps={{xl: 8, md: 12}}
        rules={[{required: true}]}
        showSearch={true}
        allowClear={true}
      />

    </ProForm.Group>
  </div>)
}

export default BasicForm;
