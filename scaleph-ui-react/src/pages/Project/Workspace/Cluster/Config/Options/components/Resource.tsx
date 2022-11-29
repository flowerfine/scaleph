import { ProFormText } from '@ant-design/pro-components';
import { Col, Divider, Row } from 'antd';
import { useState } from 'react';

const Resource: React.FC = () => {
  const [visible, setVisible] = useState<boolean>(false);
  return (
    <>
      <Divider orientation="left">
        <span
          onClick={() => {
            setVisible(visible ? false : true);
          }}
          style={{ cursor: 'pointer' }}
        >
          Resource Configuration
        </span>
      </Divider>
      {visible && (
        <>
          <Row gutter={[12, 12]}>
            <Col span={12}>
              <ProFormText
                name="jobmanager.memory.process.size"
                label={'jobmanager.memory.process.size'}
              />
            </Col>
            <Col span={12}>
              <ProFormText
                name="jobmanager.memory.flink.size"
                label={'jobmanager.memory.flink.size'}
              />
            </Col>
          </Row>
          <Row gutter={[12, 12]}>
            <Col span={12}>
              <ProFormText
                name="taskmanager.memory.process.size"
                label={'taskmanager.memory.process.size'}
              />
            </Col>
            <Col span={12}>
              <ProFormText
                name="taskmanager.memory.flink.size"
                label={'taskmanager.memory.flink.size'}
              />
            </Col>
          </Row>
        </>
      )}
    </>
  );
};

export default Resource;
