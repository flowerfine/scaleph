import React from 'react';
import {InfoCircleOutlined} from "@ant-design/icons";
import {ProFormGroup, ProFormList, ProFormText} from "@ant-design/pro-components";
import {getIntl, getLocale} from "@umijs/max";
import {SchemaParams} from "../../constant";

const FieldItem: React.FC = () => {
  const intl = getIntl(getLocale());

  return (
    <ProFormGroup
      title={intl.formatMessage({id: 'pages.project.di.step.schema'})}
      tooltip={{
        title: intl.formatMessage({id: 'pages.project.di.step.schema.tooltip'}),
        icon: <InfoCircleOutlined/>,
      }}
      collapsible={true}
      defaultCollapsed={true}
    >
      <ProFormList
        name={SchemaParams.fieldArray}
        copyIconProps={false}
        creatorButtonProps={{
          creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.schema.fields'}),
          type: 'text',
        }}
      >
        <ProFormGroup>
          <ProFormText
            name={SchemaParams.field}
            label={intl.formatMessage({id: 'pages.project.di.step.schema.fields.field'})}
            colProps={{span: 10, offset: 1}}
          />
          <ProFormText
            name={SchemaParams.type}
            label={intl.formatMessage({id: 'pages.project.di.step.schema.fields.type'})}
            colProps={{span: 10, offset: 1}}
          />
        </ProFormGroup>
      </ProFormList>
    </ProFormGroup>
  );
}

export default FieldItem;
