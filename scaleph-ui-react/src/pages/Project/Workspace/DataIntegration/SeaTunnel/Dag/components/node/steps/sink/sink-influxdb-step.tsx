import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {DrawerForm, ProFormDigit, ProFormGroup, ProFormSelect, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {InfluxDBParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "../helper";
import DataSourceItem from "../dataSource";
import CommonListItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/list";

const SinkInfluxDBStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatCommonList(values, InfluxDBParams.keyTags, InfluxDBParams.keyTags);
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

        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.influxdb.keyTags'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.influxdb.keyTags.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonListItem data={InfluxDBParams.keyTags}/>
        </ProFormGroup>
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
      </DrawerForm>
    </XFlow>
  );
};

export default SinkInfluxDBStepForm;
