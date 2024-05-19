import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {
  DrawerForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormSelect,
  ProFormText
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {KafkaParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "../helper";
import DataSourceItem from "../dataSource";
import CommonConfigItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/config/commonConfig";
import CommonListItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/list";

const SinkKafkaStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatCommonConfig(values, KafkaParams.kafkaConfig, KafkaParams.kafkaConfig);
            StepSchemaService.formatCommonList(values, KafkaParams.partitionKeyFields, KafkaParams.partitionKeyFields);
            StepSchemaService.formatCommonList(values, KafkaParams.assignPartitions, KafkaParams.assignPartitions);
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
        <DataSourceItem dataSource={'Kafka'}/>
        <ProFormText
          name={KafkaParams.topic}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.topic'})}
          rules={[{required: true}]}
        />
        <ProFormSelect
          name={'semantic'}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.semantic'})}
          allowClear={false}
          initialValue={'AT_LEAST_ONCE'}
          options={['EXACTLY_ONCE', 'AT_LEAST_ONCE', 'NON']}
        />
        <ProFormDependency name={['semantic']}>
          {({semantic}) => {
            if (semantic == 'EXACTLY_ONCE') {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={KafkaParams.transactionPrefix}
                    label={intl.formatMessage({id: 'pages.project.di.step.kafka.transactionPrefix'})}
                  />
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.kafka.partitionKeyFields'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.kafka.partitionKeyFields.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonListItem data={KafkaParams.partitionKeyFields}/>
        </ProFormGroup>
        <ProFormDigit
          name={KafkaParams.partition}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.partition'})}
          min={0}
        />
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.kafka.assignPartitions'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.kafka.assignPartitions.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonListItem data={KafkaParams.assignPartitions}/>
        </ProFormGroup>
        <ProFormSelect
          name={'format'}
          label={intl.formatMessage({id: 'pages.project.di.step.kafka.format'})}
          rules={[{required: true}]}
          initialValue={'json'}
          options={['json', 'text', "canal_json", "debezium_json", "compatible_debezium_json", "compatible_kafka_connect_json"]}
        />
        <ProFormDependency name={['format']}>
          {({format}) => {
            if (format == 'text') {
              return (
                <ProFormText
                  name={KafkaParams.fieldDelimiter}
                  label={intl.formatMessage({id: 'pages.project.di.step.kafka.fieldDelimiter'})}
                  initialValue={','}
                />
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.kafka.conf'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.kafka.conf.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonConfigItem data={KafkaParams.kafkaConfig}/>
        </ProFormGroup>
      </DrawerForm>
    </XFlow>
  );
};

export default SinkKafkaStepForm;
