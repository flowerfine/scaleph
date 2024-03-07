import React, {useEffect} from 'react';
import {Form} from 'antd';
import {DrawerForm, ProFormDigit, ProFormSelect, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {RedisParams, STEP_ATTR_TYPE} from '../constant';
import DataSourceItem from "../dataSource";

const SinkRedisStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
        <DataSourceItem dataSource={'Redis'}/>
        <ProFormDigit
          name={RedisParams.dbNum}
          label={intl.formatMessage({id: 'pages.project.di.step.redis.dbNum'})}
          initialValue={0}
          fieldProps={{
            min: 0
          }}
        />
        <ProFormText
          name={RedisParams.key}
          label={intl.formatMessage({id: 'pages.project.di.step.redis.key'})}
          rules={[{required: true}]}
        />
        <ProFormDigit
          name={RedisParams.expire}
          label={intl.formatMessage({id: 'pages.project.di.step.redis.expire'})}
          initialValue={-1}
          fieldProps={{
            min: -1
          }}
        />
        <ProFormSelect
          name={RedisParams.dataType}
          label={intl.formatMessage({id: 'pages.project.di.step.redis.dataType'})}
          rules={[{required: true}]}
          valueEnum={{
            key: 'key',
            hash: 'hash',
            listDataSetType: 'list',
            set: 'set',
            zset: 'zset',
          }}
        />
        <ProFormSelect
          name={RedisParams.format}
          label={intl.formatMessage({id: 'pages.project.di.step.redis.format'})}
          allowClear={false}
          initialValue={'json'}
          valueEnum={{
            json: 'json',
            text: 'text',
          }}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SinkRedisStepForm;
