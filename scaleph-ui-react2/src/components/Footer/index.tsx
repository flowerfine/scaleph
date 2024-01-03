import {GithubOutlined} from "@ant-design/icons";
import React from "react";
import {DefaultFooter} from "@ant-design/pro-layout";

const Footer: React.FC = () => {
  return (
    <DefaultFooter
      style={{
        background: 'none',
        height: '30px', //添加布局底部高度
      }}
      links={[
        {
          key: 'Apache Seatunnel',
          title: 'Apache Seatunnel',
          href: 'https://seatunnel.apache.org/',
          blankTarget: true,
        },
        {
          key: 'github',
          title: <GithubOutlined/>,
          href: 'https://github.com/flowerfine/scaleph',
          blankTarget: true,
        },
        {
          key: 'Ant Design',
          title: 'Ant Design',
          href: 'https://ant.design',
          blankTarget: true,
        },
      ]}
      copyright={false}
    />
  );
};

export default Footer;
