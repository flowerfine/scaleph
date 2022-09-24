import {Button, Col, Row} from "antd";
import {RollbackOutlined} from "@ant-design/icons";
import {ProDescriptions} from '@ant-design/pro-components';
import {history} from "umi";

const JobDetailWeb: React.FC = () => {

  return (<div>
    <Row>
      <Col>
        <Button onClick={() => history.back()}>
          <RollbackOutlined/>
        </Button>
      </Col>
    </Row>
    <Row>
      <Col span={23}>
        <ProDescriptions
          title="任务信息"
          request={async () => {
            return Promise.resolve({
              success: true,
              data: {
                id: 1,
                name: 'TopSpeedWindowing',
                resourceProvider: 'Native Kubernetes',
                deployMode: 'Application',
                date: '2022-09-23 12:01:35',
              },
            });
          }}
          columns={[
            {
              title: '名称',
              key: 'name',
              dataIndex: 'name',
            },
            {
              title: 'Entry Class',
              key: 'entryClass',
              dataIndex: 'entryClass',
            },
            {
              title: 'version',
              key: 'version',
              dataIndex: 'version',
            },
            {
              title: 'File Name',
              key: 'fileName',
              dataIndex: 'fileName',
            },
            {
              title: 'Flink 版本',
              key: 'flinkVersion',
              dataIndex: 'flinkVersion',
            },
            {
              title: 'Flink Resource Provider',
              key: 'resourceProvider',
              dataIndex: 'resourceProvider',
            },
            {
              title: 'Flink Deploy Mode',
              key: 'deployMode',
              dataIndex: 'deployMode',
            },
            {
              title: '时间',
              key: 'date',
              dataIndex: 'date'
            }
          ]}
          extra={(
            [
              <a target="_blank" rel="noopener noreferrer" key="link">
                链路
              </a>,
              <a target="_blank" rel="noopener noreferrer" key="warning">
                报警
              </a>,
              <a target="_blank" rel="noopener noreferrer" key="view">
                查看
              </a>,
            ]
          )}
        />
      </Col>
    </Row>
  </div>);
}

export default JobDetailWeb;
