import { Dict, ModalFormProps } from '@/app.d';
import { DICT_TYPE } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import { FlinkArtifactService } from '@/services/dev/flinkArtifact.service';
import { FlinkArtifact } from '@/services/dev/typings';
import { Form, Input, message, Modal, Select } from 'antd';
import { useEffect, useState } from 'react';
import { useIntl } from 'umi';

const FlinkArtifactForm: React.FC<ModalFormProps<FlinkArtifact>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const [flinkArtifactTypeList, setFlinkArtifactTypeList] = useState<Dict[]>([]);

  useEffect(() => {
    DictDataService.listDictDataByType(DICT_TYPE.flinkArtifactType).then((d) => {
      setFlinkArtifactTypeList(d);
    });
  }, []);
  return (
    <Modal
      open={visible}
      title={
        intl.formatMessage({ id: 'app.common.operate.new.label' }) +
        intl.formatMessage({ id: 'pages.dev.artifact' })
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          const param: FlinkArtifact = {
            id: values.id,
            name: values.name,
            type: values.type,
            remark: values.remark,
          };
          data.id
            ? FlinkArtifactService.update(param).then((d) => {
                if (d.success) {
                  message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                  if (onVisibleChange) {
                    onVisibleChange(false);
                  }
                }
              })
            : FlinkArtifactService.add(param).then((d) => {
                if (d.success) {
                  message.success(intl.formatMessage({ id: 'app.common.operate.new.success' }));
                  if (onVisibleChange) {
                    onVisibleChange(false);
                  }
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
          name: data.name,
          type: data.type?.value,
          remark: data.remark,
        }}
      >
        <Form.Item name="id" hidden>
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="name"
          label={intl.formatMessage({ id: 'pages.dev.artifact.name' })}
          rules={[{ required: true }, { max: 32 }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="type"
          label={intl.formatMessage({ id: 'pages.dev.artifact.type' })}
          rules={[{ required: true }]}
        >
          <Select allowClear={true} optionFilterProp="label">
            {flinkArtifactTypeList.map((item) => {
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
          <Input />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default FlinkArtifactForm;
