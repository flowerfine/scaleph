import React, {useEffect} from 'react';
import {Form} from 'antd';
import {DrawerForm, ProFormDigit, ProFormSwitch, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {MongoDBParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "../helper";
import DataSourceItem from "../dataSource";
import FieldItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/schema/fields";

const SinkMongoDBStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatSchema(values)
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
        <DataSourceItem dataSource={'MongoDB'}/>
        <ProFormText
          name={MongoDBParams.database}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.database'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={MongoDBParams.collection}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.collection'})}
          rules={[{required: true}]}
        />
        <ProFormDigit
          name={MongoDBParams.bufferFlushMaxRows}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.bufferFlushMaxRows'})}
          initialValue={1000}
        />
        <ProFormDigit
          name={MongoDBParams.bufferFlushInterval}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.bufferFlushInterval'})}
          initialValue={30000}
        />
        <ProFormDigit
          name={MongoDBParams.retryMax}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.retryMax'})}
          initialValue={3}
        />
        <ProFormDigit
          name={MongoDBParams.retryInterval}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.retryInterval'})}
          initialValue={1000}
        />
        <ProFormSwitch
          name={MongoDBParams.upsertEnable}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.upsertEnable'})}
          initialValue={false}
        />
        <ProFormText
          name={MongoDBParams.primaryKey}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.primaryKey'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.mongodb.primaryKey.placeholder'})}
        />
        <ProFormSwitch
          name={MongoDBParams.transaction}
          label={intl.formatMessage({id: 'pages.project.di.step.mongodb.transaction'})}
          initialValue={false}
        />
        <FieldItem/>
      </DrawerForm>
    </XFlow>
  );
};

export default SinkMongoDBStepForm;
