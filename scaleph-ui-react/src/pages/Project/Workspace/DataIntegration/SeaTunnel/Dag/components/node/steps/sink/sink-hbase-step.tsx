import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {
  DrawerForm,
  ProFormDigit,
  ProFormGroup,
  ProFormSelect,
  ProFormSwitch,
  ProFormText
} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {HbaseParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "../helper";
import CommonConfigItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/config/commonConfig";
import CommonListItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/list";

const SinkHbaseStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatCommonConfig(values, HbaseParams.familyName, HbaseParams.familyName)
            StepSchemaService.formatCommonList(values, HbaseParams.rowkeyColumn, HbaseParams.rowkeyColumn)
            StepSchemaService.formatCommonConfig(values, HbaseParams.hbaseExtraConfig, HbaseParams.hbaseExtraConfig)
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
        <ProFormText
          name={HbaseParams.zookeeperQuorum}
          label={intl.formatMessage({id: 'pages.project.di.step.hbase.zookeeperQuorum'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={HbaseParams.table}
          label={intl.formatMessage({id: 'pages.project.di.step.hbase.table'})}
          rules={[{required: true}]}
        />
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.hbase.familyName'})}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonConfigItem data={HbaseParams.familyName}/>
        </ProFormGroup>
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.hbase.rowkeyColumn'})}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonListItem data={HbaseParams.rowkeyColumn}/>
        </ProFormGroup>
        <ProFormText
          name={HbaseParams.rowkeyDelimiter}
          label={intl.formatMessage({id: 'pages.project.di.step.hbase.rowkeyDelimiter'})}
        />
        <ProFormText
          name={HbaseParams.versionColumn}
          label={intl.formatMessage({id: 'pages.project.di.step.hbase.versionColumn'})}
        />
        <ProFormSelect
          name={HbaseParams.nullMode}
          label={intl.formatMessage({id: 'pages.project.di.step.hbase.nullMode'})}
          initialValue={"skip"}
          options={["skip", "empty"]}
        />
        <ProFormSwitch
          name={HbaseParams.walWrite}
          label={intl.formatMessage({id: 'pages.project.di.step.hbase.walWrite'})}
        />
        <ProFormDigit
          name={HbaseParams.writeBufferSize}
          label={intl.formatMessage({id: 'pages.project.di.step.hbase.writeBufferSize'})}
          initialValue={1024 * 1024 * 8}
          fieldProps={{
            min: 1,
            step: 1024 * 1024
          }}
        />
        <ProFormSelect
          name={HbaseParams.encoding}
          label={intl.formatMessage({id: 'pages.project.di.step.hbase.encoding'})}
          initialValue={"utf8"}
          options={["utf8", "gbk"]}
        />

        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.hbase.hbaseExtraConfig'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.hbase.hbaseExtraConfig.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonConfigItem data={HbaseParams.hbaseExtraConfig}/>
        </ProFormGroup>
      </DrawerForm>
    </XFlow>
  );
};

export default SinkHbaseStepForm;
