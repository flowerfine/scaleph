import React, {useEffect} from 'react';
import {Form} from 'antd';
import {InfoCircleOutlined} from "@ant-design/icons";
import {DrawerForm, ProFormDigit, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {DatahubParams, STEP_ATTR_TYPE} from '../constant';
import DataSourceItem from "../dataSource";

const SinkDatahubStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
        <DataSourceItem dataSource={'DataHub'}/>
        <ProFormText
          name={DatahubParams.project}
          label={intl.formatMessage({id: 'pages.project.di.step.datahub.project'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={DatahubParams.topic}
          label={intl.formatMessage({id: 'pages.project.di.step.datahub.topic'})}
          rules={[{required: true}]}
        />
        <ProFormDigit
          name={DatahubParams.timeout}
          label={intl.formatMessage({id: 'pages.project.di.step.datahub.timeout'})}
          initialValue={1000}
          fieldProps={{
            step: 1000,
            min: 0,
          }}
        />
        <ProFormDigit
          name={DatahubParams.retryTimes}
          label={intl.formatMessage({id: 'pages.project.di.step.datahub.retryTimes'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.datahub.retryTimes.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          initialValue={0}
          fieldProps={{
            min: 0,
          }}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SinkDatahubStepForm;
