import React, {useEffect} from 'react';
import {Button, Drawer, Form} from 'antd';
import {ProForm, ProFormDigit, ProFormSwitch, ProFormText} from '@ant-design/pro-components';
import {getIntl, getLocale} from "@umijs/max";
import {Node, XFlow} from '@antv/xflow';
import {ModalFormProps} from '@/typings';
import {SentryParams, STEP_ATTR_TYPE} from '../constant';
import {StepSchemaService} from "@/pages/Project/Workspace/Artifact/DI/DiJobFlow/Dag/node/steps/helper";

const SinkSentryStepForm: React.FC<ModalFormProps<Node>> = ({data, visible, onCancel, onOK}) => {
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
          />
          <ProFormText
            name={SentryParams.dsn}
            label={intl.formatMessage({id: 'pages.project.di.step.sentry.dsn'})}
            rules={[{required: true}]}
          />
          <ProFormText
            name={SentryParams.env}
            label={intl.formatMessage({id: 'pages.project.di.step.sentry.env'})}
            colProps={{span: 12}}
          />
          <ProFormText
            name={SentryParams.release}
            label={intl.formatMessage({id: 'pages.project.di.step.sentry.release'})}
            colProps={{span: 12}}
          />
          <ProFormSwitch
            name={SentryParams.enableExternalConfiguration}
            label={intl.formatMessage({id: 'pages.project.di.step.sentry.enableExternalConfiguration'})}
          />
          <ProFormText
            name={SentryParams.cacheDirPath}
            label={intl.formatMessage({id: 'pages.project.di.step.sentry.cacheDirPath'})}
          />
          <ProFormDigit
            name={SentryParams.maxCacheItems}
            label={intl.formatMessage({id: 'pages.project.di.step.sentry.maxCacheItems'})}
            initialValue={30}
            fieldProps={{
              step: 5,
              min: 0,
            }}
          />
          <ProFormDigit
            name={SentryParams.flushTimeoutMillis}
            label={intl.formatMessage({id: 'pages.project.di.step.sentry.flushTimeoutMillis'})}
            initialValue={15000}
            fieldProps={{
              step: 1000,
              min: 0,
            }}
          />
          <ProFormDigit
            name={SentryParams.maxQueueSize}
            label={intl.formatMessage({id: 'pages.project.di.step.sentry.maxQueueSize'})}
            fieldProps={{
              step: 100,
              min: 0,
            }}
          />
        </ProForm>
      </Drawer>
    </XFlow>
  );
};

export default SinkSentryStepForm;
