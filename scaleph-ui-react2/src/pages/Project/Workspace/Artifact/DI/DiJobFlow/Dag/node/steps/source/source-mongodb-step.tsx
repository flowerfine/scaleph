import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {ProForm, ProFormDigit, ProFormSwitch, ProFormText, ProFormTextArea,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {MongoDBParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";
import FieldItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/fields";

const SourceMongoDBStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
          <DataSourceItem dataSource={"MongoDB"}/>
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

          <ProFormText
            name={MongoDBParams.matchProjection}
            label={intl.formatMessage({id: 'pages.project.di.step.mongodb.matchProjection'})}
          />
          <ProFormTextArea
            name={MongoDBParams.matchQuery}
            label={intl.formatMessage({id: 'pages.project.di.step.mongodb.matchQuery'})}
          />
          <FieldItem/>
          <ProFormText
            name={MongoDBParams.partitionSplitKey}
            label={intl.formatMessage({id: 'pages.project.di.step.mongodb.partitionSplitKey'})}
            colProps={{span: 12}}
            initialValue={"_id"}
          />
          <ProFormDigit
            name={MongoDBParams.partitionSplitSize}
            label={intl.formatMessage({id: 'pages.project.di.step.mongodb.partitionSplitSize'})}
            colProps={{span: 12}}
            initialValue={1024 * 1024 * 64}
            fieldProps={{
              step: 1024 * 1024,
              min: 1
            }}
          />
          <ProFormSwitch
            name={MongoDBParams.cursorNoTimeout}
            label={intl.formatMessage({id: 'pages.project.di.step.mongodb.cursorNoTimeout'})}
            colProps={{span: 8}}
            initialValue={true}
          />
          <ProFormDigit
            name={MongoDBParams.fetchSize}
            label={intl.formatMessage({id: 'pages.project.di.step.mongodb.fetchSize'})}
            colProps={{span: 8}}
            initialValue={1024 * 2}
            fieldProps={{
              step: 1024,
              min: 1
            }}
          />
          <ProFormDigit
            name={MongoDBParams.maxTimeMin}
            label={intl.formatMessage({id: 'pages.project.di.step.mongodb.maxTimeMin'})}
            colProps={{span: 8}}
            initialValue={600}
            fieldProps={{
              step: 60,
              min: 1
            }}
          />
          <ProFormSwitch
            name={MongoDBParams.flatSyncString}
            label={intl.formatMessage({id: 'pages.project.di.step.mongodb.flatSyncString'})}
            initialValue={true}
          />
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SourceMongoDBStepForm;
