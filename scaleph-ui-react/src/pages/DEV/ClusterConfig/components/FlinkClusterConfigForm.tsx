import {Dict, ModalFormProps} from '@/app.d';
import {Form, Input, message, Modal, Select} from 'antd';
import {useIntl} from 'umi';
import {FlinkClusterConfig} from "@/services/dev/typings";
import {useEffect, useState} from "react";
import {DICT_TYPE, RESOURCE_TYPE} from "@/constant";
import {Resource, ResourceListParam} from "@/services/resource/typings";
import {listDictDataByType} from "@/services/admin/dictData.service";
import {list} from "@/services/resource/resource.service";
import {add, update} from "@/services/dev/flinkClusterConfig.service";

const FlinkClusterConfigForm: React.FC<ModalFormProps<FlinkClusterConfig>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
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
  }, []);

  return (
    <Modal
      visible={visible}
      title={
        data.id
          ? intl.formatMessage({ id: 'app.common.operate.edit.label' }) +
          intl.formatMessage({ id: 'pages.dev.clusterConfig' })
          : intl.formatMessage({ id: 'app.common.operate.new.label' }) +
          intl.formatMessage({ id: 'pages.dev.clusterConfig' })
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          let d: FlinkClusterConfig = {
            id: values.id,
            name: values.name,
            flinkVersion: {value: values.flinkVersion},
            resourceProvider: {value: values.resourceProvider},
            deployMode: {value: values.deployMode},
            flinkRelease: {id: values.flinkRelease},
            clusterCredential: {id: values.clusterCredential},
            remark: values.remark,
          };
          data.id
            ? update({ ...d }).then((d) => {
              if (d.success) {
                message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                onVisibleChange(false);
              }
            })
            : add({ ...d }).then((d) => {
              if (d.success) {
                message.success(intl.formatMessage({ id: 'app.common.operate.new.success' }));
                onVisibleChange(false);
              }
            });
        });
      }}
    >
      <Form
        form={form}
        layout="horizontal"
        labelCol={{ span: 6 }}
        wrapperCol={{ span: 16 }}
        initialValues={data}
      >
        <Form.Item name="id" hidden>
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="name"
          label={intl.formatMessage({ id: 'pages.dev.clusterConfig.name' })}
          rules={[
            { required: true },
            { max: 30 },
            {
              pattern: /^[\w\s_]+$/,
              message: intl.formatMessage({ id: 'app.common.validate.characterWord3' }),
            },
          ]}
        >
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="flinkVersion"
          label={intl.formatMessage({ id: 'pages.dev.clusterConfig.flinkVersion' })}
          rules={[{ required: true }]}
        >
          <Select
            disabled={data.id ? true : false}
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
                resourceType: RESOURCE_TYPE.flinkRelease,
                label: value,
              }
              list(param).then((d) => {
                setFlinkReleaseList(d.records);
              });
            }}
          >
            {flinkVersionList.map((item) => {
              return (
                <Select.Option key={item.value} value={item.value}>
                  {item.label}
                </Select.Option>
              );
            })}
          </Select>
        </Form.Item>
        <Form.Item
          name="flinkRelease"
          label={intl.formatMessage({ id: 'pages.dev.clusterConfig.flinkRelease' })}
          rules={[{ required: true }]}
        >
          <Select
            disabled={data.id ? true : false}
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
          label={intl.formatMessage({ id: 'pages.dev.clusterConfig.deployMode' })}
          rules={[{ required: true }]}
        >
          <Select
            disabled={data.id ? true : false}
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
          label={intl.formatMessage({ id: 'pages.dev.clusterConfig.resourceProvider' })}
          rules={[{ required: true }]}
        >
          <Select
            disabled={data.id ? true : false}
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
          label={intl.formatMessage({ id: 'pages.dev.clusterConfig.clusterCredential' })}
          rules={[{ required: true }]}
        >
          <Select
            disabled={data.id ? true : false}
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
        <Form.Item
          name="remark"
          label={intl.formatMessage({ id: 'pages.dev.remark' })}
          rules={[{ max: 200 }]}
        >
          <Input></Input>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default FlinkClusterConfigForm;
