import { OnlineUserInfo } from '@/app.d';
import { USER_AUTH } from '@/constant';
import { AuthService } from '@/services/auth';
import { LogoutOutlined, UserOutlined } from '@ant-design/icons';
import { history } from '@umijs/max';
import { Avatar, Menu, Spin } from 'antd';
import type { MenuInfo } from 'rc-menu/lib/interface';
import React, { useCallback } from 'react';
import { useIntl } from 'umi';
import HeaderDropdown from '../HeaderDropdown';
import styles from './index.less';

export type GlobalHeaderRightProps = {
  menu?: boolean;
};

const AvatarDropdown: React.FC<GlobalHeaderRightProps> = ({ menu }) => {
  const intl = useIntl();
  const user: OnlineUserInfo = JSON.parse(localStorage.getItem(USER_AUTH.userInfo) || '');
  // const { initialState, setInitialState } = useModel('@@initialState');

  const onMenuClick = useCallback((event: MenuInfo) => {
    const { key } = event;
    if (key === 'logout') {
      AuthService.logout().then(() => {
        history.push('/login');
      });
      return;
    } else if (key === 'center') {
      history.push(`/user/${key}`, { defaultMenu: 'profile' });
    }
    history.push(`/user/${key}`);
  }, []);

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

  if (!user || !user.userName) {
    return loading;
  }

  const menuHeaderDropdown = (
    <Menu className={styles.menu} selectedKeys={[]} onClick={onMenuClick}>
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
    <HeaderDropdown overlay={menuHeaderDropdown} placement="bottom">
      <span className={`${styles.action} ${styles.account}`}>
        <Avatar size="small" className={styles.avatar} alt={user.userName} gap={2}>
          {user.userName.charAt(0)}
        </Avatar>
      </span>
    </HeaderDropdown>
  );
};

export default AvatarDropdown;
