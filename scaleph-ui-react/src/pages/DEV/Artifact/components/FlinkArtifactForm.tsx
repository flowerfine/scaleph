import {Form, Input, message, Modal, Select} from 'antd';
import {useIntl} from 'umi';
import {useEffect, useState} from "react";
import {DICT_TYPE} from "@/constant";
import {Dict, ModalFormProps} from '@/app.d';
import {FlinkArtifact} from "@/services/dev/typings";
import {listDictDataByType} from "@/services/admin/dictData.service";
import {add, update} from "@/services/dev/flinkArtifact.service";

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
    listDictDataByType(DICT_TYPE.flinkArtifactType).then((d) => {
      setFlinkArtifactTypeList(d);
    });
  }, []);
  return (
    <Modal
      visible={visible}
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
            type: { value: values.type },
            remark: values.remark
          };
          data.id
            ? update(param).then((d) => {
              if (d.success) {
                message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                onVisibleChange(false);
              }
            })
            : add(param).then((d) => {
              if (d.success) {
                message.success(intl.formatMessage({ id: 'app.common.operate.new.success' }));
                onVisibleChange(false);
              }
            });
        });
      }}
    >
      <Form form={form} layout="horizontal" labelCol={{ span: 6 }} wrapperCol={{ span: 16 }}
            initialValues={{
              id: data.id,
              name: data.name,
              type: data.type?.value,
              remark: data.remark
            }}>
        <Form.Item name="id" hidden>
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="name"
          label={intl.formatMessage({ id: 'pages.dev.artifact.name' })}
          rules={[{ required: true }, { max: 32 }]}>
          <Input/>
        </Form.Item>
        <Form.Item
          name="type"
          label={intl.formatMessage({ id: 'pages.dev.artifact.type' })}
          rules={[{ required: true }]}>
          <Select
            allowClear={true}
            optionFilterProp="label">
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
          rules={[{ max: 200 }]}>
          <Input/>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default FlinkArtifactForm;
