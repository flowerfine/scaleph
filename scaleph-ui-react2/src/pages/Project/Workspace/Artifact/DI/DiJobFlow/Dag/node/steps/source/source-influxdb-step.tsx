import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {ProForm, ProFormDigit, ProFormSelect, ProFormText, ProFormTextArea,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {InfluxDBParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import FieldItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/fields";
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";

const SourceInfluxDBStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
          <DataSourceItem dataSource={"InfluxDB"}/>
          <ProFormText
            name={InfluxDBParams.database}
            label={intl.formatMessage({id: 'pages.project.di.step.influxdb.database'})}
            rules={[{required: true}]}
          />
          <ProFormTextArea
            name={InfluxDBParams.sql}
            label={intl.formatMessage({id: 'pages.project.di.step.influxdb.sql'})}
            rules={[{required: true}]}
          />
          <FieldItem/>
          <ProFormText
            name={InfluxDBParams.splitColumn}
            label={intl.formatMessage({id: 'pages.project.di.step.influxdb.splitColumn'})}
          />
          <ProFormDigit
            name={InfluxDBParams.lowerBound}
            label={intl.formatMessage({id: 'pages.project.di.step.influxdb.lowerBound'})}
            colProps={{span: 8}}
            min={0}
          />
          <ProFormDigit
            name={InfluxDBParams.upperBound}
            label={intl.formatMessage({id: 'pages.project.di.step.influxdb.upperBound'})}
            colProps={{span: 8}}
            min={0}
          />
          <ProFormDigit
            name={InfluxDBParams.partitionNum}
            label={intl.formatMessage({id: 'pages.project.di.step.influxdb.partitionNum'})}
            colProps={{span: 8}}
            min={1}
          />
          <ProFormSelect
            name={InfluxDBParams.epoch}
            label={intl.formatMessage({id: 'pages.project.di.step.influxdb.epoch'})}
            allowClear={false}
            initialValue={"n"}
            options={["H", "m", "s", "MS", "u", "n"]}
          />
          <ProFormDigit
            name={InfluxDBParams.queryTimeoutSec}
            label={intl.formatMessage({id: 'pages.project.di.step.influxdb.queryTimeoutSec'})}
            fieldProps={{
              min: 1,
              step: 1
            }}
          />
          <ProFormDigit
            name={InfluxDBParams.connectTimeoutMs}
            label={intl.formatMessage({id: 'pages.project.di.step.influxdb.connectTimeoutMs'})}
            fieldProps={{
              min: 1,
              step: 1000
            }}
          />
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SourceInfluxDBStepForm;
