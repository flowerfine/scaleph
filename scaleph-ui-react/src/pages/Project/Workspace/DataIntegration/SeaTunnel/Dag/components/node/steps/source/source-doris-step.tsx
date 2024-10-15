import React, {useEffect} from 'react';
import {Form} from 'antd';
import {DrawerForm, ProFormDigit, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {DorisParams, STEP_ATTR_TYPE} from '../constant';
import DataSourceItem from "../dataSource";

const SourceDorisStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
          name={DorisParams.dorisReadField}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.dorisReadField'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.doris.dorisReadField.placeholder'})}
          colProps={{span: 12}}
        />
        <ProFormText
          name={DorisParams.dorisFiterQuery}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.dorisFiterQuery'})}
          placeholder={intl.formatMessage({id: 'pages.project.di.step.doris.dorisFiterQuery.placeholder'})}
          colProps={{span: 12}}
        />
        <ProFormDigit
          name={DorisParams.dorisBatchSize}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.dorisBatchSize'})}
          initialValue={1024}
          fieldProps={{
            step: 1024,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.dorisRequestConnectTimeoutMs}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.dorisRequestConnectTimeoutMs'})}
          colProps={{span: 8}}
          initialValue={30000}
          fieldProps={{
            step: 1000,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.dorisRequestQueryTimeoutS}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.dorisRequestQueryTimeoutS'})}
          colProps={{span: 8}}
          initialValue={3600}
          fieldProps={{
            step: 1,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.dorisRequestReadTimeoutMs}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.dorisRequestReadTimeoutMs'})}
          colProps={{span: 8}}
          initialValue={30000}
          fieldProps={{
            step: 1,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.dorisExecMemLimit}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.dorisExecMemLimit'})}
          initialValue={1024 * 1024 * 1024 * 2}
          fieldProps={{
            step: 1024 * 1024 * 1024,
            min: 1
          }}
        />
        <ProFormDigit
          name={DorisParams.dorisRequestRetries}
          label={intl.formatMessage({id: 'pages.project.di.step.doris.dorisRequestRetries'})}
          initialValue={3}
          fieldProps={{
            step: 3,
            min: 1
          }}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SourceDorisStepForm;
