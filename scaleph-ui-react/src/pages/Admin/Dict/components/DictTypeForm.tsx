import { ModalFormProps } from '@/app.d';
import { DictTypeService } from '@/services/admin/dictType.service';
import { SysDictType } from '@/services/admin/typings';
import { Form, Input, message, Modal } from 'antd';
import { useIntl } from 'umi';

const DictTypeForm: React.FC<ModalFormProps<SysDictType>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  return (
    <Modal
      visible={visible}
      title={
        data.id
          ? intl.formatMessage({ id: 'app.common.operate.edit.label' }) +
            intl.formatMessage({ id: 'pages.admin.dict.dictType' })
          : intl.formatMessage({ id: 'app.common.operate.new.label' }) +
            intl.formatMessage({ id: 'pages.admin.dict.dictType' })
      }
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          data.id
            ? DictTypeService.updateDictType({ ...values }).then((d) => {
                if (d.success) {
                  message.success(intl.formatMessage({ id: 'app.common.operate.edit.success' }));
                  onVisibleChange(false);
                }
              })
            : DictTypeService.addDictType({ ...values }).then((d) => {
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
          name="dictTypeCode"
          label={intl.formatMessage({ id: 'pages.admin.dict.dictTypeCode' })}
          rules={[
            { required: true },
            { max: 30 },
            {
              pattern: /^[a-zA-Z0-9_]+$/,
              message: intl.formatMessage({ id: 'app.common.validate.characterWord' }),
            },
          ]}
        >
          <Input disabled={data.id ? true : false}></Input>
        </Form.Item>
        <Form.Item
          name="dictTypeName"
          label={intl.formatMessage({ id: 'pages.admin.dict.dictTypeName' })}
          rules={[{ required: true }, { max: 100 }]}
        >
          <Input></Input>
        </Form.Item>
        <Form.Item
          name="remark"
          label={intl.formatMessage({ id: 'pages.admin.dict.remark' })}
          rules={[{ max: 200 }]}
        >
          <Input></Input>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default DictTypeForm;
