import {useRef, useState} from "react";
import {Menu} from "antd";
import {GridContent, PageContainer} from "@ant-design/pro-components";
import {useIntl} from '@umijs/max';
import BasicSetting from "./components/BasicSetting";
import EmailSetting from "./components/EmailSetting";
import styles from './index.less';

const Setting: React.FC = () => {
  const intl = useIntl();
  const dom = useRef<HTMLDivElement>();
  const basic: string = 'basic';
  const email: string = 'email';
  const menuMap: Record<string, React.ReactNode> = {
    basic: intl.formatMessage({id: 'pages.admin.setting.basic'}),
    email: intl.formatMessage({id: 'pages.admin.setting.email'})
  }
  const [selectedKey, setSelectKey] = useState<string>(basic);

  const renderChildren = () => {
    switch (selectedKey) {
      case basic:
        return <BasicSetting></BasicSetting>;
      case email:
        return <EmailSetting></EmailSetting>;
      default:
        return null;
    }
  };

  return (
    <PageContainer title={false}>
      <GridContent>
        <div className={styles.main}
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
              onClick={({key}) => {
                setSelectKey(key);
              }}
            >
              {Object.keys(menuMap).map((item) =>
                <Menu.Item key={item}>{menuMap[item]}</Menu.Item>)}
            </Menu>
          </div>
          <div className={styles.right}>
            <div className={styles.title}>
              {menuMap[selectedKey]}
            </div>
            {renderChildren()}
          </div>
        </div>
      </GridContent>
    </PageContainer>
  );
}

export default Setting;
