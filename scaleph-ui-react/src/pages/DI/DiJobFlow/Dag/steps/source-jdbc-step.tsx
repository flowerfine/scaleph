import { ModalFormProps } from '@/app.d';
import { NsGraph } from '@antv/xflow';
import { Form, Input, Modal } from 'antd';
import { useEffect } from 'react';
import { FormattedMessage } from 'umi';

const SourceJdbcStepForm: React.FC<
  ModalFormProps<{
    node: NsGraph.INodeConfig;
    graphData: NsGraph.IGraphData;
    graphMeta: NsGraph.IGraphMeta;
  }>
> = ({ data, visible, onCancel }) => {
  const [form] = Form.useForm();
  const nodeInfo = data.node.data;
  useEffect(() => {
    console.log('23452345', nodeInfo);
  }, []);
  return (
    <Modal
      visible={visible}
      title={nodeInfo.label}
      width={580}
      destroyOnClose={true}
      onCancel={onCancel}
      onOk={() => {}}
    >
      <Form form={form} layout="horizontal">
        <Form.Item
          name="name"
          label={<FormattedMessage id="app.common.operate.label"></FormattedMessage>}
          rules={[{ required: true }]}
        >
          <Input />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default SourceJdbcStepForm;
