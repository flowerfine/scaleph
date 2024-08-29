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
  ProFormTextArea,
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {CDCParams, OracleCDCParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constants/dictType";
import CommonConfigItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/config/commonConfig";

const SourceCDCOracleStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatCommonConfig(values, CDCParams.debeziums, CDCParams.debeziums);
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
          name={CDCParams.baseUrl}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.baseUrl'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.cdc.baseUrl.placeholder'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={CDCParams.username}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.username'})}
          rules={[{required: true}]}
          colProps={{span: 12}}
        />
        <ProFormText
          name={CDCParams.password}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.password'})}
          rules={[{required: true}]}
          colProps={{span: 12}}
        />
        <ProFormText
          name={CDCParams.databases}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.databases'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.cdc.databases.placeholder'})}
          colProps={{span: 8}}
        />
        <ProFormText
          name={OracleCDCParams.schemaNames}
          label={intl.formatMessage({id: 'pages.project.di.step.oracle-cdc.schemaNames'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.oracle-cdc.schemaNames.placeholder'})}
          colProps={{span: 8}}
        />
        <ProFormText
          name={CDCParams.tables}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.tables'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.cdc.tables.placeholder'})}
          rules={[{required: true}]}
          colProps={{span: 8}}
        />
        <ProFormTextArea
          name={CDCParams.tableConfig}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.tableConfig'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.cdc.tableConfig.placeholder'})}
        />
        <ProFormSelect
          name={'startupMode'}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.startupMode'})}
          allowClear={false}
          initialValue={'initial'}
          options={['initial', 'earliest', 'latest', 'specific', 'timestamp']}
        />
        <ProFormDependency name={['startupMode']}>
          {({startupMode}) => {
            if (startupMode == 'timestamp') {
              return (
                <ProFormGroup>
                  <ProFormDigit
                    name={CDCParams.startupTimestamp}
                    label={intl.formatMessage({id: 'pages.project.di.step.cdc.startupTimestamp'})}
                    rules={[{required: true}]}
                  />
                </ProFormGroup>
              );
            } else if (startupMode == 'specific') {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={CDCParams.startupSpecificOffsetFile}
                    label={intl.formatMessage({id: 'pages.project.di.step.cdc.startupSpecificOffsetFile'})}
                    rules={[{required: true}]}
                    colProps={{span: 12}}
                  />
                  <ProFormText
                    name={CDCParams.startupSpecificOffsetPos}
                    label={intl.formatMessage({id: 'pages.project.di.step.cdc.startupSpecificOffsetPos'})}
                    rules={[{required: true}]}
                    colProps={{span: 12}}
                  />
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>

        <ProFormSelect
          name={'stopMode'}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.stopMode'})}
          allowClear={false}
          initialValue={'never'}
          options={['never', 'latest', 'specific', 'timestamp']}
        />
        <ProFormDependency name={['stopMode']}>
          {({stopMode}) => {
            if (stopMode == 'timestamp') {
              return (
                <ProFormGroup>
                  <ProFormDigit
                    name={CDCParams.stopTimestamp}
                    label={intl.formatMessage({id: 'pages.project.di.step.cdc.stopTimestamp'})}
                    rules={[{required: true}]}
                  />
                </ProFormGroup>
              );
            } else if (stopMode == 'specific') {
              return (
                <ProFormGroup>
                  <ProFormText
                    name={CDCParams.stopSpecificOffsetFile}
                    label={intl.formatMessage({id: 'pages.project.di.step.cdc.stopSpecificOffsetFile'})}
                    rules={[{required: true}]}
                    colProps={{span: 12}}
                  />
                  <ProFormText
                    name={CDCParams.stopSpecificOffsetPos}
                    label={intl.formatMessage({id: 'pages.project.di.step.cdc.stopSpecificOffsetPos'})}
                    rules={[{required: true}]}
                    colProps={{span: 12}}
                  />
                </ProFormGroup>
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormDigit
          name={CDCParams.snapshotSplitSize}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.snapshotSplitSize'})}
          colProps={{span: 8}}
          initialValue={8096}
          fieldProps={{
            step: 1024,
            min: 1,
          }}
        />
        <ProFormDigit
          name={CDCParams.snapshotFetchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.snapshotFetchSize'})}
          colProps={{span: 8}}
          initialValue={1024}
          fieldProps={{
            step: 1024,
            min: 1,
          }}
        />
        <ProFormDigit
          name={CDCParams.incrementalParallelism}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.incrementalParallelism'})}
          colProps={{span: 8}}
          initialValue={1}
          fieldProps={{
            min: 0,
          }}
        />
        <ProFormText
          name={CDCParams.serverTimeZone}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.serverTimeZone'})}
          initialValue={'UTC'}
        />
        <ProFormDigit
          name={CDCParams.connectionPoolSize}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.connectionPoolSize'})}
          colProps={{span: 8}}
          initialValue={20}
          fieldProps={{
            min: 1,
          }}
        />
        <ProFormText
          name={CDCParams.connectTimeout}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.connectTimeout'})}
          colProps={{span: 8}}
          initialValue={'30s'}
        />
        <ProFormDigit
          name={CDCParams.connectMaxRetries}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.connectMaxRetries'})}
          colProps={{span: 8}}
          initialValue={3}
          fieldProps={{
            min: 0,
          }}
        />

        <ProFormDigit
          name={CDCParams.chunkKeyEvenDistributionFactorLowerBound}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.chunkKeyEvenDistributionFactorLowerBound'})}
          colProps={{span: 12}}
          initialValue={0.05}
          fieldProps={{
            min: 0,
          }}
        />
        <ProFormDigit
          name={CDCParams.chunkKeyEvenDistributionFactorUpperBound}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.chunkKeyEvenDistributionFactorUpperBound'})}
          colProps={{span: 12}}
          initialValue={1000}
          fieldProps={{
            min: 0,
          }}
        />
        <ProFormDigit
          name={CDCParams.sampleShardingThreshold}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.sampleShardingThreshold'})}
          colProps={{span: 12}}
          initialValue={1000}
          fieldProps={{
            min: 0,
          }}
        />
        <ProFormDigit
          name={CDCParams.inverseSamplingRate}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.inverseSamplingRate'})}
          colProps={{span: 12}}
          initialValue={1000}
          fieldProps={{
            min: 0,
          }}
        />
        <ProFormSwitch
          name={CDCParams.exactlyOnce}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.exactlyOnce'})}
          initialValue={true}
        />
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.cdc.debeziums'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.cdc.debeziums.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        >
          <CommonConfigItem data={CDCParams.debeziums}/>
        </ProFormGroup>
        <ProFormSelect
          name={CDCParams.format}
          label={intl.formatMessage({id: 'pages.project.di.step.cdc.format'})}
          initialValue={"DEFAULT"}
          request={() => {
            return DictDataService.listDictDataByType2(DICT_TYPE.seatunnelCDCFormat)
          }}
        />
        <ProFormSwitch
          name={OracleCDCParams.useSelectCount}
          label={intl.formatMessage({id: 'pages.project.di.step.oracle-cdc.useSelectCount'})}
          colProps={{span: 12}}
        />
        <ProFormSwitch
          name={OracleCDCParams.skipAnalyze}
          label={intl.formatMessage({id: 'pages.project.di.step.oracle-cdc.skipAnalyze'})}
          colProps={{span: 12}}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SourceCDCOracleStepForm;
