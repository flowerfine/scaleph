import React from "react";
import {Form} from "antd";
import {ProCard, ProFormDigit, ProFormText} from "@ant-design/pro-components";
import {useIntl} from "umi";

const DorisTemplateBase: React.FC = () => {
  const intl = useIntl();
  const [form] = Form.useForm()

  return (
    <ProCard>
      <ProFormDigit name={"id"} hidden/>
      <ProFormText
        name={"name"}
        label={intl.formatMessage({id: 'pages.project.doris.template.name'})}
        rules={[{required: true}]}
      />
      <ProFormText
        name={"namespace"}
        label={intl.formatMessage({id: 'pages.project.doris.template.namespace'})}
        rules={[{required: true}]}
      />
      <ProFormText
        name={"remark"}
        label={intl.formatMessage({id: 'app.common.data.remark'})}
      />
    </ProCard>
  );
}

export default DorisTemplateBase;
