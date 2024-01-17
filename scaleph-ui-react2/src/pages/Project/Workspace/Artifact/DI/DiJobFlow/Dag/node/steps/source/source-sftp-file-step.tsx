import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {ProForm, ProFormSelect, ProFormSwitch, ProFormText, ProFormTextArea,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {BaseFileParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import SchemaItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/schema";
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";

const SourceSftpFileStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
          />
          <DataSourceItem dataSource={"Sftp"}/>
          <ProFormText
            name={BaseFileParams.path}
            label={intl.formatMessage({id: 'pages.project.di.step.baseFile.path'})}
            rules={[{required: true}]}
          />
          <ProFormTextArea
            name={BaseFileParams.readColumns}
            label={intl.formatMessage({id: 'pages.project.di.step.baseFile.readColumns'})}
          />
          <ProFormText
            name={BaseFileParams.fileFilterPattern}
            label={intl.formatMessage({id: 'pages.project.di.step.baseFile.fileFilterPattern'})}
          />
          <SchemaItem/>
          <ProFormSwitch
            name={BaseFileParams.parsePartitionFromPath}
            label={intl.formatMessage({id: 'pages.project.di.step.baseFile.parsePartitionFromPath'})}
            initialValue={true}
          />
          <ProFormSelect
            name={BaseFileParams.dateFormat}
            label={intl.formatMessage({id: 'pages.project.di.step.baseFile.dateFormat'})}
            initialValue={'yyyy-MM-dd'}
            options={['yyyy-MM-dd', 'yyyy.MM.dd', 'yyyy/MM/dd']}
          />
          <ProFormSelect
            name={BaseFileParams.timeFormat}
            label={intl.formatMessage({id: 'pages.project.di.step.baseFile.timeFormat'})}
            initialValue={'HH:mm:ss'}
            options={['HH:mm:ss', 'HH:mm:ss.SSS']}
          />
          <ProFormSelect
            name={BaseFileParams.datetimeFormat}
            label={intl.formatMessage({id: 'pages.project.di.step.baseFile.datetimeFormat'})}
            initialValue={'yyyy-MM-dd HH:mm:ss'}
            options={[
              'yyyy-MM-dd HH:mm:ss',
              'yyyy.MM.dd HH:mm:ss',
              'yyyy/MM/dd HH:mm:ss',
              'yyyyMMddHHmmss',
            ]}
          />
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SourceSftpFileStepForm;
