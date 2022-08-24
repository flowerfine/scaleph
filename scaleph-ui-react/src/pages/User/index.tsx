import { GridContent } from '@ant-design/pro-components';
import { Col, Menu, Row } from 'antd';
import { useEffect, useRef, useState } from 'react';
import { useIntl, useLocation } from 'umi';
import Log from './components/Log';
import Message from './components/Message';
import Profile from './components/Profile';
import Security from './components/Security';
import styles from './index.less';

const UserCenter: React.FC<{ defaultMenu?: string }> = () => {
  const intl = useIntl();
  const urlParams = useLocation();
  const dom = useRef<HTMLDivElement>();
  const profile: string = 'profile';
  const security: string = 'security';
  const message: string = 'message';
  const log: string = 'log';
  const menuMap: Record<string, React.ReactNode> = {
    profile: intl.formatMessage({ id: 'pages.admin.usercenter.profile' }),
    security: intl.formatMessage({ id: 'pages.admin.usercenter.security' }),
    message: intl.formatMessage({ id: 'pages.admin.usercenter.message' }),
    log: intl.formatMessage({ id: 'pages.admin.usercenter.log' }),
  };
  const [selectedKey, setSelectKey] = useState<string>(profile);

  useEffect(() => {
    const params = urlParams.state;
    if (params) {
      setSelectKey(params?.defaultMenu);
    }
  }, []);

  const renderChildren = () => {
    switch (selectedKey) {
      case profile:
        return <Profile></Profile>;
      case security:
        return <Security></Security>;
      case message:
        return <Message></Message>;
      case log:
        return <Log></Log>;
      default:
        return null;
    }
  };

  return (
    <Row>
      <Col span={3}></Col>
      <Col span={18}>
        <GridContent>
          <div
            className={styles.main}
            ref={(ref) => {
              if (ref) {
                dom.current = ref;
              }
            }}
          >
            <div className={styles.leftMenu}>
              <Menu
                mode="vertical"
                selectedKeys={[selectedKey]}
                onClick={({ key }) => {
                  setSelectKey(key);
                }}
              >
                {Object.keys(menuMap).map((item) => (
                  <Menu.Item key={item}>{menuMap[item]}</Menu.Item>
                ))}
              </Menu>
            </div>
            <div className={styles.right}>
              <div className={styles.title}>
                {menuMap[selectedKey]}
                {renderChildren()}
              </div>
            </div>
          </div>
        </GridContent>
      </Col>
      <Col span={3}></Col>
    </Row>
  );
};

export default UserCenter;
