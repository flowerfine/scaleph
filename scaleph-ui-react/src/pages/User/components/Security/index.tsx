import { SecUser } from '@/services/admin/typings';
import { UserService } from '@/services/admin/user.service';
import { Button, List, Typography } from 'antd';
import { useEffect, useState } from 'react';
import { useIntl } from 'umi';
import EmailBindForm from './EmailBindForm';
import PasswordEditForm from './PasswordEditForm';

const Security: React.FC = () => {
  const intl = useIntl();
  const [user, setUser] = useState<SecUser>();
  const [passwordFormData, setPasswordFormData] = useState<{ visible: boolean; data: any }>({
    visible: false,
    data: {},
  });
  const [emailFormData, setEmailFormData] = useState<{ visible: boolean; data: any }>({
    visible: false,
    data: {},
  });

  const refreshUserInfo = () => {
    UserService.getUserInfo().then((resp) => {
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
                setPasswordFormData({ visible: true, data: {} });
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
                setEmailFormData({ visible: true, data: {} });
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
      {passwordFormData.visible && (
        <PasswordEditForm
          visible={passwordFormData.visible}
          onCancel={() => {
            setPasswordFormData({ visible: false, data: {} });
          }}
          onVisibleChange={(visible) => {
            setPasswordFormData({ visible: visible, data: {} });
            refreshUserInfo();
          }}
          data={{}}
        ></PasswordEditForm>
      )}

      {emailFormData.visible && (
        <EmailBindForm
          visible={emailFormData.visible}
          onCancel={() => {
            setEmailFormData({ visible: false, data: {} });
          }}
          onVisibleChange={(visible) => {
            setEmailFormData({ visible: visible, data: {} });
            refreshUserInfo();
          }}
          data={{}}
        ></EmailBindForm>
      )}
    </div>
  );
};

export default Security;
