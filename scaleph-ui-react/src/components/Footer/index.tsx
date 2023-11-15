import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
const Footer: React.FC = () => {
  // const currentYear = new Date().getFullYear();
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
          title: <GithubOutlined />,
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
      // copyright={`${currentYear} Scaleph`}
      copyright={false}
    />
  );
};

export default Footer;
