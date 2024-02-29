import { Settings as LayoutSettings } from '@ant-design/pro-components';

const {PUBLIC_PATH} = process.env;
const baseURL = PUBLIC_PATH ?
  (PUBLIC_PATH.endsWith("/") ? PUBLIC_PATH.substring(0, PUBLIC_PATH.length - 1) : PUBLIC_PATH)
  : ''

const Settings: LayoutSettings & {
  pwa?: boolean;
  logo?: string;
} = {
  navTheme: 'light',
  primaryColor: '#1890ff',
  layout: 'mix',
  contentWidth: 'Fluid',
  fixedHeader: true,
  fixSiderbar: true,
  colorWeak: false,
  splitMenus: true,
  title: 'Scaleph',
  pwa: false,
  logo: `${baseURL}/scaleph.svg`,
  iconfontUrl: '',
};

export default Settings;
