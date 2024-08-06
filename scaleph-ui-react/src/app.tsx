import {useEffect, useState} from "react";
import {Space} from "antd";
import {LinkOutlined} from "@ant-design/icons";
import {Settings as LayoutSettings} from "@ant-design/pro-components";
import type {RunTimeLayoutConfig} from "@umijs/max";
import {history, Link} from "@umijs/max";
import defaultSettings from "../config/defaultSettings";
import RightContent from "@/components/Header/RightContent";
import Footer from "@/components/Footer";
import {errorConfig} from "./requestErrorConfig";
import {USER_AUTH} from "@/constants";
import Icon from './icon';
import {OnlineUserInfo, ResponseBody} from "@/typings";
import {UserService} from "@/services/admin/security/user.service";
import {AuthService} from "@/services/auth";
import {AuthenticationService} from "@/services/admin/security/authentication.service";

const isDev = process.env.NODE_ENV === "development";
const whiteList: string[] = ["/user/login", "/user/register"];

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState(): Promise<{
  settings?: Partial<LayoutSettings>;
  currentUser?: OnlineUserInfo;
  loading?: boolean;
  collapsed?: boolean;
  fetchUserInfo?: () => Promise<OnlineUserInfo | undefined>;
}> {
  const fetchUserInfo = async () => {
    let user: OnlineUserInfo = {};
    try {
      await AuthenticationService.getOnlineUserInfo().then((resp) => {
        if (resp.success && resp.data) {
          user = resp.data;
        } else {
          history.push("/user/login");
        }
      });
    } catch (error) {
      history.push("/user/login");
    }
    return user;
  };

  // 如果不是登录页面，执行
  const {location} = history;
  if (!whiteList.includes(location.pathname)) {
    const currentUser = await fetchUserInfo();
    return {
      fetchUserInfo,
      currentUser,
      collapsed: false,
      settings: defaultSettings,
    };
  }
  return {
    fetchUserInfo,
    collapsed: false,
    settings: defaultSettings,
  };
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout
export const layout: RunTimeLayoutConfig = ({initialState, setInitialState}) => {

  const [defaultMenus, setDefaultMenus] = useState<{}[]>([]);
  useEffect(() => {
    const fetchData = async () => {
      const dataList: ResponseBody<any> = await AuthService.menuRoutes();
      if (dataList) {
        setDefaultMenus(dataList);
      }
    };
    fetchData();
  }, []);

  return {
    siderWidth: 256,
    token: {
      bgLayout: "#fff",
      header: {
        colorBgHeader: "#fff",
      },
      sider: {
        colorMenuBackground: "#fff",
      },
      pageContainer: {
        colorBgPageContainer: "#fff",
      },
    },
    breakpoint: false,
    headerTitleRender: (logo, title, _) => {
      const defaultDom = (
        <div style={{width: 256}}>
          {logo}
          {title}
        </div>
      );
      return (<>{defaultDom}</>);
    },
    rightContentRender: () => <RightContent/>,
    // menuDataRender: () => defaultMenus,
    menuItemRender: (menuItemProps: any, defaultDom: any) => {
      return (
        <Space align="end" size={5}>
          {menuItemProps.icon && <Icon icon={menuItemProps.icon}/>}
          <a onClick={() => history.push(menuItemProps.path)}>{menuItemProps?.name}</a>
        </Space>
      );
    },
    subMenuItemRender: (menuItemProps: any, defaultDom: any) => {
      return (
        <Space align="end" size={5}>
          {menuItemProps.icon && <Icon icon={menuItemProps.icon}/>}
          <span>{menuItemProps?.name}</span>
        </Space>
      );
    },
    footerRender: () => <Footer/>,
    onPageChange: () => {
      const {location} = history;
      const token = localStorage.getItem(USER_AUTH.token);
      if (
        !initialState?.currentUser?.userName && !whiteList.includes(location.pathname)) {
        localStorage.clear();
        history.push("/user/login");
      }
    },
    layoutBgImgList: [
      {
        src: "https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/D2LWSqNny4sAAAAAAAAAAAAAFl94AQBr",
        left: 85,
        bottom: 100,
        height: "303px",
      },
      {
        src: "https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/C2TWRpJpiC0AAAAAAAAAAAAAFl94AQBr",
        bottom: -68,
        right: -45,
        height: "303px",
      },
      {
        src: "https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/F6vSTbj8KpYAAAAAAAAAAAAAFl94AQBr",
        bottom: 0,
        left: 0,
        width: "331px",
      },
    ],
    links: isDev
      ? [
        <Link key="openapi" to="/scaleph/doc.html" target="_blank">
          <LinkOutlined/>
          <span>OpenAPI 文档</span>
        </Link>,
      ]
      : [],
    menuHeaderRender: undefined,
    ErrorBoundary: false,
    childrenRender: (children) => {
      return <>{children}</>;
    },
    ...initialState?.settings,
  };
};

/**
 * @name request 配置，可以配置错误处理
 * 它基于 axios 和 ahooks 的 useRequest 提供了一套统一的网络请求和错误处理方案。
 * @doc https://umijs.org/docs/max/request#配置
 */
export const request = {
  ...errorConfig,
  timeout: 1800000
};
