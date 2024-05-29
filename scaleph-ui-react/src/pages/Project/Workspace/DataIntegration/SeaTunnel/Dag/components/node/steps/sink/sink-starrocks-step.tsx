import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {
  DrawerForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormSwitch,
  ProFormText,
  ProFormTextArea
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {StarRocksParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "../helper";
import DataSourceItem from "../dataSource";
import SaveModeItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/saveMode";
import CommonConfigItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/config/commonConfig";

const SinkStarRocksStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatCommonConfig(values, StarRocksParams.starrocksConfig, StarRocksParams.starrocksConfig);
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
        <DataSourceItem dataSource={'StarRocks'}/>
        <ProFormText
          name={StarRocksParams.baseUrl}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.base-url'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={StarRocksParams.database}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.database'})}
          colProps={{span: 12}}
          rules={[{required: true}]}
        />
        <ProFormText
          name={StarRocksParams.table}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.table'})}
          colProps={{span: 12}}
          rules={[{required: true}]}
        />
        <ProFormText
          name={StarRocksParams.labelPrefix}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.labelPrefix'})}
        />
        <ProFormDigit
          name={StarRocksParams.httpSocketTimeoutMs}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.httpSocketTimeoutMs'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.starrocks.httpSocketTimeoutMs.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormDigit
          name={StarRocksParams.batchMaxRows}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.batchMaxRows'})}
          colProps={{span: 8}}
          initialValue={1024}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormDigit
          name={StarRocksParams.batchMaxBytes}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.batchMaxBytes'})}
          colProps={{span: 8}}
          initialValue={5 * 1024 * 1024}
          fieldProps={{
            step: 1024 * 1024,
            min: 1,
          }}
        />
        <ProFormDigit
          name={StarRocksParams.batchIntervalMs}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.batchIntervalMs'})}
          colProps={{span: 8}}
          initialValue={1000}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormDigit
          name={StarRocksParams.maxRetries}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.maxRetries'})}
          colProps={{span: 8}}
          initialValue={1}
          fieldProps={{
            step: 1,
            min: 1,
          }}
        />
        <ProFormDigit
          name={StarRocksParams.retryBackoffMultiplierMs}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.retryBackoffMultiplierMs'})}
          colProps={{span: 8}}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormDigit
          name={StarRocksParams.maxRetryBackoffMs}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.maxRetryBackoffMs'})}
          colProps={{span: 8}}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormSwitch
          name={StarRocksParams.enableUpsertDelete}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.enableUpsertDelete'})}
        />
        <SaveModeItem/>
        <ProFormDependency name={['schema_save_mode']}>
          {({schema_save_mode}) => {
            if (schema_save_mode == 'CREATE_SCHEMA_WHEN_NOT_EXIST'
              || schema_save_mode == 'RECREATE_SCHEMA') {
              return (
                <ProFormTextArea
                  name={StarRocksParams.saveModeCreateTemplate}
                  label={intl.formatMessage({id: 'pages.project.di.step.starrocks.saveModeCreateTemplate'})}
                  tooltip={{
                    title: intl.formatMessage({id: 'pages.project.di.step.starrocks.saveModeCreateTemplate.tooltip'}),
                    icon: <InfoCircleOutlined/>,
                  }}
                  placeholder={intl.formatMessage({id: 'pages.project.di.step.starrocks.saveModeCreateTemplate.placeholder'})}
                />
              )
            }
          }}
        </ProFormDependency>
        <ProFormDependency name={['data_save_mode']}>
          {({data_save_mode}) => {
            if (data_save_mode == 'CUSTOM_PROCESSING') {
              return (
                <ProFormTextArea
                  name={StarRocksParams.customSql}
                  label={intl.formatMessage({id: 'pages.project.di.step.starrocks.customSql'})}
                />
              )
            }
          }}
        </ProFormDependency>
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.starrocks.starrocksConfig'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.starrocks.starrocksConfig.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonConfigItem data={StarRocksParams.starrocksConfig}/>
        </ProFormGroup>
      </DrawerForm>
    </XFlow>
  );
};

export default SinkStarRocksStepForm;
