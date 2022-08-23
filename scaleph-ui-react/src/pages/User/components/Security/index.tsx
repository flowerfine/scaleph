import { SecUser } from '@/services/admin/typings';
import { getUserInfo } from '@/services/admin/user.service';
import { Button, List, Typography } from 'antd';
import { useEffect, useState } from 'react';
import { useIntl } from 'umi';
import EmailBindForm from './EmailBindForm';
import PasswordEditForm from './PasswordEditForm';

const Security: React.FC = () => {
  const intl = useIntl();
  const [user, setUser] = useState<SecUser>();
  const [passwordFormData, setPasswordFormData] = useState<{ visiable: boolean; data: any }>({
    visiable: false,
    data: {},
  });
  const [emailFormData, setEmailFormData] = useState<{ visiable: boolean; data: any }>({
    visiable: false,
    data: {},
  });

  const refreshUserInfo = () => {
    getUserInfo().then((resp) => {
      setUser(resp);
    });
  };

  useEffect(() => {
    refreshUserInfo();
  }, []);

  return (
    <div>
      <List itemLayout="horizontal">
        <List.Item
          extra={
            <Button
              type="link"
              onClick={() => {
                setPasswordFormData({ visiable: true, data: {} });
              }}
            >
              {intl.formatMessage({ id: 'pages.admin.usercenter.security.password.edit' })}
            </Button>
          }
        >
          <List.Item.Meta
            title={
              <Typography.Text strong>
                {intl.formatMessage({ id: 'pages.admin.usercenter.security.password' })}
              </Typography.Text>
            }
            description={
              <Typography.Text type="secondary">
                {intl.formatMessage({ id: 'pages.admin.usercenter.security.password.level' })}
              </Typography.Text>
            }
          ></List.Item.Meta>
        </List.Item>
        <List.Item
          extra={
            <Button
              type="link"
              onClick={() => {
                setEmailFormData({ visiable: true, data: {} });
              }}
            >
              {intl.formatMessage({ id: 'pages.admin.usercenter.security.email.bind' })}
            </Button>
          }
        >
          <List.Item.Meta
            title={
              <Typography.Text strong>
                {intl.formatMessage({ id: 'pages.admin.usercenter.security.email' })}
              </Typography.Text>
            }
            description={
              <Typography.Text type="secondary">
                {user?.userStatus?.label}
                {user?.userStatus?.value == '11' ? '\t' + user.email : ''}
              </Typography.Text>
            }
          ></List.Item.Meta>
        </List.Item>
      </List>
      {passwordFormData.visiable &&
        <PasswordEditForm
          visible={passwordFormData.visiable}
          onCancel={() => {
            setPasswordFormData({ visiable: false, data: {} });
          }}
          onVisibleChange={(visiable) => {
            setPasswordFormData({ visiable: visiable, data: {} });
            refreshUserInfo();
          }}
          data={{}}
        ></PasswordEditForm>}

      {emailFormData.visiable &&
        <EmailBindForm
          visible={emailFormData.visiable}
          onCancel={() => {
            setEmailFormData({ visiable: false, data: {} });
          }}
          onVisibleChange={(visiable) => {
            setEmailFormData({ visiable: visiable, data: {} });
            refreshUserInfo();
          }}
          data={{}}
        ></EmailBindForm>}
    </div>
  );
};

export default Security;
