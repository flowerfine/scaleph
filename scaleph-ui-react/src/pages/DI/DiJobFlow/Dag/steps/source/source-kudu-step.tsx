import { ModalFormProps } from '@/app.d';
import { JobService } from '@/services/project/job.service';
import { DiJob } from '@/services/project/typings';
import { ProForm, ProFormDigit, ProFormText, ProFormTextArea } from '@ant-design/pro-components';
import { NsGraph } from '@antv/xflow';
import { Form, message, Modal } from 'antd';
import { useEffect } from 'react';
import { getIntl, getLocale } from 'umi';
import { KuduParams, STEP_ATTR_TYPE } from '../../constant';

const SourceKuduStepForm: React.FC<
  ModalFormProps<{
    node: NsGraph.INodeConfig;
    graphData: NsGraph.IGraphData;
    graphMeta: NsGraph.IGraphMeta;
  }>
> = ({ data, visible, onCancel, onOK }) => {
  const nodeInfo = data.node.data;
  const jobInfo = data.graphMeta.origin as DiJob;
  const jobGraph = data.graphData;
  const intl = getIntl(getLocale(), true);
  const [form] = Form.useForm();
  useEffect(() => {
    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, nodeInfo.label);
  }, []);

  return (
    <Modal
      open={visible}
      title={nodeInfo.data.displayName}
      width={780}
      bodyStyle={{ overflowY: 'scroll', maxHeight: '640px' }}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {
        form.validateFields().then((values) => {
          let map: Map<string, string> = new Map();
          map.set(STEP_ATTR_TYPE.jobId, jobInfo.id + '');
          map.set(STEP_ATTR_TYPE.jobGraph, JSON.stringify(jobGraph));
          map.set(STEP_ATTR_TYPE.stepCode, nodeInfo.id);
          map.set(STEP_ATTR_TYPE.stepAttrs, values);
          JobService.saveStepAttr(map).then((resp) => {
            if (resp.success) {
              message.success(intl.formatMessage({ id: 'app.common.operate.success' }));
              onCancel();
              onOK ? onOK() : null;
            }
          });
        });
      }}
    >
      <ProForm form={form} initialValues={nodeInfo.data.attrs} grid={true} submitter={false}>
        <ProFormText
          name={STEP_ATTR_TYPE.stepTitle}
          label={intl.formatMessage({ id: 'pages.project.di.step.stepTitle' })}
          rules={[{ required: true }, { max: 120 }]}
        />
        <ProFormText
          name={KuduParams.kuduMaster}
          label={intl.formatMessage({ id: 'pages.project.di.step.kudu.master' })}
          rules={[{ required: true }]}
          colProps={{ span: 12 }}
        />
        <ProFormText
          name={KuduParams.kuduTable}
          label={intl.formatMessage({ id: 'pages.project.di.step.kudu.table' })}
          rules={[{ required: true }]}
          colProps={{ span: 12 }}
        />
        <ProFormTextArea
          name={KuduParams.columnsList}
          label={intl.formatMessage({ id: 'pages.project.di.step.kudu.columnsList' })}
          rules={[{ required: true }]}
        />
      </ProForm>
    </Modal>
  );
};

export default SourceKuduStepForm;
