import React, {useEffect} from 'react';
import {Form} from 'antd';
import {DrawerForm, ProFormDigit, ProFormText,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {StarRocksParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import DataSourceItem from "../dataSource";
import FieldItem from "@/pages/Project/Workspace/DataIntegration/SeaTunnel/Dag/components/node/steps/common/schema/fields";

const SourceStarRocksStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onVisibleChange, onOK}) => {
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
            StepSchemaService.formatSchema(values);
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
        <DataSourceItem dataSource={'StarRocks'}/>
        <ProFormText
          name={StarRocksParams.database}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.database'})}
          rules={[{required: true}]}
        />
        <ProFormText
          name={StarRocksParams.table}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.table'})}
          rules={[{required: true}]}
        />
        <FieldItem/>
        <ProFormText
          name={StarRocksParams.scanFilter}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.scanFilter'})}
        />
        <ProFormDigit
          name={StarRocksParams.scanConnectTimeoutMs}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.scanConnectTimeoutMs'})}
          colProps={{span: 8}}
          initialValue={30000}
          fieldProps={{
            step: 1000,
            min: 1,
          }}
        />
        <ProFormDigit
          name={StarRocksParams.scanQueryTimeoutSec}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.scanQueryTimeoutSec'})}
          colProps={{span: 8}}
          initialValue={3600}
          fieldProps={{
            step: 60,
            min: 1,
          }}
        />
        <ProFormDigit
          name={StarRocksParams.scanKeepAliveMin}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.scanKeepAliveMin'})}
          colProps={{span: 8}}
          initialValue={10}
          fieldProps={{
            step: 1,
            min: 1,
          }}
        />
        <ProFormDigit
          name={StarRocksParams.scanBatchRows}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.scanBatchRows'})}
          colProps={{span: 8}}
          initialValue={1024}
          fieldProps={{
            step: 1024,
            min: 1
          }}
        />
        <ProFormDigit
          name={StarRocksParams.scanMemLimit}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.scanMemLimit'})}
          colProps={{span: 8}}
          initialValue={1024 * 1024 * 1024 * 2}
          fieldProps={{
            step: 1024 * 1024 * 1024,
            min: 1
          }}
        />
        <ProFormDigit
          name={StarRocksParams.requestTabletSize}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.requestTabletSize'})}
          colProps={{span: 8}}
          initialValue={10}
          fieldProps={{
            step: 1,
            min: 1,
          }}
        />
        <ProFormDigit
          name={StarRocksParams.maxRetries}
          label={intl.formatMessage({id: 'pages.project.di.step.starrocks.maxRetries'})}
          initialValue={3}
          fieldProps={{
            step: 1,
            min: 1
          }}
        />
      </DrawerForm>
    </XFlow>
  );
};

export default SourceStarRocksStepForm;
