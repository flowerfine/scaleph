import { ModalFormProps } from '@/app.d';
import { UserService } from '@/services/admin/user.service';
import { Button, Col, Form, Input, message, Modal, Row } from 'antd';
import { useState } from 'react';
import { useIntl } from 'umi';

const EmailBindForm: React.FC<ModalFormProps<any>> = ({
  data,
  visible,
  onVisibleChange,
  onCancel,
}) => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const [email, setEmail] = useState<string>('');

  const validEmail = (email: string) => {
    return !(email != null && email != undefined && email != '');
  };

  return (
    <Modal
      visible={visible}
      title={intl.formatMessage({ id: 'pages.admin.usercenter.security.email' })}
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          UserService.bindEmail(values.email, values.authCode).then((d) => {
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
          name="email"
          label={intl.formatMessage({ id: 'pages.admin.usercenter.security.email.email' })}
          rules={[
            { required: true },
            { max: 100 },
            { type: 'email' },
            {
              validator: (rule, value, callback) => {
                data.id
                  ? callback()
                  : UserService.isEmailExists(value).then((resp) => {
                      if (resp) {
                        callback();
                      } else {
                        callback(intl.formatMessage({ id: 'app.common.validate.sameEmail' }));
                      }
                    });
              },
            },
          ]}
        >
          <Input
            onChange={() => {
              let m = form.getFieldValue('email');
              setEmail(m);
            }}
          ></Input>
        </Form.Item>
        <Form.Item
          name="authCode"
          label={intl.formatMessage({ id: 'pages.admin.usercenter.security.email.authCode' })}
          rules={[{ required: true }, { max: 10 }]}
        >
          <Row gutter={[16, 0]}>
            <Col span={16}>
              <Form.Item noStyle>
                <Input></Input>
              </Form.Item>
            </Col>
            <Col span={8} style={{ textAlign: 'right' }}>
              <Button
                type="primary"
                disabled={validEmail(email)}
                onClick={() => {
                  let m = form.getFieldValue('email');
                  UserService.getAuthCode(m).then((d) => {
                    message.info(
                      intl.formatMessage({
                        id: 'pages.admin.usercenter.security.email.authCode.info',
                      }),
                    );
                  });
                }}
              >
                {intl.formatMessage({ id: 'pages.admin.usercenter.security.email.authCode.get' })}
              </Button>
            </Col>
          </Row>
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EmailBindForm;
