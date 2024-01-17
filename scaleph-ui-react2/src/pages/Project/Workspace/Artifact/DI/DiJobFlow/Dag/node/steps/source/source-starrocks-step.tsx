import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {ProForm, ProFormDigit, ProFormText,} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {StarRocksParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from '../helper';
import DataSourceItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/dataSource";
import FieldItem from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/fields";

const SourceStarRocksStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
  const intl = getIntl(getLocale());
  const [form] = Form.useForm();

  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, data.data.label);
  }, []);

  return (
    <XFlow>
      <Drawer
        open={visible}
        title={data.data.label}
        width={780}
        bodyStyle={{overflowY: 'scroll'}}
        destroyOnClose={true}
        onClose={onCancel}
        extra={
          <Button
            type="primary"
            onClick={() => {
              form.validateFields().then((values) => {
                StepSchemaService.formatSchema(values);
                if (onOK) {
                  onOK(values);
                }
              });
            }}
          >
            {intl.formatMessage({id: 'app.common.operate.confirm.label'})}
          </Button>
        }
      >
        <ProForm form={form} initialValues={data.data.attrs} grid={true} submitter={false}>
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
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SourceStarRocksStepForm;
