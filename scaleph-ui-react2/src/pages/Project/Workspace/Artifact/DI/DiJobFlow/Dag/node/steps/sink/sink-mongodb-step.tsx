import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {ProForm, ProFormDigit, ProFormSwitch, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {MongoDBParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";
import FieldItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/fields";

const SinkMongoDBStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
                StepSchemaService.formatSchema(values)
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
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SinkMongoDBStepForm;
