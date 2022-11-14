import {ProCard, ProFormGroup, ProFormText} from "@ant-design/pro-components";

const Resource: React.FC = () => {
  return (<ProCard
    title={"Resource Configuration"}
    headerBordered
    collapsible={true}>
    <ProFormGroup>
      <ProFormText
        name="jobmanager.memory.process.size"
        label={'jobmanager.memory.process.size'}
        colProps={{span: 10, offset: 1}}
      />
      <ProFormText
        name="jobmanager.memory.flink.size"
        label={'jobmanager.memory.flink.size'}
        colProps={{span: 10, offset: 1}}
      />
      <ProFormText
        name="taskmanager.memory.process.size"
        label={'taskmanager.memory.process.size'}
        colProps={{span: 10, offset: 1}}
      />
      <ProFormText
        name="taskmanager.memory.flink.size"
        label={'taskmanager.memory.flink.size'}
        colProps={{span: 10, offset: 1}}
      />
    </ProFormGroup>
  </ProCard>);
}

export default Resource;
