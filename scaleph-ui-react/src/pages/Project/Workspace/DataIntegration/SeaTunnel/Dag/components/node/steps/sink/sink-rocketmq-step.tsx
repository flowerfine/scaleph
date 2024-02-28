import React, {useEffect} from 'react';
import {Form} from 'antd';
import {
  DrawerForm,
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

const SinkRocketMQStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            values[RocketMQParams.aclEnabled] = values[RocketMQParams.aclEnabledField]
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
      </DrawerForm>
    </XFlow>
  );
};

export default SinkRocketMQStepForm;
