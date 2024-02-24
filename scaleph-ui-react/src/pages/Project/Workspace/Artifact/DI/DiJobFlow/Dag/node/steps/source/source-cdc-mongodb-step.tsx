import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {DrawerForm, ProFormDigit, ProFormText,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {MongoDBCDCParams, STEP_ATTR_TYPE} from '../constant';

const SourceCDCMongoDBStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
        />
        <ProFormText
          name={MongoDBCDCParams.hosts}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb-cdc.hosts'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.mongodb-cdc.hosts.placeholder'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={MongoDBCDCParams.username}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb-cdc.username'})}
          colProps={{span: 12}}
        />
        <ProFormText
          name={MongoDBCDCParams.password}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb-cdc.password'})}
          colProps={{span: 12}}
        />
        <ProFormText
          name={MongoDBCDCParams.database}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb-cdc.database'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.mongodb-cdc.database.placeholder'})}
          rules={[{required: true}]}
          colProps={{span: 12}}
        />
        <ProFormText
          name={MongoDBCDCParams.collection}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb-cdc.collection'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.mongodb-cdc.collection.placeholder'})}
          rules={[{required: true}]}
          colProps={{span: 12}}
        />
        <ProFormText
          name={MongoDBCDCParams.connectionOptions}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb-cdc.connectionOptions'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.mongodb-cdc.connectionOptions.placeholder'})}
        />
        <ProFormDigit
          name={MongoDBCDCParams.batchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb-cdc.batchSize'})}
          colProps={{span: 8}}
          initialValue={1024}
          fieldProps={{
            step: 1024,
            min: 1,
          }}
        />
        <ProFormDigit
          name={MongoDBCDCParams.pollMaxBatchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb-cdc.pollMaxBatchSize'})}
          colProps={{span: 8}}
          initialValue={1024}
          fieldProps={{
            step: 1024,
            min: 1,
          }}
        />
        <ProFormDigit
          name={MongoDBCDCParams.pollAwaitTimeMs}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb-cdc.pollAwaitTimeMs'})}
          colProps={{span: 8}}
          initialValue={1000}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormDigit
          name={MongoDBCDCParams.heartbeatIntervalMs}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb-cdc.heartbeatIntervalMs'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.mongodb-cdc.heartbeatIntervalMs.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          initialValue={0}
          fieldProps={{
            step: 1000,
            min: 0,
          }}
        />
        <ProFormDigit
          name={MongoDBCDCParams.incrementalSnapshotChunkSizeMb}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb-cdc.incrementalSnapshotChunkSizeMb'})}
          initialValue={64}
          fieldProps={{
            step: 64,
            min: 1,
          }}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SourceCDCMongoDBStepForm;
