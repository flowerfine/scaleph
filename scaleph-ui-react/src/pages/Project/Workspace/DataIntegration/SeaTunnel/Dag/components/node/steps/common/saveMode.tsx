import React from 'react';
import {InfoCircleOutlined} from "@ant-design/icons";
import {ProFormGroup, ProFormSelect} from "@ant-design/pro-components";
import {getIntl, getLocale} from "@umijs/max";

const SaveModeItem: React.FC = () => {
  const intl = getIntl(getLocale());

  return (
    <ProFormGroup>
      <ProFormSelect
        name={"schema_save_mode"}
        label={intl.formatMessage({id: 'pages.project.di.step.saveMode.schemaSaveMode'})}
        tooltip={{
          title: intl.formatMessage({id: 'pages.project.di.step.saveMode.schemaSaveMode.tooltip'}),
          icon: <InfoCircleOutlined/>,
        }}
        colProps={{span: 12}}
        initialValue={"CREATE_SCHEMA_WHEN_NOT_EXIST"}
        options={["CREATE_SCHEMA_WHEN_NOT_EXIST", "ERROR_WHEN_SCHEMA_NOT_EXIST", "RECREATE_SCHEMA"]}
      />

      <ProFormSelect
        name={"data_save_mode"}
        label={intl.formatMessage({id: 'pages.project.di.step.saveMode.dataSaveMode'})}
        tooltip={{
          title: intl.formatMessage({id: 'pages.project.di.step.saveMode.dataSaveMode.tooltip'}),
          icon: <InfoCircleOutlined/>,
        }}
        colProps={{span: 12}}
        initialValue={"APPEND_DATA"}
        options={["DROP_DATA", "APPEND_DATA", "CUSTOM_PROCESSING", "ERROR_WHEN_DATA_EXISTS"]}
      />
    </ProFormGroup>
  );
}

export default SaveModeItem;
