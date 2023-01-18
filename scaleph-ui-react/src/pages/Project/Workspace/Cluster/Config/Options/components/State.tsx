import {ProCard, ProFormGroup, ProFormSelect, ProFormSwitch, ProFormText} from "@ant-design/pro-components";
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constant";

const State: React.FC = () => {
  return (<ProCard
    title={'State & Checkpoints & Savepoints'}
    headerBordered
    collapsible={true}>
    <ProFormGroup>
      <ProFormSelect
        name="state.backend"
        label={"state.backend"}
        colProps={{span: 10, offset: 1}}
        showSearch={true}
        request={() => DictDataService.listDictDataByType2(DICT_TYPE.flinkStateBackend)}
      />
      <ProFormText
        name="state.savepoints.dir"
        label={'state.savepoints.dir'}
        colProps={{span: 10, offset: 1}}
      />
      <ProFormText
        name="state.checkpoints.dir"
        label={"state.checkpoints.dir"}
        colProps={{span: 10, offset: 1}}
      />
      <ProFormSelect
        name="execution.checkpointing.mode"
        label={"execution.checkpointing.mode"}
        colProps={{span: 10, offset: 1}}
        showSearch={true}
        request={() => DictDataService.listDictDataByType2(DICT_TYPE.flinkSemantic)}
      />
      <ProFormSwitch
        name="execution.checkpointing.unaligned"
        label={"execution.checkpointing.unaligned"}
        colProps={{span: 10, offset: 1}}
      />
      <ProFormText
        name="execution checkpointing interval"
        label={'execution checkpointing interval'}
        colProps={{span: 10, offset: 1}}
      />
      <ProFormSelect
        name="execution.checkpointing.externalized-checkpoint-retention"
        label={"execution.checkpointing.externalized-checkpoint-retention"}
        colProps={{span: 10, offset: 1}}
        showSearch={true}
        request={() => DictDataService.listDictDataByType2(DICT_TYPE.flinkCheckpointRetain)}
      />
    </ProFormGroup>
  </ProCard>);
}

export default State;
