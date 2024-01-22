import React, {useEffect} from 'react';
import {Form} from 'antd';
import {DrawerForm, ProFormSelect, ProFormSwitch, ProFormText, ProFormTextArea,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {BaseFileParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import SchemaItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/schema";
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";

const SourceOSSJindoFileStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
          destroyOnClose: true
        }}
        onFinish={(values) => {
          if (onOK) {
            StepSchemaService.formatSchema(values);
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
        />
        <DataSourceItem dataSource={'OSSJindo'}/>
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
          label={intl.formatMessage({
            id: 'pages.project.di.step.baseFile.parsePartitionFromPath',
          })}
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
      </DrawerForm>
    </XFlow>
  );
};

export default SourceOSSJindoFileStepForm;
