import { ModalFormProps } from '@/app.d';
import { UserService } from '@/services/admin/user.service';
import { Form, Input, message, Modal } from 'antd';
import { useIntl } from 'umi';

const PasswordEditForm: React.FC<
  ModalFormProps<{
    oldPassword?: string;
    password?: string;
    confirmPassword?: string;
  }>
> = ({ data, visible, onVisibleChange, onCancel }) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  return (
    <Modal
      visible={visible}
      title={intl.formatMessage({ id: 'pages.admin.usercenter.security.password.edit' })}
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          UserService.editPassword(
            values.oldPassword,
            values.password,
            values.confirmPassword,
          ).then((d) => {
            if (d.success) {
              message.success(intl.formatMessage({ id: 'app.common.operate.success' }));
              onVisibleChange(false);
            }
          });
        });
      }}
    >
      <Form form={form} layout="horizontal" labelCol={{ span: 6 }} wrapperCol={{ span: 16 }}>
        <Form.Item
          name="oldPassword"
          label={intl.formatMessage({ id: 'pages.admin.usercenter.security.password.old' })}
          rules={[
            { required: true },
            { min: 6 },
            { max: 32 },
          ]}
        >
          <Input.Password></Input.Password>
        </Form.Item>
        <Form.Item
          name="password"
          label={intl.formatMessage({ id: 'pages.admin.usercenter.security.password.new' })}
          rules={[
            { required: true },
            { min: 6 },
            { max: 32 },
            {
              validator: (rule, value, callback) => {
                if (value && value == form.getFieldValue('oldPassword')) {
                  callback(intl.formatMessage({ id: 'app.common.validate.sameToOldPassword' }));
                } else {
                  callback();
                }
              },
            },
          ]}
        >
          <Input.Password></Input.Password>
        </Form.Item>
        <Form.Item
          name="confirmPassword"
          label={intl.formatMessage({ id: 'pages.admin.usercenter.security.password.confirm' })}
          rules={[
            { required: true },
            { min: 6 },
            { max: 32 },
            {
              validator: (rule, value, callback) => {
                if (value && value != form.getFieldValue('password')) {
                  callback(intl.formatMessage({ id: 'app.common.validate.samePassword' }));
                } else {
                  callback();
                }
              },
            },
          ]}
        >
          <Input.Password></Input.Password>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default PasswordEditForm;
