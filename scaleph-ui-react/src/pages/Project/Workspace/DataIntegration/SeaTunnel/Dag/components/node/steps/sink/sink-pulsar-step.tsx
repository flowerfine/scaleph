import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {
  DrawerForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup, ProFormList,
  ProFormSelect,
  ProFormText,
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {PulsarParams, SchemaParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import DataSourceItem from "../dataSource";
import FieldItem from "../fields";

const SinkPulsarStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatPulsarConf(values);
            StepSchemaService.formatPulsarPartitionKeyFields(values);
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
          name={PulsarParams.topic}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.topic'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.pulsar.topic.tooltip'}),
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
            if (format == 'text') {
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

        <ProFormSelect
          name={PulsarParams.semantics}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.semantics'})}
          rules={[{required: true}]}
          initialValue={"AT_LEAST_ONCE"}
          options={["NON", "AT_LEAST_ONCE", "EXACTLY_ONCE"]}
        />
        <ProFormDependency name={['semantics']}>
          {({semantics}) => {
            if (semantics == 'EXACTLY_ONCE') {
              return <ProFormDigit
                name={PulsarParams.transactionTimeout}
                label={intl.formatMessage({id: 'pages.project.di.step.pulsar.transactionTimeout'})}
                tooltip={{
                  title: intl.formatMessage({id: 'pages.project.di.step.pulsar.transactionTimeout.tooltip'}),
                  icon: <InfoCircleOutlined/>,
                }}
                initialValue={600}
              />
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormSelect
          name={PulsarParams.messageRoutingMode}
          label={intl.formatMessage({id: 'pages.project.di.step.pulsar.messageRoutingMode'})}
          rules={[{required: true}]}
          initialValue={"RoundRobinPartition"}
          options={["RoundRobinPartition", "SinglePartition", "CustomPartition"]}
        />

        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.pulsar.partitionKeyFields'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.pulsar.partitionKeyFields.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
        >
          <ProFormList
            name={PulsarParams.partitionKeyFieldArray}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.pulsar.partitionKeyField'}),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={PulsarParams.partitionKeyField}
                colProps={{span: 23, offset: 1}}
              />
            </ProFormGroup>
          </ProFormList>
        </ProFormGroup>

        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.pulsar.pulsarConfig'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.pulsar.pulsarConfig.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
        >
          <ProFormList
            name={PulsarParams.pulsarConfigMap}
            copyIconProps={false}
            creatorButtonProps={{
              creatorButtonText: intl.formatMessage({id: 'pages.project.di.step.pulsar.pulsarConfig.item'}),
              type: 'text',
            }}
          >
            <ProFormGroup>
              <ProFormText
                name={PulsarParams.pulsarConfigKey}
                label={intl.formatMessage({id: 'pages.project.di.step.pulsar.pulsarConfigKey'})}
                colProps={{span: 10, offset: 1}}
              />
              <ProFormText
                name={PulsarParams.pulsarConfigValue}
                label={intl.formatMessage({id: 'pages.project.di.step.pulsar.pulsarConfigValue'})}
                colProps={{span: 10, offset: 1}}
              />
            </ProFormGroup>
          </ProFormList>
        </ProFormGroup>


      </DrawerForm>
    </XFlow>
  );
};

export default SinkPulsarStepForm;
