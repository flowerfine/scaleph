import React from "react";
import {ProCard, ProFormDigit, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";

const DataIntegrationFlinkCDCStepBase: React.FC = () => {
  const intl = useIntl();

  return (
    <ProCard>
      <ProFormDigit name={"id"} hidden/>
      <ProFormText
        name={"name"}
        label={intl.formatMessage({id: 'pages.project.artifact.name'})}
        rules={[{required: true}]}
      />
      <ProFormDigit
        name={"parallelism"}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.base.parallelism'})}
        rules={[{required: true}]}
        initialValue={1}
        fieldProps={{
          min: 1
        }}
      />
      <ProFormText
        name={"localTimeZone"}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.base.localTimeZone'})}
      />
      <ProFormTextArea
        name={"remark"}
        label={intl.formatMessage({id: 'app.common.data.remark'})}
      />
    </ProCard>
  );
}

export default DataIntegrationFlinkCDCStepBase;
