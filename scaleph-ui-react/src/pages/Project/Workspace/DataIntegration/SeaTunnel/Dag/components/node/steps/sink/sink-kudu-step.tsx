import React, {useEffect} from 'react';
import {Form} from 'antd';
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
import {KuduParams, STEP_ATTR_TYPE} from '../constant';
import DataSourceItem from "../dataSource";
import {InfoCircleOutlined} from "@ant-design/icons";

const SinkKuduStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
        <DataSourceItem dataSource={'Kudu'}/>
        <ProFormText
          name={KuduParams.tableName}
          label={intl.formatMessage({id: 'pages.project.di.step.kudu.table'})}
          rules={[{required: true}]}
        />
        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.kudu.client'})}
          collapsible={true}
          defaultCollapsed={true}
        >
          <ProFormDigit
            name={KuduParams.clientWorkerCount}
            label={intl.formatMessage({id: 'pages.project.di.step.kudu.clientWorkerCount'})}
            tooltip={{
              title: intl.formatMessage({id: 'pages.project.di.step.kudu.clientWorkerCount.tooltip'}),
              icon: <InfoCircleOutlined/>,
            }}
            colProps={{span: 8}}
            fieldProps={{
              min: 1
            }}
          />
          <ProFormDigit
            name={KuduParams.clientDefaultOperationTimeoutMs}
            label={intl.formatMessage({id: 'pages.project.di.step.kudu.clientDefaultOperationTimeoutMs'})}
            colProps={{span: 8}}
            fieldProps={{
              min: 1,
              step: 1000
            }}
          />
          <ProFormDigit
            name={KuduParams.clientDefaultAdminOperationTimeoutMs}
            label={intl.formatMessage({id: 'pages.project.di.step.kudu.clientDefaultAdminOperationTimeoutMs'})}
            colProps={{span: 8}}
            fieldProps={{
              min: 1,
              step: 1000
            }}
          />
        </ProFormGroup>

        <ProFormGroup
          title={intl.formatMessage({id: 'pages.project.di.step.kudu.write'})}
          collapsible={true}
          defaultCollapsed={true}
        >
          <ProFormSelect
            name={KuduParams.sessionFlushMode}
            label={intl.formatMessage({id: 'pages.project.di.step.kudu.sessionFlushMode'})}
            colProps={{span: 8}}
            allowClear={false}
            initialValue={'AUTO_FLUSH_SYNC'}
            options={["AUTO_FLUSH_SYNC", "AUTO_FLUSH_BACKGROUND", "MANUAL_FLUSH"]}
          />
          <ProFormDigit
            name={KuduParams.batchSize}
            label={intl.formatMessage({id: 'pages.project.di.step.kudu.batchSize'})}
            colProps={{span: 8}}
            fieldProps={{
              min: 1,
              step: 1000
            }}
          />
          <ProFormDigit
            name={KuduParams.bufferFlushInterval}
            label={intl.formatMessage({id: 'pages.project.di.step.kudu.bufferFlushInterval'})}
            colProps={{span: 8}}
            fieldProps={{
              min: 1,
              step: 1000
            }}
          />
          <ProFormSelect
            name={KuduParams.saveMode}
            label={intl.formatMessage({id: 'pages.project.di.step.kudu.savemode'})}
            allowClear={false}
            initialValue={'append'}
            valueEnum={{
              append: {text: 'append'},
              overwrite: {text: 'overwrite'},
            }}
          />
          <ProFormSwitch
            name={KuduParams.ignoreNotFound}
            label={intl.formatMessage({id: 'pages.project.di.step.kudu.ignoreNotFound'})}
            colProps={{span: 12}}
          />
          <ProFormSwitch
            name={KuduParams.ignoreNotDuplicate}
            label={intl.formatMessage({id: 'pages.project.di.step.kudu.ignoreNotDuplicate'})}
            colProps={{span: 12}}
          />
        </ProFormGroup>
      </DrawerForm>
    </XFlow>
  );
};

export default SinkKuduStepForm;
