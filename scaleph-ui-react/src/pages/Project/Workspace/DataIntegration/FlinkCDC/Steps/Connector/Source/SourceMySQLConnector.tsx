import React from 'react';
import {useIntl} from "@umijs/max";
import {
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormSelect,
  ProFormSwitch,
  ProFormText
} from "@ant-design/pro-components";
import {MySQLParams} from "@/pages/Project/Workspace/DataIntegration/FlinkCDC/Steps/Connector/constant";

const SourceMySQLConnectorForm: React.FC = () => {
  const intl = useIntl();

  return (
    <ProFormGroup>
      <ProFormText
        name={MySQLParams.tables}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.tables'})}
        rules={[{required: true}]}
      />
      <ProFormText
        name={MySQLParams.tablesExclude}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.tablesExclude'})}
      />
      <ProFormText
        name={MySQLParams.serverId}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.serverId'})}
      />
      <ProFormDigit
        name={MySQLParams.scanIncrementalSnapshotChunkSize}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.scanIncrementalSnapshotChunkSize'})}
        colProps={{span: 12}}
        fieldProps={{
          min: 1
        }}
      />
      <ProFormDigit
        name={MySQLParams.scanSnapshotFetchSize}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.scanSnapshotFetchSize'})}
        colProps={{span: 12}}
        fieldProps={{
          min: 1
        }}
      />
      <ProFormSwitch
        name={MySQLParams.scanIncrementalCloseIdleReaderEnabled}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.scanIncrementalCloseIdleReaderEnabled'})}
        colProps={{span: 12}}
      />
      <ProFormSwitch
        name={MySQLParams.schemaChangeEnabled}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.schemaChangeEnabled'})}
        colProps={{span: 12}}
      />
      <ProFormSelect
        name={MySQLParams.startupMode}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.scanStartupMode'})}
        initialValue={'initial'}
        options={['initial', 'earliest-offset', 'latest-offset', 'specific-offset', 'timestamp']}
      />
      <ProFormDependency name={["scanStartupMode"]}>
        {({scanStartupMode}) => {
          if (scanStartupMode == 'specific-offset') {
            return <ProFormGroup>
              <ProFormText
                name={MySQLParams.scanStartupSpecificOffsetFile}
                label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.scanStartupSpecificOffsetFile'})}
                colProps={{span: 8}}
              />
              <ProFormDigit
                name={MySQLParams.scanStartupSpecificOffsetPos}
                label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.scanStartupSpecificOffsetPos'})}
                colProps={{span: 8}}
                fieldProps={{
                  min: 0
                }}
              />
              <ProFormText
                name={MySQLParams.scanStartupSpecificOffsetGtidSet}
                label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.scanStartupSpecificOffsetGtidSet'})}
                colProps={{span: 8}}
              />
            </ProFormGroup>
          }
          return <></>
        }}
      </ProFormDependency>

      <ProFormDigit
        name={MySQLParams.scanStartupSpecificOffsetSkipEvents}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.scanStartupSpecificOffsetSkipEvents'})}
        colProps={{span: 12}}
        fieldProps={{
          min: 0
        }}
      />
      <ProFormDigit
        name={MySQLParams.scanStartupSpecificOffsetSkipRows}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.scanStartupSpecificOffsetSkipRows'})}
        colProps={{span: 12}}
        fieldProps={{
          min: 0
        }}
      />
      <ProFormGroup
        title={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.connect'})}
        collapsible={true}
        defaultCollapsed={true}
      >
        <ProFormText
          name={MySQLParams.connectTimeout}
          label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.connectTimeout'})}
          colProps={{span: 8}}
          initialValue={'30s'}
        />
        <ProFormDigit
          name={MySQLParams.connectMaxRetries}
          label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.connectMaxRetries'})}
          colProps={{span: 8}}
          initialValue={3}
          fieldProps={{
            min: 0
          }}
        />
        <ProFormDigit
          name={MySQLParams.connectionPoolSize}
          label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.connectionPoolSize'})}
          colProps={{span: 8}}
          initialValue={20}
          fieldProps={{
            min: 1
          }}
        />
      </ProFormGroup>
      <ProFormText
        name={MySQLParams.heartbeatInterval}
        label={intl.formatMessage({id: 'pages.project.di.flink-cdc.step.connector.mysql.heartbeatInterval'})}
        initialValue={'30s'}
      />
    </ProFormGroup>
  );
};

export default SourceMySQLConnectorForm;
