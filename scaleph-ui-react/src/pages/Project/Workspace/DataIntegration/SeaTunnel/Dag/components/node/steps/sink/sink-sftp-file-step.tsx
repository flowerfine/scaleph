import React, {useEffect} from 'react';
import {Form} from 'antd';
import {
  DrawerForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormSelect,
  ProFormSwitch,
  ProFormText
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {BaseFileParams, STEP_ATTR_TYPE} from '../constant';
import DataSourceItem from "../dataSource";

const SinkSftpFileStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
  const intl = getIntl(getLocale());
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, data.data.label);
  }, []);

  return (
    <XFlow>
      <DrawerForm
        title={data.data.label}
        form={form}
        initialValues={data.data.attrs}
        open={visible}
        onOpenChange={onVisibleChange}
        grid={true}
        width={780}
        drawerProps={{
          styles: {body: {overflowY: 'scroll'}},
          closeIcon: null,
          destroyOnClose: true
        }}
        onFinish={(values) => {
          if (onOK) {
            onOK(values)
            return Promise.resolve(true)
          }
          return Promise.resolve(false)
        }}
      >
        <ProFormText
          name={STEP_ATTR_TYPE.stepTitle}
          label={intl.formatMessage({id: 'pages.project.di.step.stepTitle'})}
          rules={[{required: true}, {max: 120}]}
          colProps={{span: 24}}
        />
        <DataSourceItem dataSource={"Sftp"}/>
        <ProFormText
          name={BaseFileParams.path}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.path'})}
          rules={[{required: true}]}
          colProps={{span: 24}}
        />
        <ProFormSelect
          name={BaseFileParams.fileFormatType}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.fileFormat'})}
          colProps={{span: 24}}
          options={['json', 'parquet', 'orc', 'text', 'csv', 'excel']}
        />
        <ProFormDependency name={[BaseFileParams.fileFormatType]}>
          {({file_format_type}) => {
            if (file_format_type == 'text' || file_format_type == 'csv') {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={BaseFileParams.fieldDelimiter}
                    label={intl.formatMessage({id: 'pages.project.di.step.baseFile.fieldDelimiter'})}
                    rules={[{required: true}]}
                    colProps={{span: 12}}
                  />
                  <ProFormText
                    name={BaseFileParams.rowDelimiter}
                    label={intl.formatMessage({id: 'pages.project.di.step.baseFile.rowDelimiter'})}
                    rules={[{required: true}]}
                    colProps={{span: 12}}
                  />
                </ProFormGroup>
              );
            }
            if (file_format_type == 'excel') {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={BaseFileParams.sheetName}
                    label={intl.formatMessage({id: 'pages.project.di.step.baseFile.sheetName'})}
                    colProps={{span: 12}}
                  />
                  <ProFormText
                    name={BaseFileParams.maxRowsInMemory}
                    label={intl.formatMessage({id: 'pages.project.di.step.baseFile.maxRowsInMemory'})}
                    rules={[{required: true}]}
                    colProps={{span: 12}}
                  />
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormSwitch
          name={BaseFileParams.customFilename}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.customFilename'})}
          initialValue={false}
        />
        <ProFormText
          name={BaseFileParams.fileNameExpression}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.fileNameExpression'})}
          colProps={{span: 12}}
        />
        <ProFormText
          name={BaseFileParams.filenameTimeFormat}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.filenameTimeFormat'})}
          colProps={{span: 12}}
        />
        <ProFormSwitch
          name={BaseFileParams.havePartition}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.havePartition'})}
          initialValue={false}
        />
        <ProFormText
          name={BaseFileParams.partitionBy}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.partitionBy'})}
          colProps={{span: 12}}
        />
        <ProFormText
          name={BaseFileParams.partitionDirExpression}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.partitionDirExpression'})}
          colProps={{span: 12}}
        />
        <ProFormSwitch
          name={BaseFileParams.isPartitionFieldWriteInFile}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.isPartitionFieldWriteInFile'})}
          colProps={{span: 24}}
        />
        <ProFormText
          name={BaseFileParams.sinkColumns}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.sinkColumns'})}
          colProps={{span: 24}}
        />
        <ProFormSwitch
          name={BaseFileParams.isEnableTransaction}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.isEnableTransaction'})}
          colProps={{span: 24}}
          initialValue={true}
          fieldProps={{
            disabled: true,
          }}
        />
        <ProFormDigit
          name={BaseFileParams.batchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.baseFile.batchSize'})}
          colProps={{span: 24}}
          initialValue={1000000}
          fieldProps={{
            step: 10000,
            min: 0,
          }}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SinkSftpFileStepForm;
