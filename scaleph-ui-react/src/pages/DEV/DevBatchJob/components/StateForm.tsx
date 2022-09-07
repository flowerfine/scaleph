import {ProForm, ProFormSelect, ProFormSwitch, ProFormText} from "@ant-design/pro-components";
import {listDictDataByType} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constant";
import {useIntl} from "@@/exports";

const StateForm: React.FC = () => {
  const intl = useIntl();

  return (<div>
    <ProForm.Group
      title={"State & Checkpoints & Savepoints"}
      collapsible={true}
      defaultCollapsed={true}
    >
      <ProFormSelect
        name="state.backend"
        label={"state.backend"}
        colProps={{span: 8, offset: 2}}
        rules={[{required: true}]}
        showSearch={true}
        allowClear={true}
        request={() => listDictDataByType(DICT_TYPE.flinkStateBackend)}
      />
      <ProFormText
        name="state.checkpoints.dir"
        label={'state.checkpoints.dir'}
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
      <ProFormSelect
        name="execution.checkpointing.mode"
        label={"execution.checkpointing.mode"}
        colProps={{span: 8, offset: 2}}
        rules={[{required: true}]}
        showSearch={true}
        allowClear={true}
        request={() => listDictDataByType(DICT_TYPE.flinkSemantic)}
      />
      <ProFormSwitch
        name="execution.checkpointing.unaligned"
        label={"execution.checkpointing.unaligned"}
        colProps={{span: 8, offset: 2}}
        rules={[{required: true}]}
        allowClear={true}
      />
      <ProFormText
        name="state savepoints dir"
        label={'state savepoints dir'}
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
        name="execution checkpointing interval"
        label={'execution checkpointing interval'}
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
      <ProFormSelect
        name="execution.checkpointing.externalized-checkpoint-retention"
        label={"execution.checkpointing.externalized-checkpoint-retention"}
        colProps={{span: 18, offset: 2}}
        rules={[{required: true}]}
        showSearch={true}
        allowClear={true}
        request={() => listDictDataByType(DICT_TYPE.flinkCheckpointRetain)}
      />
    </ProForm.Group>
  </div>)
}

export default StateForm;
