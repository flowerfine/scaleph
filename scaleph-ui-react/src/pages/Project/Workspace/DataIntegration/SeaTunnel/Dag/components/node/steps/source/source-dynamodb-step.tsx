import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {DrawerForm, ProFormGroup, ProFormList, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {AmazonDynamoDBParams, SchemaParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';

const SourceAmazonDynamodbStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
        <ProFormText
            name={AmazonDynamoDBParams.scanItemLimit}
            label={intl.formatMessage({id: 'pages.project.di.step.dynamodb.scanItemLimit'})}
            colProps={{span: 12}}
        />
        <ProFormText
            name={AmazonDynamoDBParams.parallelScanThreads}
            label={intl.formatMessage({id: 'pages.project.di.step.dynamodb.parallelScanThreads'})}
            colProps={{span: 12}}
        />
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.schema'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.schema.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        >
          <ProFormList
            name={SchemaParams.fields}
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
      </DrawerForm>
    </XFlow>
  );
};

export default SourceAmazonDynamodbStepForm;
