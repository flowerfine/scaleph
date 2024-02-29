import React from "react";
import { Button, Result } from "antd";
import { history, useIntl } from "@umijs/max";

const AccessDenied: React.FC = () => {
  const intl = useIntl();

  return (
    <Result
      status="403"
      title="403"
      subTitle={intl.formatMessage({ id: "app.common.message.403" })}
      extra={
        <Button type="primary" onClick={() => history.push("/")}>
          {intl.formatMessage({ id: "app.common.operate.back.home" })}
        </Button>
      }
    />
  );
};

export default AccessDenied;
