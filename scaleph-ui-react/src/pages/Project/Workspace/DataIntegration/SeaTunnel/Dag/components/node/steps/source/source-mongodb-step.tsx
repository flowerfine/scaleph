import React, {useEffect} from 'react';
import {Form} from 'antd';
import {DrawerForm, ProFormDigit, ProFormSwitch, ProFormText, ProFormTextArea,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {MongoDBParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import DataSourceItem from "../dataSource";
import FieldItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/schema/fields";

const SourceMongoDBStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
      </DrawerForm>
    </XFlow>
  );
};

export default SourceMongoDBStepForm;
