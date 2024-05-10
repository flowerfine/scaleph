import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {
  DrawerForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormSelect,
  ProFormText,
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {PulsarParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import DataSourceItem from "../dataSource";
import FieldItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/schema/fields";

const SourcePulsarStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            values[PulsarParams.cursorStartupMode] = values.startMode;
            values[PulsarParams.cursorStopMode] = values.stopMode;
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
        <DataSourceItem dataSource={'Pulsar'}/>
        <ProFormText
          name={PulsarParams.subscriptionName}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.subscriptionName'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={PulsarParams.topic}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.topic'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.pulsar.topic.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        />
        <ProFormText
          name={PulsarParams.topicPattern}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.topicPattern'})}
        />
        <ProFormDigit
          name={PulsarParams.topicDiscoveryInterval}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.topicDiscoveryInterval'})}
          tooltip={{
            title: intl.formatMessage({
              id: 'pages.project.di.step.pulsar.topicDiscoveryInterval.tooltip',
            }),
            icon: <InfoCircleOutlined/>,
          }}
        />
        <ProFormSelect
          name={'format'}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.format'})}
          rules={[{required: true}]}
          initialValue={"json"}
          options={["json", "text"]}
        />
        <ProFormDependency name={['format']}>
          {({format}) => {
            if (format == 'json') {
              return <FieldItem/>
            } else if (format == 'text') {
              return (
                <ProFormText
                  name={PulsarParams.fieldDelimiter}
                  label={intl.formatMessage({id: 'pages.project.di.step.pulsar.fieldDelimiter'})}
                />
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>

        <ProFormDigit
          name={PulsarParams.pollTimeout}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.pollTimeout'})}
          colProps={{span: 8}}
          initialValue={100}
          fieldProps={{
            step: 1000,
            min: 0,
          }}
        />
        <ProFormDigit
          name={PulsarParams.pollInterval}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.pollInterval'})}
          colProps={{span: 8}}
          initialValue={50}
          fieldProps={{
            step: 1000,
            min: 0,
          }}
        />
        <ProFormDigit
          name={PulsarParams.pollBatchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.pollBatchSize'})}
          colProps={{span: 8}}
          initialValue={500}
          fieldProps={{
            step: 100,
            min: 0,
          }}
        />
        <ProFormSelect
          name={'startMode'}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.cursorStartupMode'})}
          allowClear={false}
          initialValue={'LATEST'}
          options={['LATEST', 'EARLIEST', 'SUBSCRIPTION', 'TIMESTAMP']}
        />
        <ProFormDependency name={['startMode']}>
          {({startMode}) => {
            if (startMode == 'TIMESTAMP') {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={PulsarParams.cursorStartupTimestamp}
                    label={intl.formatMessage({
                      id: 'pages.project.di.step.pulsar.cursorStartupTimestamp',
                    })}
                    rules={[{required: true}]}
                  />
                </ProFormGroup>
              );
            } else if (startMode == 'SUBSCRIPTION') {
              return (
                <ProFormGroup>
                  <ProFormSelect
                    name={PulsarParams.cursorResetMode}
                    label={intl.formatMessage({
                      id: 'pages.project.di.step.pulsar.cursorResetMode',
                    })}
                    allowClear={false}
                    initialValue={'LATEST'}
                    options={['LATEST', 'EARLIEST']}
                  />
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormSelect
          name={'stopMode'}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.cursorStopMode'})}
          tooltip={{
            title: intl.formatMessage({
              id: 'pages.project.di.step.pulsar.cursorStopMode.tooltip',
            }),
            icon: <InfoCircleOutlined/>,
          }}
          allowClear={false}
          initialValue={'NEVER'}
          options={['NEVER', 'LATEST', 'TIMESTAMP']}
        />
        <ProFormDependency name={['stopMode']}>
          {({stopMode}) => {
            if (stopMode == 'TIMESTAMP') {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={PulsarParams.cursorStopTimestamp}
                    label={intl.formatMessage({
                      id: 'pages.project.di.step.pulsar.cursorStopTimestamp',
                    })}
                    rules={[{required: true}]}
                  />
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
      </DrawerForm>
    </XFlow>
  );
};

export default SourcePulsarStepForm;
