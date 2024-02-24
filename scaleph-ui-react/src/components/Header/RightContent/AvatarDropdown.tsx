import {
  LogoutOutlined,
  UserOutlined,
} from "@ant-design/icons";
import { useEmotionCss } from "@ant-design/use-emotion-css";
import { history, useModel } from "@umijs/max";
import { Avatar, Spin } from "antd";
import { setAlpha } from "@ant-design/pro-components";
import type { MenuInfo } from "rc-menu/lib/interface";
import React, { useCallback } from "react";
import { flushSync } from "react-dom";
import HeaderDropdown from "../HeaderDropdown";
import { AuthService } from "@/services/auth";

export type GlobalHeaderRightProps = {
  menu?: boolean;
};

const AvatarLogo = () => {
  const { initialState } = useModel("@@initialState");
  const { currentUser } = initialState || {};

  const avatarClassName = useEmotionCss(({ token }) => {
    return {
      color: token.colorPrimary,
      verticalAlign: "top",
      background: setAlpha(token.colorInfoBg, 0.85),
      [`@media only screen and (max-width: ${token.screenMD}px)`]: {
        margin: 0,
      },
    };
  });

  return (
    <Avatar size="small" className={avatarClassName} alt="avatar">
      {currentUser?.userName?.charAt(0)}
    </Avatar>
  );
};

const AvatarDropdown: React.FC<GlobalHeaderRightProps> = ({ menu }) => {
  const { initialState, setInitialState } = useModel("@@initialState");

  const loginOut = async () => {
    await AuthService.logout().then((resp) => {
      if (resp.success) {
        history.push("/user/login");
      }
    });
  };

  const actionClassName = useEmotionCss(({ token }) => {
    return {
      display: "flex",
      height: "48px",
      marginLeft: "auto",
      overflow: "hidden",
      alignItems: "center",
      padding: "0 8px",
      cursor: "pointer",
      borderRadius: token.borderRadius,
      "&:hover": {
        backgroundColor: token.colorBgTextHover,
      },
    };
  });

  const onMenuClick = useCallback((event: MenuInfo) => {
      const { key } = event;
      if (key === "logout") {
        flushSync(() => {
          setInitialState((s) => ({ ...s, currentUser: undefined }));
        });
        loginOut();
        return;
      }
      history.push(`/user/center`);
    },
    [setInitialState]
  );

  const loading = (
    <span className={actionClassName}>
      <Spin
        size="small"
        style={{
          marginLeft: 8,
          marginRight: 8,
        }}
      />
    </span>
  );

  if (!initialState) {
    return loading;
  }

  const { currentUser } = initialState;

  if (!currentUser || !currentUser.userName) {
    return loading;
  }

  const menuItems = [
    ...(menu
      ? [
          {
            key: "center",
            icon: <UserOutlined />,
            label: "个人中心",
          },
          {
            type: "divider" as const,
          },
        ]
      : []),
    {
      key: "logout",
      icon: <LogoutOutlined />,
      label: "退出登录",
    },
  ];

  return (
    <HeaderDropdown
      menu={{
        selectedKeys: [],
        onClick: onMenuClick,
        items: menuItems,
      }}
    >
      <span className={actionClassName}>
        <AvatarLogo />
      </span>
    </HeaderDropdown>
  );
};

export default AvatarDropdown;
