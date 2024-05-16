import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {DrawerForm, ProFormDigit, ProFormGroup, ProFormSwitch, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {IoTDBParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "../helper";
import DataSourceItem from "../dataSource";
import CommonListItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/list";

const SinkIoTDBStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatCommonList(values, IoTDBParams.keyMeasurementFields, IoTDBParams.keyMeasurementFields);
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
        <ProFormText
          name={IoTDBParams.keyDevice}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.keyDevice'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={IoTDBParams.keyTimestamp}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.keyTimestamp'})}
        />
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.iotdb.keyMeasurementFields'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.iotdb.keyMeasurementFields.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonListItem data={IoTDBParams.keyMeasurementFields}/>
        </ProFormGroup>
        <ProFormText
          name={IoTDBParams.storageGroup}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.storageGroup'})}
        />
        <ProFormDigit
          name={IoTDBParams.batchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.batchSize'})}
          initialValue={1024}
          fieldProps={{
            step: 100,
            min: 1,
          }}
        />
        <ProFormDigit
          name={IoTDBParams.maxRetries}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.maxRetries'})}
          colProps={{span: 6}}
          fieldProps={{
            min: 1,
          }}
        />
        <ProFormDigit
          name={IoTDBParams.retryBackoffMultiplierMs}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.retryBackoffMultiplierMs'})}
          colProps={{span: 9}}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormDigit
          name={IoTDBParams.maxRetryBackoffMs}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.maxRetryBackoffMs'})}
          colProps={{span: 9}}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormDigit
          name={IoTDBParams.defaultThriftBufferSize}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.thriftDefaultBufferSize'})}
          colProps={{span: 12}}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormDigit
          name={IoTDBParams.maxThriftFrameSize}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.maxThriftFrameSize'})}
          colProps={{span: 12}}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormText
          name={IoTDBParams.zoneId}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.zoneId'})}
        />
        <ProFormSwitch
          name={IoTDBParams.enableRpcCompression}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.enableRpcCompression'})}
        />
        <ProFormDigit
          name={IoTDBParams.connectionTimeoutInMs}
          label={intl.formatMessage({id: 'pages.project.di.step.iotdb.connectionTimeoutInMs'})}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SinkIoTDBStepForm;
