import React from "react";
import { Button, Result } from "antd";
import { history, useIntl } from "@umijs/max";

const ServerError: React.FC = () => {
  const intl = useIntl();

  return (
    <Result
      status="500"
      title="500"
      subTitle={intl.formatMessage({ id: "app.common.message.500" })}
      extra={
        <Button type="primary" onClick={() => history.push("/")}>
          {intl.formatMessage({ id: "app.common.operate.back.home" })}
        </Button>
      }
    />
  );
};

export default ServerError;
