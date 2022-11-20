import {ModalFormProps} from '@/app.d';
import {JobService} from '@/services/project/job.service';
import {DiJob} from '@/services/project/typings';
import {ProForm, ProFormDigit, ProFormSwitch, ProFormText} from '@ant-design/pro-components';
import {NsGraph} from '@antv/xflow';
import {Form, message, Modal} from 'antd';
import {useEffect} from 'react';
import {getIntl, getLocale} from 'umi';
import {SentryParams, STEP_ATTR_TYPE} from '../../constant';

const SinkSentryStepForm: React.FC<ModalFormProps<{
  node: NsGraph.INodeConfig;
  graphData: NsGraph.IGraphData;
  graphMeta: NsGraph.IGraphMeta;
}>> = ({data, visible, onCancel, onOK}) => {
  const nodeInfo = data.node.data;
  const jobInfo = data.graphMeta.origin as DiJob;
  const jobGraph = data.graphData;
  const intl = getIntl(getLocale(), true);
  const [form] = Form.useForm();
  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, nodeInfo.data.displayName);
  }, []);

  return (
    <Modal
      open={visible}
      title={nodeInfo.data.displayName}
      width={780}
      bodyStyle={{overflowY: 'scroll', maxHeight: '640px'}}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          let map: Map<string, any> = new Map();
          map.set(STEP_ATTR_TYPE.jobId, jobInfo.id);
          map.set(STEP_ATTR_TYPE.jobGraph, JSON.stringify(jobGraph));
          map.set(STEP_ATTR_TYPE.stepCode, nodeInfo.id);
          map.set(STEP_ATTR_TYPE.stepAttrs, values);
          JobService.saveStepAttr(map).then((resp) => {
            if (resp.success) {
              message.success(intl.formatMessage({id: 'app.common.operate.success'}));
              onCancel();
              onOK ? onOK(values) : null;
            }
          });
        });
      }}
    >
      <ProForm form={form} initialValues={nodeInfo.data.attrs} grid={true} submitter={false}>
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
            min: 0
          }}
        />
        <ProFormDigit
          name={SentryParams.flushTimeoutMillis}
          label={intl.formatMessage({id: 'pages.project.di.step.sentry.flushTimeoutMillis'})}
          initialValue={15000}
          fieldProps={{
            step: 1000,
            min: 0
          }}
        />
        <ProFormDigit
          name={SentryParams.maxQueueSize}
          label={intl.formatMessage({id: 'pages.project.di.step.sentry.maxQueueSize'})}
          fieldProps={{
            step: 100,
            min: 0
          }}
        />
      </ProForm>
    </Modal>
  );
};

export default SinkSentryStepForm;
