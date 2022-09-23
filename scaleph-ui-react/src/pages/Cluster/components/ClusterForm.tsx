import { Dict, ModalFormProps } from '@/app.d';
import { DICT_TYPE } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import { ClusterService } from '@/services/cluster/cluster.service';
import { DiClusterConfig } from '@/services/cluster/typings';
import { Form, Input, message, Modal, Select } from 'antd';
import { useEffect, useState } from 'react';
import { useIntl } from 'umi';

const ClusterForm: React.FC<ModalFormProps<DiClusterConfig>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const [clusterTypeList, setClusterTypeList] = useState<Dict[]>([]);
  useEffect(() => {
    DictDataService.listDictDataByType(DICT_TYPE.clusterType).then((d) => {
      setClusterTypeList(d);
    });
  }, []);

  return (
    <Modal
      visible={visible}
      title={
        data.id
          ? intl.formatMessage({ id: 'app.common.operate.edit.label' }) +
            intl.formatMessage({ id: 'pages.cluster' })
          : intl.formatMessage({ id: 'app.common.operate.new.label' }) +
            intl.formatMessage({ id: 'pages.cluster' })
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          let d: DiClusterConfig = {
            id: values.id,
            clusterName: values.clusterName,
            clusterType: { value: values.clusterType },
            clusterHome: values.clusterHome,
            clusterVersion: values.clusterVersion,
            clusterConf: values.clusterConf,
            remark: values.remark,
          };
          data.id
            ? ClusterService.updateCluster({ ...d }).then((d) => {
                if (d.success) {
                  message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                  onVisibleChange(false);
                }
              })
            : ClusterService.addCluster({ ...d }).then((d) => {
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
        initialValues={{
          id: data.id,
          clusterName: data.clusterName,
          clusterType: data.clusterType?.value,
          clusterHome: data.clusterHome,
          clusterConf: data.clusterConf,
          clusterVersion: data.clusterVersion,
          remark: data.remark,
        }}
      >
        <Form.Item name="id" hidden>
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="clusterName"
          label={intl.formatMessage({ id: 'pages.cluster.clusterName' })}
          rules={[{ required: true }, { max: 128 }]}
        >
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="clusterType"
          label={intl.formatMessage({ id: 'pages.cluster.clusterType' })}
          rules={[{ required: true }]}
        >
          <Select
            disabled={data.id ? true : false}
            showSearch={true}
            allowClear={true}
            optionFilterProp="label"
            filterOption={(input, option) =>
              (option!.children as unknown as string).toLowerCase().includes(input.toLowerCase())
            }
          >
            {clusterTypeList.map((item) => {
              return (
                <Select.Option key={item.value} value={item.value}>
                  {item.label}
                </Select.Option>
              );
            })}
          </Select>
        </Form.Item>
        <Form.Item
          name="clusterHome"
          label={intl.formatMessage({ id: 'pages.cluster.clusterHome' })}
          rules={[{ required: true }, { max: 256 }]}
        >
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="clusterVersion"
          label={intl.formatMessage({ id: 'pages.cluster.clusterVersion' })}
          rules={[{ max: 60 }]}
        >
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="clusterConf"
          label={intl.formatMessage({ id: 'pages.cluster.clusterConf' })}
          rules={[{ required: true }]}
        >
          <Input.TextArea></Input.TextArea>
        </Form.Item>
        <Form.Item
          name="remark"
          label={intl.formatMessage({ id: 'pages.cluster.remark' })}
          rules={[{ max: 200 }]}
        >
          <Input></Input>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default ClusterForm;
