import {
  ProCard,
  ProFormDigit,
  ProFormGroup,
  ProFormSelect,
  ProFormSwitch,
  ProFormText
} from "@ant-design/pro-components";
import {DICT_TYPE} from "@/constants/dictType";
import {DictDataService} from "@/services/admin/dictData.service";

const AdvancedCheckpoint: React.FC = () => {
  return (<ProCard
    title={'Checkpoint'}
    headerBordered
    collapsible={true}>
    <ProFormGroup>
      <ProFormSelect
        name="execution.checkpointing.mode"
        label={"execution.checkpointing.mode"}
        colProps={{span: 10, offset: 1}}
        showSearch={true}
        help={"The checkpointing mode (exactly-once vs. at-least-once)."}
        request={() => DictDataService.listDictDataByType2(DICT_TYPE.flinkSemantic)}
      />
      <ProFormText
        name="execution.checkpointing.interval"
        label={'execution.checkpointing.interval'}
        colProps={{span: 10, offset: 1}}
        initialValue={"180s"}
      />
      <ProFormText
        name="execution.checkpointing.min-pause"
        label={'execution.checkpointing.min-pause'}
        colProps={{span: 10, offset: 1}}
        help={"The minimal pause between checkpointing attempts."}
      />
      <ProFormText
        name="execution.checkpointing.timeout"
        label={'execution.checkpointing.timeout'}
        colProps={{span: 10, offset: 1}}
        initialValue={"10min"}
      />
      <ProFormDigit
        name="execution.checkpointing.max-concurrent-checkpoints"
        label={'execution.checkpointing.max-concurrent-checkpoints'}
        colProps={{span: 10, offset: 1}}
        help={"The maximum number of checkpoint attempts that may be in progress at the same time."}
        initialValue={1}
      />
      <ProFormSwitch
        name="execution.checkpointing.unaligned"
        label={"execution.checkpointing.unaligned"}
        colProps={{span: 10, offset: 1}}
      />
      <ProFormText
        name="execution.checkpointing.alignment-timeout"
        label={'execution.checkpointing.alignment-timeout'}
        colProps={{span: 10, offset: 1}}
        initialValue={"10s"}
        help={"Only relevant if execution.checkpointing.unaligned is enabled."}
      />
      <ProFormSelect
        name="execution.checkpointing.externalized-checkpoint-retention"
        label={"execution.checkpointing.externalized-checkpoint-retention"}
        colProps={{span: 10, offset: 1}}
        showSearch={true}
        request={() => DictDataService.listDictDataByType2(DICT_TYPE.flinkCheckpointRetain)}
      />
      <ProFormDigit
        name="state.checkpoints.num-retained"
        label={'state.checkpoints.num-retained'}
        colProps={{span: 10, offset: 1}}
        help={"The maximum number of completed checkpoints to retain."}
        initialValue={1}
      />
    </ProFormGroup>
  </ProCard>);
}

export default AdvancedCheckpoint;
