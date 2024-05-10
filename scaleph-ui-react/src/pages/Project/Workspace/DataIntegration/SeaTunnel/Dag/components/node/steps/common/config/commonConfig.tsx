import React from 'react';
import {ProFormGroup, ProFormList, ProFormText} from "@ant-design/pro-components";
import {getIntl, getLocale} from "@umijs/max";
import {CommonConfigParams} from "../../constant";
import {Props} from "@/typings";

const CommonConfigItem: React.FC<Props<string>> = ({data}) => {
  const intl = getIntl(getLocale());

  return (
    <ProFormList
      name={data + CommonConfigParams.commonConfig}
      copyIconProps={false}
      creatorButtonProps={{
        creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.commonConfig'}),
        type: 'text',
      }}
    >
      <ProFormGroup>
        <ProFormText
          name={data + CommonConfigParams.commonConfigKey}
          label={intl.formatMessage({id: 'pages.project.di.step.commonConfig.key'})}
          colProps={{span: 10, offset: 1}}
        />
        <ProFormText
          name={data + CommonConfigParams.commonConfigValue}
          label={intl.formatMessage({id: 'pages.project.di.step.commonConfig.value'})}
          colProps={{span: 10, offset: 1}}
        />
      </ProFormGroup>
    </ProFormList>
  );
}

export default CommonConfigItem;
