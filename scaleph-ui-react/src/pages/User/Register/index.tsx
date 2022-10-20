import { AuthCode, RegisterInfo } from '@/app.d';
import Footer from '@/components/Footer';
import { UserService } from '@/services/admin/user.service';
import { AuthService } from '@/services/auth';
import { Button, Col, Form, Input, message, Row } from 'antd';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { SelectLang, useIntl } from 'umi';
import styles from './index.less';

const Register: React.FC = () => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const navigate = useNavigate();
  const [authCode, setAuthCode] = useState<AuthCode>();

  useEffect(() => {
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
          <Form
            form={form}
            layout="vertical"
            onFinish={() => {
              form.validateFields().then((values: any) => {
                const params: RegisterInfo = { ...values, uuid: authCode?.uuid };
                AuthService.register(params).then((d) => {
                  if (d.success) {
                    const msg = intl.formatMessage({ id: 'pages.user.register.success' });
                    message.success(msg);
                    setTimeout(() => {
                      navigate('/login');
                    }, 500);
                  } else {
                    refreshAuthCode();
                  }
                });
              });
            }}
          >
            <Form.Item
              label={intl.formatMessage({ id: 'pages.user.register.userName' })}
              name="userName"
              rules={[
                { required: true, min: 5, max: 30 },
                {
                  validator: (rule, value, callback) => {
                    UserService.isUserExists(value).then((resp) => {
                      if (resp) {
                        callback();
                      } else {
                        callback(intl.formatMessage({ id: 'app.common.validate.sameUserName' }));
                      }
                    });
                  },
                },
              ]}
            >
              <Input
                placeholder={intl.formatMessage({ id: 'pages.user.register.userName.placeholder' })}
              />
            </Form.Item>
            <Form.Item
              label={intl.formatMessage({ id: 'pages.user.register.email' })}
              name="email"
              rules={[
                { required: true },
                { type: 'email' },
                {
                  validator: (rule, value, callback) => {
                    UserService.isEmailExists(value).then((resp) => {
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
                placeholder={intl.formatMessage({ id: 'pages.user.register.userName.placeholder' })}
              />
            </Form.Item>
            <Form.Item
              label={intl.formatMessage({ id: 'pages.user.register.password' })}
              name="password"
              rules={[{ required: true }, { min: 6 }, { max: 32 }]}
            >
              <Input.Password
                placeholder={intl.formatMessage({ id: 'pages.user.register.password.placeholder' })}
              />
            </Form.Item>
            <Form.Item
              label={intl.formatMessage({ id: 'pages.user.register.confirmPassword' })}
              name="confirmPassword"
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
              <Input.Password
                placeholder={intl.formatMessage({id: 'pages.user.register.confirmPassword.placeholder'})}
              />
            </Form.Item>
            <Form.Item
              label={intl.formatMessage({ id: 'pages.user.register.authCode' })}
              rules={[{ required: true, len: 5 }]}
              name="authCode"
            >
              <Row gutter={[16, 0]}>
                <Col span={15}>
                  <Form.Item noStyle>
                    <Input
                      placeholder={intl.formatMessage({id: 'pages.user.register.authCode.placeholder'})}
                    />
                  </Form.Item>
                </Col>
                <Col span={9}>
                  <img
                    src={authCode?.img}
                    alt={intl.formatMessage({ id: 'pages.user.register.authCode' })}
                    onClick={refreshAuthCode}
                  />
                </Col>
              </Row>
            </Form.Item>
            <Form.Item style={{ marginBottom: '6px' }}>
              <Button type="primary" size="large" htmlType="submit" style={{ width: '100%' }}>
                {intl.formatMessage({ id: 'pages.user.register.register' })}
              </Button>
            </Form.Item>
            <Form.Item>
              <a href="/login" style={{ float: 'right' }}>
                {intl.formatMessage({ id: 'pages.user.register.login' })}
              </a>
            </Form.Item>
          </Form>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default Register;
