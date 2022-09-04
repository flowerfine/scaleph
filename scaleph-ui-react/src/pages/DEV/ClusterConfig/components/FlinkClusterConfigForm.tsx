import {Dict, ModalFormProps} from '@/app.d';
import { DiProject } from '@/services/project/typings';
import { addProject, updateProject } from '@/services/project/project.service';
import {Form, Input, message, Modal, Select} from 'antd';
import { useIntl } from 'umi';
import {FlinkClusterConfig} from "@/services/dev/typings";
import {useEffect, useState} from "react";
import {listDictDataByType} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constant";

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
          let d: DiProject = {
            id: values.id,
            projectCode: values.projectCode,
            projectName: values.projectName,
            remark: values.remark,
          };
          data.id
            ? updateProject({ ...d }).then((d) => {
              if (d.success) {
                message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                onVisibleChange(false);
              }
            })
            : addProject({ ...d }).then((d) => {
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
