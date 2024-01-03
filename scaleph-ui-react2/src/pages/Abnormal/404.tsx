import React from "react";
import { Button, Result } from "antd";
import { history, useIntl } from "@umijs/max";

const NoFoundPage: React.FC = () => {
  const intl = useIntl();

  return (
    <Result
      status="404"
      title="404"
      subTitle={intl.formatMessage({ id: "app.common.message.404" })}
      extra={
        <Button type="primary" onClick={() => history.push("/")}>
          {intl.formatMessage({ id: "app.common.operate.back.home" })}
        </Button>
      }
    />
  );
};

export default NoFoundPage;
