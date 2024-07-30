import {useEffect, useState} from "react";
import {Button, Col, Form, Input, message, Row} from "antd";
import {useEmotionCss} from "@ant-design/use-emotion-css";
import {Helmet, SelectLang, useIntl, useModel, useNavigate} from "@umijs/max";
import {PATTERNS} from "@/constants";
import AppsLogo from "@/components/AppLogoComponent";
import {AuthService} from "@/services/auth";
import {UserService} from "@/services/admin/security/user.service";
import styles from "../index.less";
import {AuthCode, RegisterInfo} from "@/typings";

const Lang = () => {
  const langClassName = useEmotionCss(({token}) => {
    return {
      width: 42,
      height: 42,
      lineHeight: "42px",
      position: "fixed",
      right: 16,
      textAlign: "center",
      borderRadius: token.borderRadius,
      ":hover": {
        backgroundColor: token.colorBgTextHover,
      },
    };
  });

  return <SelectLang className={langClassName}/>;
};

const Register: React.FC = () => {
  const intl = useIntl();
  const [form] = Form.useForm();
  const navigate = useNavigate();
  const [authCode, setAuthCode] = useState<AuthCode>();
  const {initialState, setInitialState} = useModel("@@initialState");

  useEffect(() => {
    refreshAuthCode();
  }, []);

  const refreshAuthCode = async () => {
    const data = await AuthService.refreshAuthImage();
    setAuthCode(data);
  };

  const handleSubmit = () => {
    try {
      form.validateFields().then((values: RegisterInfo) => {
        const params: RegisterInfo = {
          ...values,
          uuid: authCode?.uuid as string,
        };
        AuthService.register(params).then((resp) => {
          if (resp.success) {
            message.success(intl.formatMessage({id: "pages.user.register.success"}));
            setTimeout(() => {
              navigate("/user/login");
            }, 500);
          } else {
            refreshAuthCode();
          }
        });
      });
    } catch (error) {
      message.error(intl.formatMessage({id: "pages.user.register.failure"}));
    }
  };
  return (
    <div className={styles.container}>
      <Helmet>
        <title>
          {intl.formatMessage({id: "menu.register"})}-{" Scaleph"}
        </title>
      </Helmet>
      <Lang/>
      <div className={styles.mainContent}>
        <div className={styles.logoInfo}>
          <AppsLogo
            width={60}
            height={60}
            color={initialState?.settings?.colorPrimary as string}
          />
          <span className={styles.title}>{initialState?.settings?.title}</span>
        </div>
        <div className={styles.registerForm}>
          <Form form={form} layout="vertical" onFinish={handleSubmit}>
            <Form.Item
              name="userName"
              label={intl.formatMessage({id: "pages.user.register.userName"})}
              rules={[
                {required: true, max: 30, min: 5},
                {
                  pattern: PATTERNS.characterWord,
                  message: intl.formatMessage({id: "pages.common.validate.characterWord"}),
                },
                {
                  validator: (rule, value, callback) => {
                    UserService.isUserExists(value).then((resp) => {
                      if (resp) {
                        callback();
                      } else {
                        callback(intl.formatMessage({id: "pages.common.validate.sameUserName"}));
                      }
                    });
                  },
                },
              ]}
            >
              <Input
                placeholder={intl.formatMessage({id: "pages.user.register.userName.placeholder"})}
              />
            </Form.Item>
            <Form.Item
              name="email"
              label={intl.formatMessage({id: "pages.user.register.email"})}
              rules={[
                {required: true},
                {type: "email"},
                {
                  validator: (rule, value, callback) => {
                    UserService.isEmailExists(value).then((resp) => {
                      if (resp) {
                        callback();
                      } else {
                        callback(intl.formatMessage({id: "pages.common.validate.sameEmail"}));
                      }
                    });
                  },
                },
              ]}
            >
              <Input
                placeholder={intl.formatMessage({id: "pages.user.register.email.placeholder"})}
              />
            </Form.Item>
            <Form.Item
              name="password"
              label={intl.formatMessage({id: "pages.user.register.password"})}
              rules={[{required: true, min: 6, max: 32}]}
            >
              <Input.Password
                placeholder={intl.formatMessage({id: "pages.user.register.password.placeholder"})}
              />
            </Form.Item>
            <Form.Item
              name="confirmPassword"
              label={intl.formatMessage({id: "pages.user.register.confirmPassword"})}
              rules={[
                {
                  required: true,
                  min: 6,
                  max: 32,
                  validator: (rule, value, callback) => {
                    if (value && value != form.getFieldValue("password")) {
                      callback(
                        intl.formatMessage({id: "pages.common.validate.samePassword"})
                      );
                    } else {
                      callback();
                    }
                  },
                },
              ]}
            >
              <Input.Password
                placeholder={intl.formatMessage({id: "pages.user.register.confirmPassword.placeholder"})}
              />
            </Form.Item>
            <Form.Item
              name="authCode"
              label={intl.formatMessage({id: "pages.user.register.authCode"})}
              rules={[{required: true, len: 5}]}
            >
              <Row gutter={[16, 0]}>
                <Col span={15}>
                  <Form.Item noStyle>
                    <Input
                      placeholder={intl.formatMessage({id: "pages.user.register.authCode.placeholder"})}
                    />
                  </Form.Item>
                </Col>
                <Col span={9}>
                  <img
                    src={authCode?.img}
                    alt={intl.formatMessage({id: "pages.user.register.authCode"})}
                    onClick={refreshAuthCode}
                  />
                </Col>
              </Row>
            </Form.Item>
            <Form.Item style={{marginBottom: "6px"}}>
              <Button
                type="primary"
                size="large"
                htmlType="submit"
                style={{width: "100%"}}
                onClick={() => {
                }}
              >
                {intl.formatMessage({id: "pages.user.register.register"})}
              </Button>
            </Form.Item>
            <Form.Item>
              <a href="/user/login" style={{float: "right"}}>
                {intl.formatMessage({id: "pages.user.register.login"})}
              </a>
            </Form.Item>
          </Form>
        </div>
      </div>
    </div>
  );
};

export default Register;
