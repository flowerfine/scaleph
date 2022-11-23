import { DICT_TYPE } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import { ProFormSelect, ProFormSwitch, ProFormText } from '@ant-design/pro-components';
import { Col, Divider, Row } from 'antd';
import { useState } from 'react';

const State: React.FC = () => {
  const [visible, setVisible] = useState<boolean>(true);
  return (
    <>
      <Divider orientation="left">
        <span
          onClick={() => {
            setVisible(visible ? false : true);
          }}
          style={{ cursor: 'pointer' }}
        >
          State & Checkpoints & Savepoints
        </span>
      </Divider>
      {visible && (
        <>
          <ProFormSelect
            name="state.backend"
            label={'state.backend'}
            showSearch={true}
            request={() => DictDataService.listDictDataByType(DICT_TYPE.flinkStateBackend)}
          />
          <Row gutter={[12, 12]}>
            <Col span={12}>
              <ProFormText name="state.savepoints.dir" label={'state.savepoints.dir'} />
            </Col>
            <Col span={12}>
              <ProFormText name="state.checkpoints.dir" label={'state.checkpoints.dir'} />
            </Col>
          </Row>
          <ProFormSelect
            name="execution.checkpointing.mode"
            label={'execution.checkpointing.mode'}
            showSearch={true}
            request={() => DictDataService.listDictDataByType(DICT_TYPE.flinkSemantic)}
          />
          <Row gutter={[12, 12]}>
            <Col span={12}>
              <ProFormSwitch
                name="execution.checkpointing.unaligned"
                label={'execution.checkpointing.unaligned'}
              />
            </Col>
            <Col span={12}>
              <ProFormText
                name="execution checkpointing interval"
                label={'execution checkpointing interval'}
              />
            </Col>
          </Row>
          <ProFormSelect
            name="execution.checkpointing.externalized-checkpoint-retention"
            label={'execution.checkpointing.externalized-checkpoint-retention'}
            showSearch={true}
            request={() => DictDataService.listDictDataByType(DICT_TYPE.flinkCheckpointRetain)}
          />
        </>
      )}
    </>
  );
};

export default State;
