import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {
  DrawerForm,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormSwitch,
  ProFormText
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {ClickHouseParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "../helper";
import DataSourceItem from "../dataSource";
import CommonConfigItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/config/commonConfig";

const SinkClickHouseStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatCommonConfig(values, ClickHouseParams.clickhouseConf, ClickHouseParams.clickhouseConf);
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
        <DataSourceItem dataSource={'ClickHouse'}/>
        <ProFormText
          name={STEP_ATTR_TYPE.table}
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.table'})}
          rules={[{required: true}]}
        />
        <ProFormDigit
          name={STEP_ATTR_TYPE.bulkSize}
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.bulkSize'})}
          initialValue={20000}
          fieldProps={{
            min: 1,
            step: 1000,
          }}
        />
        <ProFormSwitch
          name={'split_mode'}
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.splitMode'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.clickhosue.splitMode.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          colProps={{span: 8}}
        />
        <ProFormSwitch
          name={'support_upsert'}
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.supportUpsert'})}
          colProps={{span: 8}}
        />
        <ProFormSwitch
          name={ClickHouseParams.allowExperimentalLightweightDelete}
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.allowExperimentalLightweightDelete'})}
          colProps={{span: 8}}
        />
        <ProFormDependency name={['split_mode']}>
          {({split_mode}) => {
            if (split_mode) {
              return (
                <ProFormText
                  name={ClickHouseParams.shardingKey}
                  label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.shardingKey'})}
                  rules={[{required: true}]}
                />
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormDependency name={['support_upsert']}>
          {({support_upsert}) => {
            if (support_upsert) {
              return (
                <ProFormText
                  name={ClickHouseParams.primaryKey}
                  label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.primaryKey'})}
                  rules={[{required: true}]}
                />
              );
            }
            return <ProFormGroup/>;
          }}
        </ProFormDependency>
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.clickhosue.clickhouseConf'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.clickhosue.clickhouseConf.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonConfigItem data={ClickHouseParams.clickhouseConf}/>
        </ProFormGroup>
      </DrawerForm>
    </XFlow>
  );
};

export default SinkClickHouseStepForm;
