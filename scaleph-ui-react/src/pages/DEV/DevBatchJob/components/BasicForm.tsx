import {Card, Form, Select} from "antd";
import {ProForm, ProFormSelect, ProFormText} from "@ant-design/pro-components";
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
  const [flinkVersionList, setFlinkVersionList] = useState<Dict[]>([]);
  const [resourceProviderList, setResourceProviderList] = useState<Dict[]>([]);
  const [deployModeList, setDeployModeList] = useState<Dict[]>([]);
  const [flinkReleaseList, setFlinkReleaseList] = useState<Resource[]>([]);
  const [clusterCredentialList, setClusterCredentialList] = useState<Resource[]>([]);

  useEffect(() => {
    listDictDataByType(DICT_TYPE.flinkVersion).then((d) => {
      setFlinkVersionList(d);
    });
    listDictDataByType(DICT_TYPE.flinkResourceProvider).then((d) => {
      setResourceProviderList(d);
    });
    listDictDataByType(DICT_TYPE.flinkDeploymentMode).then((d) => {
      setDeployModeList(d);
    });
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
      <Card title="仓库管理" bordered={false}>

      </Card>
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
      />
      <Form.Item
        name="flinkRelease"
        label={intl.formatMessage({id: 'pages.dev.clusterConfig.flinkRelease'})}
        rules={[{required: true}]}
      >
        <Select
          showSearch={true}
          allowClear={true}
          optionFilterProp="label"
          filterOption={(input, option) =>
            (option!.children as unknown as string)
              .toLowerCase()
              .includes(input.toLowerCase())
          }
        >
          {flinkReleaseList.map((item) => {
            return (
              <Select.Option key={item.id} value={item.id}>
                {item.name}
              </Select.Option>
            );
          })}
        </Select>
      </Form.Item>
      <Form.Item
        name="deployMode"
        label={intl.formatMessage({id: 'pages.dev.clusterConfig.deployMode'})}
        rules={[{required: true}]}
      >
        <Select
          showSearch={true}
          allowClear={true}
          optionFilterProp="label"
          filterOption={(input, option) =>
            (option!.children as unknown as string)
              .toLowerCase()
              .includes(input.toLowerCase())
          }
        >
          {deployModeList.map((item) => {
            return (
              <Select.Option key={item.value} value={item.value}>
                {item.label}
              </Select.Option>
            );
          })}
        </Select>
      </Form.Item>
      <Form.Item
        name="resourceProvider"
        label={intl.formatMessage({id: 'pages.dev.clusterConfig.resourceProvider'})}
        rules={[{required: true}]}
      >
        <Select
          showSearch={true}
          allowClear={true}
          optionFilterProp="label"
          filterOption={(input, option) =>
            (option!.children as unknown as string)
              .toLowerCase()
              .includes(input.toLowerCase())
          }
          onChange={(value, option) => {
            const param: ResourceListParam = {
              resourceType: RESOURCE_TYPE.clusterCredential,
              label: value,
            }
            list(param).then((d) => {
              setClusterCredentialList(d.records);
            });
            form.setFieldValue('clusterCredential', null)
          }}
        >
          {resourceProviderList.map((item) => {
            return (
              <Select.Option key={item.value} value={item.value}>
                {item.label}
              </Select.Option>
            );
          })}
        </Select>
      </Form.Item>
      <Form.Item
        name="clusterCredential"
        label={intl.formatMessage({id: 'pages.dev.clusterConfig.clusterCredential'})}
        rules={[{required: true}]}
      >
        <Select
          showSearch={true}
          allowClear={true}
          optionFilterProp="label"
          filterOption={(input, option) =>
            (option!.children as unknown as string)
              .toLowerCase()
              .includes(input.toLowerCase())
          }
        >
          {clusterCredentialList.map((item) => {
            return (
              <Select.Option key={item.id} value={item.id}>
                {item.name}
              </Select.Option>
            );
          })}
        </Select>
      </Form.Item>
    </ProForm.Group>
  </div>)
}

export default BasicForm;
