import React from 'react';
import {useIntl} from "@umijs/max";
import {ProFormDependency, ProFormDigit, ProFormGroup, ProFormSwitch, ProFormText} from "@ant-design/pro-components";
import {DorisParams} from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/Connector/constant";

const SinkDorisConnectorForm: React.FC = () => {
  const intl = useIntl();

  return (
    <ProFormGroup>
      <ProFormSwitch
        name={DorisParams.autoRedirect}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.doris.autoRedirect'})}
      />
      <ProFormSwitch
        name={DorisParams.sinkEnableBatchModeParam}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.doris.sinkEnableBatchMode'})}
        initialValue={true}
      />
      <ProFormDependency name={["sinkEnableBatchModeParam"]}>
        {({sinkEnableBatchModeParam}) => {
          if (sinkEnableBatchModeParam) {
            return <ProFormGroup>
              <ProFormDigit
                name={DorisParams.sinkFlushQueueSize}
                label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.doris.sinkFlushQueueSize'})}
                initialValue={2}
                fieldProps={{
                  min: 1
                }}
              />
              <ProFormDigit
                name={DorisParams.sinkBufferFlushMaxRows}
                label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.doris.sinkBufferFlushMaxRows'})}
                colProps={{span: 8}}
                initialValue={50000}
                fieldProps={{
                  min: 1
                }}
              />
              <ProFormDigit
                name={DorisParams.sinkBufferFlushMaxBytes}
                label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.doris.sinkBufferFlushMaxBytes'})}
                colProps={{span: 8}}
                initialValue={1024 * 1024 * 10}
                fieldProps={{
                  min: 1
                }}
              />
              <ProFormText
                name={DorisParams.sinkBufferFlushInterval}
                label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.doris.sinkBufferFlushInterval'})}
                colProps={{span: 8}}
                initialValue={'10s'}
              />
            </ProFormGroup>
          } else {
            return <></>
          }
        }}
      </ProFormDependency>
    </ProFormGroup>
  );
};

export default SinkDorisConnectorForm;
