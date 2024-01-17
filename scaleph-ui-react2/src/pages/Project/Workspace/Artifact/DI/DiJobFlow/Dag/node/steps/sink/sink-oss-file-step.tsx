import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {
  ProForm,
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
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";

const SinkOSSFileStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
  const intl = getIntl(getLocale());
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, data.data.label);
  }, []);

  return (
    <XFlow>
      <Drawer
        open={visible}
        title={data.data.label}
        width={780}
        bodyStyle={{overflowY: 'scroll'}}
        destroyOnClose={true}
        onClose={onCancel}
        extra={
          <Button
            type="primary"
            onClick={() => {
              form.validateFields().then((values) => {
                StepSchemaService.formatSchema(values);
                if (onOK) {
                  onOK(values);
                }
              });
            }}
          >
            {intl.formatMessage({id: 'app.common.operate.confirm.label'})}
          </Button>
        }
      >
        <ProForm form={form} initialValues={data.data.attrs} grid={true} submitter={false}>
          <ProFormText
            name={STEP_ATTR_TYPE.stepTitle}
            label={intl.formatMessage({id: 'pages.project.di.step.stepTitle'})}
            rules={[{required: true}, {max: 120}]}
            colProps={{span: 24}}
          />
          <DataSourceItem dataSource={'OSS'}/>
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
          />
          <ProFormSwitch
            name={BaseFileParams.isPartitionFieldWriteInFile}
            label={intl.formatMessage({id: 'pages.project.di.step.baseFile.isPartitionFieldWriteInFile'})}
          />
          <ProFormText
            name={BaseFileParams.sinkColumns}
            label={intl.formatMessage({id: 'pages.project.di.step.baseFile.sinkColumns'})}
          />
          <ProFormSwitch
            name={BaseFileParams.isEnableTransaction}
            label={intl.formatMessage({id: 'pages.project.di.step.baseFile.isEnableTransaction'})}
            initialValue={true}
            disabled
          />
          <ProFormDigit
            name={BaseFileParams.batchSize}
            label={intl.formatMessage({id: 'pages.project.di.step.baseFile.batchSize'})}
            initialValue={1000000}
            fieldProps={{
              step: 10000,
              min: 0,
            }}
          />
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SinkOSSFileStepForm;
