import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {ProForm, ProFormDigit, ProFormGroup, ProFormList, ProFormSwitch, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {IoTDBParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";

const SinkIoTDBStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
                StepSchemaService.formatMeasurementFields(values);
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
          >
            <ProFormList
              name={IoTDBParams.keyMeasurementFieldArray}
              copyIconProps={false}
              creatorButtonProps={{
                creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.iotdb.keyMeasurementFields.field'}),
                type: 'text',
              }}
            >
              <ProFormText name={IoTDBParams.keyMeasurementField}/>
            </ProFormList>
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
            name={IoTDBParams.batchIntervalMs}
            label={intl.formatMessage({id: 'pages.project.di.step.iotdb.batchIntervalMs'})}
            initialValue={1000}
            fieldProps={{
              step: 1000,
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
            fieldProps={{
              step: 1000,
              min: 1,
            }}
          />
          <ProFormDigit
            name={IoTDBParams.maxThriftFrameSize}
            label={intl.formatMessage({id: 'pages.project.di.step.iotdb.thriftMaxFrameSize'})}
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
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SinkIoTDBStepForm;
