import React, {useEffect} from 'react';
import {Form} from 'antd';
import {
  DrawerForm,
  ProFormDigit,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {IoTDBParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import FieldItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/schema/fields";
import DataSourceItem from "../dataSource";

const SourceIoTDBStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
        <DataSourceItem dataSource={'IoTDB'}/>
        <ProFormTextArea
          name={IoTDBParams.sql}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.sql'})}
          rules={[{required: true}]}
        />
        <FieldItem/>
        <ProFormDigit
          name={IoTDBParams.fetchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.fetchSize'})}
          min={1}
        />
        <ProFormDigit
          name={IoTDBParams.thriftDefaultBufferSize}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.thriftDefaultBufferSize'})}
          colProps={{span: 12}}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormDigit
          name={IoTDBParams.thriftMaxFrameSize}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.thriftMaxFrameSize'})}
          colProps={{span: 12}}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormSwitch
          name={IoTDBParams.enableCacheLeader}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.enableCacheLeader'})}
        />
        <ProFormSelect
          name={IoTDBParams.version}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.version'})}
          valueEnum={{V_0_12: 'V_0_12', V_0_13: 'V_0_13'}}
        />
        <ProFormDigit
          name={IoTDBParams.numPartitions}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.numPartitions'})}
          colProps={{span: 8}}
          fieldProps={{
            min: 1,
          }}
        />
        <ProFormDigit
          name={IoTDBParams.lowerBound}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.lowerBound'})}
          colProps={{span: 8}}
          fieldProps={{
            step: 100000,
            min: 0,
          }}
        />
        <ProFormDigit
          name={IoTDBParams.upperBound}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.upperBound'})}
          colProps={{span: 8}}
          fieldProps={{
            step: 100000,
            min: 1,
          }}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SourceIoTDBStepForm;
