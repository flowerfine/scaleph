import Footer from '@/components/Footer';
import { login } from '@/services/ant-design-pro/api';
import { SelectLang } from '@umijs/max';
import { Button, Checkbox, Col, Form, Input, Row } from 'antd';
import React, { useState } from 'react';
import styles from './index.less';

const Login: React.FC = () => {
  const [form] = Form.useForm();
  const [checkNeed, setCheckNeed] = useState(false);
  const handleSubmit = async () => {
    try {
      form.validateFields().then((values: any) => {
        console.log(1111111, { ...values, remember: checkNeed });
      });
      //   // 登录
      const msg = await login({ username: '111', password: '2222' });
      //   if (msg.status === 'ok') {
      //     const defaultLoginSuccessMessage = intl.formatMessage({
      //       id: 'pages.login.success',
      //       defaultMessage: '登录成功！',
      //     });
      //     message.success(defaultLoginSuccessMessage);
      //     await fetchUserInfo();
      //     const urlParams = new URL(window.location.href).searchParams;
      //     history.push(urlParams.get('redirect') || '/');
      //     return;
      //   }
      //   console.log(msg);
      //   // 如果失败去设置用户错误信息
      //   setUserLoginState(msg);
    } catch (error) {
      //   const defaultLoginFailureMessage = intl.formatMessage({
      //     id: 'pages.login.failure',
      //     defaultMessage: '登录失败，请重试！',
      //   });
      //   console.log(error);
      //   message.error(defaultLoginFailureMessage);
    }
  };
  const onCheckboxChange = (e: { target: { checked: boolean } }) => {
    setCheckNeed(e.target.checked);
  };
  return (
    <div className={styles.container}>
      <div className={styles.lang} data-lang>
        {SelectLang && <SelectLang />}
      </div>
      <div className={styles.content}>
        <div className={styles.logoInfo}>
          <img className={styles.logo} alt="logo" src="/devui-logo.svg" />
          <span className={styles.title}>Scaleph</span>
        </div>
        <div className={styles.loginForm}>
          <Form form={form} layout="vertical">
            <Form.Item label="用户名:" name="userName" rules={[{ required: true }]}>
              <Input placeholder="请输入用户名：sys_admin" />
            </Form.Item>
            <Form.Item label="密码:" name="password" rules={[{ required: true }]}>
              <Input type="password" placeholder="请输入密码：123456" />
            </Form.Item>
            <Form.Item label="验证码:">
              <Row>
                <Col>
                  <Form.Item name="uuid" rules={[{ required: true }]}>
                    <Input placeholder="请输入验证码" />
                  </Form.Item>
                </Col>
                <Col>
                  <img
                    style={{ height: '32px', marginLeft: '30px' }}
                    src="data:image/png;base64,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"
                    alt="验证码"
                  />
                </Col>
              </Row>
            </Form.Item>
            <Form.Item>
              <Checkbox checked={checkNeed} onChange={(e) => onCheckboxChange(e)}>
                记住我
              </Checkbox>
              <div style={{ float: 'right' }}>
                <a>忘记密码？</a>
              </div>
            </Form.Item>
            <Form.Item>
              <Button
                type="primary"
                htmlType="submit"
                style={{ width: '100%' }}
                onClick={handleSubmit}
              >
                登录
              </Button>
            </Form.Item>
          </Form>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default Login;
