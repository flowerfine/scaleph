import { DICT_TYPE } from '@/constant';
import { DictDataService } from '@/services/admin/dictData.service';
import {
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormSelect,
  ProFormText,
} from '@ant-design/pro-components';
import { Divider } from 'antd';
import { useState } from 'react';

const FaultTolerance: React.FC = () => {
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
          Fault Tolerance
        </span>
      </Divider>
      {visible && (
        <>
          <ProFormSelect
            name="strategy"
            label={'restart-strategy'}
            showSearch={true}
            request={() => DictDataService.listDictDataByType(DICT_TYPE.flinkRestartStrategy)}
          />

          <ProFormDependency name={['strategy']}>
            {({ strategy }) => {
              if (strategy == 'fixeddelay') {
                return (
                  <ProFormGroup>
                    <ProFormDigit
                      name="restart-strategy.fixed-delay.attempts"
                      label="restart-strategy.fixed-delay.attempts"
                      min={1}
                      fieldProps={{ precision: 0 }}
                    />
                    <ProFormText
                      name="restart-strategy.fixed-delay.delay"
                      label={'restart-strategy.fixed-delay.delay'}
                    />
                  </ProFormGroup>
                );
              }
              if (strategy == 'failurerate') {
                return (
                  <ProFormGroup>
                    <ProFormText
                      name="restart-strategy.failure-rate.delay"
                      label={'restart-strategy.failure-rate.delay'}
                    />
                    <ProFormText
                      name="restart-strategy.failure-rate.failure-rate-interval"
                      label={'restart-strategy.failure-rate.failure-rate-interval'}
                    />
                    <ProFormText
                      name="restart-strategy.failure-rate.max-failures-per-interval"
                      label={'restart-strategy.failure-rate.max-failures-per-interval'}
                    />
                  </ProFormGroup>
                );
              }
              if (strategy == 'exponentialdelay') {
                return (
                  <ProFormGroup>
                    <ProFormText
                      name="restart-strategy.exponential-delay.initial-backoff"
                      label={'restart-strategy.exponential-delay.initial-backoff'}
                    />
                    <ProFormText
                      name="restart-strategy.exponential-delay.backoff-multiplier"
                      label={'restart-strategy.exponential-delay.backoff-multiplier'}
                    />
                    <ProFormText
                      name="restart-strategy.exponential-delay.max-backoff"
                      label={'restart-strategy.exponential-delay.max-backoff'}
                    />
                    <ProFormText
                      name="restart-strategy.exponential-delay.reset-backoff-threshold"
                      label={'restart-strategy.exponential-delay.reset-backoff-threshold'}
                    />
                    <ProFormText
                      name="restart-strategy.exponential-delay.jitter-factor"
                      label={'restart-strategy.exponential-delay.jitter-factor'}
                    />
                  </ProFormGroup>
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

export default FaultTolerance;
