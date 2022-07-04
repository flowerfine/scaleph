import { Settings as LayoutSettings } from '@ant-design/pro-components';

const Settings: LayoutSettings & {
  pwa?: boolean;
  logo?: string;
} = {
  navTheme: 'light',
  // 拂晓蓝
  primaryColor: '#1890ff',
  layout: 'mix',
  contentWidth: 'Fluid',
  fixedHeader: false,
  fixSiderbar: true,
  colorWeak: false,
  title: 'Scaleph',
  pwa: false,
  logo: 'https://res.hc-cdn.com/x-roma-components/1.0.10/assets/devui/logo.svg',
  iconfontUrl: '',
};

export default Settings;
