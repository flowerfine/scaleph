import { MsgService } from "@/services/user/msg.service";
import { BellOutlined, QuestionCircleOutlined } from "@ant-design/icons";
import { useEmotionCss } from "@ant-design/use-emotion-css";
import { Badge } from "antd";
import React, { useEffect, useState } from "react";
import { SelectLang, history, useModel } from "@umijs/max";
import Avatar from "./AvatarDropdown";

export type SiderTheme = "light" | "dark";

const GlobalHeaderRight: React.FC = () => {
  const [msgCount, setMsgCount] = useState<number>();
  const className = useEmotionCss(() => {
    return {
      display: "flex",
      height: "48px",
      marginLeft: "auto",
      overflow: "hidden",
      cursor: "pointer",
      gap: 4,
    };
  });

  const actionClassName = useEmotionCss(({ token }) => {
    return {
      display: "flex",
      float: "right",
      height: "48px",
      marginLeft: "auto",
      overflow: "hidden",
      cursor: "pointer",
      padding: "0 12px",
      borderRadius: token.borderRadius,
      "&:hover": {
        backgroundColor: token.colorBgTextHover,
      },
    };
  });

  const bellClassName = useEmotionCss(({ token }) => {
    return {
      padding: "0 12px",
      borderRadius: token.borderRadius,
      "&:hover": {
        backgroundColor: token.colorBgTextHover,
      },
    };
  });

  const { initialState } = useModel("@@initialState");

  if (!initialState || !initialState.settings) {
    return null;
  }

  useEffect(() => {
    MsgService.countUnReadMsg().then((resp) => {
      if (resp.success) {
        setMsgCount(resp.data);
      }
    });
  }, []);

  return (
    <div className={className}>
      <span
        className={actionClassName}
        onClick={() => {
          window.open("https://flowerfine.github.io/scaleph-repress-site");
        }}
      >
        <QuestionCircleOutlined />
      </span>
      <span
        className={bellClassName}
        onClick={() => {
          history.push("/user/center", { key: "message" });
        }}
      >
        <div style={{ marginTop: -3 }}>
          <Badge count={msgCount} showZero={false} size="small" dot={true}>
            <BellOutlined />
          </Badge>
        </div>
      </span>
      <Avatar menu={true} />
      <SelectLang className={actionClassName} />
    </div>
  );
};
export default GlobalHeaderRight;
