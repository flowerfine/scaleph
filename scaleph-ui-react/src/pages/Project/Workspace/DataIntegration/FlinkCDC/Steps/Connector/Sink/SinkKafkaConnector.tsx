import React from 'react';
import {useIntl} from "@umijs/max";
import {ProFormGroup, ProFormSelect, ProFormSwitch, ProFormText} from "@ant-design/pro-components";
import {KafkaParams} from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/Connector/constant";

const SinkKafkaConnectorForm: React.FC<{prefix: string}> = ({prefix}) => {
  const intl = useIntl();

  return (
    <ProFormGroup>
      <ProFormText
        name={[prefix, KafkaParams.propertiesBootstrapServers]}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.kafka.propertiesBootstrapServers'})}
        rules={[{required: true}]}
      />
      <ProFormText
        name={[prefix, KafkaParams.topic]}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.kafka.topic'})}
      />
      <ProFormSelect
        name={[prefix, KafkaParams.valueFormat]}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.kafka.valueFormat'})}
        allowClear={false}
        initialValue={'debezium-json'}
        options={['debezium-json', 'canal-json']}
      />
      <ProFormSwitch
        name={[prefix, KafkaParams.sinkAddTableIdToHeaderEnabled]}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.kafka.sinkAddTableIdToHeaderEnabled'})}
      />
      <ProFormText
        name={[prefix, KafkaParams.sinkCustomHeader]}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.kafka.sinkCustomHeader'})}
      />
    </ProFormGroup>
  );
};

export default SinkKafkaConnectorForm;
