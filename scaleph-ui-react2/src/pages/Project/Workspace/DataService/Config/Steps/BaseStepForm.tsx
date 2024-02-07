import React from "react";
import {ProCard, ProFormDigit, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";

const DataserviceConfigBaseStep: React.FC = () => {
  const intl = useIntl();

  return (
    <ProCard>
      <ProFormDigit name={"id"} hidden/>
      <ProFormText
        name={"name"}
        label={intl.formatMessage({id: 'pages.project.dataservice.config.name'})}
        rules={[{required: true}]}
      />
      <ProFormText
        name={"path"}
        label={intl.formatMessage({id: 'pages.project.dataservice.config.path'})}
        rules={[{required: true}]}
      />
      <ProFormText
        name={"method"}
        label={intl.formatMessage({id: 'pages.project.dataservice.config.method'})}
        rules={[{required: true}]}
      />
      <ProFormText
        name={"contentType"}
        label={intl.formatMessage({id: 'pages.project.dataservice.config.contentType'})}
        rules={[{required: true}]}
      />
      <ProFormTextArea
        name={"remark"}
        label={intl.formatMessage({id: 'app.common.data.remark'})}
      />
    </ProCard>
  );
}

export default DataserviceConfigBaseStep;
