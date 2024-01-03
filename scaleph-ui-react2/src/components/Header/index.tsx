import React from "react";
import { HeaderViewProps } from "@ant-design/pro-layout/es/components/Header";
import { Divider } from "antd";
import HeaderMegaMenu from "./HeaderMegaMenu";
import classNames from "classnames";
import AppsLogo from "../AppLogoComponent";
import GlobalHeaderRight from "./RightContent";
import "./index.less";

const Header: React.FC<{
  headerProps: HeaderViewProps;
}> = (props) => {
  const { headerProps } = props;

  return (
    <>
      <div className={classNames("scaleph-header")}>
        <div className={classNames("scaleph-header-main")}>
          <div
            className={classNames("scaleph-header-main-left")}
            style={{ width: (headerProps.siderWidth as number) - 8 }}
          >
            <HeaderMegaMenu headerProps={headerProps} />
            <Divider type="vertical" style={{ height: "80%" }}></Divider>
            <div className={classNames("scaleph-header-main-left-title")}>
              <a className={classNames("scaleph-header-main-left-title-logo")} href="/">
                <AppsLogo
                  width={24}
                  height={24}
                  color={headerProps.colorPrimary as string}
                />
                <h1 className={classNames("scaleph-header-main-left-title-text")}>
                  {headerProps.title}
                </h1>
              </a>
            </div>
          </div>
          <div className={classNames("scaleph-header-main-center")}></div>
          <div className={classNames("scaleph-header-main-right")}>
            <GlobalHeaderRight />
          </div>
        </div>
      </div>
    </>
  );
};
export default Header;
