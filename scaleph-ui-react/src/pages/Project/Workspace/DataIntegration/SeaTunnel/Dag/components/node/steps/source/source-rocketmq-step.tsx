import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {
  DrawerForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormSelect,
  ProFormSwitch,
  ProFormText,
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {RocketMQParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import FieldItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/schema/fields";
import CommonConfigItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/config/commonConfig";

const SourceRocketMQStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            values[RocketMQParams.startMode] = values[RocketMQParams.startModeField]
            StepSchemaService.formatSchema(values)
            StepSchemaService.formatCommonConfig(values, RocketMQParams.startModeOffsets, RocketMQParams.startModeOffsets)
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
          name={RocketMQParams.topics}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.topics'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.rocketmq.topics.placeholder'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={RocketMQParams.consumerGroup}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.consumerGroup'})}
          initialValue={'SeaTunnel-Consumer-Group'}
        />
        <ProFormDigit
          name={RocketMQParams.partitionDiscoveryIntervalMillis}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.partitionDiscoveryIntervalMillis'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.rocketmq.partitionDiscoveryIntervalMillis.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          colProps={{span: 8}}
          initialValue={-1}
          fieldProps={{
            step: 1000
          }}
        />
        <ProFormDigit
          name={RocketMQParams.batchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.batchSize'})}
          colProps={{span: 8}}
          initialValue={100}
          fieldProps={{
            step: 1000,
            min: 1
          }}
        />
        <ProFormDigit
          name={RocketMQParams.consumerPollTimeoutMillis}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.consumerPollTimeoutMillis'})}
          colProps={{span: 8}}
          initialValue={5000}
          fieldProps={{
            step: 1000,
            min: 1
          }}
        />

        <ProFormSwitch
          name={RocketMQParams.commitOnCheckpoint}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.commitOnCheckpoint'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.rocketmq.commitOnCheckpoint.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          initialValue={true}
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
            if (format == 'json') {
              return <FieldItem/>;
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>

        <ProFormSelect
          name={RocketMQParams.startModeField}
          label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.startMode'})}
          options={["CONSUME_FROM_LAST_OFFSET", "CONSUME_FROM_FIRST_OFFSET", "CONSUME_FROM_GROUP_OFFSETS", "CONSUME_FROM_TIMESTAMP", "CONSUME_FROM_SPECIFIC_OFFSETS"]}
          initialValue={"CONSUME_FROM_GROUP_OFFSETS"}
        />

        <ProFormDependency name={[RocketMQParams.startModeField]}>
          {({startModeField}) => {
            if (startModeField == 'CONSUME_FROM_TIMESTAMP') {
              return <ProFormGroup>
                <ProFormDigit
                  name={RocketMQParams.startModeTimestamp}
                  label={intl.formatMessage({id: 'pages.project.di.step.rocketmq.startModeTimestamp'})}
                  rules={[{required: true}]}
                />
              </ProFormGroup>;
            }
            if (startModeField == 'CONSUME_FROM_SPECIFIC_OFFSETS') {
              return <ProFormGroup
                title={intl.formatMessage({id: 'pages.project.di.step.rocketmq.startModeOffsets'})}
                tooltip={{
                  title: intl.formatMessage({id: 'pages.project.di.step.rocketmq.startModeOffsets.tooltip'}),
                  icon: <InfoCircleOutlined/>,
                }}
              >
                <CommonConfigItem data={RocketMQParams.startModeOffsets}/>
              </ProFormGroup>;
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
      </DrawerForm>
    </XFlow>
  );
};

export default SourceRocketMQStepForm;
