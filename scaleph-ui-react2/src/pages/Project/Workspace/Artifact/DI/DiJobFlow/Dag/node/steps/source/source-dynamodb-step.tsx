import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {ProForm, ProFormGroup, ProFormList, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {AmazonDynamoDBParams, SchemaParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';

const SourceAmazonDynamodbStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
            rules={[{required: true}]}
          />
          <ProFormText
            name={AmazonDynamoDBParams.secretAccessKey}
            label={intl.formatMessage({id: 'pages.project.di.step.dynamodb.secretAccessKey'})}
            rules={[{required: true}]}
          />
          <ProFormText
            name={AmazonDynamoDBParams.table}
            label={intl.formatMessage({id: 'pages.project.di.step.dynamodb.table'})}
            rules={[{required: true}]}
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
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SourceAmazonDynamodbStepForm;
