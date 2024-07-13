import React from 'react';
import {useIntl} from "@umijs/max";
import {ProFormDigit, ProFormGroup, ProFormSwitch, ProFormText} from "@ant-design/pro-components";
import {StarRocksParams} from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/Connector/constant";

const SinkStarRocksConnectorForm: React.FC<{prefix: string}> = ({prefix}) => {
  const intl = useIntl();

  return (
    <ProFormGroup>
      <ProFormText
        name={[prefix, StarRocksParams.sinkLabelPrefix]}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.starrocks.sinkLabelPrefix'})}
      />
      <ProFormText
        name={[prefix, StarRocksParams.tableSchemaChangeTimeout]}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.starrocks.tableSchemaChangeTimeout'})}
        initialValue={'30min'}
      />
      <ProFormDigit
        name={[prefix, StarRocksParams.sinkConnectTimeoutMs]}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.starrocks.sinkConnectTimeoutMs'})}
        initialValue={30000}
        fieldProps={{
          min: 100,
          step: 1000,
          max: 60000
        }}
      />
      <ProFormDigit
        name={[prefix, StarRocksParams.sinkWaitForContinueTimeoutMs]}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.starrocks.sinkWaitForContinueTimeoutMs'})}
        initialValue={30000}
        fieldProps={{
          min: 3000,
          step: 1000,
          max: 600000
        }}
      />
      <ProFormDigit
        name={[prefix, StarRocksParams.sinkBufferFlushMaxBytes]}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.starrocks.sinkBufferFlushMaxBytes'})}
        colProps={{span: 12}}
        initialValue={1024 * 1024 * 150}
        fieldProps={{
          min: 1024 * 1024 * 64,
          step: 1024 * 1024 * 32,
          max: 1024 * 1024 * 1024 * 10
        }}
      />
      <ProFormDigit
        name={[prefix, StarRocksParams.sinkBufferFlushIntervalMs]}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.starrocks.sinkBufferFlushIntervalMs'})}
        colProps={{span: 12}}
        initialValue={300000}
        fieldProps={{
          min: 0,
          step: 1000
        }}
      />
      <ProFormDigit
        name={[prefix, StarRocksParams.sinkScanFrequencyMs]}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.starrocks.sinkScanFrequencyMs'})}
        initialValue={50}
        fieldProps={{
          min: 0,
          step: 1
        }}
      />
      <ProFormDigit
        name={[prefix, StarRocksParams.sinkIoThreadCount]}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.starrocks.sinkIoThreadCount'})}
        initialValue={2}
        fieldProps={{
          min: 1,
          step: 1
        }}
      />
      <ProFormSwitch
        name={[prefix, StarRocksParams.sinkAtLeastOnceUseTransactionStreamLoad]}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.starrocks.sinkAtLeastOnceUseTransactionStreamLoad'})}
        initialValue={true}
      />
      <ProFormDigit
        name={[prefix, StarRocksParams.tableCreateNumBuckets]}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.starrocks.tableCreateNumBuckets'})}
      />
    </ProFormGroup>
  );
};

export default SinkStarRocksConnectorForm;
