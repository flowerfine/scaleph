import React from 'react';
import {InfoCircleOutlined} from "@ant-design/icons";
import {ProFormGroup, ProFormList, ProFormText} from "@ant-design/pro-components";
import {getIntl, getLocale} from "@umijs/max";
import {ColumnParams} from "../../constant";

const ColumnItem: React.FC = () => {
  const intl = getIntl(getLocale());

  return (
    <ProFormGroup
      title={intl.formatMessage({id: 'pages.project.di.step.column'})}
      tooltip={{
        title: intl.formatMessage({id: 'pages.project.di.step.column.tooltip'}),
        icon: <InfoCircleOutlined/>,
      }}
      collapsible={true}
      defaultCollapsed={true}
    >
      <ProFormList
        name={ColumnParams.readColumnArray}
        copyIconProps={false}
        creatorButtonProps={{
          creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.column.field'}),
          type: 'text',
        }}
      >
        <ProFormGroup>
          <ProFormText
            name={ColumnParams.readColumn}
            colProps={{span: 20, offset: 2}}
          />
        </ProFormGroup>
      </ProFormList>
    </ProFormGroup>
  );
}

export default ColumnItem;
