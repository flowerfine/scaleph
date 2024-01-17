import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {ProForm, ProFormDigit, ProFormSelect, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {InfluxDBParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";

const SinkInfluxDBStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
                StepSchemaService.formatKeyTags(values);
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
          <DataSourceItem dataSource={'InfluxDB'}/>
          <ProFormText
            name={InfluxDBParams.database}
            label={intl.formatMessage({id: 'pages.project.di.step.influxdb.database'})}
            rules={[{required: true}]}
          />
          <ProFormText
            name={InfluxDBParams.measurement}
            label={intl.formatMessage({id: 'pages.project.di.step.influxdb.measurement'})}
            rules={[{required: true}]}
          />
          <ProFormText
            name={InfluxDBParams.keyKime}
            label={intl.formatMessage({id: 'pages.project.di.step.influxdb.keyKime'})}
          />
          <ProFormSelect
            name={InfluxDBParams.keyTagArray}
            label={intl.formatMessage({id: 'pages.project.di.step.influxdb.keyTags'})}
            fieldProps={{
              mode: 'tags',
            }}
          />
          <ProFormDigit
            name={InfluxDBParams.batchSize}
            label={intl.formatMessage({id: 'pages.project.di.step.influxdb.batchSize'})}
            colProps={{span: 12}}
            initialValue={1024}
            fieldProps={{
              min: 1,
              step: 1000,
            }}
          />
          <ProFormDigit
            name={InfluxDBParams.batchIntervalMs}
            label={intl.formatMessage({id: 'pages.project.di.step.influxdb.batchIntervalMs'})}
            colProps={{span: 12}}
            initialValue={1000}
            fieldProps={{
              min: 1,
              step: 1000,
            }}
          />
          <ProFormDigit
            name={InfluxDBParams.maxRetries}
            label={intl.formatMessage({id: 'pages.project.di.step.influxdb.maxRetries'})}
            colProps={{span: 12}}
            min={1}
          />
          <ProFormDigit
            name={InfluxDBParams.retryBackoffMultiplierMs}
            label={intl.formatMessage({
              id: 'pages.project.di.step.influxdb.retryBackoffMultiplierMs',
            })}
            colProps={{span: 12}}
            fieldProps={{
              min: 1,
              step: 1000,
            }}
          />
          <ProFormDigit
            name={InfluxDBParams.connectTimeoutMs}
            label={intl.formatMessage({id: 'pages.project.di.step.influxdb.connectTimeoutMs'})}
            fieldProps={{
              min: 1,
              step: 1000,
            }}
          />
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SinkInfluxDBStepForm;
