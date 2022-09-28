import Footer from '@/components/Footer';
import RightContent from '@/components/RightContent';
import { LinkOutlined } from '@ant-design/icons';
import { SettingDrawer, Settings as LayoutSettings } from '@ant-design/pro-components';
import type { RunTimeLayoutConfig } from '@umijs/max';
import { history, Link } from '@umijs/max';
import { message, notification } from 'antd';
import { RequestConfig } from 'umi';
import defaultSettings from '../config/defaultSettings';
import { OnlineUserInfo, ResponseBody } from './app.d';
import { USER_AUTH } from './constant';
import { UserService } from './services/admin/user.service';
import { AuthService } from './services/auth';

const isDev = process.env.NODE_ENV === 'development';
const whiteList: string[] = ['login', 'register'];
/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState(): Promise<{
  settings?: Partial<LayoutSettings>;
  currentUser?: OnlineUserInfo;
}> {
  const token = localStorage.getItem(USER_AUTH.token);
  if (token != null && token != undefined && token != '') {
    const info = await UserService.getOnlineUserInfo(token);
    if (info.success) {
      await AuthService.setSession(info.data || {});
    }
  }
  const user: OnlineUserInfo = JSON.parse(localStorage.getItem(USER_AUTH.userInfo) || '');
  return {
    settings: defaultSettings,
    currentUser: user,
  };
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout
export const layout: RunTimeLayoutConfig = ({ initialState, setInitialState }) => {
  return {
    rightContentRender: () => <RightContent />,
    disableContentMargin: false,
    // waterMarkProps: {
    //   content: initialState?.currentUser?.userName,
    // },
    contentStyle: { margin: 12 },
    footerRender: () => <Footer />,
    onPageChange: () => {
      const { location } = history;
      const token = localStorage.getItem(USER_AUTH.token);
      if (!token && !whiteList.includes(location.pathname)) {
        localStorage.clear();
        history.push('/login');
      } else if (whiteList.includes(location.pathname)) {
        localStorage.clear();
      } else if (token) {
        //check if token expired
        let expireTime = Number(localStorage.getItem(USER_AUTH.expireTime));
        let ctime = new Date().getTime();
        if (expireTime == null || expireTime == undefined || ctime > expireTime) {
          history.push('/login');
        }
      }
    },
    layoutBgImgList: [
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/D2LWSqNny4sAAAAAAAAAAAAAFl94AQBr',
        left: 85,
        bottom: 100,
        height: '303px',
      },
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/C2TWRpJpiC0AAAAAAAAAAAAAFl94AQBr',
        bottom: -68,
        right: -45,
        height: '303px',
      },
      {
        src: 'https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/F6vSTbj8KpYAAAAAAAAAAAAAFl94AQBr',
        bottom: 0,
        left: 0,
        width: '331px',
      },
    ],
    links: isDev
      ? [
          <Link key="api" to="/scaleph/doc.html" target="_blank">
            <LinkOutlined />
            <span>API 文档</span>
          </Link>,
        ]
      : [],
    menuHeaderRender: undefined,
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    // 增加一个 loading 的状态
    childrenRender: (children, props) => {
      // if (initialState?.loading) return <PageLoading />;
      return (
        <>
          {children}
          {/* {!props.location?.pathname?.includes('/login') && (
            <SettingDrawer
              disableUrlParams
              enableDarkTheme
              settings={initialState?.settings}
              onSettingChange={(settings) => {
                setInitialState((preInitialState) => ({
                  ...preInitialState,
                  settings,
                }));
              }}
            />
          )} */}
        </>
      );
    },
    ...initialState?.settings,
  };
};

const requestHeaderInterceptor: any = (url: string, options: RequestConfig) => {
  const headers = {
    ...options.headers,
    u_token: localStorage.getItem(USER_AUTH.token) || '',
  };
  return {
    url: `${url}`,
    options: { ...options, interceptors: true, headers: headers },
  };
};

const responseErrorInterceptor: any = (response: any, options: RequestConfig) => {
  // debugger
  // check response status
  if (response.status != 200) {
    switch (response.status) {
      case 401:
        history.push('/login');
        break;
      case 403:
        history.push('/403');
        break;
      default:
        break;
    }
  }
  // check response body info
  let respBody: ResponseBody<any> = response?.data;
  if (respBody && respBody.success != null && respBody.success != undefined && !respBody.success) {
    if (respBody.errorCode == '401') {
      history.push('/login');
    } else if (respBody.errorCode == '403') {
      history.push('/403');
    } else {
      handleError(respBody.errorCode, respBody.errorMessage, respBody.showType);
    }
  }
  return response;
};

const handleError = (
  errorCode: string | undefined,
  errorMessage: string,
  showType: string | undefined,
) => {
  if (showType == '1') {
    message.warning(errorMessage, 2);
  } else if (showType == '2') {
    message.error(errorMessage, 2);
  } else if (showType == '4') {
    notification.error({ message: 'Error:' + errorCode, description: errorMessage, duration: 3 });
  }
};

export const request: RequestConfig = {
  timeout: 1800000,
  errorConfig: {},
  requestInterceptors: [requestHeaderInterceptor],
  responseInterceptors: [responseErrorInterceptor],
};
