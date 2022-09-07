import {ProForm} from "@ant-design/pro-components";
import {Col, Row, Space} from "antd";
import BasicForm from "@/pages/DEV/DevBatchJob/components/BasicForm";
import StateForm from "@/pages/DEV/DevBatchJob/components/StateForm";
import FaultToleranceForm from "@/pages/DEV/DevBatchJob/components/FaultToleranceForm";
import HAForm from "@/pages/DEV/DevBatchJob/components/HAForm";
import MemoryForm from "@/pages/DEV/DevBatchJob/components/MemoryForm";

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
              <Col span={14}>
                <Space>{doms}</Space>
              </Col>
            </Row>
          )
        },
      }}
    >
      <BasicForm></BasicForm>
      <StateForm></StateForm>
      <FaultToleranceForm></FaultToleranceForm>
      <HAForm></HAForm>
      <MemoryForm></MemoryForm>
    </ProForm>
  </div>);
}

export default DevBatchJob;
