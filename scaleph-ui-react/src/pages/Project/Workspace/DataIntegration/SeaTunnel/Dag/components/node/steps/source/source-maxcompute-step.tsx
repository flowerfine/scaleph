import React, {useEffect} from 'react';
import {Form} from 'antd';
import {DrawerForm, ProFormDigit, ProFormText,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {MaxComputeParams, STEP_ATTR_TYPE} from '../constant';
import DataSourceItem from "../dataSource";
import FieldItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/schema/fields";
import {StepSchemaService} from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/helper";

const SourceMaxComputeStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
        <DataSourceItem dataSource={'MaxCompute'}/>
        <ProFormText
          name={MaxComputeParams.project}
          label={intl.formatMessage({id: 'pages.project.di.step.maxcompute.project'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={MaxComputeParams.tableName}
          label={intl.formatMessage({id: 'pages.project.di.step.maxcompute.tableName'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={MaxComputeParams.partitionSpec}
          label={intl.formatMessage({id: 'pages.project.di.step.maxcompute.partitionSpec'})}
        />
        <ProFormDigit
          name={MaxComputeParams.splitRow}
          label={intl.formatMessage({id: 'pages.project.di.step.maxcompute.splitRow'})}
          initialValue={10000}
          fieldProps={{
            step: 10000,
            min: 1,
          }}
        />
        <FieldItem/>
      </DrawerForm>
    </XFlow>
  );
};

export default SourceMaxComputeStepForm;
