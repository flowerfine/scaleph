import React, {useEffect} from 'react';
import {Form} from 'antd';
import {DrawerForm, ProFormSelect, ProFormText, ProFormTextArea,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {CassandraParams, STEP_ATTR_TYPE} from '../constant';
import DataSourceItem from "../dataSource";

const SourceCassandraStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
        <DataSourceItem dataSource={'Cassandra'}/>
        <ProFormSelect
          name={CassandraParams.consistencyLevel}
          label={intl.formatMessage({id: 'pages.project.di.step.cassandra.consistencyLevel'})}
          initialValue={'LOCAL_ONE'}
          options={[
            'ANY',
            'ONE',
            'TWO',
            'THREE',
            'QUORUM',
            'ALL',
            'LOCAL_ONE',
            'LOCAL_QUORUM',
            'EACH_QUORUM',
            'SERIAL',
            'LOCAL_SERIAL',
          ]}
        />
        <ProFormTextArea
          name={CassandraParams.cql}
          label={intl.formatMessage({id: 'pages.project.di.step.cassandra.cql'})}
          rules={[{required: true}]}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SourceCassandraStepForm;
