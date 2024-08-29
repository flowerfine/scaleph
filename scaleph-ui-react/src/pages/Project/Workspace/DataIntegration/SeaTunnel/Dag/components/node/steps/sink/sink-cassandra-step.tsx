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
import {CassandraParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "../helper";
import DataSourceItem from "../dataSource";
import CommonListItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/list";

const SinkCassandraStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatCommonList(values, CassandraParams.fields, CassandraParams.fields);
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
          allowClear={false}
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
        <ProFormText
          name={CassandraParams.table}
          label={intl.formatMessage({id: 'pages.project.di.step.cassandra.table'})}
          rules={[{required: true}]}
        />
        <ProFormSwitch
          name={CassandraParams.asyncWrite}
          label={intl.formatMessage({id: 'pages.project.di.step.cassandra.asyncWrite'})}
          colProps={{span: 8}}
          initialValue={true}
        />
        <ProFormSelect
          name={CassandraParams.batchType}
          label={intl.formatMessage({id: 'pages.project.di.step.cassandra.batchType'})}
          colProps={{span: 8}}
          allowClear={false}
          initialValue={'UNLOGGED'}
          options={['LOGGED', 'UNLOGGED', 'COUNTER']}
        />
        <ProFormDigit
          name={CassandraParams.batchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.cassandra.batchSize'})}
          colProps={{span: 8}}
          initialValue={5000}
          fieldProps={{
            step: 1000,
            min: 0,
          }}
        />
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.cassandra.fields'})}
          tooltip={{
            title: intl.formatMessage({id: 'pages.project.di.step.cassandra.fields.tooltip'}),
            icon: <InfoCircleOutlined/>,
          }}
          collapsible={true}
          defaultCollapsed={true}
        >
          <CommonListItem data={CassandraParams.fields}/>
        </ProFormGroup>
      </DrawerForm>
    </XFlow>
  );
};

export default SinkCassandraStepForm;
