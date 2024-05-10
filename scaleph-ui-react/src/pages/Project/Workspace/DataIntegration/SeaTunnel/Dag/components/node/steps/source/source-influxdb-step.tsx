import React, {useEffect} from 'react';
import {Form} from 'antd';
import {DrawerForm, ProFormDigit, ProFormSelect, ProFormText, ProFormTextArea,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {InfluxDBParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import FieldItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/schema/fields";
import DataSourceItem from "../dataSource";

const SourceInfluxDBStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
      </DrawerForm>
    </XFlow>
  );
};

export default SourceInfluxDBStepForm;
