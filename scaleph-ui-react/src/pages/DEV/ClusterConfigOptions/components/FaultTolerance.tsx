import {
  ProCard,
  ProFormDependency,
  ProFormDigit,
  ProFormGroup,
  ProFormSelect,
  ProFormText
} from "@ant-design/pro-components";
import {listDictDataByType} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constant";

const FaultTolerance: React.FC = () => {
  return (<ProCard
    title={"Fault Tolerance"}
    headerBordered
    collapsible={true}>
    <ProFormGroup>
      <ProFormSelect
        name="strategy"
        label={"restart-strategy"}
        colProps={{span: 10, offset: 1}}
        showSearch={true}
        request={() => listDictDataByType(DICT_TYPE.flinkRestartStrategy)}
      />

      <ProFormDependency name={['strategy']}>
        {({strategy}) => {
          if (strategy == 'fixeddelay') {
            return (
              <ProFormGroup>
                <ProFormDigit
                  name="restart-strategy.fixed-delay.attempts"
                  label="restart-strategy.fixed-delay.attempts"
                  min={1}
                  fieldProps={{precision: 0}}
                  colProps={{span: 10, offset: 1}}
                />
                <ProFormText
                  name="restart-strategy.fixed-delay.delay"
                  label={'restart-strategy.fixed-delay.delay'}
                  colProps={{span: 10, offset: 1}}
                />
              </ProFormGroup>
            )
          }
          if (strategy == 'failurerate') {
            return (
              <ProFormGroup>
                <ProFormText
                  name="restart-strategy.failure-rate.delay"
                  label={'restart-strategy.failure-rate.delay'}
                  colProps={{span: 10, offset: 1}}
                />
                <ProFormText
                  name="restart-strategy.failure-rate.failure-rate-interval"
                  label={'restart-strategy.failure-rate.failure-rate-interval'}
                  colProps={{span: 10, offset: 1}}
                />
                <ProFormText
                  name="restart-strategy.failure-rate.max-failures-per-interval"
                  label={'restart-strategy.failure-rate.max-failures-per-interval'}
                  colProps={{span: 10, offset: 1}}
                />

              </ProFormGroup>
            )
          }
          if (strategy == 'exponentialdelay') {
            return (
              <ProFormGroup>
                <ProFormText
                  name="restart-strategy.exponential-delay.initial-backoff"
                  label={'restart-strategy.exponential-delay.initial-backoff'}
                  colProps={{span: 10, offset: 1}}
                />
                <ProFormText
                  name="restart-strategy.exponential-delay.backoff-multiplier"
                  label={'restart-strategy.exponential-delay.backoff-multiplier'}
                  colProps={{span: 10, offset: 1}}
                />
                <ProFormText
                  name="restart-strategy.exponential-delay.max-backoff"
                  label={'restart-strategy.exponential-delay.max-backoff'}
                  colProps={{span: 10, offset: 1}}
                />
                <ProFormText
                  name="restart-strategy.exponential-delay.reset-backoff-threshold"
                  label={'restart-strategy.exponential-delay.reset-backoff-threshold'}
                  colProps={{span: 10, offset: 1}}
                />
                <ProFormText
                  name="restart-strategy.exponential-delay.jitter-factor"
                  label={'restart-strategy.exponential-delay.jitter-factor'}
                  colProps={{span: 10, offset: 1}}
                />
              </ProFormGroup>
            )
          }
          return <ProFormGroup/>;
        }}
      </ProFormDependency>
    </ProFormGroup>
  </ProCard>);
}

export default FaultTolerance;
