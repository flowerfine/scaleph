import React from "react";
import {ProCard, ProFormGroup, ProFormList, ProFormText} from "@ant-design/pro-components";
import {useIntl} from "@umijs/max";

const AdvancedAdditional: React.FC = () => {
  const intl = useIntl();

  return (<ProCard
    title={"Additional Config Options"}
    headerBordered
    collapsible={true}
    defaultCollapsed>
    <ProFormList
      name="options"
      copyIconProps={false}
      colProps={{span: 21, offset: 1}}
      creatorButtonProps={{
        creatorButtonText: intl.formatMessage({id: 'pages.project.flink.kubernetes.template.step.advanced.configOptions'}),
        type: "text",
      }}
    >
      <ProFormGroup>
        <ProFormText
          name="key"
          label={intl.formatMessage({id: 'pages.project.flink.kubernetes.template.step.advanced.configOptions.key'})}
          colProps={{span: 10, offset: 1}}
        />
        <ProFormText
          name="value"
          label={intl.formatMessage({id: 'pages.project.flink.kubernetes.template.step.advanced.configOptions.value'})}
          colProps={{span: 10, offset: 1}}
        />
      </ProFormGroup>
    </ProFormList>
  </ProCard>);
}

export default AdvancedAdditional;
