import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {
  ProForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormSelect,
  ProFormSwitch,
  ProFormText
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {RocketMQParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";

const SinkRocketMQStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
                values[RocketMQParams.aclEnabled] = values[RocketMQParams.aclEnabledField]
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
            colProps={{span: 24}}
          />

          <ProFormText
            name={RocketMQParams.nameSrvAddr}
            label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.nameSrvAddr'})}
            rules={[{required: true}]}
          />
          <ProFormSwitch
            name={RocketMQParams.aclEnabledField}
            label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.aclEnabled'})}
            initialValue={false}
          />
          <ProFormDependency name={[RocketMQParams.aclEnabledField]}>
            {({acl_enabled}) => {
              if (acl_enabled) {
                return <ProFormGroup>
                  <ProFormText
                    name={RocketMQParams.accessKey}
                    label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.accessKey'})}
                    rules={[{required: true}]}
                    colProps={{span: 12}}
                  />
                  <ProFormText
                    name={RocketMQParams.secretKey}
                    label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.secretKey'})}
                    rules={[{required: true}]}
                    colProps={{span: 12}}
                  />
                </ProFormGroup>;
              }

              return <ProFormGroup/>;
            }}
          </ProFormDependency>

          <ProFormText
            name={RocketMQParams.topic}
            label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.topic'})}
            rules={[{required: true}]}
          />
          <ProFormSwitch
            name={RocketMQParams.exactlyOnce}
            label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.exactlyOnce'})}
            initialValue={false}
          />

          <ProFormText
            name={RocketMQParams.partitionKeyFields}
            label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.partitionKeyFields'})}
            placeholder={intl.formatMessage({id: 'pages.project.di.step.rocketmq.partitionKeyFields.placeholder'})}
          />
          <ProFormSwitch
            name={RocketMQParams.producerSendSync}
            label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.producerSendSync'})}
            colProps={{span: 8}}
            initialValue={false}
          />
          <ProFormDigit
            name={RocketMQParams.maxMessageSize}
            label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.maxMessageSize'})}
            colProps={{span: 8}}
            initialValue={1024 * 1024 * 4}
            fieldProps={{
              step: 1024 * 1024,
              min: 1
            }}
          />
          <ProFormDigit
            name={RocketMQParams.sendMessageTimeout}
            label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.sendMessageTimeout'})}
            colProps={{span: 8}}
            initialValue={3000}
            fieldProps={{
              step: 1000,
              min: 1
            }}
          />
          <ProFormSelect
            name={RocketMQParams.format}
            label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.format'})}
            options={["json", "text"]}
            initialValue={"json"}
          />
          <ProFormDependency name={[RocketMQParams.format]}>
            {({format}) => {
              if (format == 'text') {
                return <ProFormGroup>
                  <ProFormText
                    name={RocketMQParams.fieldDelimiter}
                    label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.fieldDelimiter'})}
                    initialValue={','}
                  />
                </ProFormGroup>;
              }
              return <ProFormGroup/>;
            }}
          </ProFormDependency>
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SinkRocketMQStepForm;
