import { DICT_TYPE } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import {
  ProFormDependency,
  ProFormGroup,
  ProFormSelect,
  ProFormText,
} from '@ant-design/pro-components';
import { Col, Divider, Row } from 'antd';
import { useState } from 'react';

const HighAvailability: React.FC = () => {
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
          High Availability
        </span>
      </Divider>
      {visible && (
        <>
          <ProFormSelect
            name="ha"
            label={'high-availability'}
            showSearch={true}
            request={() => DictDataService.listDictDataByType(DICT_TYPE.flinkHA)}
          />
          <Row gutter={[12, 12]}>
            <Col span={12}>
              <ProFormText
                name="high-availability.storageDir"
                label={'high-availability.storageDir'}
              />
            </Col>
            <Col span={12}>
              <ProFormText
                name="high-availability.cluster-id"
                label={'high-availability.cluster-id'}
              />
            </Col>
          </Row>

          <ProFormDependency name={['ha']}>
            {({ ha }) => {
              if (ha == 'zookeeper') {
                return (
                  <Row gutter={[12, 12]}>
                    <Col span={12}>
                      <ProFormText
                        name="high-availability.zookeeper.path.root"
                        label={'high-availability.zookeeper.path.root'}
                      />
                    </Col>
                    <Col span={12}>
                      <ProFormText
                        name="high-availability.zookeeper.quorum"
                        label={'high-availability.zookeeper.quorum'}
                      />
                    </Col>
                  </Row>
                );
              }
              return <ProFormGroup />;
            }}
          </ProFormDependency>
        </>
      )}
    </>
  );
};

export default HighAvailability;
