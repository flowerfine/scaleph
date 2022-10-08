import {ProCard, ProFormGroup, ProFormSelect, ProFormText} from "@ant-design/pro-components";
import {DictDataService} from "@/services/admin/dictData.service";
import {DICT_TYPE} from "@/constant";

const FlinkImageOptions: React.FC = () => {
  return (
    <ProCard title={'Flink Image'} headerBordered collapsible={true}>
      <ProFormGroup>
        <ProFormText
          name="registry"
          label={"registry"}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}, {max: 128}]}/>

        <ProFormText
          name="repository"
          label={"repository"}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}, {max: 128}]}/>

        <ProFormText
          name="image"
          label={"image"}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}, {max: 128}]}/>

        <ProFormText
          name="flink.version"
          label={"flink.version"}
          colProps={{span: 21, offset: 1}}
          rules={[{required: true}, {max: 128}]}/>
      </ProFormGroup>
    </ProCard>
  );

}

export default FlinkImageOptions;
