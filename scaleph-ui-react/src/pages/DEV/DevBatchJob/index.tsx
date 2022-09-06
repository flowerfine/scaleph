import {ProForm} from "@ant-design/pro-components";
import {Col, Form, Input, Row, Space} from "antd";
import BasicForm from "@/pages/DEV/DevBatchJob/components/BasicForm";

const DevBatchJob: React.FC = () => {
  return (<div>
    <ProForm
      layout={"horizontal"}
      grid={true}
      rowProps={{gutter: [16, 0]}}
      submitter={{
        render: (props, doms) => {
          return (
            <Row>
              <Col span={14} offset={4}>
                <Space>{doms}</Space>
              </Col>
            </Row>
          )
        },
      }}
    >
      <BasicForm></BasicForm>
      <ProForm.Group title={"State & Checkpoints & Savepoints"} collapsible={true} defaultCollapsed={true}>
        <Form.Item name="switch" label="Switch" valuePropName="checked">
          <Input/>
        </Form.Item>
      </ProForm.Group>
    </ProForm>
  </div>);
}

export default DevBatchJob;
