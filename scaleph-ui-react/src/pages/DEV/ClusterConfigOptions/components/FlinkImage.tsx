import {ProCard, ProFormGroup, ProFormText} from "@ant-design/pro-components";

const FlinkImageOptions: React.FC = () => {
  return (
    <ProCard title={'Flink Image'} headerBordered collapsible={true}>
      <ProFormGroup>
        <ProFormText
          name="registry"
          label={"registry"}
          colProps={{span: 10, offset: 1}}
          rules={[{required: true}, {max: 128}]}/>
        <ProFormText
          name="repository"
          label={"repository"}
          colProps={{span: 10, offset: 1}}
          rules={[{required: true}, {max: 128}]}/>
        <ProFormText
          name="flink.version"
          label={"flink.version"}
          colProps={{span: 10, offset: 1}}
          rules={[{required: true}, {max: 128}]}/>
        <ProFormText
          name="image"
          label={"image"}
          colProps={{span: 10, offset: 1}}
          rules={[{required: true}, {max: 128}]}/>
      </ProFormGroup>
    </ProCard>
  );
}

export default FlinkImageOptions;
