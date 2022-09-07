import {useIntl} from "@@/exports";
import {ProForm, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {listDictDataByType} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constant";

const FaultToleranceForm: React.FC = () => {
  const intl = useIntl();

  return (<div>
    <ProForm.Group
      title={"Fault Tolerance"}
      collapsible={true}
      defaultCollapsed={true}
    >
      <ProFormSelect
        name="restart-strategy"
        label={"restart-strategy"}
        colProps={{span: 8, offset: 2}}
        rules={[{required: true}]}
        showSearch={true}
        allowClear={true}
        request={() => listDictDataByType(DICT_TYPE.flinkRestartStrategy)}
      />
      <ProFormText
        name="restart-strategy.fixed-delay.attempts"
        label={'restart-strategy.fixed-delay.attempts'}
        colProps={{span: 8, offset: 2}}
        rules={[
          {required: true},
          {max: 30},
          {
            pattern: /^[\w\s_]+$/,
            message: intl.formatMessage({id: 'app.common.validate.characterWord3'}),
          },
        ]}
      />
      <ProFormText
        name="restart-strategy.fixed-delay.delay"
        label={'restart-strategy.fixed-delay.delay'}
        colProps={{span: 8, offset: 2}}
        rules={[
          {required: true},
          {max: 30},
          {
            pattern: /^[\w\s_]+$/,
            message: intl.formatMessage({id: 'app.common.validate.characterWord3'}),
          },
        ]}
      />
      <ProFormText
        name="restart-strategy.failure-rate.failure-rate-interval"
        label={'restart-strategy.failure-rate.failure-rate-interval'}
        colProps={{span: 8, offset: 2}}
        rules={[
          {required: true},
          {max: 30},
          {
            pattern: /^[\w\s_]+$/,
            message: intl.formatMessage({id: 'app.common.validate.characterWord3'}),
          },
        ]}
      />
      <ProFormText
        name="restart-strategy.failure-rate.delay"
        label={'restart-strategy.failure-rate.delay'}
        colProps={{span: 8, offset: 2}}
        rules={[
          {required: true},
          {max: 30},
          {
            pattern: /^[\w\s_]+$/,
            message: intl.formatMessage({id: 'app.common.validate.characterWord3'}),
          },
        ]}
      />
      <ProFormText
        name="restart-strategy.failure-rate.max-failures-per-interval"
        label={'restart-strategy.failure-rate.max-failures-per-interval'}
        colProps={{span: 8, offset: 2}}
        rules={[
          {required: true},
          {max: 30},
          {
            pattern: /^[\w\s_]+$/,
            message: intl.formatMessage({id: 'app.common.validate.characterWord3'}),
          },
        ]}
      />
      <ProFormText
        name="restart-strategy.exponential-delay.initial-backoff"
        label={'restart-strategy.exponential-delay.initial-backoff'}
        colProps={{span: 8, offset: 2}}
        rules={[
          {required: true},
          {max: 30},
          {
            pattern: /^[\w\s_]+$/,
            message: intl.formatMessage({id: 'app.common.validate.characterWord3'}),
          },
        ]}
      />
      <ProFormText
        name="restart-strategy.exponential-delay.backoff-multiplier"
        label={'restart-strategy.exponential-delay.backoff-multiplier'}
        colProps={{span: 8, offset: 2}}
        rules={[
          {required: true},
          {max: 30},
          {
            pattern: /^[\w\s_]+$/,
            message: intl.formatMessage({id: 'app.common.validate.characterWord3'}),
          },
        ]}
      />
      <ProFormText
        name="restart-strategy.exponential-delay.max-backoff"
        label={'restart-strategy.exponential-delay.max-backoff'}
        colProps={{span: 8, offset: 2}}
        rules={[
          {required: true},
          {max: 30},
          {
            pattern: /^[\w\s_]+$/,
            message: intl.formatMessage({id: 'app.common.validate.characterWord3'}),
          },
        ]}
      />
      <ProFormText
        name="restart-strategy.exponential-delay.reset-backoff-threshold"
        label={'restart-strategy.exponential-delay.reset-backoff-threshold'}
        colProps={{span: 8, offset: 2}}
        rules={[
          {required: true},
          {max: 30},
          {
            pattern: /^[\w\s_]+$/,
            message: intl.formatMessage({id: 'app.common.validate.characterWord3'}),
          },
        ]}
      />
      <ProFormText
        name="restart-strategy.exponential-delay.jitter-factor"
        label={'restart-strategy.exponential-delay.jitter-factor'}
        colProps={{span: 8, offset: 2}}
        rules={[
          {required: true},
          {max: 30},
          {
            pattern: /^[\w\s_]+$/,
            message: intl.formatMessage({id: 'app.common.validate.characterWord3'}),
          },
        ]}
      />

    </ProForm.Group>
  </div>)
}
export default FaultToleranceForm;
