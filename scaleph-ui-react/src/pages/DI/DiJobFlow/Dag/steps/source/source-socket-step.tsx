import { ModalFormProps } from '@/app.d';
import { JobService } from '@/services/project/job.service';
import { DiJob } from '@/services/project/typings';
import { NsGraph } from '@antv/xflow';
import { Col, Form, Input, InputNumber, message, Modal, Row } from 'antd';
import { useEffect } from 'react';
import { getIntl, getLocale } from 'umi';
import { STEP_ATTR_TYPE } from '../../constant';

const SourceSocketStepForm: React.FC<
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
  const defaultFormValue = { port: 9999 };
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
      <Form
        form={form}
        layout="vertical"
        initialValues={{ ...defaultFormValue, ...nodeInfo.data.attrs }}
      >
        <Form.Item
          name={STEP_ATTR_TYPE.stepTitle}
          label={intl.formatMessage({ id: 'pages.project.di.step.stepTitle' })}
          rules={[{ required: true }, { max: 120 }]}
        >
          <Input />
        </Form.Item>
        <Row gutter={[12, 12]}>
          <Col span={12}>
            <Form.Item
              name={STEP_ATTR_TYPE.host}
              label={intl.formatMessage({ id: 'pages.project.di.step.host' })}
              rules={[{ required: true }]}
            >
              <Input />
            </Form.Item>
          </Col>
          <Col span={12}>
            <Form.Item
              name={STEP_ATTR_TYPE.port}
              label={intl.formatMessage({ id: 'pages.project.di.step.port' })}
              rules={[{ required: true }]}
            >
              <InputNumber style={{ width: '100%' }} />
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default SourceSocketStepForm;
