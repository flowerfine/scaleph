import { AuthCode, LoginInfo } from '@/app.d';
import Footer from '@/components/Footer';
import { USER_AUTH } from '@/constant';
import { AuthService } from '@/services/auth';
import { Button, Checkbox, Col, Form, Input, message, Row } from 'antd';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { SelectLang, useIntl, useModel } from 'umi';
import styles from './index.less';

const Login: React.FC = () => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const navigate = useNavigate();
  const [authCode, setAuthCode] = useState<AuthCode>();
  const { refresh } = useModel('@@initialState');
  const [initialValues, setInitialValues] = useState({
    remember: true,
  });
  const handleSubmit = async () => {
    try {
      form.validateFields().then((values: any) => {
        const params: LoginInfo = { ...values, uuid: authCode?.uuid };
        AuthService.login(params).then(async (d) => {
          if (d.success) {
            localStorage.setItem(USER_AUTH.token, d.data);
            refresh();
            const msg = intl.formatMessage({ id: 'pages.user.login.success' });
            message.success(msg);
            setTimeout(() => {
              navigate('/');
            }, 500);
          } else {
            refreshAuthCode();
          }
        });
      });
    } catch (error) {
      const defaultLoginFailureMessage = intl.formatMessage({
        id: 'pages.user.login.failure',
      });
      console.log(error);
      message.error(defaultLoginFailureMessage);
    }
  };

  useEffect(() => {
    localStorage.clear();
    refreshAuthCode();
  }, []);

  const refreshAuthCode = async () => {
    const data = await AuthService.refreshAuthImage();
    setAuthCode(data);
  };

  return (
    <div className={styles.container}>
      <div className={styles.lang} data-lang>
        {SelectLang && <SelectLang />}
      </div>
      <div className={styles.content}>
        <div className={styles.logoInfo}>
          <img className={styles.logo} alt="logo" src="/scaleph.svg" />
          <span className={styles.title}>Scaleph</span>
        </div>
        <div className={styles.loginForm}>
          <Form form={form} initialValues={initialValues} layout="vertical">
            <Form.Item
              label={intl.formatMessage({ id: 'pages.user.login.userName' })}
              name="userName"
              rules={[{ required: true, min: 5, max: 30 }]}
            >
              <Input
                placeholder={intl.formatMessage({ id: 'pages.user.login.userName.placeholder' })}
              />
            </Form.Item>
            <Form.Item
              label={intl.formatMessage({ id: 'pages.user.login.password' })}
              name="password"
              rules={[{ required: true, min: 6, max: 32 }]}
            >
              <Input.Password
                placeholder={intl.formatMessage({ id: 'pages.user.login.password.placeholder' })}
              />
            </Form.Item>
            <Form.Item
              label={intl.formatMessage({ id: 'pages.user.login.authCode' })}
              rules={[{ required: true, len: 5 }]}
              name="authCode"
            >
              <Row gutter={[16, 0]}>
                <Col span={15}>
                  <Form.Item noStyle>
                    <Input
                      placeholder={intl.formatMessage({
                        id: 'pages.user.login.authCode.placeholder',
                      })}
                    />
                  </Form.Item>
                </Col>
                <Col span={9}>
                  <img
                    src={authCode?.img}
                    alt={intl.formatMessage({ id: 'pages.user.login.authCode' })}
                    onClick={refreshAuthCode}
                  />
                </Col>
              </Row>
            </Form.Item>
            <Form.Item style={{ marginBottom: '12px' }}>
              <Form.Item noStyle name="remember" valuePropName="checked">
                <Checkbox>{intl.formatMessage({ id: 'pages.user.login.remember' })}</Checkbox>
              </Form.Item>
              <div style={{ float: 'right' }}>
                <a>{intl.formatMessage({ id: 'pages.user.login.forget' })}</a>
              </div>
            </Form.Item>
            <Form.Item style={{ marginBottom: '6px' }}>
              <Button
                type="primary"
                size="large"
                htmlType="submit"
                style={{ width: '100%' }}
                onClick={handleSubmit}
              >
                {intl.formatMessage({ id: 'pages.user.login.login' })}
              </Button>
            </Form.Item>
            <Form.Item>
              <a href="/register" style={{ float: 'right' }}>
                {intl.formatMessage({ id: 'pages.user.login.register' })}
              </a>
            </Form.Item>
          </Form>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default Login;
