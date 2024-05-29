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
import {DorisParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "../helper";
import DataSourceItem from "../dataSource";
import SaveModeItem from "../common/saveMode";
import CommonConfigItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/config/commonConfig";

const SinkDorisStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatCommonConfig(values, DorisParams.dorisConfig, DorisParams.dorisConfig);
            // doris.config 是必需参数
            if (!values.hasOwnProperty(DorisParams.dorisConfig)) {
              values[DorisParams.dorisConfig] = JSON.stringify({})
            }
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
        <DataSourceItem dataSource={'Doris'}/>
        <ProFormText
          name={DorisParams.database}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.database'})}
          colProps={{span: 12}}
          rules={[{required: true}]}
        />
        <ProFormText
          name={DorisParams.table}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.table'})}
          colProps={{span: 12}}
          rules={[{required: true}]}
        />
        <ProFormText
          name={DorisParams.sinkLabelPrefix}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkLabelPrefix'})}
          rules={[{required: true}]}
        />
        <ProFormSwitch
          name={DorisParams.sinkEnable2PC}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkEnable2PC'})}
          colProps={{span: 8}}
          initialValue={true}
        />
        <ProFormSwitch
          name={DorisParams.sinkEnableDelete}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkEnableDelete'})}
          colProps={{span: 8}}
          initialValue={false}
        />
        <ProFormSwitch
          name={DorisParams.needsUnsupportedTypeCasting}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.needsUnsupportedTypeCasting'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.doris.needsUnsupportedTypeCasting.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          colProps={{span: 8}}
          initialValue={false}
        />
        <ProFormDigit
          name={DorisParams.sinkCheckInterval}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkCheckInterval'})}
          colProps={{span: 12}}
          initialValue={10000}
          fieldProps={{
            step: 1000,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.sinkMaxRetries}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkMaxRetries'})}
          colProps={{span: 12}}
          initialValue={3}
          fieldProps={{
            step: 1,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.sinkBufferSize}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkBufferSize'})}
          colProps={{span: 8}}
          initialValue={256 * 1024}
          fieldProps={{
            step: 1024 * 1024,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.sinkBufferCount}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.sinkBufferCount'})}
          colProps={{span: 8}}
          initialValue={3}
          fieldProps={{
            step: 1,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.dorisBatchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.dorisBatchSize'})}
          colProps={{span: 8}}
          initialValue={1024}
          fieldProps={{
            step: 1024,
            min: 1
          }}
        />

        <SaveModeItem/>
        <ProFormDependency name={['schema_save_mode']}>
          {({schema_save_mode}) => {
            if (schema_save_mode == 'CREATE_SCHEMA_WHEN_NOT_EXIST'
              || schema_save_mode == 'RECREATE_SCHEMA') {
              return (
                <ProFormTextArea
                  name={DorisParams.saveModeCreateTemplate}
                  label={intl.formatMessage({id: 'pages.project.di.step.doris.saveModeCreateTemplate'})}
                  tooltip={{
                    title: intl.formatMessage({id: 'pages.project.di.step.doris.saveModeCreateTemplate.tooltip'}),
                    icon: <InfoCircleOutlined/>,
                  }}
                  placeholder={intl.formatMessage({id: 'pages.project.di.step.doris.saveModeCreateTemplate.placeholder'})}
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
                  name={DorisParams.customSql}
                  label={intl.formatMessage({id: 'pages.project.di.step.doris.customSql'})}
                />
              )
            }
          }}
        </ProFormDependency>

        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.doris.dorisConfig'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.doris.dorisConfig.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonConfigItem data={DorisParams.dorisConfig}/>
        </ProFormGroup>
      </DrawerForm>
    </XFlow>
  );
};

export default SinkDorisStepForm;
