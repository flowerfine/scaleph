import {ProCard, ProFormGroup, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {DICT_TYPE} from "@/constant";
import {DictDataService} from "@/services/admin/dictData.service";

const AdvancedBasic: React.FC = () => {
  return (<ProCard
    title={'Basic'}
    headerBordered
    collapsible={true}>
    <ProFormGroup>
      <ProFormSelect
        name="flinkVersion"
        label={"flinkVersion"}
        colProps={{span: 10, offset: 1}}
        showSearch={true}
        options={["v1_15", "v1_16", "v1_17", "v1_18"]}
        initialValue={"v1_18"}
      />
      <ProFormText
        name="serviceAccount"
        label={'serviceAccount'}
        colProps={{span: 10, offset: 1}}
        initialValue={"flink"}
      />
      <ProFormText
        name="image"
        label={'image'}
        colProps={{span: 10, offset: 1}}
        initialValue={"flink:1.18"}
      />
      <ProFormSelect
        name="imagePullPolicy"
        label={"imagePullPolicy"}
        colProps={{span: 10, offset: 1}}
        request={() => DictDataService.listDictDataByType2(DICT_TYPE.imagePullPolicy)}
        initialValue={"IfNotPresent"}
      />
    </ProFormGroup>
  </ProCard>);
}

export default AdvancedBasic;
