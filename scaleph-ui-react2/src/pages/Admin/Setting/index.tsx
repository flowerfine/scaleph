import { GridContent } from "@ant-design/pro-components";
import { Menu } from "antd";
import { useRef, useState } from "react";
import { useIntl } from 'umi';
import BasicSetting from "./components/BasicSetting";
import EmailSetting from "./components/EmailSetting";
import styles from './index.less';

const Setting: React.FC = () => {
    const intl = useIntl();
    const dom = useRef<HTMLDivElement>();
    const basic: string = 'basic';
    const email: string = 'email';
    const menuMap: Record<string, React.ReactNode> = {
        basic: intl.formatMessage({ id: 'pages.admin.setting.basic' }),
        email: intl.formatMessage({ id: 'pages.admin.setting.email' })
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
                        onClick={({ key }) => {
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
    );
}

export default Setting;