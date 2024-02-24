import { SysConfigService } from '@/services/admin/sysConfig.service';
import { Button, Col, Form, Input, message, Row } from 'antd';
import { useEffect } from 'react';
import { useIntl } from 'umi';

const BasicSetting: React.FC = () => {
  const intl = useIntl();
  const [form] = Form.useForm();

  useEffect(() => {
    SysConfigService.getBasicConfig().then((d) => {
      form.setFieldsValue(d);
    });
  }, []);

  return (
    <Row>
      <Col span={6}>
        <Form
          name="basicSetting"
          form={form}
          layout="vertical"
          onFinish={() => {
            form.validateFields().then((values: any) => {
              SysConfigService.configBasic(values).then((resp) => {
                if (resp.success) {
                  message.success(intl.formatMessage({ id: 'app.common.operate.success' }));
                }
              });
            });
          }}
        >
          <Form.Item
            label={intl.formatMessage({ id: 'pages.admin.setting.basic.seatunnelHome' })}
            name="seatunnelHome"
            rules={[{ required: true }, { max: 1024 }]}
          >
            <Input></Input>
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

export default BasicSetting;
