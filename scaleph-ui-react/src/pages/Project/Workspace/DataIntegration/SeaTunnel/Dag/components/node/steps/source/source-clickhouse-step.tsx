import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {DrawerForm, ProFormGroup, ProFormText, ProFormTextArea,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {ClickHouseParams, STEP_ATTR_TYPE} from '../constant';
import DataSourceItem from "../dataSource";
import CommonConfigItem
  from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/config/commonConfig";

const SourceClickHouseStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
        <ProFormTextArea
          name={ClickHouseParams.sql}
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.sql'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={ClickHouseParams.serverTimeZone}
          label={intl.formatMessage({id: 'pages.project.di.step.clickhosue.serverTimeZone'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.clickhosue.serverTimeZone.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
        />
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

export default SourceClickHouseStepForm;
