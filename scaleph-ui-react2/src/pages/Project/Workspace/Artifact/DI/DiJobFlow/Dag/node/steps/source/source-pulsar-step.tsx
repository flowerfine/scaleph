import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {
  ProForm,
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
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";
import FieldItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/fields";

const SourcePulsarStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
                values[PulsarParams.cursorStartupMode] = values.startMode;
                values[PulsarParams.cursorStopMode] = values.stopMode;
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
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SourcePulsarStepForm;
