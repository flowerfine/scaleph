import React from 'react';
import {useIntl} from "@umijs/max";
import {ProFormDigit, ProFormGroup, ProFormSelect, ProFormSwitch, ProFormText} from "@ant-design/pro-components";
import {
  KafkaParams,
  StarRocksParams
} from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/Connector/constant";

const SourceKafkaConnectorForm: React.FC = () => {
  const intl = useIntl();

  return (
    <ProFormGroup>
      <ProFormText
        name={KafkaParams.propertiesBootstrapServers}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.kafka.propertiesBootstrapServers'})}
        rules={[{required: true}]}
      />
      <ProFormText
        name={KafkaParams.topic}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.kafka.topic'})}
      />
      <ProFormSelect
        name={KafkaParams.valueFormat}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.kafka.valueFormat'})}
        allowClear={false}
        initialValue={'debezium-json'}
        options={['debezium-json', 'canal-json']}
      />
      <ProFormSwitch
        name={KafkaParams.sinkAddTableIdToHeaderEnabled}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.kafka.sinkAddTableIdToHeaderEnabled'})}
      />
      <ProFormText
        name={KafkaParams.sinkCustomHeader}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.kafka.sinkCustomHeader'})}
      />
    </ProFormGroup>
  );
};

export default SourceKafkaConnectorForm;
