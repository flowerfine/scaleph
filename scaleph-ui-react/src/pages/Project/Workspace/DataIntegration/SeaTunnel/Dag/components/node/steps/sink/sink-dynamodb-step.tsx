import React, {useEffect} from 'react';
import {Form} from 'antd';
import {DrawerForm, ProFormDigit, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {AmazonDynamoDBParams, STEP_ATTR_TYPE} from '../constant';

const SinkAmazonDynamodbStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
          name={AmazonDynamoDBParams.url}
          label={intl.formatMessage({id: 'pages.project.di.step.dynamodb.url'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={AmazonDynamoDBParams.region}
          label={intl.formatMessage({id: 'pages.project.di.step.dynamodb.region'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={AmazonDynamoDBParams.accessKeyId}
          label={intl.formatMessage({id: 'pages.project.di.step.dynamodb.accessKeyId'})}
          colProps={{span: 12}}
          rules={[{required: true}]}
        />
        <ProFormText
          name={AmazonDynamoDBParams.secretAccessKey}
          label={intl.formatMessage({id: 'pages.project.di.step.dynamodb.secretAccessKey'})}
          colProps={{span: 12}}
          rules={[{required: true}]}
        />
        <ProFormText
          name={AmazonDynamoDBParams.table}
          label={intl.formatMessage({id: 'pages.project.di.step.dynamodb.table'})}
          rules={[{required: true}]}
        />
        <ProFormDigit
          name={AmazonDynamoDBParams.batchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.dynamodb.batchSize'})}
          colProps={{span: 12}}
          initialValue={25}
          fieldProps={{
            step: 100,
            min: 0,
          }}
        />
        <ProFormDigit
          name={AmazonDynamoDBParams.batchIntervalMs}
          label={intl.formatMessage({id: 'pages.project.di.step.dynamodb.batchIntervalMs'})}
          colProps={{span: 12}}
          initialValue={1000}
          fieldProps={{
            step: 1000,
            min: 0,
          }}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SinkAmazonDynamodbStepForm;
