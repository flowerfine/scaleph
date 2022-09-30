import {NsGraph} from "@antv/xflow";
import {ModalFormProps} from '@/app.d';
import {STEP_ATTR_TYPE} from "@/pages/DI/DiJobFlow/Dag/constant";
import {JobService} from "@/services/project/job.service";
import {Col, Form, Input, InputNumber, message, Modal, Row} from "antd";
import {DiJob} from "@/services/project/typings";
import {getIntl, getLocale} from "umi";
import {InfoCircleOutlined} from "@ant-design/icons";
import {useEffect} from "react";


const SourceFakeStepForm: React.FC<ModalFormProps<{
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

    form.setFieldValue(STEP_ATTR_TYPE.stepTitle, nodeInfo.label);
    JobService.listStepAttr(jobInfo.id + '', nodeInfo.id).then((resp) => {
      let stepAttrMap: Map<string, string> = new Map();
      resp.map((step) => {
        stepAttrMap.set(step.stepAttrKey, step.stepAttrValue);
      });
      form.setFieldValue(STEP_ATTR_TYPE.schema, stepAttrMap.get(STEP_ATTR_TYPE.schema));
      form.setFieldValue(STEP_ATTR_TYPE.rowNum, stepAttrMap.get(STEP_ATTR_TYPE.rowNum));
    });
  }, []);

  return (<Modal
    open={visible}
    title={nodeInfo.data.displayName}
    width={780}
    bodyStyle={{overflowY: 'scroll', maxHeight: '640px'}}
    destroyOnClose={true}
    onCancel={onCancel}
    onOk={() => {
      form.validateFields().then((values) => {
        let map: Map<string, string> = new Map();
        map.set(STEP_ATTR_TYPE.jobId, jobInfo.id + '');
        map.set(STEP_ATTR_TYPE.jobGraph, JSON.stringify(jobGraph));
        map.set(STEP_ATTR_TYPE.stepCode, nodeInfo.id);
        map.set(STEP_ATTR_TYPE.stepTitle, values[STEP_ATTR_TYPE.stepTitle]);
        map.set(STEP_ATTR_TYPE.schema, values[STEP_ATTR_TYPE.schema]);
        map.set(STEP_ATTR_TYPE.rowNum, values[STEP_ATTR_TYPE.rowNum]);
        JobService.saveStepAttr(map).then((resp) => {
          if (resp.success) {
            message.success(intl.formatMessage({id: 'app.common.operate.success'}));
            onCancel();
            onOK ? onOK() : null;
          }
        });
      });
    }}
  >

    <Form form={form} layout="vertical">
      <Form.Item
        name={STEP_ATTR_TYPE.stepTitle}
        label={intl.formatMessage({ id: 'pages.project.di.step.stepTitle' })}
        rules={[{ required: true }, { max: 120 }]}
      >
        <Input />
      </Form.Item>
    </Form>

    <Form form={form} layout="vertical">
    <Row gutter={[12, 12]}>
        <Col span={19}>
          <Form.Item
            name={STEP_ATTR_TYPE.schema}
            label={intl.formatMessage({ id: 'pages.project.di.step.schema' })}
            rules={[{ required: true }]}
            tooltip={{
              title: intl.formatMessage({ id: 'pages.project.di.step.fake.schema.tooltip' }),
              icon: <InfoCircleOutlined />,
            }}
          >
            <Input.TextArea rows={8}></Input.TextArea>
          </Form.Item>
        </Col>

    </Row>
    </Form>

    <Form form={form} layout="vertical">
    <Form.Item
      name={STEP_ATTR_TYPE.rowNum}
      label={intl.formatMessage({ id: 'pages.project.di.step.rowNum' })}
    >
      <InputNumber defaultValue={10} step={100} style={{ width: '100%' }} />
    </Form.Item>
    </Form>

  </Modal>);
}

export default SourceFakeStepForm;
