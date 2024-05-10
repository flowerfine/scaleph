import React, {useEffect} from 'react';
import {Form} from 'antd';
import {DrawerForm, ProFormDigit, ProFormText, ProFormTextArea,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {Neo4jParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import DataSourceItem from "../dataSource";
import FieldItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/schema/fields";

const SourceNeo4jStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
        <DataSourceItem dataSource={"Neo4j"}/>
        <ProFormText
          name={Neo4jParams.database}
          label={intl.formatMessage({id: 'pages.project.di.step.neo4j.database'})}
          rules={[{required: true}]}
        />
        <ProFormTextArea
          name={Neo4jParams.query}
          label={intl.formatMessage({id: 'pages.project.di.step.neo4j.query'})}
          rules={[{required: true}]}
        />
        <FieldItem/>
        <ProFormDigit
          name={Neo4jParams.maxTransactionRetryTime}
          label={intl.formatMessage({id: 'pages.project.di.step.neo4j.maxTransactionRetryTime'})}
          initialValue={30}
          fieldProps={{
            step: 5,
            min: 0
          }}
        />
        <ProFormDigit
          name={Neo4jParams.maxConnectionTimeout}
          label={intl.formatMessage({id: 'pages.project.di.step.neo4j.maxConnectionTimeout'})}
          initialValue={30}
          fieldProps={{
            step: 5,
            min: 0
          }}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SourceNeo4jStepForm;
