import {PageContainer} from "@ant-design/pro-components";
import {Col, Menu, Row} from "antd";
import React, {useEffect, useState} from "react";
import {useIntl, useLocation} from "@umijs/max";
import UserLog from "./components/Log";
import UserMessage from "./components/Message";
import UserProfile from "./components/Profile";
import UserSecurity from "./components/Security";

const UserCenter: React.FC = () => {
  const intl = useIntl();
  const [selectedKey, setSelectKey] = useState<string>("profile");
  const target: { key: string } = useLocation().state as { key: string };

  useEffect(() => {
    if (target?.key) {
      setSelectKey(target.key);
    }
  }, []);

  const renderChildren = () => {
    switch (selectedKey) {
      case 'profile':
        return <UserProfile/>;
      case 'security':
        return <UserSecurity/>;
      case 'message':
        return <UserMessage/>;
      case 'log':
        return <UserLog/>;
      default:
        return <></>;
    }
  };

  return (
    <PageContainer header={{title: null, breadcrumb: {}}}>
      <Row>
        <Col span={4}/>
        <Col span={16}>
          <Row>
            <Col flex="220px">
              <Menu
                mode="vertical"
                selectedKeys={[selectedKey]}
                onClick={({key}) => {
                  setSelectKey(key);
                }}
                items={[
                  {
                    key: "profile",
                    label: intl.formatMessage({id: "pages.admin.usercenter.profile"}),
                  },
                  {
                    key: "security",
                    label: intl.formatMessage({id: "pages.admin.usercenter.security"}),
                  },
                  {
                    key: "message",
                    label: intl.formatMessage({id: "pages.admin.usercenter.message"}),
                  },
                  {
                    key: "log",
                    label: intl.formatMessage({id: "pages.admin.usercenter.log"}),
                  },
                ]}
              />
            </Col>
            <Col flex="auto" style={{paddingLeft: 18}}>
              {renderChildren()}
            </Col>
          </Row>
        </Col>
        <Col span={4}/>
      </Row>
    </PageContainer>
  );
};

export default UserCenter;
