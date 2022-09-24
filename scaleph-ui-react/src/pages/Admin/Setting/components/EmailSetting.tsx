import { SysConfigService } from '@/services/admin/sysConfig.service';
import { Button, Col, Form, Input, InputNumber, message, Row } from 'antd';
import { useEffect } from 'react';
import { useIntl } from 'umi';

const EmailSetting: React.FC = () => {
  const intl = useIntl();
  const [form] = Form.useForm();

  useEffect(() => {
    SysConfigService.getEmailConfig().then((d) => {
      form.setFieldsValue(d);
    });
  }, []);

  return (
    <Row>
      <Col span={6}>
        <Form
          name="emailSetting"
          form={form}
          layout="vertical"
          initialValues={{ port: 25 }}
          onFinish={() => {
            form.validateFields().then((values: any) => {
              SysConfigService.configEmail(values).then((resp) => {
                if (resp.success) {
                  message.success(intl.formatMessage({ id: 'app.common.operate.success' }));
                }
              });
            });
          }}
        >
          <Form.Item
            label={intl.formatMessage({ id: 'pages.admin.setting.email.host' })}
            name="host"
            rules={[{ required: true }]}
          >
            <Input></Input>
          </Form.Item>
          <Form.Item
            label={intl.formatMessage({ id: 'pages.admin.setting.email.port' })}
            name="port"
            rules={[{ required: true }]}
          >
            <InputNumber style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item
            label={intl.formatMessage({ id: 'pages.admin.setting.email.email' })}
            name="email"
            rules={[{ required: true }, { type: 'email' }, { max: 100 }]}
          >
            <Input></Input>
          </Form.Item>
          <Form.Item
            label={intl.formatMessage({ id: 'pages.admin.setting.email.password' })}
            name="password"
            rules={[{ required: true }]}
          >
            <Input.Password></Input.Password>
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit">
              {intl.formatMessage({ id: 'app.common.operate.confirm.label' })}
            </Button>
          </Form.Item>
        </Form>
      </Col>
    </Row>
  );
};

export default EmailSetting;
