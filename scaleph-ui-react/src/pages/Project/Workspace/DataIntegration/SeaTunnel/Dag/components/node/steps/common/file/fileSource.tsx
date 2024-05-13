import React from 'react';
import {ProFormGroup, ProFormSelect, ProFormSwitch, ProFormText, ProFormTextArea} from "@ant-design/pro-components";
import {getIntl, getLocale} from "@umijs/max";
import {BaseFileParams} from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/constant";
import SchemaItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/file/schema";

const FileSourceItem: React.FC = () => {
  const intl = getIntl(getLocale());

  return (
    <ProFormGroup>
      <ProFormText
        name={BaseFileParams.path}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.path'})}
        rules={[{required: true}]}
      />
      <ProFormText
        name={BaseFileParams.fileFilterPattern}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.fileFilterPattern'})}
      />
      <ProFormSwitch
        name={BaseFileParams.parsePartitionFromPath}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.parsePartitionFromPath'})}
        initialValue={true}
      />
      <ProFormText
        name={BaseFileParams.encoding}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.encoding'})}
        placeholder={intl.formatMessage({id: 'pages.project.di.step.baseFile.encoding.placeholder'})}
      />
      <ProFormSelect
        name={BaseFileParams.dateFormat}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.dateFormat'})}
        colProps={{span: 8}}
        initialValue={'yyyy-MM-dd'}
        options={['yyyy-MM-dd', 'yyyy.MM.dd', 'yyyy/MM/dd']}
      />
      <ProFormSelect
        name={BaseFileParams.timeFormat}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.timeFormat'})}
        colProps={{span: 8}}
        initialValue={'HH:mm:ss'}
        options={['HH:mm:ss', 'HH:mm:ss.SSS']}
      />
      <ProFormSelect
        name={BaseFileParams.datetimeFormat}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.datetimeFormat'})}
        colProps={{span: 8}}
        initialValue={'yyyy-MM-dd HH:mm:ss'}
        options={[
          'yyyy-MM-dd HH:mm:ss',
          'yyyy.MM.dd HH:mm:ss',
          'yyyy/MM/dd HH:mm:ss',
          'yyyyMMddHHmmss',
        ]}
      />
      <ProFormTextArea
        name={BaseFileParams.readColumns}
        label={intl.formatMessage({id: 'pages.project.di.step.baseFile.readColumns'})}
      />
      <SchemaItem/>
    </ProFormGroup>
  );
}

export default FileSourceItem;
