import { outLogin } from '@/services/ant-design-pro/api';
import { LogoutOutlined, UserOutlined } from '@ant-design/icons';
import { history, useModel } from '@umijs/max';
import { useIntl } from 'umi';
import { Avatar, Menu, Spin } from 'antd';
import { stringify } from 'querystring';
import type { MenuInfo } from 'rc-menu/lib/interface';
import React, { useCallback } from 'react';
import HeaderDropdown from '../HeaderDropdown';
import styles from './index.less';
import { logout } from '@/services/auth';

export type GlobalHeaderRightProps = {
  menu?: boolean;
};

const AvatarDropdown: React.FC<GlobalHeaderRightProps> = ({ menu }) => {
  const intl = useIntl();
  const { initialState, setInitialState } = useModel('@@initialState');

  const onMenuClick = useCallback(
    (event: MenuInfo) => {
      const { key } = event;
      if (key === 'logout') {
        logout().then(() => {
          history.push('/login');
        });
        return;
      }
      history.push(`/account/${key}`);
    }, []
  );

  const loading = (
    <span className={`${styles.action} ${styles.account}`}>
      <Spin
        size="small"
        style={{
          marginLeft: 8,
          marginRight: 8,
        }}
      />
    </span>
  );

  if (!initialState) {
    return loading;
  }

  const { currentUser } = initialState;

  if (!currentUser || !currentUser.userName) {
    return loading;
  }

  const menuHeaderDropdown = (
    <Menu className={styles.menu} selectedKeys={[]} onClick={onMenuClick}  >
      {menu && (
        <Menu.Item key={'center'}>
          <UserOutlined />
          {intl.formatMessage({ id: 'menu.user.center' })}
        </Menu.Item>
      )}
      {menu && <Menu.Divider />}
      <Menu.Item key={'logout'}>
        <LogoutOutlined />
        {intl.formatMessage({ id: 'menu.user.logout' })}
      </Menu.Item>
    </Menu>
  );

  return (
    <HeaderDropdown overlay={menuHeaderDropdown}>
      <span className={`${styles.action} ${styles.account}`}>
        <Avatar size="small" className={styles.avatar} alt={currentUser.userName} gap={2}>
          {currentUser.userName.charAt(0)}
        </Avatar>
      </span>
    </HeaderDropdown>
  );
};

export default AvatarDropdown;
