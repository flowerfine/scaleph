import {ProCard, ProFormGroup, ProFormList, ProFormText} from "@ant-design/pro-components";

const Additional: React.FC = () => {
  return (<ProCard title={"Additional Config Options"} headerBordered collapsible={true}>
    <ProFormList name="options">
      <ProFormGroup>
        <ProFormText name="key" label={'Options'} colProps={{span: 10, offset: 1}}/>
        <ProFormText name="value" label={'Value'} colProps={{span: 10, offset: 1}}/>
      </ProFormGroup>
    </ProFormList>
  </ProCard>);
}

export default Additional;
